package com.uhlisys.hadoop.utilities.hclean.ast.integer;

import com.uhlisys.hadoop.utilities.hclean.RuleNode;
import org.apache.hadoop.fs.FileStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Timestamp Literal Node
 *
 * Format: yyyy-MM-dd'T'HH:mm:ss
 *
 * @see com.uhlisys.hadoop.utilities.hclean.RuleNode.IntegerNode
 */
public final class TimestampLiteralNode extends RuleNode.IntegerNode {

    private static final Logger logger = LoggerFactory.getLogger(TimestampLiteralNode.class);
    private static final DateTimeFormatter timestampFormatter = ISODateTimeFormat.dateHourMinuteSecond().withZoneUTC();
    private static final DateTimeFormatter dateFormatter = ISODateTimeFormat.date().withZoneUTC();
    private final DateTime calendar;
    private final String timestampString;

    public TimestampLiteralNode(final String timestampString, final boolean isTimestamp) {
        this.timestampString = timestampString;
        if (isTimestamp) {
            this.calendar = timestampFormatter.parseDateTime(timestampString);
        } else {
            this.calendar = dateFormatter.parseDateTime(timestampString);
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
        final long time = calendar.getMillis();
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("evaluated %s == %d", toAstnode(), time));
        }
        return time;
    }

    @Override
    public String evaluateToString(final String relativePath, final FileStatus fileStatus) {
        return String.format("%s( '%s' ) -> %d", 
                getClass().getSimpleName(), 
                this.timestampString, 
                evaluate(relativePath, fileStatus));
    }
    
    @Override
    public String toRulenode() {
        return "timestamp{ '" + timestampString + "' }";
    }

    @Override
    public String toAstnode() {
        return String.format("%s( '%s' )", getClass().getSimpleName(), this.timestampString);
    }

    @Override
    public boolean equalTo(final RuleNode other) {
        return getClass().isInstance(other)
                && calendar.getMillis() == ((TimestampLiteralNode) other).calendar.getMillis();
    }
}
