package com.uhlisys.hadoop.utilities.hclean.ast.string;

import com.uhlisys.hadoop.utilities.hclean.RuleNode;
import org.apache.hadoop.fs.FileStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Concatenated String Node.
 *
 * @see com.uhlisys.hadoop.utilities.hclean.RuleNode.StringNode
 */
public final class StringExpressionNode extends RuleNode.StringNode {

    private static final Logger logger = LoggerFactory.getLogger(StringExpressionNode.class);
    private final RuleNode.StringNode left;
    private final RuleNode.StringNode right;
    private final RuleNode.StringOperator op;

    public StringExpressionNode(final StringNode left, final StringNode right, final StringOperator op) {
        this.left = left;
        this.right = right;
        this.op = op;
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("construct %s", toAstnode()));
        }
    }

    @Override
    public String evaluate(final String relativePath, final FileStatus fileStatus) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("evaluating %s", toAstnode()));
        }
        final String leftString = left.evaluate(relativePath, fileStatus);
        final String rightString = right.evaluate(relativePath, fileStatus);
        final String rv;
        switch (op) {
            case Concatenation: {
                rv = leftString + rightString;
                break;
            }
            default: {
                throw new RuntimeException("error evaluating " + toAstnode());
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("evaluated %s == '%s'", toAstnode(), rv);
        }
        return rv;
    }

    @Override
    public String evaluateToString(final String relativePath, final FileStatus fileStatus) {
        return String.format("%s( %s, %s, %s ) -> %s", getClass().getSimpleName(),
                left.evaluateToString(relativePath, fileStatus),
                right.evaluateToString(relativePath, fileStatus),
                op, evaluate(relativePath, fileStatus));
    }

    @Override
    public String toRulenode() {
        final String opString;
        switch (op) {
            case Concatenation: {
                opString = "+";
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
                && this.left.equalTo(((StringExpressionNode) other).left)
                && this.right.equalTo(((StringExpressionNode) other).right);
    }
}
