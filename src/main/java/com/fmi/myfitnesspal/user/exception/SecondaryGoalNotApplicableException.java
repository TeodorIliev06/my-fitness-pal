package com.fmi.myfitnesspal.user.exception;

public class SecondaryGoalNotApplicableException extends RuntimeException {
    public SecondaryGoalNotApplicableException(String message) {
        super(message);
    }

    public SecondaryGoalNotApplicableException(String message, Throwable cause) {
        super(message, cause);
    }
}
