package com.uhlisys.hadoop.utilities.hclean.ast.string;

import com.uhlisys.hadoop.utilities.hclean.RuleNode;
import org.apache.hadoop.fs.FileStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Owning Group Node
 *
 * @see com.uhlisys.hadoop.utilities.hclean.RuleNode.StringNode
 */
public final class OwningGroupNode extends RuleNode.StringNode {

    private static final Logger logger = LoggerFactory.getLogger(OwningGroupNode.class);

    public OwningGroupNode() {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("construct %s", toAstnode()));
        }
    }

    @Override
    public String evaluate(final String relativePath, final FileStatus fileStatus) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("evaluating %s", toAstnode()));
        }
        final String group = fileStatus.getGroup();
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("evaluated %s == '%s'", toAstnode(), group));
        }
        return group;
    }

    @Override
    public String evaluateToString(final String relativePath, final FileStatus fileStatus) {
        return String.format("%s() -> %s", getClass().getSimpleName(),
                evaluate(relativePath, fileStatus));
    }

    @Override
    public String toRulenode() {
        return "group";
    }

    @Override
    public String toAstnode() {
        return String.format("%s()", getClass().getSimpleName());
    }

    @Override
    public boolean equalTo(final RuleNode other) {
        return getClass().isInstance(other);
    }
}
