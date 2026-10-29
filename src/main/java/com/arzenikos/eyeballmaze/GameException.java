package com.arzenikos.eyeballmaze;
public class GameException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;

    private final ErrorCode errorCode;

    public GameException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
