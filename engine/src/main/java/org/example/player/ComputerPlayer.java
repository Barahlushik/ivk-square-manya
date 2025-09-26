package org.example.player;

import org.example.engine.MoveEngine;
import org.example.command.GameContext;

import org.example.model.Point;


public class ComputerPlayer extends Player {
    private final MoveEngine moveEngine;

    public ComputerPlayer(String name, char color) {
        super(name, color);
        this.moveEngine = new MoveEngine();
    }

    @Override
    public String makeMove(GameContext context) {
        Point move = moveEngine.generateMove(context.getBoard(), color, this.equals(context.getFirstPlayer()) ? context.getSecondPlayer().color : color);
        context.getBoard().setCell(move, color);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s (%c) пошел на (%d, %d)\n",
                name, color, move.x(), move.y()));
        sb.append(context.getBoard().getBoardString());
        if (context.getBoard().hasSquare(color)) {
            sb.append("Игра закончена. ").append(name)
                    .append(" (").append(color).append(") выйграл!");
            context.setGameOver(true);
        } else if (context.getBoard().isFull()) {
            sb.append("Игра закончена. Ничья");
            context.setGameOver(true);
        } else {
            context.switchPlayer();
        }

        return sb.toString();
    }


}