package com.uhlisys.hadoop.utilities.hclean;

import com.uhlisys.hadoop.utilities.hclean.ast.bool.BooleanLiteralNode;
import java.io.IOException;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.FileStatus;

/**
 * Valid Operators
 * A <> B - String A Contains B
 * A EQ B - String A Equals B
 * A NE B - String A Not Equal To B
 * A == B - Number A Equals B
 * A != B - Number A Not Equal To B
 * A > B - Number A Greater Than B
 * A >= B - Number A Greater Than Or Equal To B
 * A <= B - Number A Less Than Or Equal To B
 * A < B - Number A Less Than B
 */
public final class RuleEngine {

    public enum Mode {

        NORMAL,
        LEXERDEBUG,
        PARSERDEBUG,
        ASTDEBUG;
    }
    final static Log log = LogFactory.getLog(RuleEngine.class);
    final RuleNode.BooleanNode retentionRuleset;

    public RuleEngine(final String ruleset) {
        this(ruleset, Mode.NORMAL);
    }

    public RuleEngine(final String ruleset, final Mode mode) {
        final ANTLRInputStream input = new ANTLRInputStream(ruleset);
        final HCleanLexer lexer = new HCleanLexer(input);
        final CommonTokenStream tokens = new CommonTokenStream(lexer);
        final HCleanParser parser = new HCleanParser(tokens);
        final ParseTreeWalker walker = new ParseTreeWalker();
        try {
            switch (mode) {
                case LEXERDEBUG: {
                    System.out.println("===================================  Tokens  ===================================");
                    tokens.fill();
                    for (final Token tok : tokens.getTokens()) {
                        System.out.println(tok);
                    }
                    System.out.println("================================================================================");
                    retentionRuleset = new BooleanLiteralNode(true);
                    break;
                }
                case PARSERDEBUG: {
                    parser.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);
                    parser.addErrorListener(new DiagnosticErrorListener());
                    parser.setBuildParseTree(true);
                    parser.setTrace(true);

                    final ParseTree parseTree = parser.booleanExpression();
                    System.out.println("================================== Parse Tree ==================================");
                    System.out.println("Tree:\n" + parseTree.toStringTree(parser));
                    System.out.println("================================================================================");
                    retentionRuleset = new BooleanLiteralNode(true);
                    break;
                }
                case ASTDEBUG: {
                    final RuleListener listener = new RuleListener(true);
                    final ParseTree parseTree = parser.booleanExpression();
                    walker.walk(listener, parseTree);
                    retentionRuleset = listener.getExpression();
                    break;
                }
                case NORMAL:
                default: {
                    final RuleListener listener = new RuleListener();
                    final ParseTree parseTree = parser.booleanExpression();
                    walker.walk(listener, parseTree);
                    retentionRuleset = listener.getExpression();
                    break;
                }
            }
        } catch (final RecognitionException ex) {
            log.fatal("Error parsing ruleset: " + ex.getMessage());
            throw ex;
        }
    }

    /**
     * Compares one RuleEngine to another
     *
     * @param that Rule Engine to compare to.
     *
     * @return
     */
    public boolean equalToRuleEngine(final RuleEngine that) {
        return this.retentionRuleset.equalTo(that.retentionRuleset);
    }

    /**
     * Evaluate this node and all subtree nodes
     *
     * @param relativepath Path relative to base to use
     * @param fileStatus File Status Object from FileSystem
     * @return Returns true if the provided path should be kept
     *
     * @throws IOException
     */
    public boolean evaluate(final String relativepath, final FileStatus fileStatus) throws IOException {
        return retentionRuleset.evaluate(relativepath, fileStatus);
    }

    /**
     * Evaluate this node and all subtree nodes
     *
     * @param relativepath Path relative to base to use
     * @param fileStatus File Status Object from FileSystem
     * @return Returns true if the provided path should be kept
     *
     * @throws IOException
     */
    public String evaluateToString(final String relativepath, final FileStatus fileStatus) throws IOException {
        return retentionRuleset.evaluateToString(relativepath, fileStatus);
    }
    
    /**
     * Return a textual Representation of this node
     *
     * @return
     */
    public String toRulenode() {
        return retentionRuleset.toRulenode();
    }

    /**
     * Return a textual Representation of this node
     *
     * @return
     */
    public String toAstnode() {
        return String.format("RuleEngine( %s )", retentionRuleset.toAstnode());
    }
}
