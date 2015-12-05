package com.uhlisys.hadoop.utilities.hclean.ast.bool;

import com.uhlisys.hadoop.utilities.hclean.HCleanLexer;
import com.uhlisys.hadoop.utilities.hclean.HCleanParser;
import com.uhlisys.hadoop.utilities.hclean.RuleListener;
import com.uhlisys.hadoop.utilities.hclean.RuleNode;
import com.uhlisys.hadoop.utilities.hclean.TestHelper;
import com.uhlisys.hadoop.utilities.hclean.TestNode;
import com.uhlisys.hadoop.utilities.hclean.ast.integer.AccessTimeNode;
import com.uhlisys.hadoop.utilities.hclean.ast.string.StringLiteralNode;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit Test for StringComparisonNode
 */
public final class StringComparisonNodeTest {

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Testing StringComparisonNode");
    }

    /**
     * Test of evaluate method of class StringComparisonNode.
     */
    @Test
    public void testEvaluate() {
        System.out.println("StringComparisonNode - testEvaluate");
        final StringLiteralNode literal = new StringLiteralNode(TestHelper.testString);
        final StringComparisonNode instance = new StringComparisonNode(
                literal, literal, RuleNode.StringComparator.Equal);
        final boolean expResult = true;
        final boolean result = instance.evaluate(TestHelper.relativePathString, TestHelper.fileStatus);
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toRulenode method of class StringComparisonNode.
     */
    @Test
    public void testToRulenode() {
        System.out.println("StringComparisonNode - testToRulenode");
        final StringLiteralNode literal = new StringLiteralNode(TestHelper.testString);
        final StringComparisonNode instance = new StringComparisonNode(
                literal, literal, RuleNode.StringComparator.Equal);
        final String expResult = String.format("%s == %s", literal.toRulenode(), literal.toRulenode());
        final String result = instance.toRulenode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toAstnode method of class StringComparisonNode.
     */
    @Test
    public void testToAstnode() {
        System.out.println("StringComparisonNode - testToAstnode");
        final StringLiteralNode literal = new StringLiteralNode(TestHelper.testString);
        final StringComparisonNode instance = new StringComparisonNode(
                literal, literal, RuleNode.StringComparator.Equal);
        final String expResult = String.format("StringComparisonNode( %s, %s, Equal )", 
                literal.toAstnode(), literal.toAstnode());
        final String result = instance.toAstnode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of equalTo method of class StringComparisonNode.
     */
    @Test
    public void testEqualTo() {
        System.out.println("StringComparisonNode - testEqualTo");
        final StringLiteralNode literal = new StringLiteralNode(TestHelper.testString);
        final StringComparisonNode instance1 = new StringComparisonNode(
                literal, literal, RuleNode.StringComparator.Equal);        
        final StringComparisonNode instance2 = new StringComparisonNode(
                literal, literal, RuleNode.StringComparator.Equal);        
        final TestNode testInstance = TestNode.instance;
        assertTrue(instance1.equalTo(instance2));
        assertFalse(instance1.equalTo(testInstance));
    }
    
    /**
     * Test of AST Generation of class StringComparisonNode.
     */
    @Test
    public void testAstGeneration() {
        System.out.println("StringComparisonNode - testAstGeneration");
        final StringLiteralNode literal = new StringLiteralNode(TestHelper.testString);
        final StringComparisonNode node = new StringComparisonNode(literal, literal, RuleNode.StringComparator.Equal);
        final String inputRuleset = node.toRulenode();
        System.out.printf("Input Ruleset: %s\n", inputRuleset);
        final ANTLRInputStream inStream = new ANTLRInputStream(inputRuleset);
        final HCleanLexer lexer = new HCleanLexer(inStream);
        final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        System.out.println("====================================================================");
        tokenStream.fill();
        for(final Token token:tokenStream.getTokens()) {
            System.out.println(token);
        }
        System.out.println("====================================================================");        
        final HCleanParser parser = new HCleanParser(tokenStream);
        parser.addErrorListener(new DiagnosticErrorListener(false));        
        final ParseTreeWalker walker = new ParseTreeWalker();
        final RuleListener listener = new RuleListener();
        final ParseTree parseTree = parser.stringComparison();
        walker.walk(listener, parseTree);
        final String outputRuleSet = listener.peekStack().toRulenode();
        System.out.printf("Expected: %s\n", inputRuleset);
        System.out.printf("  Result: %s\n", outputRuleSet);
        assertEquals(inputRuleset, outputRuleSet);
    }
}
