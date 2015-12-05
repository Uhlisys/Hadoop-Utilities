package com.uhlisys.hadoop.utilities.hclean.ast.bool;

import com.uhlisys.hadoop.utilities.hclean.HCleanLexer;
import com.uhlisys.hadoop.utilities.hclean.HCleanParser;
import com.uhlisys.hadoop.utilities.hclean.RuleListener;
import com.uhlisys.hadoop.utilities.hclean.RuleNode;
import com.uhlisys.hadoop.utilities.hclean.TestHelper;
import com.uhlisys.hadoop.utilities.hclean.TestNode;
import com.uhlisys.hadoop.utilities.hclean.ast.integer.IntegerLiteralNode;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit Test for BooleanLiteralNode
 */
public final class BooleanLiteralNodeTest {

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Testing BooleanLiteralNode");
    }

    /**
     * Test of evaluate method of class BooleanLiteralNode.
     */
    @Test
    public void testEvaluate() {
        System.out.println("BooleanLiteralNode - testEvaluate");
        final BooleanLiteralNode instance = new BooleanLiteralNode(true);
        final boolean expResult = true;
        final boolean result = instance.evaluate(TestHelper.relativePathString, TestHelper.fileStatus);
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toRulenode method of class BooleanLiteralNode.
     */
    @Test
    public void testToRulenode() {
        System.out.println("BooleanLiteralNode - testToRulenode");
        final BooleanLiteralNode instance = new BooleanLiteralNode(true);
        final String expResult = "true";
        final String result = instance.toRulenode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toAstnode method of class BooleanLiteralNode.
     */
    @Test
    public void testToAstnode() {
        System.out.println("BooleanLiteralNode - testToAstnode");
        final BooleanLiteralNode instance = new BooleanLiteralNode(true);
        final String expResult = String.format("BooleanLiteralNode( true )");
        final String result = instance.toAstnode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of equalTo method of class BooleanLiteralNode.
     */
    @Test
    public void testEqualTo() {
        System.out.println("BooleanLiteralNode - testEqualTo");
        final BooleanLiteralNode instance1 = new BooleanLiteralNode(true);
        final BooleanLiteralNode instance2 = new BooleanLiteralNode(true);
        final TestNode testInstance = TestNode.instance;
        assertTrue(instance1.equalTo(instance2));
        assertFalse(instance1.equalTo(testInstance));
    }
    
    /**
     * Test of AST Generation of class IntegerComparisonNode.
     */
    @Test
    public void testAstGeneration() {
        System.out.println("BooleanLiteralNode - testAstGeneration");
        final BooleanLiteralNode literal = new BooleanLiteralNode(true);
        final String inputRuleset = literal.toRulenode();
        System.out.printf("Input Ruleset: %s\n", inputRuleset);
        final ANTLRInputStream inStream = new ANTLRInputStream(inputRuleset);
        final HCleanLexer lexer = new HCleanLexer(inStream);
        final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        final HCleanParser parser = new HCleanParser(tokenStream);
        final ParseTreeWalker walker = new ParseTreeWalker();
        final RuleListener listener = new RuleListener();
        final ParseTree parseTree = parser.booleanLiteral();
        walker.walk(listener, parseTree);
        final String outputRuleSet = listener.peekStack().toRulenode();
        System.out.printf("Expected: %s\n", inputRuleset);
        System.out.printf("  Result: %s\n", outputRuleSet);
        assertEquals(inputRuleset, outputRuleSet);
    }
}
