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
 * JUnit Test for TimeLiteralNode
 */
public final class TimeLiteralNodeTest {

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Testing TimeLiteralNode");
    }

    /**
     * Test of evaluate method of class TimeLiteralNode.
     */
    @Test
    public void testEvaluate() {
        System.out.println("TimeLiteralNode - testEvaluate");

        final TimeLiteralNode instance1 = new TimeLiteralNode("1w");
        final long expResult1 = TestHelper.weekInMs;
        final long result1 = instance1.evaluate(TestHelper.relativePathString, TestHelper.fileStatus);
        System.out.printf("Expected: %s\n", expResult1);
        System.out.printf("  Result: %s\n", result1);
        assertEquals(expResult1, result1);

        final TimeLiteralNode instance2 = new TimeLiteralNode("1d");
        final long expResult2 = TestHelper.dayInMs;
        final long result2 = instance2.evaluate(TestHelper.relativePathString, TestHelper.fileStatus);
        System.out.printf("Expected: %s\n", expResult2);
        System.out.printf("  Result: %s\n", result2);
        assertEquals(expResult2, result2);

        final TimeLiteralNode instance3 = new TimeLiteralNode("1h");
        final long expResult3 = TestHelper.hourInMs;
        final long result3 = instance3.evaluate(TestHelper.relativePathString, TestHelper.fileStatus);
        System.out.printf("Expected: %s\n", expResult3);
        System.out.printf("  Result: %s\n", result3);
        assertEquals(expResult3, result3);

        final TimeLiteralNode instance4 = new TimeLiteralNode("1m");
        final long expResult4 = TestHelper.minuteInMs;
        final long result4 = instance4.evaluate(TestHelper.relativePathString, TestHelper.fileStatus);
        System.out.printf("Expected: %s\n", expResult4);
        System.out.printf("  Result: %s\n", result4);
        assertEquals(expResult4, result4);

        final TimeLiteralNode instance5 = new TimeLiteralNode("1s");
        final long expResult5 = TestHelper.secondInMs;
        final long result5 = instance5.evaluate(TestHelper.relativePathString, TestHelper.fileStatus);
        System.out.printf("Expected: %s\n", expResult5);
        System.out.printf("  Result: %s\n", result5);
        assertEquals(expResult5, result5);

        final TimeLiteralNode instance6 = new TimeLiteralNode("1ms");
        final long expResult6 = 1;
        final long result6 = instance6.evaluate(TestHelper.relativePathString, TestHelper.fileStatus);
        System.out.printf("Expected: %s\n", expResult6);
        System.out.printf("  Result: %s\n", result6);
        assertEquals(expResult6, result6);
    }

    /**
     * Test of toRulenode method of class TimeLiteralNode.
     */
    @Test
    public void testToRulenode() {
        System.out.println("TimeLiteralNode - testToRulenode");
        final TimeLiteralNode instance = new TimeLiteralNode("1 ms");
        final String expResult = String.format("time{ '%s' }", "1 ms");
        final String result = instance.toRulenode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toAstnode method of class TimeLiteralNode.
     */
    @Test
    public void testToAstnode() {
        System.out.println("TimeLiteralNode - testToAstnode");
        final TimeLiteralNode instance = new TimeLiteralNode("1 ms");
        final String expResult = String.format("TimeLiteralNode( '%s' = %d )", "1 ms", 1);
        final String result = instance.toAstnode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of equalTo method of class TimeLiteralNode.
     */
    @Test
    public void testEqualTo() {
        System.out.println("TimeLiteralNode - testEqualTo");
        final TimeLiteralNode instance1 = new TimeLiteralNode("1ms");
        final TimeLiteralNode instance2 = new TimeLiteralNode("1ms");
        final TestNode testInstance = TestNode.instance;
        assertTrue(instance1.equalTo(instance2));
        assertFalse(instance1.equalTo(testInstance));
    }

    /**
     * Test of AST Generation of class TimeLiteralNode.
     */
    @Test
    public void testAstGeneration() {
        System.out.println("TimeLiteralNode - testAstGeneration");
        final TimeLiteralNode literal = new TimeLiteralNode("1 ms");
        final String inputRuleset = literal.toRulenode();
        System.out.printf("Input Ruleset: %s\n", inputRuleset);
        final ANTLRInputStream inStream = new ANTLRInputStream(inputRuleset);
        final HCleanLexer lexer = new HCleanLexer(inStream);
        final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        final HCleanParser parser = new HCleanParser(tokenStream);
        final ParseTreeWalker walker = new ParseTreeWalker();
        final RuleListener listener = new RuleListener();
        final ParseTree parseTree = parser.timeLiteral();
        walker.walk(listener, parseTree);
        final String outputRuleSet = listener.peekStack().toRulenode();
        System.out.printf("Expected: %s\n", inputRuleset);
        System.out.printf("  Result: %s\n", outputRuleSet);
        assertEquals(inputRuleset, outputRuleSet);
    }
}
