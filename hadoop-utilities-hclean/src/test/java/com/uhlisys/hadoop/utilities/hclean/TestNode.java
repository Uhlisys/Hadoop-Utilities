package com.uhlisys.hadoop.utilities.hclean;

import org.apache.hadoop.fs.FileStatus;

/**
 * Used to test Object Equality
 */
public final class TestNode extends RuleNode.BooleanNode {

    public static final TestNode instance = new TestNode();

    @Override
    public boolean evaluate(final String relativePath, final FileStatus fileStatus) {
        return false;
    }

    @Override
    public String evaluateToString(final String relativePath, final FileStatus fileStatus) {
        return "";
    }

    @Override
    public String toRulenode() {
        return "";
    }

    @Override
    public boolean equalTo(final RuleNode other) {
        return getClass().equals(other.getClass());
    }

    @Override
    public String toAstnode() {
        return getClass().getSimpleName() + "()";
    }
}
