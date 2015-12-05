package com.uhlisys.hadoop.utilities.hclean.ast.bool;

import com.uhlisys.hadoop.utilities.hclean.RuleNode;
import java.util.regex.Pattern;
import org.apache.hadoop.fs.FileStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * String Evaluation Node
 *
 * @see com.uhlisys.hadoop.utilities.hclean.RuleNode.BooleanNode
 */
public final class StringComparisonNode extends RuleNode.BooleanNode {

    private static final Logger logger = LoggerFactory.getLogger(StringComparisonNode.class);
    private final RuleNode.StringNode left;
    private final RuleNode.StringNode right;
    private final RuleNode.StringComparator op;

    public StringComparisonNode(final RuleNode.StringNode left, final RuleNode.StringNode right, final RuleNode.StringComparator op) {
        this.left = left;
        this.right = right;
        this.op = op;
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("construct %s", toAstnode()));
        }
    }

    @Override
    public boolean evaluate(final String relativePath, final FileStatus fileStatus) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("evaluating %s", toAstnode()));
        }
        final String leftString = left.evaluate(relativePath, fileStatus);
        final String rightString = right.evaluate(relativePath, fileStatus);
        final boolean rv;
        switch (op) {
            case Contains: {
                rv = leftString.contains(rightString);
                break;
            }
            case Equal: {
                rv = leftString.equals(rightString);
                break;
            }
            case Matches: {
                rv = Pattern.compile(rightString).matcher(leftString).matches();
                break;
            }
            case NotEqual: {
                rv = !leftString.equals(rightString);
                break;
            }
            default: {
                throw new RuntimeException("error evaluating " + toAstnode());
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("evaluate %s == %s", toAstnode(), rv));
        }
        return rv;
    }

    @Override
    public String evaluateToString(final String relativePath, final FileStatus fileStatus) {
        return String.format("%s( %s, %s, %s ) -> %s",
                getClass().getSimpleName(),
                left.evaluateToString(relativePath, fileStatus),
                right.evaluateToString(relativePath, fileStatus),
                op, evaluate(relativePath, fileStatus));
    }

    @Override
    public String toRulenode() {
        final String opString;
        switch (op) {
            case Contains: {
                opString = "<>";
                break;
            }
            case Equal: {
                opString = "==";
                break;
            }
            case Matches: {
                opString = "=~";
                break;
            }
            case NotEqual: {
                opString = "!=";
                break;
            }
            default: {
                throw new RuntimeException("error evaluating " + toAstnode());
            }
        }
        return String.format("%s %s %s", left.toRulenode(), opString, right.toRulenode());
    }

    @Override
    public String toAstnode() {
        return String.format("%s( %s, %s, %s )", getClass().getSimpleName(), left.toAstnode(), right.toAstnode(), op);
    }

    @Override
    public boolean equalTo(final RuleNode other) {
        return getClass().isInstance(other)
                && this.left.equalTo(((StringComparisonNode) other).left)
                && this.right.equalTo(((StringComparisonNode) other).right);
    }
}
