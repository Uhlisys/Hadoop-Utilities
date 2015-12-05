package com.uhlisys.hadoop.utilities.hclean.ast.integer;

import com.uhlisys.hadoop.utilities.hclean.RuleNode;
import org.apache.hadoop.fs.FileStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * String Evaluation Node
 *
 * @see com.uhlisys.hadoop.utilities.hclean.RuleNode.BooleanNode
 */
public final class IntegerExpressionNode extends RuleNode.IntegerNode {

    private static final Logger logger = LoggerFactory.getLogger(IntegerExpressionNode.class);
    private final RuleNode.IntegerNode left;
    private final RuleNode.IntegerNode right;
    private final RuleNode.IntegerOperator op;

    public IntegerExpressionNode(final RuleNode.IntegerNode left, final RuleNode.IntegerNode right, final RuleNode.IntegerOperator op) {
        this.left = left;
        this.right = right;
        this.op = op;
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("construct %s", toAstnode()));
        }
    }

    @Override
    public long evaluate(final String relativePath, final FileStatus fileStatus) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("evaluating %s", toAstnode()));
        }
        final long leftLong = left.evaluate(relativePath, fileStatus);
        final long rightLong = right.evaluate(relativePath, fileStatus);
        final long rv;
        switch (op) {
            case Addition: {
                rv = leftLong + rightLong;
                break;
            }
            case Division: {
                rv = leftLong / rightLong;
                break;
            }
            case Exponentiation: {
                rv = (long) Math.pow(leftLong, rightLong);
                break;
            }
            case Modulus: {
                rv = (leftLong % rightLong < 0)
                        ? (leftLong % rightLong) + rightLong
                        : leftLong % rightLong;
                break;
            }
            case Multiplication: {
                rv = leftLong * rightLong;
                break;
            }
            case Subtraction: {
                rv = leftLong - rightLong;
                break;
            }
            default: {
                throw new RuntimeException("error evaluating " + toAstnode());
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("evaluated %s == %s", toAstnode(), rv));
        }
        return rv;
    }

    @Override
    public String evaluateToString(final String relativePath, final FileStatus fileStatus) {
        return String.format("%s( %s, %s, %s ) -> %d", getClass().getSimpleName(),
                left.evaluateToString(relativePath, fileStatus),
                right.evaluateToString(relativePath, fileStatus),
                op, evaluate(relativePath, fileStatus));
    }

    @Override
    public String toRulenode() {
        final String opString;
        switch (op) {
            case Addition: {
                opString = "+";
                break;
            }
            case Division: {
                opString = "/";
                break;
            }
            case Exponentiation: {
                opString = "**";
                break;
            }
            case Modulus: {
                opString = "%";
                break;
            }
            case Multiplication: {
                opString = "*";
                break;
            }
            case Subtraction: {
                opString = "-";
                break;
            }
            default: {
                throw new RuntimeException("error returning " + toAstnode());
            }

        }
        return String.format("%s %s %s", left.toRulenode(), opString, right.toRulenode());
    }

    @Override
    public String toAstnode() {
        return String.format("%s( %s, %s, %s )", getClass().getSimpleName(), left.toAstnode(), right.toAstnode(), op);
    }

    @Override
    public boolean equalTo(final RuleNode other) {
        return getClass().isInstance(other)
                && this.left.equalTo(((IntegerExpressionNode) other).left)
                && this.right.equalTo(((IntegerExpressionNode) other).right);
    }
}
