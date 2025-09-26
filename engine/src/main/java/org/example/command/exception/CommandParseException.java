package org.example.command.exception;

public class CommandParseException extends Exception {
    public CommandParseException(String message) {
        super(message);
    }
    public CommandParseException(String message, Throwable cause) {
        super(message, cause);
    }
}