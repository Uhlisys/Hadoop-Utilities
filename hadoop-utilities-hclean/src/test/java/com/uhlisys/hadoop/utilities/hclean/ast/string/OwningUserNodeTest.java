package com.uhlisys.hadoop.utilities.hclean.ast.string;

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
 * JUnit Test for OwningUserNode
 */
public final class OwningUserNodeTest {

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Testing OwningUserNode");
    }

    /**
     * Test of evaluate method of class OwningUserNode.
     */
    @Test
    public void testEvaluate() {
        System.out.println("OwningUserNode - testEvaluate");
        final OwningUserNode instance = new OwningUserNode();
        final String expResult = TestHelper.fileOwner;
        final String result = instance.evaluate(TestHelper.relativePathString, TestHelper.fileStatus);
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toRulenode method of class OwningUserNode.
     */
    @Test
    public void testToRulenode() {
        System.out.println("OwningUserNode - testToRulenode");
        final OwningUserNode instance = new OwningUserNode();
        final String expResult = "user";
        final String result = instance.toRulenode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toAstnode method of class OwningUserNode.
     */
    @Test
    public void testToAstnode() {
        System.out.println("OwningUserNode - testToAstnode");
        final OwningUserNode instance = new OwningUserNode();
        final String expResult = "OwningUserNode()";
        final String result = instance.toAstnode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of equalTo method of class OwningUserNode.
     */
    @Test
    public void testEqualTo() {
        System.out.println("OwningUserNode - testEqualTo");
        final OwningUserNode instance1 = new OwningUserNode();
        final OwningUserNode instance2 = new OwningUserNode();
        final TestNode testInstance = TestNode.instance;
        assertTrue(instance1.equalTo(instance2));
        assertFalse(instance1.equalTo(testInstance));
    }

    /**
     * Test of AST Generation of class OwningUserNode.
     */
    @Test
    public void testAstGeneration() {
        System.out.println("OwningUserNode - testAstGeneration");
        final OwningUserNode literal = new OwningUserNode();
        final String inputRuleset = literal.toRulenode();
        System.out.printf("Input Ruleset: %s\n", inputRuleset);
        final ANTLRInputStream inStream = new ANTLRInputStream(inputRuleset);
        final HCleanLexer lexer = new HCleanLexer(inStream);
        final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        final HCleanParser parser = new HCleanParser(tokenStream);
        final ParseTreeWalker walker = new ParseTreeWalker();
        final RuleListener listener = new RuleListener();
        final ParseTree parseTree = parser.owningUser();
        walker.walk(listener, parseTree);
        final String outputRuleSet = listener.peekStack().toRulenode();
        System.out.printf("Expected: %s\n", inputRuleset);
        System.out.printf("  Result: %s\n", outputRuleSet);
        assertEquals(inputRuleset, outputRuleSet);
    }
}
