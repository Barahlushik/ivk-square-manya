package org.example.command.impl;


import org.example.command.GameContext;
import org.example.command.Command;

public class ExitCommand implements Command {
    @Override
    public String execute(GameContext context) {
        GameContext.requestExit();
        return "Выход из игры...";
    }
}