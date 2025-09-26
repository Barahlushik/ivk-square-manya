package org.example.command.reader.impl;

import org.example.command.Command;
import org.example.command.exception.CommandParseException;
import org.example.command.reader.CommandReader;
import org.example.command.util.CommandParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileCommandReader implements CommandReader {
    private final BufferedReader reader;

    public FileCommandReader(String filePath) throws IOException {
        this.reader = new BufferedReader(new FileReader(filePath));
    }

    @Override
    public Command readCommand() throws IOException {
        try {
            return CommandParser.parse(reader.readLine());
        } catch (CommandParseException e) {
            throw new RuntimeException("Ошибка скрипта: " + e.getMessage());
        }
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}