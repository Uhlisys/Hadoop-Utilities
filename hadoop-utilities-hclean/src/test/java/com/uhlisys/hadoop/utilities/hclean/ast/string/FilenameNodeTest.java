package com.uhlisys.hadoop.utilities.hclean.ast.string;

import com.uhlisys.hadoop.utilities.hclean.HCleanLexer;
import com.uhlisys.hadoop.utilities.hclean.HCleanParser;
import com.uhlisys.hadoop.utilities.hclean.RuleListener;
import com.uhlisys.hadoop.utilities.hclean.TestHelper;
import com.uhlisys.hadoop.utilities.hclean.TestNode;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JUnit Test for FilenameNode
 */
public final class FilenameNodeTest {

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Testing FilenameNode");
    }

    /**
     * Test of evaluate method of class FilenameNode.
     */
    @Test
    public void testEvaluate() {
        System.out.println("FilenameNode - testEvaluate");
        final FilenameNode instance = new FilenameNode();
        final String expResult = TestHelper.filenameString;
        final String result = instance.evaluate(TestHelper.relativePathString, TestHelper.fileStatus);
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toRulenode method of class FilenameNode.
     */
    @Test
    public void testToRulenode() {
        System.out.println("FilenameNode - testToRulenode");
        final FilenameNode instance = new FilenameNode();
        final String expResult = "filename";
        final String result = instance.toRulenode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toAstnode method of class FilenameNode.
     */
    @Test
    public void testToAstnode() {
        System.out.println("FilenameNode - testToAstnode");
        final FilenameNode instance = new FilenameNode();
        final String expResult = "FilenameNode()";
        final String result = instance.toAstnode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of equalTo method of class FilenameNode.
     */
    @Test
    public void testEqualTo() {
        System.out.println("FilenameNode - testEqualTo");
        final FilenameNode instance1 = new FilenameNode();
        final FilenameNode instance2 = new FilenameNode();
        final TestNode testInstance = TestNode.instance;
        assertTrue(instance1.equalTo(instance2));
        assertFalse(instance1.equalTo(testInstance));
    }

    /**
     * Test of AST Generation of class FilenameNode.
     */
    @Test
    public void testAstGeneration() {
        System.out.println("FilenameNode - testAstGeneration");
        final FilenameNode literal = new FilenameNode();
        final String inputRuleset = literal.toRulenode();
        System.out.printf("Input Ruleset: %s\n", inputRuleset);
        final ANTLRInputStream inStream = new ANTLRInputStream(inputRuleset);
        final HCleanLexer lexer = new HCleanLexer(inStream);
        final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        final HCleanParser parser = new HCleanParser(tokenStream);
        final ParseTreeWalker walker = new ParseTreeWalker();
        final RuleListener listener = new RuleListener();
        final ParseTree parseTree = parser.filename();
        walker.walk(listener, parseTree);
        final String outputRuleSet = listener.peekStack().toRulenode();
        System.out.printf("Expected: %s\n", inputRuleset);
        System.out.printf("  Result: %s\n", outputRuleSet);
        assertEquals(inputRuleset, outputRuleSet);
    }
}
