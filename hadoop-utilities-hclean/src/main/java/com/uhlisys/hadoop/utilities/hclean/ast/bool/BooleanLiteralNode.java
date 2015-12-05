package com.uhlisys.hadoop.utilities.hclean.ast.bool;

import com.uhlisys.hadoop.utilities.hclean.RuleNode;
import org.apache.hadoop.fs.FileStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Boolean Literal Node
 *
 * @see com.uhlisys.hadoop.utilities.hclean.RuleNode.BooleanNode
 */
public final class BooleanLiteralNode extends RuleNode.BooleanNode {

    private static final Logger logger = LoggerFactory.getLogger(BooleanLiteralNode.class);
    private final boolean value;

    public BooleanLiteralNode(final boolean value) {
        this.value = value;
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("construct %s", toAstnode()));
        }
    }

    @Override
    public boolean evaluate(final String relativePath, final FileStatus fileStatus) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("evaluating %s", toAstnode()));
        }
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("evaluated %s == %s", toAstnode(), toRulenode()));
        }
        return value;        
    }
    
         @Override
    public String evaluateToString(final String relativePath, final FileStatus fileStatus) {
        return String.format("%s( %s ) -> %s", 
                getClass().getSimpleName(), 
                value ? "true" : "false",
                evaluate(relativePath, fileStatus));
    }


    @Override
    public String toRulenode() {
        return value ? "true" : "false";
    }

    @Override
    public String toAstnode() {
        return String.format("%s( %s )", getClass().getSimpleName(), toRulenode());
    }

    @Override
    public boolean equalTo(final RuleNode other) {
        return getClass().isInstance(other)
                && this.value == ((BooleanLiteralNode) other).value;
    }
}
