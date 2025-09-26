package org.example.command.reader;

import org.example.command.reader.impl.ConsoleCommandReader;
import org.example.command.reader.impl.FileCommandReader;

import java.io.File;

public class CommandReaderFactory {

    public static CommandReader create(String path) throws Exception {
        if (path != null && !path.isBlank()) {
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                return new FileCommandReader(file.getPath());
            } else {
                throw new IllegalArgumentException("Скрипт не найден: " + file.getPath());
            }
        }

        return new ConsoleCommandReader();
    }
}