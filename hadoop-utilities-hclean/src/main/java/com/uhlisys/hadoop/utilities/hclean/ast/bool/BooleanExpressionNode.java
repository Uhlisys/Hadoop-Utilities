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
public final class BooleanExpressionNode extends RuleNode.BooleanNode {

    private static final Logger logger = LoggerFactory.getLogger(BooleanExpressionNode.class);
    private final RuleNode.BooleanNode left;
    private final RuleNode.BooleanNode right;
    private final RuleNode.BooleanOperator op;

    public BooleanExpressionNode(final RuleNode.BooleanNode left, final RuleNode.BooleanNode right, final RuleNode.BooleanOperator op) {
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
        final boolean rv;
        switch (op) {
            case And: {
                if (!left.evaluate(relativePath, fileStatus)) {
                    rv = false;
                    break;
                }
                if (!right.evaluate(relativePath, fileStatus)) {
                    rv = false;
                    break;
                }
                rv = true;
                break;
            }
            case Or: {
                if (left.evaluate(relativePath, fileStatus)) {
                    rv = true;
                    break;
                }
                if (right.evaluate(relativePath, fileStatus)) {
                    rv = true;
                    break;
                }
                rv = false;
                break;
            }
            case Xor: {
                if (left.evaluate(relativePath, fileStatus) ^ right.evaluate(relativePath, fileStatus)) {
                    rv = true;
                    break;
                }
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
            case And: {
                opString = "AND";
                break;
            }
            case Or: {
                opString = "OR";
                break;
            }
            case Xor: {
                opString = "XOR";
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
                && left.equalTo(((BooleanExpressionNode) other).left)
                && right.equalTo(((BooleanExpressionNode) other).right);
    }
}
