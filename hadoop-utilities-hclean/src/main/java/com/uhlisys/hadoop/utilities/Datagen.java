package com.uhlisys.hadoop.utilities;

import com.uhlisys.hadoop.utilities.hclean.RuleEngine;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class Datagen extends Configured implements Tool {

    private static final Logger log = LoggerFactory.getLogger(Datagen.class);
    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("/yyyy/MM/dd/");

    public void usage(final String error) {
        if (error != null) {
            System.out.printf("ERROR: %s\n\n", error);
        }
        System.out.printf("USAGE: datagen [OPTIONS] <target>");

        System.out.printf("  Options:\n");
    }

    @Override
    public int run(final String[] rawArguments) throws Exception {
        final Map<String, String> env = System.getenv();
        final Options options = new Options();
        options.addOption("s", "start", true, "Start Hourperiod");
        options.addOption("e", "end", true, "End Hourperiod");
        options.addOption("p", "parts", true, "Output part files");
        options.addOption("h", "help", false, "Show help");
        final GenericOptionsParser gop = new GenericOptionsParser(getConf(), options, rawArguments);
        final CommandLine commandLine = gop.getCommandLine();
        final String[] arguments = gop.getRemainingArgs();
        if (commandLine.hasOption('h')) {
            usage(null);
        }

        final int start, end, parts;
        if (commandLine.hasOption('s')) {
            start = Integer.parseInt(commandLine.getOptionValue('s'));
        } else {
            start = 376944;
        }
        if (commandLine.hasOption('e')) {
            end = Integer.parseInt(commandLine.getOptionValue('e'));
        } else {
            end = 385247;
        }
        if (commandLine.hasOption('p')) {
            parts = Integer.parseInt(commandLine.getOptionValue('p'));
        } else {
            parts = 10;
        }
        final URI baseUri = new URI(arguments[0]);
        final FileSystem fs = FileSystem.get(baseUri, getConf());
        fs.delete(new Path(baseUri), true);
        long fileCount = 0;
        for (long hp = start; hp <= end; hp++) {
            for (int p = 0; p < parts; p++) {
                final Path path = new Path(String.format("%s%s%s/part-r-%05d", baseUri, formatter.print(hp * 3600000), hp, p));
                log.info(path.toString());
                fs.create(path).close();
                fileCount++;
            }
        }
        log.info(String.format("Created %s files", fileCount));
        return 0;
    }

    public static void main(final String[] args) throws Exception {
        final Configuration conf = new Configuration();
        System.exit(ToolRunner.run(conf, new Datagen(), args));
    }
}
