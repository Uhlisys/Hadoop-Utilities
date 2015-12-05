package com.uhlisys.hadoop.utilities;

import com.uhlisys.hadoop.utilities.hclean.RuleEngine;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class HClean extends Configured implements Tool {

    private enum Mode {

        Retain,
        Delete;

        static Mode getMode(final String mode) {
            if ("Retain".equalsIgnoreCase(mode)) {
                return Retain;
            } else if ("Delete".equalsIgnoreCase(mode)) {
                return Delete;
            } else {
                throw new IllegalArgumentException(mode + " is an unknown Evaluation Mode (retain or delete).");
            }
        }
    }

    private class Scanner implements Runnable {

        final Logger logicLog = LoggerFactory.getLogger(HClean.class.getName() + ".Logic");
        final Logger deleteLog = LoggerFactory.getLogger(HClean.class.getName() + ".Deletes");
        final Logger retainLog = LoggerFactory.getLogger(HClean.class.getName() + ".Retains");
        final Configuration conf = new Configuration();
        final AtomicLong directoriesScanned = new AtomicLong(0);
        final AtomicLong directoriesDeleted = new AtomicLong(0);
        final AtomicLong directoriesRetained = new AtomicLong(0);
        final AtomicLong directoriesPurged = new AtomicLong(0);
        final AtomicLong filesScanned = new AtomicLong(0);
        final AtomicLong filesDeleted = new AtomicLong(0);
        final AtomicLong filesRetained = new AtomicLong(0);
        final String expression;
        final Path basePath;
        final boolean purge;
        final String name;
        final Mode mode;
        boolean complete, successful;

        public Scanner(final String name, final Path basePath, final String expression, final boolean purge, final Mode mode) {
            this.expression = expression;
            this.basePath = basePath;
            this.purge = purge;
            this.name = name;
            this.mode = mode;
            this.complete = false;
            this.successful = false;
        }

        public boolean isComplete() {
            return complete;
        }

        public boolean isSuccessful() {
            return successful;
        }

        @Override
        public void run() {
            try {
                final FileSystem fs = FileSystem.get(basePath.toUri(), conf);
                Thread.currentThread().setName(name);
                if (!fs.exists(basePath)) {
                    throw new FileNotFoundException(String.format("%s does not exist.", basePath));
                }
                final RuleEngine engine = new RuleEngine(expression);
                logicLog.debug(String.format("Recursing into %s.", basePath));
                logicLog.debug(String.format("if evaluate() == true then %s", mode));
                logicLog.info("Scanning " + basePath);
                process(fs, engine, "", fs.getFileStatus(basePath), false);
                complete = true;
                successful = true;
                logicLog.info("Scanning " + basePath + " Complete.");
                logicLog.info("Scanned " + directoriesScanned.get() + " directories.");
                logicLog.info("Retained " + directoriesRetained.get() + " directories.");
                logicLog.info("Deleted " + directoriesDeleted.get() + " directories.");
                logicLog.info("Purged " + directoriesPurged.get() + " empty directories.");
                logicLog.info("Scanned " + filesScanned.get() + " files.");
                logicLog.info("Retained " + filesRetained.get() + " files.");
                logicLog.info("Deleted " + filesDeleted.get() + " files.");
            } catch (final Exception ex) {
                runtimeLog.error("Error Executing Target '" + name + "': " + ex.getLocalizedMessage());
                complete = true;
                successful = false;
            }
        }

        public void process(final FileSystem fs, final RuleEngine engine, final String parentRelativePath, final FileStatus parentStatus, final boolean deletionScan) throws IOException {
            // Evaluate each child of the parent
            for (final FileStatus childStatus : fs.listStatus(parentStatus.getPath())) {
                final String childRelativePath = String.format("%s/%s", parentRelativePath, childStatus.getPath().getName());
                final Path childAbsolutePath = childStatus.getPath();
                final Boolean evalResult = engine.evaluate(parentRelativePath, childStatus);
                if(logicLog.isDebugEnabled()) {
                    logicLog.debug("Evaluated Child " + childRelativePath + " as " + evalResult);
                    logicLog.debug(engine.evaluateToString(parentRelativePath, childStatus));
                }
                if (deletionScan) {
                    // We are doing a recursive Scan of everything being deleted
                    // As such we are simply reporing deletes here and not 
                    // actually deleting.
                    deleteLog.info("TRUNC  " + childRelativePath);
                    if (childStatus.isDirectory()) {
                        directoriesScanned.incrementAndGet();
                        directoriesDeleted.incrementAndGet();
                        process(fs, engine, childRelativePath, childStatus, true);
                    } else if (childStatus.isFile()) {
                        filesScanned.incrementAndGet();
                        filesDeleted.incrementAndGet();
                    }
                } else if ((this.mode == Mode.Delete && evalResult) || (this.mode == Mode.Retain && !evalResult)) {
                    // Expression Engine has marked this child as removable.
                    // If its a directory we want to scan its children for reporting.                    
                    if (childStatus.isDirectory()) {
                        directoriesScanned.incrementAndGet();
                        directoriesDeleted.incrementAndGet();
                        deleteLog.info("DELETE " + childRelativePath);
                        process(fs, engine, childRelativePath, childStatus, true);
                        if (destructiveMode) {
                            fs.delete(childAbsolutePath, true);
                        }
                    } else if (childStatus.isFile()) {
                        filesScanned.incrementAndGet();
                        filesDeleted.incrementAndGet();
                    }
                } else if ((this.mode == Mode.Delete && !evalResult) || (this.mode == Mode.Retain && evalResult)) {
                    // Expression Engine has marked this child as retained.
                    // If its a directory we will recurse into it, if its empty
                    // and we are purging empty directories, we will purge it.
                    if (childStatus.isDirectory()) {
                        directoriesScanned.incrementAndGet();
                        process(fs, engine, childRelativePath, childStatus, false);
                        boolean validChildren = false;
                        for (final FileStatus status : fs.listStatus(childStatus.getPath())) {
                            final String filename = status.getPath().getName();
                            validChildren |= !(".".equals(filename) || "..".equals(filename));
                        }
                        if (purge && !validChildren) {
                            directoriesPurged.incrementAndGet();
                            deleteLog.info(" PURGE " + childRelativePath);
                            logicLog.debug(String.format("Purging Empty Directory %s.", childRelativePath));
                            if (destructiveMode) {
                                fs.delete(childAbsolutePath, true);
                            }
                        } else {
                            retainLog.info("RETAIN " + childRelativePath);
                            directoriesRetained.incrementAndGet();
                        }
                    } else if (childStatus.isFile()) {
                        filesScanned.incrementAndGet();
                        filesRetained.incrementAndGet();
                        retainLog.info("RETAIN " + childRelativePath);
                    }
                }
            }
        }
    }

    private static final Logger runtimeLog = LoggerFactory.getLogger(HClean.class.getName() + ".Runtime");
    private final XMLConfiguration config = new XMLConfiguration();
    private boolean destructiveMode = false;

    public void usage(final String error, final Set<String> targets) {
        if (error != null) {
            System.out.printf("ERROR: %s\n\n", error);
        }
        System.out.printf("USAGE: hclean [OPTIONS] <target>\n\n");

        if (targets != null && !targets.isEmpty()) {
            System.out.printf("  Targets: %s\n\n", targets);
        }

        System.out.printf("  Options:\n");
        System.out.printf("    -c   Specify Configuration file (Defaults to ./hclean.conf)\n");
        System.out.printf("    -h   Display this help screen\n");
        System.out.printf("    -d   Disable Safe Mode (Delete Things)\n\n");

        System.out.printf("  Retention Expressions:\n");
        System.out.printf("  Note: Parenthesis may be used around any expression for clarity & order of operations.\n\n");

        System.out.printf("    Boolean Evaluation:\n");
        System.out.printf("      A AND B    - true if A and B are true\n");
        System.out.printf("      A OR B     - true if A or B are true\n");
        System.out.printf("      A XOR B    - true if A or B are true but not both\n");
        System.out.printf("      !A         - true if A is false\n\n");

        System.out.printf("    Boolean Values:\n");
        System.out.printf("      true        - true constant\n");
        System.out.printf("      false       - false constant\n");
        System.out.printf("      isDirectory - true if entity is a directory\n");
        System.out.printf("      isSymlink   - true if entity is a symlink\n");
        System.out.printf("      isFile      - true if entity is a file\n\n");

        System.out.printf("    String Comparison (evaluates to boolean):\n");
        System.out.printf("      A <> B     - true if A Contains B\n");
        System.out.printf("      A == B     - true if A is equal to B\n");
        System.out.printf("      A != B     - true if A is not equal to B\n");
        System.out.printf("      A =~ B     - true if regex B matches the string A\n\n");

        System.out.printf("    String Values:\n");
        System.out.printf("      apath      - Absolute Path of a file\n");
        System.out.printf("      rpath      - File Path Relative to the base\n");
        System.out.printf("      filename   - File Name\n");
        System.out.printf("      group      - Owning Group\n");
        System.out.printf("      user       - Owning User\n");
        System.out.printf("      'foobar'   - String Constant\n\n");

        System.out.printf("    Integer Comparison (becomes boolean):\n");
        System.out.printf("      A <  B     - true if A is less than B\n");
        System.out.printf("      A <= B     - true if A is less than or equal to B\n");
        System.out.printf("      A == B     - true if A is equal to B\n");
        System.out.printf("      A != B     - true if A is not equal to B\n");
        System.out.printf("      A >= B     - true if A is greater than or equal B\n");
        System.out.printf("      A >  B     - true if A is greater than B\n");

        System.out.printf("    Integer Expressions:\n");
        System.out.printf("      A + B      - Add A and B\n");
        System.out.printf("      A - B      - Subtract B from A\n");
        System.out.printf("      A * B      - Multiply A and B\n");
        System.out.printf("      A / B      - Divide A and B\n");
        System.out.printf("      A ** B     - Raise A to the B power\n");
        System.out.printf("      A %% B      - Remainder of A divided by B\n\n");

        System.out.printf("    Integer Values:\n");
        System.out.printf("      atime      - Last Access time of a file or Directory\n");
        System.out.printf("      ctime      - Current time of evaluation\n");
        System.out.printf("      mtime      - Last Modification time of a file or Directory\n");
        System.out.printf("      rtime      - When this retention cycle began\n");
        System.out.printf("      1234567    - Integer Constant\n");
        System.out.printf("      time{ '1w' }                                - Unit of time Time Constant [w|d|h|m|s|ms] \n");
        System.out.printf("      date{ yyyy-mm-dd }                          - Date Constant\n");
        System.out.printf("      timestamp{ yyy-mm-dd'T'hh:mm::ss }          - Specific timestamp Constant\n");
        System.out.printf("      path{ /(?<year>\\d+)/(?<month>\\d+) , def } - Parse path into a time(Requires JDK7),\n");
        System.out.printf("                                                    default is an Integer Expression used if path does not match\n");
        System.out.printf("                                                  - year,month,day,hour,minute,second,dayperiod,hourperiod\n\n");

        System.exit(error == null ? 0 : -1);
    }

    @Override
    public int run(final String[] rawArguments) throws Exception {
        final Map<String, String> env = System.getenv();
        final Options options = new Options();
        options.addOption("c", "config", true, "Configuration File");
        options.addOption("h", "help", false, "Show help");
        options.addOption("d", "destructive", false, "Destructive Mode (Deletions Will Occur)");
        final GenericOptionsParser gop = new GenericOptionsParser(getConf(), options, rawArguments);
        final CommandLine commandLine = gop.getCommandLine();
        final String[] arguments = gop.getRemainingArgs();
        File configfile;
        if (commandLine.hasOption('c') && (configfile = new File(commandLine.getOptionValue('c'))).canRead()) {
            runtimeLog.debug("Config file from Command line: " + configfile);
        } else if (System.getProperty("hclean.conf") != null && (configfile = new File(System.getProperty("hclean.conf"))).canRead()) {
            runtimeLog.debug("Config file from -Dhclean.conf: " + configfile);
        } else if ((configfile = new File(env.get("HCLEAN_HOME") + "/conf/hclean.conf")).canRead()) {
            runtimeLog.debug("Config file from default location: " + configfile);
        } else {
            throw new RuntimeException("HClean Configuration file '" + configfile + "' must exist and be readable.");
        }
        runtimeLog.info("Using Config: " + configfile);
        config.setListDelimiter('\0');
        config.setDelimiterParsingDisabled(true);
        config.setAttributeSplittingDisabled(true);
        config.load(configfile);

        if (destructiveMode = commandLine.hasOption('d')) {
            runtimeLog.info("Destructive Mode Enabled! Files & Directories will be deleted.");
        } else {
            runtimeLog.info("Safe Mode Enabled! No changes will be made.");
        }
        final int maxThreads = config.getInt("[@maxThreads]", 1);
        final Map<String, Scanner> targets = new TreeMap<>();
        final Set<Scanner> active = new HashSet<>();
        final ExecutorService threadPool = Executors.newFixedThreadPool(maxThreads);
        for (final HierarchicalConfiguration targetConfig : config.configurationsAt("target")) {
            final String name = targetConfig.getString("[@name]");
            final Path basePath = new Path(targetConfig.getString("[@path]"));
            final Mode mode = Mode.getMode(targetConfig.getString("[@mode]", "retain"));
            final boolean purgeEmpty = targetConfig.getBoolean("[@purgeEmpty]", true);
            final String expression = targetConfig.getString("");
            targets.put(name, new Scanner(name, basePath, expression, purgeEmpty, mode));
        }

        if (commandLine.hasOption('h')) {
            usage(null, targets.keySet());
        }
        runtimeLog.info("Available Targets " + targets.keySet());
        runtimeLog.info("Executing Targets " + Arrays.toString(arguments));
        for (final String targetName : arguments) {
            final Scanner target;
            if ((target = targets.get(targetName)) == null) {
                throw new IllegalArgumentException("Unknown Target: " + targetName);
            }
            threadPool.submit(target);
            active.add(target);
        }
        while (!active.isEmpty()) {
            for (final Iterator<Scanner> it = active.iterator(); it.hasNext();) {
                final Scanner target = it.next();
                if (target.isComplete()) {
                    if (!target.isSuccessful()) {
                        runtimeLog.error("Error Processing " + target.name);
                    }
                    it.remove();
                }
            }
            Thread.sleep(1000);
            runtimeLog.debug("Waiting for targets to finish.");
        }
        return 0;
    }

    public String[] configure(String[] arguments) throws IOException {
        return new GenericOptionsParser(arguments).getRemainingArgs();
    }

    public static void main(final String[] arguments) throws Exception {
        final Configuration conf = new Configuration();
        System.exit(ToolRunner.run(conf, new HClean(), arguments));
    }
}
