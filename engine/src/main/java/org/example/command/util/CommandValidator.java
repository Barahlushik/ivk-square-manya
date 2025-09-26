package org.example.command.util;

import org.example.command.exception.CommandParseException;
import org.example.model.Point;

public final class CommandValidator {

    private CommandValidator() {}

    public static GameCommandArgs gameCommand(String[] parts) throws CommandParseException {
        if (parts.length != 6) {
            throw new CommandParseException("Использование: GAME <size> <U1_TYPE> <U1_COLOR> <U2_TYPE> <U2_COLOR>");
        }

        int size;
        try {
            size = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new CommandParseException("Некорректный размер доски: " + parts[1]);
        }

        if (size < 3) {
            throw new CommandParseException("Размер доски должен быть не меньше 3");
        }

        String u1Type = parts[2];
        String u1Color = parts[3];
        String u2Type = parts[4];
        String u2Color = parts[5];

        validatePlayerType(u1Type);
        validatePlayerType(u2Type);
        validateColor(u1Color);
        validateColor(u2Color);

        return new GameCommandArgs(size, u1Type, u1Color, u2Type, u2Color);
    }

    public static Point moveCommand(String[] parts) throws CommandParseException {
        if (parts.length != 3) {
            throw new CommandParseException("Использование: MOVE <x> <y>");
        }

        try {
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            return new Point(x, y);
        } catch (NumberFormatException e) {
            throw new CommandParseException("Координаты должны быть целыми числами");
        }
    }

    private static void validatePlayerType(String type) throws CommandParseException {
        if (!(type.equalsIgnoreCase("user") || type.equalsIgnoreCase("comp"))) {
            throw new CommandParseException("Недопустимый тип игрока: " + type + ". Ожидается 'user' или 'comp'");
        }
    }

    private static void validateColor(String color) throws CommandParseException {
        if (!(color.equalsIgnoreCase("W") || color.equalsIgnoreCase("B"))) {
            throw new CommandParseException("Недопустимый цвет: " + color + ". Ожидается 'W' или 'B'");
        }
    }

    public record GameCommandArgs(int size, String u1Type, String u1Color, String u2Type, String u2Color) {}
}

