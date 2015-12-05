package com.uhlisys.hadoop.utilities.hclean.ast.bool;

import com.uhlisys.hadoop.utilities.hclean.RuleNode;
import org.apache.hadoop.fs.FileStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * String Evaluation Node
 *
 * @see com.uhlisys.hadoop.utilities.hclean.RuleNode.BooleanNode
 */
public final class IntegerComparisonNode extends RuleNode.BooleanNode {

    private static final Logger logger = LoggerFactory.getLogger(IntegerComparisonNode.class);
    private final RuleNode.IntegerNode left;
    private final RuleNode.IntegerNode right;
    private final RuleNode.IntegerComparator op;

    public IntegerComparisonNode(final RuleNode.IntegerNode left, final RuleNode.IntegerNode right, final RuleNode.IntegerComparator op) {
        this.left = left;
        this.right = right;
        this.op = op;
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("construct %s", toAstnode()));
        }
    }

    @Override
    public boolean evaluate(final String relativePath, final FileStatus fileStatus) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("evaluating %s", toAstnode()));
        }
        final long leftLong = left.evaluate(relativePath, fileStatus);
        final long rightLong = right.evaluate(relativePath, fileStatus);
        final boolean rv;
        switch (op) {
            case EqualTo: {
                rv = leftLong == rightLong;
                break;
            }
            case GreaterThan: {
                rv = leftLong > rightLong;
                break;
            }
            case GreaterThanOrEqualTo: {
                rv = leftLong >= rightLong;
                break;
            }
            case LessThan: {
                rv = leftLong < rightLong;
                break;
            }
            case LessThanOrEqualTo: {
                rv = leftLong <= rightLong;
                break;
            }
            case NotEqualTo: {
                rv = leftLong != rightLong;
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
        return String.format("%s( %s, %s, %s ) -> %s",
                getClass().getSimpleName(),
                left.evaluateToString(relativePath, fileStatus),
                right.evaluateToString(relativePath, fileStatus),
                op, evaluate(relativePath, fileStatus));
    }

    @Override
    public String toRulenode() {
        final String opString;
        switch (op) {
            case EqualTo: {
                opString = "==";
                break;
            }
            case GreaterThan: {
                opString = ">";
                break;
            }
            case GreaterThanOrEqualTo: {
                opString = ">=";
                break;
            }
            case LessThan: {
                opString = "<";
                break;
            }
            case LessThanOrEqualTo: {
                opString = "<=";
                break;
            }
            case NotEqualTo: {
                opString = "!=";
                break;
            }
            default: {
                throw new RuntimeException("error evaluating " + toAstnode());
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
                && this.left.equalTo(((IntegerComparisonNode) other).left)
                && this.right.equalTo(((IntegerComparisonNode) other).right);
    }
}
