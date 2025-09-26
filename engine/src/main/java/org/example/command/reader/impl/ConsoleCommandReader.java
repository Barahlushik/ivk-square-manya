package org.example.command.reader.impl;

import org.example.command.Command;
import org.example.command.exception.CommandParseException;
import org.example.command.reader.CommandReader;
import org.example.command.util.CommandParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleCommandReader implements CommandReader {
    private final BufferedReader reader;

    public ConsoleCommandReader() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public Command readCommand() throws IOException, CommandParseException {

        String line = reader.readLine();

        if (line == null) {
            return null;
        }

        return CommandParser.parse(line);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
