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
 * JUnit Test for IntegerComparisonNode
 */
public final class IntegerComparisonNodeTest {

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Testing IntegerComparisonNode");
    }

    /**
     * Test of evaluate method of class IntegerComparisonNode.
     */
    @Test
    public void testEvaluate() {
        System.out.println("IntegerComparisonNode - testEvaluate");
        final IntegerLiteralNode literal = new IntegerLiteralNode( 1 );
        final IntegerComparisonNode instance = new IntegerComparisonNode(
                literal, literal, RuleNode.IntegerComparator.EqualTo);
        final boolean expResult = true;
        final boolean result = instance.evaluate(TestHelper.relativePathString, TestHelper.fileStatus);
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toRulenode method of class IntegerComparisonNode.
     */
    @Test
    public void testToRulenode() {
        System.out.println("IntegerComparisonNode - testToRulenode");
        final IntegerLiteralNode literal = new IntegerLiteralNode( 1 );
        final IntegerComparisonNode instance = new IntegerComparisonNode(
                literal, literal, RuleNode.IntegerComparator.EqualTo);
        final String expResult = String.format("%s == %s", literal.toRulenode(), literal.toRulenode());
        final String result = instance.toRulenode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toAstnode method of class IntegerComparisonNode.
     */
    @Test
    public void testToAstnode() {
        System.out.println("IntegerComparisonNode - testToAstnode");
        final IntegerLiteralNode literal = new IntegerLiteralNode( 1 );
        final IntegerComparisonNode instance = new IntegerComparisonNode(
                literal, literal, RuleNode.IntegerComparator.EqualTo);
        final String expResult = String.format("IntegerComparisonNode( %s, %s, EqualTo )", 
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
        System.out.println("IntegerComparisonNode - testEqualTo");
        final IntegerLiteralNode literal = new IntegerLiteralNode( 1 );
        final IntegerComparisonNode instance1 = new IntegerComparisonNode(
                literal, literal, RuleNode.IntegerComparator.EqualTo);
        final IntegerComparisonNode instance2 = new IntegerComparisonNode(
                literal, literal, RuleNode.IntegerComparator.EqualTo);
        final TestNode testInstance = TestNode.instance;
        assertTrue(instance1.equalTo(instance2));
        assertFalse(instance1.equalTo(testInstance));
    }
    
    /**
     * Test of AST Generation of class IntegerComparisonNode.
     */
    @Test
    public void testAstGeneration() {
        System.out.println("IntegerComparisonNode - testAstGeneration");
        final IntegerLiteralNode literal = new IntegerLiteralNode(TestHelper.atime);
        final IntegerComparisonNode node = new IntegerComparisonNode(literal,literal,RuleNode.IntegerComparator.EqualTo);
        final String inputRuleset = node.toRulenode();
        System.out.printf("Input Ruleset: %s\n", inputRuleset);
        final ANTLRInputStream inStream = new ANTLRInputStream(inputRuleset);
        final HCleanLexer lexer = new HCleanLexer(inStream);
        final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        final HCleanParser parser = new HCleanParser(tokenStream);
        final ParseTreeWalker walker = new ParseTreeWalker();
        final RuleListener listener = new RuleListener();
        final ParseTree parseTree = parser.integerComparison();
        walker.walk(listener, parseTree);
        final String outputRuleSet = listener.peekStack().toRulenode();
        System.out.printf("Expected: %s\n", inputRuleset);
        System.out.printf("  Result: %s\n", outputRuleSet);
        assertEquals(inputRuleset, outputRuleSet);
    }
}
