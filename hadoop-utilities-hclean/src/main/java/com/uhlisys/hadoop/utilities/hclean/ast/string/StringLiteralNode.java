package com.uhlisys.hadoop.utilities.hclean.ast.string;

import com.uhlisys.hadoop.utilities.hclean.RuleNode;
import org.apache.hadoop.fs.FileStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * String Literal Node.
 *
 * @see com.uhlisys.hadoop.utilities.hclean.RuleNode.StringNode
 */
public final class StringLiteralNode extends RuleNode.StringNode {

    private static final Logger logger = LoggerFactory.getLogger(StringLiteralNode.class);
    private final String value;

    public StringLiteralNode(final String value) {
        this.value = value;
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("construct %s", toAstnode()));
        }
    }

    @Override
    public String evaluate(final String relativePath, final FileStatus fileStatus) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("evaluating %s", toAstnode()));
        }
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("evaluated %s == '%s'", toAstnode(), value));
        }
        return value;
    }

    @Override
    public String evaluateToString(final String relativePath, final FileStatus fileStatus) {
        return String.format("%s('%s') -> %s", getClass().getSimpleName(), value, value);
    }
    
    @Override
    public String toRulenode() {
        return "'" + value + "'";
    }

    @Override
    public String toAstnode() {
        return String.format("%s('%s')", getClass().getSimpleName(), value);
    }

    @Override
    public boolean equalTo(final RuleNode other) {
        return getClass().isInstance(other)
                && this.value.equals(((StringLiteralNode) other).value);
    }
}
