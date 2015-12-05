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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JUnit Test for IntegerExpressionNode
 */
public final class IntegerExpressionNodeTest {

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Testing IntegerExpressionNode");
    }

    /**
     * Test of evaluate method of class IntegerExpressionNode.
     */
    @Test
    public void testEvaluate() {
        System.out.println("IntegerExpressionNode - testEvaluate");
        final IntegerLiteralNode literal = new IntegerLiteralNode(1);
        final IntegerExpressionNode instance = new IntegerExpressionNode(
                literal, literal, RuleNode.IntegerOperator.Addition);
        final long expResult = 2;
        final long result = instance.evaluate(TestHelper.relativePathString, TestHelper.fileStatus);
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toRulenode method of class ModificationTimeNode.
     */
    @Test
    public void testToRulenode() {
        System.out.println("IntegerExpressionNode - testToRulenode");
        final IntegerLiteralNode literal = new IntegerLiteralNode(1);
        final IntegerExpressionNode instance = new IntegerExpressionNode(
                literal, literal, RuleNode.IntegerOperator.Addition);
        final String expResult = String.format("%s + %s", literal.toRulenode(), literal.toRulenode());
        final String result = instance.toRulenode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toAstnode method of class ModificationTimeNode.
     */
    @Test
    public void testToAstnode() {
        System.out.println("IntegerExpressionNode - testToAstnode");
        final IntegerLiteralNode literal = new IntegerLiteralNode(1);
        final IntegerExpressionNode instance = new IntegerExpressionNode(
                literal, literal, RuleNode.IntegerOperator.Addition);
        final String expResult = String.format("IntegerExpressionNode( %s, %s, Addition )", literal.toAstnode(), literal.toAstnode());
        final String result = instance.toAstnode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of equalTo method of class IntegerExpressionNode.
     */
    @Test
    public void testEqualTo() {
        System.out.println("IntegerExpressionNode - testEqualTo");
        final IntegerLiteralNode literal = new IntegerLiteralNode(0);
        final IntegerExpressionNode instance1 = new IntegerExpressionNode(
                literal, literal, RuleNode.IntegerOperator.Addition);
        final IntegerExpressionNode instance2 = new IntegerExpressionNode(
                literal, literal, RuleNode.IntegerOperator.Addition);
        final TestNode testInstance = TestNode.instance;
        assertTrue(instance1.equalTo(instance2));
        assertFalse(instance1.equalTo(testInstance));
    }

    /**
     * Test of AST Generation of class IntegerExpressionNode.
     */
    @Test
    public void testAstGeneration() {
        System.out.println("IntegerExpressionNode - testAstGeneration");
        final IntegerLiteralNode literal = new IntegerLiteralNode(TestHelper.atime);
        final String inputRuleset = new IntegerExpressionNode(literal, literal, RuleNode.IntegerOperator.Addition).toRulenode();
        final ANTLRInputStream inStream = new ANTLRInputStream(inputRuleset);
        final HCleanLexer lexer = new HCleanLexer(inStream);
        final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        final HCleanParser parser = new HCleanParser(tokenStream);
        final ParseTreeWalker walker = new ParseTreeWalker();
        final RuleListener listener = new RuleListener();
        final ParseTree parseTree = parser.integerExpression();
        walker.walk(listener, parseTree);
        final String outputRuleSet = listener.peekStack().toRulenode();
        System.out.printf("Expected: %s\n", inputRuleset);
        System.out.printf("  Result: %s\n", outputRuleSet);
        assertEquals(inputRuleset, outputRuleSet);
    }
}
