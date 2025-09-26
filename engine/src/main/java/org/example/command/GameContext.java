package org.example.command;

import org.example.model.Board;
import org.example.player.Player;

public class GameContext {
    private Board board;
    private Player firstPlayer;
    private Player secondPlayer;
    private Player currentPlayer;
    private boolean gameOver;
    private static boolean exit = false;

    public Board getBoard() { return board; }
    public void setBoard(Board board) { this.board = board; }

    public Player getFirstPlayer() { return firstPlayer; }
    public void setFirstPlayer(Player firstPlayer) { this.firstPlayer = firstPlayer; }

    public Player getSecondPlayer() { return secondPlayer; }
    public void setSecondPlayer(Player secondPlayer) { this.secondPlayer = secondPlayer; }

    public Player getCurrentPlayer() { return currentPlayer; }
    public void setCurrentPlayer(Player currentPlayer) { this.currentPlayer = currentPlayer; }

    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == firstPlayer) ? secondPlayer : firstPlayer;
    }

    public static boolean shouldExit() { return exit; }
    public static void requestExit() { exit = true; }
}

