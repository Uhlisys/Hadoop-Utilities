package com.uhlisys.hadoop.utilities.hclean.ast.integer;

import com.uhlisys.hadoop.utilities.hclean.RuleNode;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.hadoop.fs.FileStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;

/**
 * Timestamp Literal Node
 *
 * Format: Standard Java 7 Regex Pattern Accepted groups:
 * <ul>
 * <li>year</li>
 * <li>month</li>
 * <li>day</li>
 * <li>hour</li>
 * <li>minute</li>
 * <li>second</li>
 * <li>millisecond</li>
 * <li>dayperiod</li>
 * <li>hourperiod</li>
 * </ul>
 *
 * @see com.uhlisys.hadoop.utilities.hclean.RuleNode.IntegerNode
 */
public final class PathConversionNode extends RuleNode.IntegerNode {

    private static final Pattern groupPattern = Pattern.compile("\\(\\?<([A-Za-z]+)>");
    private static final Logger logger = LoggerFactory.getLogger(PathConversionNode.class);
    private final Set<String> groups = new TreeSet<>();
    private final Pattern pathPattern;
    private final RuleNode.IntegerNode defaultValue;

    public PathConversionNode(final String pathPatternString, final RuleNode.IntegerNode defaultValue) {
        this.pathPattern = Pattern.compile(pathPatternString);
        this.defaultValue = defaultValue;
        final Matcher matcher = groupPattern.matcher(pathPatternString);
        while (matcher.find()) {
            groups.add(pathPatternString.substring(matcher.start() + 3, matcher.end() - 1));
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
        final Long parsedValue = pathToValue(relativePath);
        if (parsedValue == null) {
            final long def = defaultValue.evaluate(relativePath, fileStatus);
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("evaluated %s == %s (%dms default)", toAstnode(), relativePath, def));
            }
            return defaultValue.evaluate(relativePath, fileStatus);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("evaluated %s == %s (%dms)", toAstnode(), relativePath, parsedValue));
        }
        return parsedValue;
    }

    public Long pathToValue(final String relativePath) {
        final Matcher matcher = pathPattern.matcher(relativePath);
        if (!matcher.find()) {
            return null;
        }
        final MutableDateTime calendar = new MutableDateTime(0, DateTimeZone.UTC);
        String value;
        if (groups.contains("year") && !(value = matcher.group("year")).isEmpty()) {
            calendar.set(DateTimeFieldType.year(), Integer.parseInt(value));
        }
        if (groups.contains("month") && !(value = matcher.group("month")).isEmpty()) {
            calendar.set(DateTimeFieldType.monthOfYear(), Integer.parseInt(value));
        }
        if (groups.contains("day") && !(value = matcher.group("day")).isEmpty()) {
            calendar.set(DateTimeFieldType.dayOfMonth(), Integer.parseInt(value));
        }
        if (groups.contains("hour") && !(value = matcher.group("hour")).isEmpty()) {
            calendar.set(DateTimeFieldType.hourOfDay(), Integer.parseInt(value));
        }
        if (groups.contains("minute") && !(value = matcher.group("minute")).isEmpty()) {
            calendar.set(DateTimeFieldType.minuteOfHour(), Integer.parseInt(value));
        }
        if (groups.contains("second") && !(value = matcher.group("second")).isEmpty()) {
            calendar.set(DateTimeFieldType.secondOfMinute(), Integer.parseInt(value));
        }
        if (groups.contains("dayperiod") && !(value = matcher.group("dayperiod")).isEmpty()) {
            calendar.setMillis(Long.parseLong(value) * 86400000);
        }
        if (groups.contains("hourperiod") && !(value = matcher.group("hourperiod")).isEmpty()) {
            calendar.setMillis(Long.parseLong(value) * 3600000);
        }
        return calendar.getMillis();
    }

    @Override
    public String evaluateToString(final String relativePath, final FileStatus fileStatus) {
        return String.format("%s( '%s' -> %d, %s ) -> %d",
                getClass().getSimpleName(), pathPattern.pattern(),
                pathToValue(relativePath),
                defaultValue.evaluateToString(relativePath, fileStatus),
                evaluate(relativePath, fileStatus));
    }

    @Override
    public String toRulenode() {
        return String.format("path{ '%s', %s }", pathPattern.pattern(), defaultValue.toRulenode());
    }

    @Override
    public String toAstnode() {
        return String.format("%s( '%s', %s )", getClass().getSimpleName(), pathPattern.pattern(), defaultValue.toAstnode());
    }

    @Override
    public boolean equalTo(final RuleNode other) {
        return getClass().isInstance(other)
                && this.pathPattern.pattern().equals(((PathConversionNode) other).pathPattern.pattern())
                && this.defaultValue.equalTo(((PathConversionNode) other).defaultValue);
    }
}
