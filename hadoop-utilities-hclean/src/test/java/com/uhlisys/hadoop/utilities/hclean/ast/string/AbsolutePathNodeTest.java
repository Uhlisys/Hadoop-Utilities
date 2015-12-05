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
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit Test for AbsolutePathNode
 */
public final class AbsolutePathNodeTest {

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Testing AbsolutePathNode");
    }

    /**
     * Test of evaluate method of class AbsolutePathNode.
     */
    @Test
    public void testEvaluate() {
        System.out.println("AbsolutePathNode - testEvaluate");
        final AbsolutePathNode instance = new AbsolutePathNode();
        final String expResult = TestHelper.absolutePathString;
        final String result = instance.evaluate(TestHelper.relativePathString, TestHelper.fileStatus);
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toRulenode method of class AbsolutePathNode.
     */
    @Test
    public void testToRulenode() {
        System.out.println("AbsolutePathNode - testToRulenode");
        final AbsolutePathNode instance = new AbsolutePathNode();
        final String expResult = "apath";
        final String result = instance.toRulenode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toAstnode method of class AbsolutePathNode.
     */
    @Test
    public void testToAstnode() {
        System.out.println("RelativePathNode - testToAstnode");
        final AbsolutePathNode instance = new AbsolutePathNode();
        final String expResult = "AbsolutePathNode()";
        final String result = instance.toAstnode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of equalTo method of class AbsolutePathNode.
     */
    @Test
    public void testEqualTo() {
        System.out.println("AbsolutePathNode - testEqualTo");
        final AbsolutePathNode instance1 = new AbsolutePathNode();
        final AbsolutePathNode instance2 = new AbsolutePathNode();
        final TestNode testInstance = TestNode.instance;
        assertTrue(instance1.equalTo(instance2));
        assertFalse(instance1.equalTo(testInstance));
    }

    /**
     * Test of AST Generation of class AbsolutePathNode.
     */
    @Test
    public void testAstGeneration() {
        System.out.println("AbsolutePathNode - testAstGeneration");
        final AbsolutePathNode literal = new AbsolutePathNode();
        final String inputRuleset = literal.toRulenode();
        System.out.printf("Input Ruleset: %s\n", inputRuleset);
        final ANTLRInputStream inStream = new ANTLRInputStream(inputRuleset);
        final HCleanLexer lexer = new HCleanLexer(inStream);
        final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        final HCleanParser parser = new HCleanParser(tokenStream);
        final ParseTreeWalker walker = new ParseTreeWalker();
        final RuleListener listener = new RuleListener();
        final ParseTree parseTree = parser.absolutePath();
        walker.walk(listener, parseTree);
        final String outputRuleSet = listener.peekStack().toRulenode();
        System.out.printf("Expected: %s\n", inputRuleset);
        System.out.printf("  Result: %s\n", outputRuleSet);
        assertEquals(inputRuleset, outputRuleSet);
    }
}
