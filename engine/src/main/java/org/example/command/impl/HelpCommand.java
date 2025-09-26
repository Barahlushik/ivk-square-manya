package org.example.command.impl;

import org.example.command.Command;
import org.example.command.GameContext;

public class HelpCommand implements Command {

    @Override
    public String execute(GameContext context) {
        return """
                Доступные команды:
                GAME <size> <U1> <U2>   - начать новую игру с полем size x size
                MOVE <x> <y> - сделать ход в клетку (x, y)
                HELP         - показать эту справку
                EXIT         - выйти из программы
                """;
    }
}