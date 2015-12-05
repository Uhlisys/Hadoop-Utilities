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
 * JUnit Test for CurrentTimeNode
 */
public final class CurrentTimeNodeTest {

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Testing CurrentTimeNode");
    }

    /**
     * Test of evaluate method of class CurrentTimeNode.
     */
    @Test
    public void testEvaluate() {
        System.out.println("CurrentTimeNode - testEvaluate");
        final CurrentTimeNode instance = new CurrentTimeNode();
        final long expResult = System.currentTimeMillis();
        final long result = instance.evaluate(TestHelper.relativePathString, TestHelper.fileStatus);
        System.out.printf("Expected: %d - %d \n", expResult - 1000, expResult + 1000);
        System.out.printf("  Result: %s\n", result);
        assertTrue(expResult - 1000 <= result && result <= expResult + 1000);
    }

    /**
     * Test of toRulenode method of class CurrentTimeNode.
     */
    @Test
    public void testToRulenode() {
        System.out.println("CurrentTimeNode - testToRulenode");
        final CurrentTimeNode instance = new CurrentTimeNode();
        final String expResult = "ctime";
        final String result = instance.toRulenode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toAstnode method of class CurrentTimeNode.
     */
    @Test
    public void testToAstnode() {
        System.out.println("CurrentTimeNode - testToAstnode");
        final CurrentTimeNode instance = new CurrentTimeNode();
        final String expResult = String.format("CurrentTimeNode()");
        final String result = instance.toAstnode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of equalTo method of class CurrentTimeNode.
     */
    @Test
    public void testEqualTo() {
        System.out.println("CurrentTimeNode - testEqualTo");
        final CurrentTimeNode instance1 = new CurrentTimeNode();
        final CurrentTimeNode instance2 = new CurrentTimeNode();
        final TestNode testInstance = TestNode.instance;
        assertTrue(instance1.equalTo(instance2));
        assertFalse(instance1.equalTo(testInstance));
    }
    
    
    /**
     * Test of AST Generation of class CurrentTimeNode.
     */
    @Test
    public void testAstGeneration() {
        System.out.println("CurrentTimeNode - testAstGeneration");
        final CurrentTimeNode literal = new CurrentTimeNode();
        final String inputRuleset = literal.toRulenode();
        System.out.printf("Input Ruleset: %s\n", inputRuleset);
        final ANTLRInputStream inStream = new ANTLRInputStream(inputRuleset);
        final HCleanLexer lexer = new HCleanLexer(inStream);
        final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        final HCleanParser parser = new HCleanParser(tokenStream);
        final ParseTreeWalker walker = new ParseTreeWalker();
        final RuleListener listener = new RuleListener();
        final ParseTree parseTree = parser.currentTime();
        walker.walk(listener, parseTree);
        final String outputRuleSet = listener.peekStack().toRulenode();
        System.out.printf("Expected: %s\n", inputRuleset);
        System.out.printf("  Result: %s\n", outputRuleSet);
        assertEquals(inputRuleset, outputRuleSet);
    }
}
