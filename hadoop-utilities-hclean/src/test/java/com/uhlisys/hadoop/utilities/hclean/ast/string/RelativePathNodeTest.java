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
 * JUnit Test for RelativePathNode
 */
public final class RelativePathNodeTest {

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Testing RelativePathNode");
    }

    /**
     * Test of evaluate method, of class RelativePathNode.
     */
    @Test
    public void testEvaluate() {
        System.out.println("RelativePathNode - testEvaluate");
        final RelativePathNode instance = new RelativePathNode();
        final String expResult = TestHelper.relativePathString;
        final String result = instance.evaluate(TestHelper.relativePathString, TestHelper.fileStatus);
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toRulenode method, of class RelativePathNode.
     */
    @Test
    public void testToRulenode() {
        System.out.println("RelativePathNode - testToRulenode");
        final RelativePathNode instance = new RelativePathNode();
        final String expResult = "rpath";
        final String result = instance.toRulenode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toAstnode method, of class RelativePathNode.
     */
    @Test
    public void testToAstnode() {
        System.out.println("RelativePathNode - testToAstnode");
        final RelativePathNode instance = new RelativePathNode();
        final String expResult = "RelativePathNode()";
        final String result = instance.toAstnode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of equalTo method, of class RelativePathNode.
     */
    @Test
    public void testEqualTo() {
        System.out.println("RelativePathNode - testEqualTo");
        final RelativePathNode instance1 = new RelativePathNode();
        final RelativePathNode instance2 = new RelativePathNode();
        final TestNode testInstance = TestNode.instance;
        assertTrue(instance1.equalTo(instance2));
        assertFalse(instance1.equalTo(testInstance));
    }

    /**
     * Test of AST Generation of class RelativePathNode.
     */
    @Test
    public void testAstGeneration() {
        System.out.println("RelativePathNode - testAstGeneration");
        final RelativePathNode literal = new RelativePathNode();
        final String inputRuleset = literal.toRulenode();
        System.out.printf("Input Ruleset: %s\n", inputRuleset);
        final ANTLRInputStream inStream = new ANTLRInputStream(inputRuleset);
        final HCleanLexer lexer = new HCleanLexer(inStream);
        final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        final HCleanParser parser = new HCleanParser(tokenStream);
        final ParseTreeWalker walker = new ParseTreeWalker();
        final RuleListener listener = new RuleListener();
        final ParseTree parseTree = parser.relativePath();
        walker.walk(listener, parseTree);
        final String outputRuleSet = listener.peekStack().toRulenode();
        System.out.printf("Expected: %s\n", inputRuleset);
        System.out.printf("  Result: %s\n", outputRuleSet);
        assertEquals(inputRuleset, outputRuleSet);
    }
}
