package com.uhlisys.hadoop.utilities.hclean.ast.integer;

import com.uhlisys.hadoop.utilities.hclean.RuleNode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.hadoop.fs.FileStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TimeLiteral Node
 *
 * @see com.uhlisys.hadoop.utilities.hclean.RuleNode.IntegerNode
 */
public final class TimeLiteralNode extends RuleNode.IntegerNode {

    private static final Logger logger = LoggerFactory.getLogger(TimeLiteralNode.class);
    private static final Pattern p = Pattern.compile("(\\d+)\\s*(ms|w|d|h|m|s)?", Pattern.CASE_INSENSITIVE);
    final String timeString;
    final long value;

    public TimeLiteralNode(final String timeString) {
        this.timeString = timeString;
        final Matcher m = p.matcher(timeString);
        if (!m.matches()) {
            throw new RuntimeException("Error parsing Literal Time: " + timeString);
        }
        final long v = Long.parseLong(m.group(1));
        if (m.groupCount() > 1) {
            final String unit = m.group(2);
            if ("w".equalsIgnoreCase(unit.trim())) {
                this.value = v * 7 * 24 * 60 * 60 * 1000;
            } else if ("d".equalsIgnoreCase(unit.trim())) {
                this.value = v * 24 * 60 * 60 * 1000;
            } else if ("h".equalsIgnoreCase(unit.trim())) {
                this.value = v * 60 * 60 * 1000;
            } else if ("m".equalsIgnoreCase(unit.trim())) {
                this.value = v * 60 * 1000;
            } else if ("s".equalsIgnoreCase(unit.trim())) {
                this.value = v * 1000;
            } else if ("ms".equalsIgnoreCase(unit.trim())) {
                this.value = v;
            } else {
                throw new RuntimeException("Error parsing Literal Time: " + timeString);
            }
        } else {
            this.value = v;
        }
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
        return String.format("%s( '%s' ) -> %d", 
                getClass().getSimpleName(), 
                this.timeString, 
                evaluate(relativePath, fileStatus));
    }
    
    @Override
    public String toRulenode() {
        return "time{ '" + timeString + "' }";
    }

    @Override
    public String toAstnode() {
        return String.format("%s( '%s' = %d )", getClass().getSimpleName(), timeString, value);
    }

    @Override
    public boolean equalTo(RuleNode other) {
        return getClass().isInstance(other)
                && this.value == ((TimeLiteralNode) other).value;
    }
}
