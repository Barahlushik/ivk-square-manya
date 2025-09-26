package org.example.player;

import org.example.command.GameContext;
import org.example.model.Board;

public class UserPlayer extends Player {

    public UserPlayer(String name, char color) {
        super(name, color);
    }

    @Override
    public String makeMove(GameContext context) {
        if (currentMovePoint == null) {
            return "Точка хода не задана";
        }
        if (!context.getBoard().isCellFree(currentMovePoint)) {
            return "Клетка занята!";
        }
        context.getBoard().setCell(currentMovePoint,color);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s (%c) пошел на (%d, %d)\n",
                name, color, currentMovePoint.x(), currentMovePoint.y()));
        return sb.toString();
    }

}