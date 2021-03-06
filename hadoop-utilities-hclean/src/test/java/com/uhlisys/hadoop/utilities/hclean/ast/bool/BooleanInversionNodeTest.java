package com.uhlisys.hadoop.utilities.hclean.ast.bool;

import com.uhlisys.hadoop.utilities.hclean.HCleanLexer;
import com.uhlisys.hadoop.utilities.hclean.HCleanParser;
import com.uhlisys.hadoop.utilities.hclean.RuleListener;
import com.uhlisys.hadoop.utilities.hclean.TestHelper;
import com.uhlisys.hadoop.utilities.hclean.TestNode;
import com.uhlisys.hadoop.utilities.hclean.ast.integer.RandomIntegerNode;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit Test for BooleanInversionNode
 */
public final class BooleanInversionNodeTest {

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Testing BooleanInversionNode");
    }

    /**
     * Test of evaluate method of class BooleanInversionNode.
     */
    @Test
    public void testEvaluate() {
        System.out.println("BooleanInversionNode - testEvaluate");
        final BooleanLiteralNode literal = new BooleanLiteralNode(true);
        final BooleanInversionNode instance = new BooleanInversionNode(literal);
        final boolean expResult = false;
        final boolean result = instance.evaluate(TestHelper.relativePathString, TestHelper.fileStatus);
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toRulenode method of class BooleanInversionNode.
     */
    @Test
    public void testToRulenode() {
        System.out.println("BooleanInversionNode - testToRulenode");
        final BooleanLiteralNode literal = new BooleanLiteralNode(true);
        final BooleanInversionNode instance = new BooleanInversionNode(literal);        
        final String expResult = String.format("!%s",literal.toRulenode());
        final String result = instance.toRulenode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toAstnode method of class BooleanInversionNode.
     */
    @Test
    public void testToAstnode() {
        System.out.println("BooleanInversionNode - testToAstnode");
        final BooleanLiteralNode literal = new BooleanLiteralNode(true);
        final BooleanInversionNode instance = new BooleanInversionNode(literal);        
        final String expResult = String.format("BooleanInversionNode( %s )", literal.toAstnode());
        final String result = instance.toAstnode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of equalTo method of class BooleanInversionNode.
     */
    @Test
    public void testEqualTo() {
        System.out.println("BooleanInversionNode - testEqualTo");
        final BooleanLiteralNode literal = new BooleanLiteralNode(true);
        final BooleanInversionNode instance1 = new BooleanInversionNode(literal);        
        final BooleanInversionNode instance2 = new BooleanInversionNode(literal);
        final TestNode testInstance = TestNode.instance;
        assertTrue(instance1.equalTo(instance2));
        assertFalse(instance1.equalTo(testInstance));
    }
    
    
    /**
     * Test of AST Generation of class BooleanInversionNode.
     */
    @Test
    public void testAstGeneration() {
        System.out.println("BooleanInversionNode - testAstGeneration");
        final BooleanLiteralNode literal = new BooleanLiteralNode(true);
        final BooleanInversionNode node = new BooleanInversionNode(literal);
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