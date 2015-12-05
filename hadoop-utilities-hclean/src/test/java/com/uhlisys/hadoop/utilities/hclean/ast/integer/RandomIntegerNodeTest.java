package com.uhlisys.hadoop.utilities.hclean.ast.integer;

import com.uhlisys.hadoop.utilities.hclean.HCleanLexer;
import com.uhlisys.hadoop.utilities.hclean.HCleanParser;
import com.uhlisys.hadoop.utilities.hclean.RuleListener;
import com.uhlisys.hadoop.utilities.hclean.TestHelper;
import com.uhlisys.hadoop.utilities.hclean.TestNode;
import java.util.Random;
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
 * JUnit Test for RandomIntegerNode
 */
public final class RandomIntegerNodeTest {

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Testing RandomIntegerNode");
    }

    /**
     * Test of evaluate method of class RandomIntegerNode.
     */
    @Test
    public void testEvaluate() {
        System.out.println("RandomIntegerNode - testEvaluate");
        final Random rng = new Random(0);
        final RandomIntegerNode instance = new RandomIntegerNode(0);
        final long expResult = rng.nextLong();
        final long result = instance.evaluate(TestHelper.relativePathString, TestHelper.fileStatus);
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toRulenode method of class RandomIntegerNode.
     */
    @Test
    public void testToRulenode() {
        System.out.println("RandomIntegerNode - testToRulenode");
        final RandomIntegerNode instance = new RandomIntegerNode();
        final String expResult = "random";
        final String result = instance.toRulenode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toAstnode method of class RandomIntegerNode.
     */
    @Test
    public void testToAstnode() {
        System.out.println("RandomIntegerNode - testToAstnode");
        final long seed = 0;
        final RandomIntegerNode instance = new RandomIntegerNode(seed);
        final String expResult = String.format("RandomIntegerNode( %d )", seed);
        final String result = instance.toAstnode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of equalTo method of class RandomIntegerNode.
     */
    @Test
    public void testEqualTo() {
        System.out.println("RandomIntegerNode - testEqualTo");
        final RandomIntegerNode instance1 = new RandomIntegerNode();
        final RandomIntegerNode instance2 = new RandomIntegerNode();
        final TestNode testInstance = TestNode.instance;
        assertTrue(instance1.equalTo(instance2));
        assertFalse(instance1.equalTo(testInstance));
    }

    /**
     * Test of AST Generation of class RandomIntegerNode.
     */
    @Test
    public void testAstGeneration() {
        System.out.println("RandomIntegerNode - testAstGeneration");
        final RandomIntegerNode literal = new RandomIntegerNode();
        final String inputRuleset = literal.toRulenode();
        System.out.printf("Input Ruleset: %s\n", inputRuleset);
        final ANTLRInputStream inStream = new ANTLRInputStream(inputRuleset);
        final HCleanLexer lexer = new HCleanLexer(inStream);
        final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        final HCleanParser parser = new HCleanParser(tokenStream);
        final ParseTreeWalker walker = new ParseTreeWalker();
        final RuleListener listener = new RuleListener();
        final ParseTree parseTree = parser.randomInteger();
        walker.walk(listener, parseTree);
        final String outputRuleSet = listener.peekStack().toRulenode();
        System.out.printf("Expected: %s\n", inputRuleset);
        System.out.printf("  Result: %s\n", outputRuleSet);
        assertEquals(inputRuleset, outputRuleSet);
    }
}
