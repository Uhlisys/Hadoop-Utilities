package com.uhlisys.hadoop.utilities.hclean.ast.bool;

import com.uhlisys.hadoop.utilities.hclean.RuleNode;
import org.apache.hadoop.fs.FileStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Is File Node.
 *
 * @see com.uhlisys.hadoop.utilities.hclean.RuleNode.BooleanNode
 */
public final class IsFileNode extends RuleNode.BooleanNode {

    private static final Logger logger = LoggerFactory.getLogger(IsFileNode.class);

    public IsFileNode() {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("construct %s", toAstnode()));
        }
    }

    @Override
    public boolean evaluate(final String relativePath, final FileStatus fileStatus) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("evaluating %s", toAstnode()));
        }
        final boolean isFile = fileStatus.isFile();
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("evaluated %s == '%s'", toAstnode(), isFile));
        }
        return isFile;
    }

    @Override
    public String evaluateToString(final String relativePath, final FileStatus fileStatus) {
        return String.format("%s() -> %s",
                getClass().getSimpleName(),
                evaluate(relativePath, fileStatus));
    }

    @Override
    public String toRulenode() {
        return "isFile";
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
