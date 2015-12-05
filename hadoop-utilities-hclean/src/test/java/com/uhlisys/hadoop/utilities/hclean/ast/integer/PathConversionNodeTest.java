package com.uhlisys.hadoop.utilities.hclean.ast.integer;

import com.uhlisys.hadoop.utilities.hclean.HCleanLexer;
import com.uhlisys.hadoop.utilities.hclean.HCleanParser;
import com.uhlisys.hadoop.utilities.hclean.RuleListener;
import com.uhlisys.hadoop.utilities.hclean.RuleNode;
import com.uhlisys.hadoop.utilities.hclean.TestHelper;
import com.uhlisys.hadoop.utilities.hclean.TestNode;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit Test for PathConversionNode
 */
public final class PathConversionNodeTest {

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Testing PathConversionNode");
    }

    /**
     * Test of evaluate method of class PathConversionNode.
     */
    @Test
    public void testEvaluate() {
        System.out.println("PathConversionNode - testEvaluate");
        {
            final RuleNode.IntegerNode defaultValue = new IntegerLiteralNode(0);
            final PathConversionNode instance = new PathConversionNode(TestHelper.pathPattern, defaultValue);
            final long expResult = TestHelper.mtime;
            final long result = instance.evaluate(TestHelper.relativePathString, TestHelper.fileStatus);
            System.out.printf("Expected: %s\n", expResult);
            System.out.printf("  Result: %s\n", result);
            assertEquals(expResult, result);
        }        
    }
    
    @Test
    public void testEvaluateToString() {
        System.out.println("PathConversionNode - testEvaluateToString");
        {
            final RuleNode.IntegerNode defaultValue = new IntegerLiteralNode(50);
            final PathConversionNode instance = new PathConversionNode("(?<year>[0-9]+)/(?<month>[0-9]+)/(?<day>[0-9]+)", defaultValue);
            final String expResult = "PathConversionNode( '(?<year>[0-9]+)/(?<month>[0-9]+)/(?<day>[0-9]+)' -> 0, IntegerLiteralNode( 50 ) -> 50 ) -> 0";
            final String result = instance.evaluateToString("/1970/01/01", TestHelper.fileStatus);
            System.out.printf("Expected: %s\n", expResult);
            System.out.printf("  Result: %s\n", result);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of toRulenode method of class PathConversionNode.
     */
    @Test
    public void testToRulenode() {
        System.out.println("PathConversionNode - testToRulenode");
        final RuleNode.IntegerNode defaultValue = new IntegerLiteralNode(0);
        final PathConversionNode instance = new PathConversionNode(TestHelper.pathPattern, defaultValue);
        final String expResult = String.format("path{ '%s', %s }", TestHelper.pathPattern, defaultValue.toRulenode());
        final String result = instance.toRulenode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toAstnode method of class PathConversionNode.
     */
    @Test
    public void testToAstnode() {
        System.out.println("PathConversionNode - testToAstnode");
        final RuleNode.IntegerNode defaultValue = new IntegerLiteralNode(0);
        final PathConversionNode instance = new PathConversionNode(TestHelper.pathPattern, defaultValue);
        final String expResult = String.format("PathConversionNode( '%s', %s )", TestHelper.pathPattern, defaultValue.toAstnode());
        final String result = instance.toAstnode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of equalTo method of class PathConversionNode.
     */
    @Test
    public void testEqualTo() {
        System.out.println("PathConversionNode - testEqualTo");
        final RuleNode.IntegerNode defaultValue = new IntegerLiteralNode(0);
        final PathConversionNode instance1 = new PathConversionNode(TestHelper.pathPattern, defaultValue);
        final PathConversionNode instance2 = new PathConversionNode(TestHelper.pathPattern, defaultValue);
        final TestNode testInstance = TestNode.instance;
        assertTrue(instance1.equalTo(instance2));
        assertFalse(instance1.equalTo(testInstance));
    }

    /**
     * Test of AST Generation of class PathConversionNode.
     */
    @Test
    public void testAstGeneration() {
        System.out.println("PathConversionNode - testAstGeneration");
        final RuleNode.IntegerNode defaultValue = new IntegerLiteralNode(0);
        final PathConversionNode literal = new PathConversionNode(TestHelper.pathPattern, defaultValue);
        final String inputRuleset = literal.toRulenode();
        System.out.printf("Input Ruleset: %s\n", inputRuleset);
        final ANTLRInputStream inStream = new ANTLRInputStream(inputRuleset);
        final HCleanLexer lexer = new HCleanLexer(inStream);
        final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        final HCleanParser parser = new HCleanParser(tokenStream);
        final ParseTreeWalker walker = new ParseTreeWalker();
        final RuleListener listener = new RuleListener();
        final ParseTree parseTree = parser.pathConversion();
        walker.walk(listener, parseTree);
        final String outputRuleSet = listener.peekStack().toRulenode();
        System.out.printf("Expected: %s\n", inputRuleset);
        System.out.printf("  Result: %s\n", outputRuleSet);
        assertEquals(inputRuleset, outputRuleSet);
    }
}
