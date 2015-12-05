package com.uhlisys.hadoop.utilities.hclean.ast.string;

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
 * JUnit Test for StringExpressionNode
 */
public final class StringExpressionNodeTest {

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Testing StringExpressionNode");
    }

    /**
     * Test of evaluate method of class StringExpressionNode.
     */
    @Test
    public void testEvaluate() {
        System.out.println("StringExpressionNode - testEvaluate");
        final StringExpressionNode instance = new StringExpressionNode(
                new StringLiteralNode(TestHelper.testString),
                new StringLiteralNode(TestHelper.testString),
                RuleNode.StringOperator.Concatenation);
        final String expResult = TestHelper.testString + TestHelper.testString;
        final String result = instance.evaluate(TestHelper.relativePathString, TestHelper.fileStatus);
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toRulenode method of class StringConcatenationNode.
     */
    @Test
    public void testToRulenode() {
        System.out.println("StringExpressionNode - testToRulenode");
        final StringLiteralNode literal = new StringLiteralNode(TestHelper.testString);
        final StringExpressionNode instance = new StringExpressionNode(
                literal, literal, RuleNode.StringOperator.Concatenation);
        final String expResult = String.format("%s + %s", literal.toRulenode(), literal.toRulenode());
        final String result = instance.toRulenode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toAstnode method of class StringExpressionNode.
     */
    @Test
    public void testToAstnode() {
        System.out.println("StringExpressionNode - testToAstnode");
        final StringLiteralNode literal = new StringLiteralNode(TestHelper.testString);
        final StringExpressionNode instance = new StringExpressionNode(
                literal, literal, RuleNode.StringOperator.Concatenation);
        final String expResult = String.format("StringExpressionNode( %s, %s, Concatenation )", literal.toAstnode(), literal.toAstnode());
        final String result = instance.toAstnode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of equalTo method of class StringExpressionNode.
     */
    @Test
    public void testEqualTo() {
        System.out.println("StringExpressionNode - testEqualTo");
        final StringExpressionNode instance1 = new StringExpressionNode(
                new StringLiteralNode(TestHelper.testString),
                new StringLiteralNode(TestHelper.testString),
                RuleNode.StringOperator.Concatenation);
        final StringExpressionNode instance2 = new StringExpressionNode(
                new StringLiteralNode(TestHelper.testString),
                new StringLiteralNode(TestHelper.testString),
                RuleNode.StringOperator.Concatenation);
        final TestNode testInstance = TestNode.instance;
        assertTrue(instance1.equalTo(instance2));
        assertFalse(instance1.equalTo(testInstance));
    }

    /**
     * Test of AST Generation of class StringExpressionNode.
     */
    @Test
    public void testAstGeneration() {
        System.out.println("StringExpressionNode - testAstGeneration");
        final StringLiteralNode literal = new StringLiteralNode(TestHelper.filenameString);
        final StringExpressionNode node = new StringExpressionNode(literal, literal, RuleNode.StringOperator.Concatenation);
        final String inputRuleset = node.toRulenode();
        System.out.printf("Input Ruleset: %s\n", inputRuleset);
        final ANTLRInputStream inStream = new ANTLRInputStream(inputRuleset);
        final HCleanLexer lexer = new HCleanLexer(inStream);
        final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        final HCleanParser parser = new HCleanParser(tokenStream);
        final ParseTreeWalker walker = new ParseTreeWalker();
        final RuleListener listener = new RuleListener();
        final ParseTree parseTree = parser.stringExpression();
        walker.walk(listener, parseTree);
        final String outputRuleSet = listener.peekStack().toRulenode();
        System.out.printf("Expected: %s\n", inputRuleset);
        System.out.printf("  Result: %s\n", outputRuleSet);
        assertEquals(inputRuleset, outputRuleSet);
    }

}
