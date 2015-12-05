package com.uhlisys.hadoop.utilities.hclean.ast.bool;

import com.uhlisys.hadoop.utilities.hclean.HCleanLexer;
import com.uhlisys.hadoop.utilities.hclean.HCleanParser;
import com.uhlisys.hadoop.utilities.hclean.RuleListener;
import com.uhlisys.hadoop.utilities.hclean.RuleNode;
import com.uhlisys.hadoop.utilities.hclean.TestHelper;
import com.uhlisys.hadoop.utilities.hclean.TestNode;
import com.uhlisys.hadoop.utilities.hclean.ast.string.StringLiteralNode;
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
 * JUnit Test for IsDirectoryNode
 */
public final class IsDirectoryNodeTest {

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Testing IsDirectoryNode");
    }

    /**
     * Test of evaluate method of class IsDirectoryNode.
     */
    @Test
    public void testEvaluate() {
        System.out.println("IsDirectoryNode - testEvaluate");
        final IsDirectoryNode instance = new IsDirectoryNode();
        final boolean expResult = false;
        final boolean result = instance.evaluate(TestHelper.relativePathString, TestHelper.fileStatus);
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toRulenode method of class IsDirectoryNode.
     */
    @Test
    public void testToRulenode() {
        System.out.println("IsDirectoryNode - testToRulenode");
        final IsDirectoryNode instance = new IsDirectoryNode();
        final String expResult = "isDirectory";
        final String result = instance.toRulenode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toAstnode method of class IsDirectoryNode.
     */
    @Test
    public void testToAstnode() {
        System.out.println("IsDirectoryNode - testToAstnode");
        final IsDirectoryNode instance = new IsDirectoryNode();
        final String expResult = "IsDirectoryNode()";
        final String result = instance.toAstnode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of equalTo method of class IsDirectoryNode.
     */
    @Test
    public void testEqualTo() {
        System.out.println("IsDirectoryNode - testEqualTo");
        final IsDirectoryNode instance1 = new IsDirectoryNode();
        final IsDirectoryNode instance2 = new IsDirectoryNode();
        final TestNode testInstance = TestNode.instance;
        assertTrue(instance1.equalTo(instance2));
        assertFalse(instance1.equalTo(testInstance));
    }
    
    /**
     * Test of AST Generation of class IsDirectoryNode.
     */
    @Test
    public void testAstGeneration() {
        System.out.println("IsDirectoryNode - testAstGeneration");
        final IsDirectoryNode literal = new IsDirectoryNode();
        final String inputRuleset = literal.toRulenode();
        System.out.printf("Input Ruleset: %s\n", inputRuleset);
        final ANTLRInputStream inStream = new ANTLRInputStream(inputRuleset);
        final HCleanLexer lexer = new HCleanLexer(inStream);
        final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        final HCleanParser parser = new HCleanParser(tokenStream);
        final ParseTreeWalker walker = new ParseTreeWalker();
        final RuleListener listener = new RuleListener();
        final ParseTree parseTree = parser.isDirectory();
        walker.walk(listener, parseTree);
        final String outputRuleSet = listener.peekStack().toRulenode();
        System.out.printf("Expected: %s\n", inputRuleset);
        System.out.printf("  Result: %s\n", outputRuleSet);
        assertEquals(inputRuleset, outputRuleSet);
    }
}
