package com.uhlisys.hadoop.utilities.hclean.ast.bool;

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
 * JUnit Test for BooleanExpressionNode
 */
public final class BooleanExpressionNodeTest {

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Testing BooleanExpressionNode");
    }

    /**
     * Test of evaluate method of class BooleanExpressionNode.
     */
    @Test
    public void testEvaluate() {
        System.out.println("BooleanExpressionNode - testEvaluate");
        final BooleanLiteralNode literal = new BooleanLiteralNode(true);
        final BooleanExpressionNode instance = new BooleanExpressionNode(
                literal, literal, RuleNode.BooleanOperator.And);
        final boolean expResult = true;
        final boolean result = instance.evaluate(TestHelper.relativePathString, TestHelper.fileStatus);
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toRulenode method of class BooleanExpressionNode.
     */
    @Test
    public void testToRulenode() {
        System.out.println("BooleanExpressionNode - testToRulenode");
        final BooleanLiteralNode literal = new BooleanLiteralNode(true);
        final BooleanExpressionNode instance = new BooleanExpressionNode(
                literal, literal, RuleNode.BooleanOperator.And);
        final String expResult = String.format("%s AND %s", literal.toRulenode(), literal.toRulenode());
        final String result = instance.toRulenode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toAstnode method of class BooleanExpressionNode.
     */
    @Test
    public void testToAstnode() {
        System.out.println("BooleanExpressionNode - testToAstnode");
        final BooleanLiteralNode literal = new BooleanLiteralNode(true);
        final BooleanExpressionNode instance = new BooleanExpressionNode(
                literal, literal, RuleNode.BooleanOperator.And);
        final String expResult = String.format("BooleanExpressionNode( %s, %s, And )", literal.toAstnode(), literal.toAstnode());
        final String result = instance.toAstnode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of equalTo method of class BooleanExpressionNode.
     */
    @Test
    public void testEqualTo() {
        System.out.println("BooleanExpressionNode - testEqualTo");
        final BooleanLiteralNode literal = new BooleanLiteralNode(true);
        final BooleanExpressionNode instance1 = new BooleanExpressionNode(
                literal, literal, RuleNode.BooleanOperator.And);
        final BooleanExpressionNode instance2 = new BooleanExpressionNode(
                literal, literal, RuleNode.BooleanOperator.And);
        final TestNode testInstance = TestNode.instance;
        assertTrue(instance1.equalTo(instance2));
        assertFalse(instance1.equalTo(testInstance));
    }
    
    /**
     * Test of AST Generation of class BooleanInversionNode.
     */
    @Test
    public void testAstGeneration() {
        System.out.println("BooleanExpressionNode - testAstGeneration");
        final BooleanLiteralNode literal = new BooleanLiteralNode(true);
        final BooleanExpressionNode node = new BooleanExpressionNode(literal, literal, RuleNode.BooleanOperator.Or);
        final String inputRuleset = node.toRulenode();
        System.out.printf("Input Ruleset: %s\n", inputRuleset);
        final ANTLRInputStream inStream = new ANTLRInputStream(inputRuleset);
        final HCleanLexer lexer = new HCleanLexer(inStream);
        final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        final HCleanParser parser = new HCleanParser(tokenStream);
        final ParseTreeWalker walker = new ParseTreeWalker();
        final RuleListener listener = new RuleListener();
        final ParseTree parseTree = parser.booleanExpression();
        walker.walk(listener, parseTree);
        final String outputRuleSet = listener.peekStack().toRulenode();
        System.out.printf("Expected: %s\n", inputRuleset);
        System.out.printf("  Result: %s\n", outputRuleSet);
        assertEquals(inputRuleset, outputRuleSet);
    }
}
