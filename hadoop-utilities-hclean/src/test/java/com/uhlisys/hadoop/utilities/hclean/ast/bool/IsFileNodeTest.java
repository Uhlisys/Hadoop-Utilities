package com.uhlisys.hadoop.utilities.hclean.ast.bool;

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
 * JUnit Test for IsFileNode
 */
public final class IsFileNodeTest {

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Testing IsFileNode");
    }

    /**
     * Test of evaluate method of class IsFileNode.
     */
    @Test
    public void testEvaluate() {
        System.out.println("IsFileNode - testEvaluate");
        final IsFileNode instance = new IsFileNode();
        final boolean expResult = true;
        final boolean result = instance.evaluate(TestHelper.relativePathString, TestHelper.fileStatus);
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toRulenode method of class IsFileNode.
     */
    @Test
    public void testToRulenode() {
        System.out.println("IsFileNode - testToRulenode");
        final IsFileNode instance = new IsFileNode();
        final String expResult = "isFile";
        final String result = instance.toRulenode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toAstnode method of class IsFileNode.
     */
    @Test
    public void testToAstnode() {
        System.out.println("IsFileNode - testToAstnode");
        final IsFileNode instance = new IsFileNode();
        final String expResult = "IsFileNode()";
        final String result = instance.toAstnode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of equalTo method of class IsFileNode.
     */
    @Test
    public void testEqualTo() {
        System.out.println("IsFileNode - testEqualTo");
        final IsFileNode instance1 = new IsFileNode();
        final IsFileNode instance2 = new IsFileNode();
        final TestNode testInstance = TestNode.instance;
        assertTrue(instance1.equalTo(instance2));
        assertFalse(instance1.equalTo(testInstance));
    }
    
    /**
     * Test of AST Generation of class IsFileNode.
     */
    @Test
    public void testAstGeneration() {
        System.out.println("IsFileNode - testAstGeneration");
        final IsFileNode literal = new IsFileNode();
        final String inputRuleset = literal.toRulenode();
        System.out.printf("Input Ruleset: %s\n", inputRuleset);
        final ANTLRInputStream inStream = new ANTLRInputStream(inputRuleset);
        final HCleanLexer lexer = new HCleanLexer(inStream);
        final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        final HCleanParser parser = new HCleanParser(tokenStream);
        final ParseTreeWalker walker = new ParseTreeWalker();
        final RuleListener listener = new RuleListener();
        final ParseTree parseTree = parser.isFile();
        walker.walk(listener, parseTree);
        final String outputRuleSet = listener.peekStack().toRulenode();
        System.out.printf("Expected: %s\n", inputRuleset);
        System.out.printf("  Result: %s\n", outputRuleSet);
        assertEquals(inputRuleset, outputRuleSet);
    }
}
