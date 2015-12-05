package com.uhlisys.hadoop.utilities.hclean;

/**
 * Exception Thrown while loading Rule Set
 */
public class RulesetException extends RuntimeException {

    public RulesetException() {
    }

    public RulesetException(String message) {
        super(message);
    }

    public RulesetException(Throwable cause) {
        super(cause);
    }

    public RulesetException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
