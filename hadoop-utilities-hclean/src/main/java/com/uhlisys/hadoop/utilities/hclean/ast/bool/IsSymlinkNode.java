package com.uhlisys.hadoop.utilities.hclean.ast.bool;

import com.uhlisys.hadoop.utilities.hclean.RuleNode;
import org.apache.hadoop.fs.FileStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Is Symlink Node.
 *
 * @see com.uhlisys.hadoop.utilities.hclean.RuleNode.BooleanNode
 */
public final class IsSymlinkNode extends RuleNode.BooleanNode {

    private static final Logger logger = LoggerFactory.getLogger(IsSymlinkNode.class);

    public IsSymlinkNode() {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("construct %s", toAstnode()));
        }
    }

    @Override
    public boolean evaluate(final String relativePath, final FileStatus fileStatus) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("evaluating %s", toAstnode()));
        }
        final boolean isSymlink = fileStatus.isSymlink();
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("evaluated %s == '%s'", toAstnode(), isSymlink));
        }
        return isSymlink;
    }

    @Override
    public String evaluateToString(final String relativePath, final FileStatus fileStatus) {
        return String.format("%s() -> %s",
                getClass().getSimpleName(),
                evaluate(relativePath, fileStatus));
    }

    @Override
    public String toRulenode() {
        return "isSymlink";
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
