package com.uhlisys.hadoop.utilities.hclean.ast.integer;

import com.uhlisys.hadoop.utilities.hclean.RuleNode;
import org.apache.hadoop.fs.FileStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Integer Literal Node.
 *
 * @see com.uhlisys.hadoop.utilities.hclean.RuleNode.IntegerNode
 */
public final class IntegerLiteralNode extends RuleNode.IntegerNode {

    private static final Logger logger = LoggerFactory.getLogger(IntegerLiteralNode.class);
    private final long value;

    public IntegerLiteralNode(final long value) {
        this.value = value;
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("construct %s", toAstnode()));
        }
    }

    @Override
    public long evaluate(final String relativePath, final FileStatus fileStatus) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("evaluating %s", toAstnode()));
        }
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("evaluated %s == %d", toAstnode(), value));
        }
        return value;
    }

    @Override
    public String evaluateToString(final String relativePath, final FileStatus fileStatus) {
        return String.format("%s( %d ) -> %d",
                getClass().getSimpleName(), value,
                evaluate(relativePath, fileStatus));
    }

    @Override
    public String toRulenode() {
        return String.valueOf(value);
    }

    @Override
    public String toAstnode() {
        return String.format("%s( %d )", getClass().getSimpleName(), value);
    }

    @Override
    public boolean equalTo(final RuleNode other) {
        return getClass().isInstance(other)
                && this.value == ((IntegerLiteralNode) other).value;
    }
}
