package com.uhlisys.hadoop.utilities.hclean.ast.integer;

import com.uhlisys.hadoop.utilities.hclean.RuleNode;
import org.apache.hadoop.fs.FileStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CurrentTime AST Node
 *
 * @see com.uhlisys.hadoop.utilities.hclean.RuleNode.IntegerNode
 */
public final class CurrentTimeNode extends RuleNode.IntegerNode {

    private static final Logger logger = LoggerFactory.getLogger(CurrentTimeNode.class);

    public CurrentTimeNode() {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("construct %s", toAstnode()));
        }
    }

    @Override
    public long evaluate(String relativePath, FileStatus fileStatus) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("evaluating %s", toAstnode()));
        }
        final long time = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("evaluated %s == %d", toAstnode(), time));
        }
        return time;
    }

    @Override
    public String evaluateToString(final String relativePath, final FileStatus fileStatus) {
        return String.format("%s() -> %d", 
                getClass().getSimpleName(), 
                evaluate(relativePath, fileStatus));
    }
    
    @Override
    public String toRulenode() {
        return "ctime";
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
