package org.example.player;

import org.example.command.GameContext;
import org.example.model.Board;
import org.example.model.Point;

public abstract class Player {
    protected final String name;
    protected final char color;
    protected Point currentMovePoint;

    public Player(String name, char color) {
        this.name = name;
        this.color = color;
    }

    public String getName() { return name; }
    public char getColor() { return color; }

    public abstract String makeMove(GameContext context);
    public void setCurrentMovePoint(Point currentMovePoint) {
        this.currentMovePoint = currentMovePoint;
    }
}
