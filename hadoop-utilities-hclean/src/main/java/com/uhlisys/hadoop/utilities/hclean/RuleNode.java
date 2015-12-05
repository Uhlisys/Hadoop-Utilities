package com.uhlisys.hadoop.utilities.hclean;

import org.apache.hadoop.fs.FileStatus;

/**
 * RuleNodes are the basis for all AST Nodes
 */
public interface RuleNode {

    /**
     * Interface for Integer AST Nodes
     */
    public abstract class IntegerNode implements RuleNode {

        /**
         * Return back integer based on rule
         *
         * @param relativePath
         * @param fileStatus
         * @return
         */
        public abstract long evaluate(String relativePath, FileStatus fileStatus);
    }

    /**
     * Interface for String AST Nodes
     */
    public abstract class StringNode implements RuleNode {

        /**
         * Return back integer based on rule
         *
         * @param relativePath
         * @param fileStatus
         * @return
         */
        public abstract String evaluate(String relativePath, FileStatus fileStatus);
    }

    /**
     * Interface for Boolean AST Nodes
     */
    public abstract class BooleanNode implements RuleNode {

        /**
         * Return back integer based on rule
         *
         * @param relativePath Relative path from the base path.
         * @param fileStatus Hadoop FileStatus Object
         * @return true if node evaluates properly
         */
        public abstract boolean evaluate(String relativePath, FileStatus fileStatus);
    }

    /**
     * Operators used for Integer Comparisons
     */
    public enum IntegerComparator {

        EqualTo,
        NotEqualTo,
        GreaterThan,
        GreaterThanOrEqualTo,
        LessThanOrEqualTo,
        LessThan;

        public static IntegerComparator getOperator(final String op) {
            switch (op) {
                case "==":
                    return EqualTo;
                case "!=":
                    return NotEqualTo;
                case ">":
                    return GreaterThan;
                case ">=":
                    return GreaterThanOrEqualTo;
                case "<":
                    return LessThan;
                case "<=":
                    return LessThanOrEqualTo;
                default:
                    throw new RulesetException("'" + op + "' is not a valid Integer operator.");
            }
        }
    }

    /**
     * Operators used by Integer Expressions
     */
    public enum IntegerOperator {

        Addition,
        Subtraction,
        Multiplication,
        Division,
        Exponentiation,
        Modulus;

        public static IntegerOperator getOperator(final String op) {
            switch (op) {
                case "+":
                    return Addition;
                case "-":
                    return Subtraction;
                case "*":
                    return Multiplication;
                case "/":
                    return Division;
                case "**":
                    return Exponentiation;
                case "%":
                    return Modulus;
                default:
                    throw new RulesetException("'" + op + "' is not a valid Integer Operator.");
            }
        }
    }

    /**
     * Operators used for String comparison
     */
    public enum StringComparator {

        Contains,
        Equal,
        NotEqual,
        Matches;

        public static StringComparator getOperator(final String op) {
            switch (op) {
                case "<>":
                    return Contains;
                case "==":
                    return Equal;
                case "!=":
                    return NotEqual;
                case "=~":
                    return Matches;
                default:
                    throw new RulesetException("'" + op + "' is not a valid String Comparator.");
            }
        }
    }

    public enum StringOperator {

        Concatenation;

        public static StringOperator getOperator(final String op) {
            switch (op) {
                case "+":
                    return Concatenation;
                default:
                    throw new RulesetException("'" + op + "' is not a valid String Operator.");
            }
        }
    }

    /**
     * Operators used for String comparison
     */
    public enum BooleanOperator {

        And,
        Or,
        Xor
    }

    /**
     * Evauluate
     *
     * @param relativePath
     * @param fileStatus
     * @return
     */
    String evaluateToString(String relativePath, FileStatus fileStatus);

    /**
     * Return back a string representation of this node in parsable format
     *
     * @return Parsable string representation of this node.
     */
    String toRulenode();

    /**
     * Return back a string representation of this node.
     *
     * @return String Representation of this AST Node
     */
    String toAstnode();

    /**
     * Test Node Equality
     *
     * @param other Node to compare to.
     * @return true if nodes are equal
     */
    boolean equalTo(RuleNode other);
}
