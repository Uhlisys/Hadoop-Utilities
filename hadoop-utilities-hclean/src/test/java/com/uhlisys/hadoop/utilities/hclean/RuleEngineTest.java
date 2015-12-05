package com.uhlisys.hadoop.utilities.hclean;

import com.uhlisys.hadoop.utilities.hclean.ast.bool.BooleanExpressionNode;
import com.uhlisys.hadoop.utilities.hclean.ast.bool.BooleanLiteralNode;
import java.io.IOException;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class RuleEngineTest {

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Testing RuleEngine");
    }

    /**
     * Test of evaluate method of class StringComparisonNode.
     */
    @Test
    public void testEvaluate() throws IOException {
        System.out.println("RuleEngine - testEvaluate");
        final RuleEngine engine = new RuleEngine("true == true");
        final boolean expResult = true;
        final boolean result = engine.evaluate(TestHelper.relativePathString, TestHelper.fileStatus);
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toRulenode method, of class RuleEngine.
     */
    @Test
    public void testToRulenode() throws IOException {
        System.out.println("RuleEngine - testToRulenode");
        final RuleEngine engine = new RuleEngine("true == true");
        final String expResult = engine.toRulenode();
        final String result = engine.toRulenode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toAstnode method, of class RuleEngine.
     */
    @Test
    public void testToAstnode() {
        System.out.println("RuleEngine - testToAstnode");
        final BooleanLiteralNode literal = new BooleanLiteralNode(true);
        final BooleanExpressionNode node = new BooleanExpressionNode(literal, literal, RuleNode.BooleanOperator.Or);
        final RuleEngine engine = new RuleEngine(node.toRulenode());
        final String expResult = String.format("RuleEngine( %s )",node.toAstnode());
        final String result = engine.toAstnode();
        System.out.printf("Expected: %s\n", expResult);
        System.out.printf("  Result: %s\n", result);
        assertEquals(expResult, result);
    }

    /**
     * Test of equalToRuleEngine method, of class RuleEngine.
     */
    @Test
    public void testEqualToRuleEngine() {
        System.out.println("equalToRuleEngine");
        final BooleanLiteralNode literal = new BooleanLiteralNode(true);
        final BooleanExpressionNode node = new BooleanExpressionNode(literal, literal, RuleNode.BooleanOperator.Or);        
        final RuleEngine instance1 = new RuleEngine(node.toRulenode());
        final RuleEngine instance2 = new RuleEngine(node.toRulenode());
        assertTrue(instance1.equalToRuleEngine(instance2));
    }
}
