package com.uhlisys.hadoop.utilities.hclean.ast.bool;

import com.uhlisys.hadoop.utilities.hclean.RuleNode;
import org.apache.hadoop.fs.FileStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Boolean Inversion Node
 *
 * @see com.uhlisys.hadoop.utilities.hclean.RuleNode.BooleanNode
 */
public final class BooleanInversionNode extends RuleNode.BooleanNode {

    private static final Logger logger = LoggerFactory.getLogger(BooleanInversionNode.class);
    private final RuleNode.BooleanNode left;

    public BooleanInversionNode(final RuleNode.BooleanNode left) {
        this.left = left;
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("construct %s", toAstnode()));
        }
    }

    @Override
    public boolean evaluate(final String relativePath, final FileStatus fileStatus) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("evaluating %s", toAstnode()));
        }
        final boolean rv = !left.evaluate(relativePath, fileStatus);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("evaluated %s == %s", toAstnode(), rv));
        }
        return rv;
    }

    @Override
    public String evaluateToString(final String relativePath, final FileStatus fileStatus) {
        return String.format("%s( %s ) -> %s", getClass().getSimpleName(),
                left.evaluateToString(relativePath, fileStatus),
                evaluate(relativePath, fileStatus));
    }

    @Override
    public String toRulenode() {
        return "!" + left.toRulenode();
    }

    @Override
    public String toAstnode() {
        return String.format("%s( %s )", getClass().getSimpleName(), left.toAstnode());
    }

    @Override
    public boolean equalTo(final RuleNode other) {
        return getClass().isInstance(other)
                && left.equalTo(((BooleanInversionNode) other).left);
    }
}
