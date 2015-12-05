package com.uhlisys.hadoop.utilities.hclean;

import com.uhlisys.hadoop.utilities.hclean.ast.bool.BooleanExpressionNode;
import com.uhlisys.hadoop.utilities.hclean.ast.bool.BooleanInversionNode;
import com.uhlisys.hadoop.utilities.hclean.ast.bool.BooleanLiteralNode;
import com.uhlisys.hadoop.utilities.hclean.ast.bool.IntegerComparisonNode;
import com.uhlisys.hadoop.utilities.hclean.ast.bool.IsDirectoryNode;
import com.uhlisys.hadoop.utilities.hclean.ast.bool.IsFileNode;
import com.uhlisys.hadoop.utilities.hclean.ast.bool.IsSymlinkNode;
import com.uhlisys.hadoop.utilities.hclean.ast.bool.StringComparisonNode;
import com.uhlisys.hadoop.utilities.hclean.ast.integer.AccessTimeNode;
import com.uhlisys.hadoop.utilities.hclean.ast.integer.CurrentTimeNode;
import com.uhlisys.hadoop.utilities.hclean.ast.integer.IntegerExpressionNode;
import com.uhlisys.hadoop.utilities.hclean.ast.integer.IntegerLiteralNode;
import com.uhlisys.hadoop.utilities.hclean.ast.integer.ModificationTimeNode;
import com.uhlisys.hadoop.utilities.hclean.ast.integer.PathConversionNode;
import com.uhlisys.hadoop.utilities.hclean.ast.integer.RandomIntegerNode;
import com.uhlisys.hadoop.utilities.hclean.ast.integer.RuntimeNode;
import com.uhlisys.hadoop.utilities.hclean.ast.integer.TimeLiteralNode;
import com.uhlisys.hadoop.utilities.hclean.ast.integer.TimestampLiteralNode;
import com.uhlisys.hadoop.utilities.hclean.ast.string.AbsolutePathNode;
import com.uhlisys.hadoop.utilities.hclean.ast.string.FilenameNode;
import com.uhlisys.hadoop.utilities.hclean.ast.string.OwningGroupNode;
import com.uhlisys.hadoop.utilities.hclean.ast.string.OwningUserNode;
import com.uhlisys.hadoop.utilities.hclean.ast.string.RelativePathNode;
import com.uhlisys.hadoop.utilities.hclean.ast.string.StringExpressionNode;
import com.uhlisys.hadoop.utilities.hclean.ast.string.StringLiteralNode;
import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener for Building Rule sets from HClean.g4 ANTLR Grammar
 */
public class RuleListener extends HCleanParserBaseListener {

    private final static Logger logger = LoggerFactory.getLogger(RuleListener.class);
    private final Stack<RuleNode> expressionStack = new Stack<>();
    boolean debug = false;

    public RuleListener() {
    }

    public RuleListener(boolean debug) {
        this.debug = debug;
    }

    public RuleNode.BooleanNode getExpression() {
        return !expressionStack.isEmpty() ? (RuleNode.BooleanNode) expressionStack.pop() : null;
    }

    public RuleNode peekStack() {
        return expressionStack.peek();
    }

    @Override
    public void enterBooleanAndExpression(final HCleanParser.BooleanAndExpressionContext baec) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterBooleanAndExpression");
        }
    }

    @Override
    public void exitBooleanAndExpression(final HCleanParser.BooleanAndExpressionContext baec) {
        final RuleNode.BooleanNode right = (RuleNode.BooleanNode) expressionStack.pop();
        final RuleNode.BooleanNode left = (RuleNode.BooleanNode) expressionStack.pop();
        final RuleNode.BooleanNode node = new BooleanExpressionNode(left, right, RuleNode.BooleanOperator.And);
        expressionStack.push(node);
        if (logger.isDebugEnabled()) {
            logger.debug("exitBooleanAndExpression");
        }
    }

    @Override
    public void enterBooleanXorExpression(final HCleanParser.BooleanXorExpressionContext bxec) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterBooleanXorExpression");
        }
    }

    @Override
    public void exitBooleanXorExpression(final HCleanParser.BooleanXorExpressionContext bxec) {        
        final RuleNode.BooleanNode right = (RuleNode.BooleanNode) expressionStack.pop();
        final RuleNode.BooleanNode left = (RuleNode.BooleanNode) expressionStack.pop();
        final RuleNode.BooleanNode node = new BooleanExpressionNode(left, right, RuleNode.BooleanOperator.Xor);
        expressionStack.push(node);
        if (logger.isDebugEnabled()) {
            logger.debug("exitBooleanXorExpression");
        }
    }

    @Override
    public void enterBooleanOrExpression(final HCleanParser.BooleanOrExpressionContext boec) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterBooleanOrExpression");
        }
    }

    @Override
    public void exitBooleanOrExpression(final HCleanParser.BooleanOrExpressionContext boec) {        
        final RuleNode.BooleanNode right = (RuleNode.BooleanNode) expressionStack.pop();
        final RuleNode.BooleanNode left = (RuleNode.BooleanNode) expressionStack.pop();
        final RuleNode.BooleanNode node = new BooleanExpressionNode(left, right, RuleNode.BooleanOperator.Or);
        expressionStack.push(node);
        if (logger.isDebugEnabled()) {
            logger.debug("exitBooleanOrExpression");
        }
    }

    @Override
    public void enterBooleanNotExpression(final HCleanParser.BooleanNotExpressionContext bnec) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterBooleanNotExpression");
        }
    }

    @Override
    public void exitBooleanNotExpression(final HCleanParser.BooleanNotExpressionContext bnec) {
        final RuleNode.BooleanNode left = (RuleNode.BooleanNode) expressionStack.pop();
        final RuleNode.BooleanNode node = new BooleanInversionNode(left);
        expressionStack.push(node);
        if (logger.isDebugEnabled()) {
            logger.debug("exitBooleanNotExpression");
        }        
    }

    @Override
    public void enterBooleanLiteral(final HCleanParser.BooleanLiteralContext blc) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterBooleanLiteral");
        }
    }

    @Override
    public void exitBooleanLiteral(final HCleanParser.BooleanLiteralContext blc) {
        final String text = blc.BOOLEANLITERAL().getText();
        final boolean value = "true".equalsIgnoreCase(text);
        final RuleNode.BooleanNode node = new BooleanLiteralNode(value);
        expressionStack.push(node);
        if (logger.isDebugEnabled()) {
            logger.debug("exitBooleanLiteral");
        }
    }

    @Override
    public void enterIsDirectory(final HCleanParser.IsDirectoryContext idc) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterIsDirectory");
        }
    }

    @Override
    public void exitIsDirectory(final HCleanParser.IsDirectoryContext idc) {
        final RuleNode.BooleanNode node = new IsDirectoryNode();
        expressionStack.push(node);
        if (logger.isDebugEnabled()) {
            logger.debug("exitIsDirectory");
        }
    }

    @Override
    public void enterIsFile(final HCleanParser.IsFileContext ifc) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterIsFile");
        }
    }

    @Override
    public void exitIsFile(final HCleanParser.IsFileContext ifc) {        
        final RuleNode.BooleanNode node = new IsFileNode();
        expressionStack.push(node);
        if (logger.isDebugEnabled()) {
            logger.debug("exitIsFile");
        }
    }

    @Override
    public void enterIsSymlink(final HCleanParser.IsSymlinkContext isc) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterIsSymlink");
        }
    }

    @Override
    public void exitIsSymlink(final HCleanParser.IsSymlinkContext isc) {
        final RuleNode.BooleanNode node = new IsSymlinkNode();
        expressionStack.push(node);
        if (logger.isDebugEnabled()) {
            logger.debug("exitIsSymlink");
        }        
    }

    /* Integer Nodes */
    @Override
    public void enterIntegerLessThanComparison(final HCleanParser.IntegerLessThanComparisonContext iltcc) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterIntegerLessThanComparison");
        }
    }

    @Override
    public void exitIntegerLessThanComparison(final HCleanParser.IntegerLessThanComparisonContext iltcc) {
        final RuleNode.IntegerNode right = (RuleNode.IntegerNode) expressionStack.pop();
        final RuleNode.IntegerNode left = (RuleNode.IntegerNode) expressionStack.pop();
        final RuleNode.BooleanNode node = new IntegerComparisonNode(left, right, RuleNode.IntegerComparator.LessThan);
        expressionStack.push(node);
        if (logger.isDebugEnabled()) {
            logger.debug("enterIntegerLessThanComparison");
        }        
    }

    @Override
    public void enterIntegerLessThanOrEqualComparison(final HCleanParser.IntegerLessThanOrEqualComparisonContext iltc) {
        if (logger.isDebugEnabled()) {
            logger.debug("exitIntegerLessThanOrEqualComparison");
        }
    }

    @Override
    public void exitIntegerLessThanOrEqualComparison(final HCleanParser.IntegerLessThanOrEqualComparisonContext iltc) {
        final RuleNode.IntegerNode right = (RuleNode.IntegerNode) expressionStack.pop();
        final RuleNode.IntegerNode left = (RuleNode.IntegerNode) expressionStack.pop();
        final RuleNode.BooleanNode node = new IntegerComparisonNode(left, right, RuleNode.IntegerComparator.LessThanOrEqualTo);
        expressionStack.push(node);
        if (logger.isDebugEnabled()) {
            logger.debug("exitIntegerLessThanOrEqualComparison");
        }        
    }

    @Override
    public void enterIntegerEqualComparison(final HCleanParser.IntegerEqualComparisonContext iecc) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterIntegerEqualComparison");
        }
    }

    @Override
    public void exitIntegerEqualComparison(final HCleanParser.IntegerEqualComparisonContext iecc) {
        final RuleNode.IntegerNode right = (RuleNode.IntegerNode) expressionStack.pop();
        final RuleNode.IntegerNode left = (RuleNode.IntegerNode) expressionStack.pop();
        expressionStack.push(new IntegerComparisonNode(left, right, RuleNode.IntegerComparator.EqualTo));
        if (logger.isDebugEnabled()) {
            logger.debug("exitIntegerLessThanOrEqualComparison");
        }        
    }

    @Override
    public void enterIntegerNonEqualComparison(final HCleanParser.IntegerNonEqualComparisonContext inecc) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterIntegerNonEqualComparison");
        }
    }

    @Override
    public void exitIntegerNonEqualComparison(final HCleanParser.IntegerNonEqualComparisonContext inecc) {
        final RuleNode.IntegerNode right = (RuleNode.IntegerNode) expressionStack.pop();
        final RuleNode.IntegerNode left = (RuleNode.IntegerNode) expressionStack.pop();
        final RuleNode.BooleanNode node = new IntegerComparisonNode(left, right, RuleNode.IntegerComparator.NotEqualTo);
        expressionStack.push(node);
        if (logger.isDebugEnabled()) {
            logger.debug("exitIntegerNonEqualComparison");
        }        
    }

    @Override
    public void enterIntegerGreaterThanOrEqualComparison(final HCleanParser.IntegerGreaterThanOrEqualComparisonContext igtc) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterIntegerGreaterThanOrEqualComparison");
        }
    }

    @Override
    public void exitIntegerGreaterThanOrEqualComparison(final HCleanParser.IntegerGreaterThanOrEqualComparisonContext igtc) {        
        final RuleNode.IntegerNode right = (RuleNode.IntegerNode) expressionStack.pop();
        final RuleNode.IntegerNode left = (RuleNode.IntegerNode) expressionStack.pop();
        final RuleNode.BooleanNode node = new IntegerComparisonNode(left, right, RuleNode.IntegerComparator.GreaterThanOrEqualTo);
        expressionStack.push(node);
        if (logger.isDebugEnabled()) {
            logger.debug("exitIntegerGreaterThanOrEqualComparison");
        }
    }

    @Override
    public void enterIntegerGreaterThanComparison(final HCleanParser.IntegerGreaterThanComparisonContext igtcc) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterIntegerGreaterThanComparison");
        }
    }

    @Override
    public void exitIntegerGreaterThanComparison(final HCleanParser.IntegerGreaterThanComparisonContext igtcc) {
        final RuleNode.IntegerNode right = (RuleNode.IntegerNode) expressionStack.pop();
        final RuleNode.IntegerNode left = (RuleNode.IntegerNode) expressionStack.pop();
        final RuleNode.BooleanNode node = new IntegerComparisonNode(left, right, RuleNode.IntegerComparator.GreaterThan);
        expressionStack.push(node);
        if (logger.isDebugEnabled()) {
            logger.debug("exitIntegerGreaterThanComparison");
        }
    }

    @Override
    public void enterIntegerAdditionExpression(final HCleanParser.IntegerAdditionExpressionContext iaec) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterIntegerAdditionExpression");
        }
    }

    @Override
    public void exitIntegerAdditionExpression(final HCleanParser.IntegerAdditionExpressionContext iaec) {
        final RuleNode.IntegerNode right = (RuleNode.IntegerNode) expressionStack.pop();
        final RuleNode.IntegerNode left = (RuleNode.IntegerNode) expressionStack.pop();
        final RuleNode.IntegerNode node = new IntegerExpressionNode(left, right, RuleNode.IntegerOperator.Addition);
        expressionStack.push(node);
        if (logger.isDebugEnabled()) {
            logger.debug("exitIntegerAdditionExpression");
        }        
    }

    @Override
    public void enterIntegerSubtractionExpression(final HCleanParser.IntegerSubtractionExpressionContext isec) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterIntegerSubtractionExpression");
        }
    }

    @Override
    public void exitIntegerSubtractionExpression(final HCleanParser.IntegerSubtractionExpressionContext isec) {
        final RuleNode.IntegerNode right = (RuleNode.IntegerNode) expressionStack.pop();
        final RuleNode.IntegerNode left = (RuleNode.IntegerNode) expressionStack.pop();
        final RuleNode.IntegerNode node = new IntegerExpressionNode(left, right, RuleNode.IntegerOperator.Subtraction);
        expressionStack.push(node);
        if (logger.isDebugEnabled()) {
            logger.debug("exitIntegerSubtractionExpression");
        }        
    }

    @Override
    public void enterIntegerMultiplicationExpression(final HCleanParser.IntegerMultiplicationExpressionContext imec) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterIntegerMultiplicationExpression");
        }
    }

    @Override
    public void exitIntegerMultiplicationExpression(final HCleanParser.IntegerMultiplicationExpressionContext imec) {
        final RuleNode.IntegerNode right = (RuleNode.IntegerNode) expressionStack.pop();
        final RuleNode.IntegerNode left = (RuleNode.IntegerNode) expressionStack.pop();
        final RuleNode.IntegerNode node = new IntegerExpressionNode(left, right, RuleNode.IntegerOperator.Multiplication);
        expressionStack.push(node);
        if (logger.isDebugEnabled()) {
            logger.debug("exitIntegerMultiplicationExpression");
        }        
    }

    @Override
    public void enterIntegerDivisionExpression(final HCleanParser.IntegerDivisionExpressionContext idec) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterIntegerDivisionExpression");
        }
    }

    @Override
    public void exitIntegerDivisionExpression(final HCleanParser.IntegerDivisionExpressionContext idec) {        
        final RuleNode.IntegerNode right = (RuleNode.IntegerNode) expressionStack.pop();
        final RuleNode.IntegerNode left = (RuleNode.IntegerNode) expressionStack.pop();
        final RuleNode.IntegerNode node = new IntegerExpressionNode(left, right, RuleNode.IntegerOperator.Division);
        expressionStack.push(node);
        if (logger.isDebugEnabled()) {
            logger.debug("exitIntegerDivisionExpression");
        }
    }

    @Override
    public void enterIntegerPowersExpression(final HCleanParser.IntegerPowersExpressionContext ipec) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterIntegerPowersExpression");
        }
    }

    @Override
    public void exitIntegerPowersExpression(final HCleanParser.IntegerPowersExpressionContext ipec) {        
        final RuleNode.IntegerNode right = (RuleNode.IntegerNode) expressionStack.pop();
        final RuleNode.IntegerNode left = (RuleNode.IntegerNode) expressionStack.pop();
        final RuleNode.IntegerNode node = new IntegerExpressionNode(left, right, RuleNode.IntegerOperator.Exponentiation);
        expressionStack.push(node);
        if (logger.isDebugEnabled()) {
            logger.debug("exitIntegerPowersExpression");
        }
    }

    @Override
    public void enterIntegerModuloExpression(final HCleanParser.IntegerModuloExpressionContext imec) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterIntegerModuloExpression");
        }
    }

    @Override
    public void exitIntegerModuloExpression(final HCleanParser.IntegerModuloExpressionContext imec) {
        final RuleNode.IntegerNode right = (RuleNode.IntegerNode) expressionStack.pop();
        final RuleNode.IntegerNode left = (RuleNode.IntegerNode) expressionStack.pop();
        final RuleNode.IntegerNode node = new IntegerExpressionNode(left, right, RuleNode.IntegerOperator.Modulus);
        expressionStack.push(node);
        if (logger.isDebugEnabled()) {
            logger.debug("exitIntegerModuloExpression");
        }        
    }

    @Override
    public void enterAccessTime(final HCleanParser.AccessTimeContext atc) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterAccessTime");
        }
    }

    @Override
    public void exitAccessTime(final HCleanParser.AccessTimeContext atc) {
        final RuleNode.IntegerNode node = new AccessTimeNode();
        expressionStack.push(node);
        if (logger.isDebugEnabled()) {
            logger.debug("exitAccessTime");
        }        
    }

    @Override
    public void enterCurrentTime(final HCleanParser.CurrentTimeContext ctc) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterCurrentTime");
        }
    }

    @Override
    public void exitCurrentTime(final HCleanParser.CurrentTimeContext ctc) {
        final RuleNode.IntegerNode node = new CurrentTimeNode();
        expressionStack.push(node);
        if (logger.isDebugEnabled()) {
            logger.debug("exitCurrentTime");
        }        
    }

    @Override
    public void enterIntegerLiteral(final HCleanParser.IntegerLiteralContext ilc) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterIntegerLiteral");
        }
    }

    @Override
    public void exitIntegerLiteral(final HCleanParser.IntegerLiteralContext ilc) {
        final long value = Long.parseLong(ilc.INTLITERAL().getText());
        expressionStack.push(new IntegerLiteralNode(value));
        if (logger.isDebugEnabled()) {
            logger.debug("exitIntegerLiteral");
        }        
    }

    @Override
    public void enterModifyTime(final HCleanParser.ModifyTimeContext mtc) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterModifyTime");
        }
    }

    @Override
    public void exitModifyTime(final HCleanParser.ModifyTimeContext mtc) {
        expressionStack.push(new ModificationTimeNode());
        if (logger.isDebugEnabled()) {
            logger.debug("exitModifyTime");
        }        
    }

    @Override
    public void enterPathConversion(final HCleanParser.PathConversionContext pcc) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterPathConversion");
        }
    }

    @Override
    public void exitPathConversion(final HCleanParser.PathConversionContext pcc) {
        final String text = pcc.STRLITERAL().getText();
        final String pathLiteral = text.substring(1, text.length() - 1);
        final RuleNode.IntegerNode defaultValue = (RuleNode.IntegerNode)expressionStack.pop();
        expressionStack.push(new PathConversionNode(pathLiteral, defaultValue));
        if (logger.isDebugEnabled()) {
            logger.debug("exitPathConversion");
        }        
    }

    @Override
    public void enterRandomInteger(final HCleanParser.RandomIntegerContext ric) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterRandomInteger");
        }
    }

    @Override
    public void exitRandomInteger(final HCleanParser.RandomIntegerContext ric) {
        expressionStack.push(new RandomIntegerNode());
        if (logger.isDebugEnabled()) {
            logger.debug("exitRandomInteger");
        }        
    }

    @Override
    public void enterRunTime(final HCleanParser.RunTimeContext rtc) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterRunTime");
        }
    }

    @Override
    public void exitRunTime(final HCleanParser.RunTimeContext rtc) {
        expressionStack.push(new RuntimeNode());
        if (logger.isDebugEnabled()) {
            logger.debug("exitRunTime");
        }        
    }

    @Override
    public void enterTimeLiteral(final HCleanParser.TimeLiteralContext tlc) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterTimeLiteral");
        }
    }

    @Override
    public void exitTimeLiteral(final HCleanParser.TimeLiteralContext tlc) {
        final String text = tlc.STRLITERAL().getText();
        final String value = text.substring(1, text.length() - 1);
        expressionStack.push(new TimeLiteralNode(value));
        if (logger.isDebugEnabled()) {
            logger.debug("enterTimeLiteral");
        }        
    }

    @Override
    public void enterDateLiteral(final HCleanParser.DateLiteralContext dlc) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterDateLiteral");
        }
    }

    @Override
    public void exitDateLiteral(final HCleanParser.DateLiteralContext tlc) {
        final String text = tlc.STRLITERAL().getText();
        final String value = text.substring(1, text.length()-1);        
        expressionStack.push(new TimestampLiteralNode(value, false));
        if (logger.isDebugEnabled()) {
            logger.debug("exitDateLiteral");
        }        
    }

    @Override
    public void enterTimestampLiteral(final HCleanParser.TimestampLiteralContext tlc) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterTimestampLiteral");
        }
    }

    @Override
    public void exitTimestampLiteral(final HCleanParser.TimestampLiteralContext tlc) {
        final String text = tlc.STRLITERAL().getText();
        final String value = text.substring(1, text.length()-1);
        expressionStack.push(new TimestampLiteralNode(value, true));
        if (logger.isDebugEnabled()) {
            logger.debug("exitTimestampLiteral");
        }        
    }

    /* String */
    @Override
    public void enterStringEquality(final HCleanParser.StringEqualityContext sec) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterStringEquality");
        }
    }

    @Override
    public void exitStringEquality(final HCleanParser.StringEqualityContext sec) {
        final RuleNode.StringNode right = (RuleNode.StringNode) expressionStack.pop();
        final RuleNode.StringNode left = (RuleNode.StringNode) expressionStack.pop();
        expressionStack.push(new StringComparisonNode(left, right, RuleNode.StringComparator.Equal));
        if (logger.isDebugEnabled()) {
            logger.debug("exitStringEquality");
        }
    }

    @Override
    public void enterStringNonEquality(final HCleanParser.StringNonEqualityContext snec) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterStringNonEquality");
        }
    }

    @Override
    public void exitStringNonEquality(final HCleanParser.StringNonEqualityContext snec) {
        final RuleNode.StringNode right = (RuleNode.StringNode) expressionStack.pop();
        final RuleNode.StringNode left = (RuleNode.StringNode) expressionStack.pop();
        expressionStack.push(new StringComparisonNode(left, right, RuleNode.StringComparator.NotEqual));
        if (logger.isDebugEnabled()) {
            logger.debug("exitStringNonEquality");
        }        
    }

    @Override
    public void enterStringContains(final HCleanParser.StringContainsContext scc) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterStringContains");
        }
    }

    @Override
    public void exitStringContains(final HCleanParser.StringContainsContext scc) {
        final RuleNode.StringNode right = (RuleNode.StringNode) expressionStack.pop();
        final RuleNode.StringNode left = (RuleNode.StringNode) expressionStack.pop();
        expressionStack.push(new StringComparisonNode(left, right, RuleNode.StringComparator.Contains));
        if (logger.isDebugEnabled()) {
            logger.debug("exitStringContains");
        }        
    }

    @Override
    public void enterStringMatches(final HCleanParser.StringMatchesContext smc) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterStringMatches");
        }
    }

    @Override
    public void exitStringMatches(final HCleanParser.StringMatchesContext smc) {
        final RuleNode.StringNode right = (RuleNode.StringNode) expressionStack.pop();
        final RuleNode.StringNode left = (RuleNode.StringNode) expressionStack.pop();
        expressionStack.push(new StringComparisonNode(left, right, RuleNode.StringComparator.Matches));
        if (logger.isDebugEnabled()) {
            logger.debug("exitStringMatches");
        }        
    }

    @Override
    public void enterStringConcatenationExpression(HCleanParser.StringConcatenationExpressionContext scec) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterStringConcatenationExpression");
        }
    }

    @Override
    public void exitStringConcatenationExpression(final HCleanParser.StringConcatenationExpressionContext scec) {
        final RuleNode.StringNode right = (RuleNode.StringNode) expressionStack.pop();
        final RuleNode.StringNode left = (RuleNode.StringNode) expressionStack.pop();
        expressionStack.push(new StringExpressionNode(left, right, RuleNode.StringOperator.Concatenation));
        if (logger.isDebugEnabled()) {
            logger.debug("exitStringConcatenationExpression");
        }        
    }

    @Override
    public void enterAbsolutePath(HCleanParser.AbsolutePathContext apc) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterAbsolutePath");
        }
    }

    @Override
    public void exitAbsolutePath(final HCleanParser.AbsolutePathContext apc) {
        expressionStack.push(new AbsolutePathNode());
        if (logger.isDebugEnabled()) {
            logger.debug("exitAbsolutePath");
        }        
    }

    @Override
    public void enterFilename(HCleanParser.FilenameContext fc) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterFilename");
        }
    }

    @Override
    public void exitFilename(final HCleanParser.FilenameContext fc) {
        expressionStack.push(new FilenameNode());
        if (logger.isDebugEnabled()) {
            logger.debug("exitFilename");
        }        
    }

    @Override
    public void enterOwningGroup(HCleanParser.OwningGroupContext ogc) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterOwningGroup");
        }
    }

    @Override
    public void exitOwningGroup(final HCleanParser.OwningGroupContext ogc) {
        expressionStack.push(new OwningGroupNode());
        if (logger.isDebugEnabled()) {
            logger.debug("exitOwningGroup");
        }        
    }

    @Override
    public void enterOwningUser(HCleanParser.OwningUserContext ouc) {
        if (logger.isDebugEnabled()) {
            logger.debug("exitOwningUser");
        }
    }

    @Override
    public void exitOwningUser(final HCleanParser.OwningUserContext ouc) {
        expressionStack.push(new OwningUserNode());
        if (logger.isDebugEnabled()) {
            logger.debug("exitOwningUser");
        }        
    }

    @Override
    public void enterRelativePath(HCleanParser.RelativePathContext rpc) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterRelativePath");
        }
    }

    @Override
    public void exitRelativePath(final HCleanParser.RelativePathContext rpc) {
        expressionStack.push(new RelativePathNode());
        if (logger.isDebugEnabled()) {
            logger.debug("exitRelativePath");
        }        
    }

    @Override
    public void enterStringLiteral(HCleanParser.StringLiteralContext slc) {
        if (logger.isDebugEnabled()) {
            logger.debug("enterStringLiteral");
        }
    }

    @Override
    public void exitStringLiteral(final HCleanParser.StringLiteralContext slc) {
        final String text = slc.STRLITERAL().getText();
        final String value = text.substring(1, text.length() - 1);
        expressionStack.push(new StringLiteralNode(value));
        if (logger.isDebugEnabled()) {
            logger.debug("exitStringLiteral");
        }        
    }

}
