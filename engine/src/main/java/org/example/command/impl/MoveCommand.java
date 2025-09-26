package org.example.command.impl;

import org.example.command.GameContext;
import org.example.model.Board;
import org.example.model.Point;
import org.example.command.Command;
import org.example.player.Player;

public class MoveCommand implements Command {
    private final Point point;

    public MoveCommand(Point point) { this.point = point; }

    @Override
    public String execute(GameContext context) {
        if (context.isGameOver()) {
            return "Игра завершена!";
        }

        Player player = context.getCurrentPlayer();
        player.setCurrentMovePoint(point);
        Board board = context.getBoard();
        String move = player.makeMove(context);

        StringBuilder sb = new StringBuilder();
        sb.append(move).append(board.getBoardString());
        if (board.hasSquare(player.getColor())) {
            sb.append("Игра закончена. ").append(player.getName())
                    .append(" (").append(player.getColor()).append(") выйграл!");
            context.setGameOver(true);
        } else if (board.isFull()) {
            sb.append("Игра закончена. Ничья");
            context.setGameOver(true);
        } else {
            context.switchPlayer();
        }

        return sb.toString();
    }
}
