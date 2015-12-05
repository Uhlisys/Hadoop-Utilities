package com.uhlisys.hadoop.utilities.hclean.ast.bool;

import com.uhlisys.hadoop.utilities.hclean.HCleanLexer;
import com.uhlisys.hadoop.utilities.hclean.HCleanParser;
import com.uhlisys.hadoop.utilities.hclean.RuleListener;
import com.uhlisys.hadoop.utilities.hclean.TestHelper;
import com.uhlisys.hadoop.utilities.hclean.TestNode;
import com.uhlisys.hadoop.utilities.hclean.ast.integer.AccessTimeNode;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit Test for IsSymlinkNode
 */
public final class IsSymlinkNodeTest {

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Testing IsSymlinkNode");
    }

    /**
     * Test of evaluate method of class IsSymlinkNode.
     */
    @Test
    public void testEvaluate() {
        System.out.println("IsSymlinkNode - testEvaluate");
        final IsSymlinkNode instance = new IsSymlinkNode();
        final boolean expResult = false;
        final boolean result = instance.evaluate(TestHelper.relativePathString, TestHelper.fileStatus);
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toRulenode method of class IsSymlinkNode.
     */
    @Test
    public void testToRulenode() {
        System.out.println("IsSymlinkNode - testToRulenode");
        final IsSymlinkNode instance = new IsSymlinkNode();
        final String expResult = "isSymlink";
        final String result = instance.toRulenode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toAstnode method of class IsSymlinkNode.
     */
    @Test
    public void testToAstnode() {
        System.out.println("IsSymlinkNode - testToAstnode");
        final IsSymlinkNode instance = new IsSymlinkNode();
        final String expResult = "IsSymlinkNode()";
        final String result = instance.toAstnode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of equalTo method of class IsSymlinkNode.
     */
    @Test
    public void testEqualTo() {
        System.out.println("IsSymlinkNode - testEqualTo");
        final IsSymlinkNode instance1 = new IsSymlinkNode();
        final IsSymlinkNode instance2 = new IsSymlinkNode();
        final TestNode testInstance = TestNode.instance;
        assertTrue(instance1.equalTo(instance2));
        assertFalse(instance1.equalTo(testInstance));
    }
    
    /**
     * Test of AST Generation of class IsSymlinkNode.
     */
    @Test
    public void testAstGeneration() {
        System.out.println("IsSymlinkNode - testAstGeneration");
        final IsSymlinkNode literal = new IsSymlinkNode();
        final String inputRuleset = literal.toRulenode();
        System.out.printf("Input Ruleset: %s\n", inputRuleset);
        final ANTLRInputStream inStream = new ANTLRInputStream(inputRuleset);
        final HCleanLexer lexer = new HCleanLexer(inStream);
        final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        final HCleanParser parser = new HCleanParser(tokenStream);
        final ParseTreeWalker walker = new ParseTreeWalker();
        final RuleListener listener = new RuleListener();
        final ParseTree parseTree = parser.isSymlink();
        walker.walk(listener, parseTree);
        final String outputRuleSet = listener.peekStack().toRulenode();
        System.out.printf("Expected: %s\n", inputRuleset);
        System.out.printf("  Result: %s\n", outputRuleSet);
        assertEquals(inputRuleset, outputRuleSet);
    }
}
