package com.uhlisys.hadoop.utilities.hclean.ast.integer;

import com.uhlisys.hadoop.utilities.hclean.RuleNode;
import java.util.Random;
import org.apache.hadoop.fs.FileStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RandomInteger Node
 *
 * @see com.uhlisys.hadoop.utilities.hclean.RuleNode.IntegerNode
 */
public final class RandomIntegerNode extends RuleNode.IntegerNode {

    private static final Logger logger = LoggerFactory.getLogger(RandomIntegerNode.class);
    private final Random rng = new Random();
    private final long seed;

    public RandomIntegerNode() {
        this(System.currentTimeMillis());
    }

    public RandomIntegerNode(final long seed) {
        rng.setSeed(this.seed = seed);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("construct %s", toAstnode()));
        }
    }

    @Override
    public long evaluate(final String relativePath, final FileStatus fileStatus) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("evaluating %s", toAstnode()));
        }
        final long value = rng.nextLong();
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("evaluated %s == %d", toAstnode(), value));
        }
        return value;
    }

    @Override
    public String evaluateToString(final String relativePath, final FileStatus fileStatus) {
        return String.format("%s( %d ) -> %d", 
                getClass().getSimpleName(), 
                this.seed, 
                evaluate(relativePath, fileStatus));
    }
    
    @Override
    public String toRulenode() {
        return "random";
    }

    @Override
    public String toAstnode() {
        return String.format("%s( %s )", getClass().getSimpleName(), this.seed);
    }

    @Override
    public boolean equalTo(final RuleNode other) {
        return getClass().isInstance(other);
    }
}
