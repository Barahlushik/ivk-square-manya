package org.example.command.impl;

import org.example.model.Board;
import org.example.command.GameContext;
import org.example.command.Command;
import org.example.player.Player;

public class GameCommand implements Command {
    private final Board board;
    private final Player first;
    private final Player second;

    public GameCommand(Board board, Player first, Player second) {
        this.board = board;
        this.first = first;
        this.second = second;
    }

    @Override
    public String execute(GameContext context) {
        context.setBoard(board);
        context.setFirstPlayer(first);
        context.setSecondPlayer(second);
        context.setCurrentPlayer(first);
        context.setGameOver(false);
        return "Ваше поле! Играйте!\n" + board.getBoardString();
    }
}