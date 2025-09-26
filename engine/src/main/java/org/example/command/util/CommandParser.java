package org.example.command.util;

import org.example.command.Command;
import org.example.command.exception.CommandParseException;
import org.example.command.impl.ExitCommand;
import org.example.command.impl.GameCommand;
import org.example.command.impl.HelpCommand;
import org.example.command.impl.MoveCommand;
import org.example.model.Board;
import org.example.model.Point;
import org.example.player.ComputerPlayer;
import org.example.player.Player;
import org.example.player.UserPlayer;

public final class CommandParser {

    private CommandParser() {}

    public static Command parse(String in) throws CommandParseException {
        if (in == null || in.isBlank()) {
            throw new CommandParseException("Пустая команда");
        }

        String[] parts = in.trim().split("\\s+");
        String keyword = parts[0].toUpperCase();

        try {
            return switch (keyword) {
                case "GAME" -> {

                    if (parts.length != 6) {
                        throw new CommandParseException("Формат: GAME N TYPE1 C1 TYPE2 C2");
                    }
                    int size = parsePositiveInt(parts[1], "Размер доски");
                    if (size < 3) {
                        throw new CommandParseException("Размер доски должен быть >= 3");
                    }
                    Player fPlayer = parsePlayer("первый", parts[2], parts[3]);
                    Player sPlayer = parsePlayer("второй", parts[4], parts[5]);
                    yield new GameCommand(new Board(size), fPlayer, sPlayer);
                }
                case "MOVE" -> {
                    if (parts.length != 3) {
                        throw new CommandParseException("Формат: MOVE X Y");
                    }
                    int x = parsePositiveInt(parts[1], "X");
                    int y = parsePositiveInt(parts[2], "Y");
                    yield new MoveCommand(new Point(x, y));
                }
                case "EXIT" -> {
                    if (parts.length != 1) {
                        throw new CommandParseException("Команда EXIT не принимает аргументов");
                    }
                    yield new ExitCommand();
                }
                case "HELP" -> {
                    if (parts.length != 1) {
                        throw new CommandParseException("Команда HELP не принимает аргументов");
                    }
                    yield new HelpCommand();
                }
                default -> throw new CommandParseException("Неизвестная команда: " + keyword);
            };
        } catch (NumberFormatException e) {
            throw new CommandParseException("Некорректный числовой аргумент: " + e.getMessage(), e);
        }
    }

    private static int parsePositiveInt(String token, String name) throws CommandParseException {
        try {
            int value = Integer.parseInt(token);
            if (value < 0) {
                throw new CommandParseException(name + " должен быть неотрицательным числом");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new CommandParseException("Некорректное число для " + name + ": " + token, e);
        }
    }

    private static Player parsePlayer(String label, String type, String colorStr) throws CommandParseException {
        if (colorStr.length() != 1) {
            throw new CommandParseException("Цвет игрока " + label + " должен быть 'W' или 'B'");
        }
        char color = Character.toUpperCase(colorStr.charAt(0));
        if (color != 'W' && color != 'B') {
            throw new CommandParseException("Цвет игрока " + label + " должен быть 'W' или 'B'");
        }

        return switch (type.toLowerCase()) {
            case "user" -> new UserPlayer(label, color);
            case "comp" -> new ComputerPlayer(label, color);
            default -> throw new CommandParseException("Тип игрока " + label + " должен быть 'user' или 'comp'");
        };
    }
}