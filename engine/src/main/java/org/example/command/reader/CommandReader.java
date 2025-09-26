package org.example.command.reader;

import org.example.command.Command;
import org.example.command.exception.CommandParseException;

import java.io.IOException;


public interface CommandReader extends AutoCloseable{
    Command readCommand() throws IOException, CommandParseException;
}
