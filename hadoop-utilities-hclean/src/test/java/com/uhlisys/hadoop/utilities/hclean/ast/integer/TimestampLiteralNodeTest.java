package com.uhlisys.hadoop.utilities.hclean.ast.integer;

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
 * JUnit Test for TimestampLiteralNode
 */
public final class TimestampLiteralNodeTest {

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Testing TimestampLiteralNode");
    }

    /**
     * Test of evaluate method of class TimestampLiteralNode.
     */
    @Test
    public void testEvaluate() {
        System.out.println("TimestampLiteralNode - testEvaluate");
        final TimestampLiteralNode instance = new TimestampLiteralNode(TestHelper.atimestamp, true);
        final long expResult = TestHelper.atime;
        final long result = instance.evaluate(TestHelper.relativePathString, TestHelper.fileStatus);
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toRulenode method of class TimestampLiteralNode.
     */
    @Test
    public void testToRulenode() {
        System.out.println("TimestampLiteralNode - testToRulenode");
        final TimestampLiteralNode instance = new TimestampLiteralNode(TestHelper.atimestamp, true);
        final String expResult = String.format("timestamp{ '%s' }", TestHelper.atimestamp);
        final String result = instance.toRulenode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toAstnode method of class TimestampLiteralNode.
     */
    @Test
    public void testToAstnode() {
        System.out.println("TimestampLiteralNode - testToAstnode");
        final TimestampLiteralNode instance = new TimestampLiteralNode(TestHelper.atimestamp, true);
        final String expResult = String.format("TimestampLiteralNode( '%s' )", TestHelper.atimestamp);
        final String result = instance.toAstnode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of equalTo method of class TimestampLiteralNode.
     */
    @Test
    public void testEqualTo() {
        System.out.println("TimestampLiteralNode - testEqualTo");
        final TimestampLiteralNode instance1 = new TimestampLiteralNode(TestHelper.atimestamp, true);
        final TimestampLiteralNode instance2 = new TimestampLiteralNode(TestHelper.atimestamp, true);
        final TestNode testInstance = TestNode.instance;
        assertTrue(instance1.equalTo(instance2));
        assertFalse(instance1.equalTo(testInstance));
    }

    /**
     * Test of AST Generation of class TimestampLiteralNode.
     */
    @Test
    public void testAstGeneration() {
        System.out.println("TimestampLiteralNode - testAstGeneration");
        final TimestampLiteralNode literal = new TimestampLiteralNode(TestHelper.atimestamp, true);
        final String inputRuleset = literal.toRulenode();
        System.out.printf("Input Ruleset: %s\n", inputRuleset);
        final ANTLRInputStream inStream = new ANTLRInputStream(inputRuleset);
        final HCleanLexer lexer = new HCleanLexer(inStream);
        final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        final HCleanParser parser = new HCleanParser(tokenStream);
        final ParseTreeWalker walker = new ParseTreeWalker();
        final RuleListener listener = new RuleListener();
        final ParseTree parseTree = parser.timestampLiteral();
        walker.walk(listener, parseTree);
        final String outputRuleSet = listener.peekStack().toRulenode();
        System.out.printf("Expected: %s\n", inputRuleset);
        System.out.printf("  Result: %s\n", outputRuleSet);
        assertEquals(inputRuleset, outputRuleSet);
    }
}
