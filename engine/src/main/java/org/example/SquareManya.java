package org.example;

import org.example.command.*;
import org.example.command.exception.CommandParseException;
import org.example.command.reader.CommandReader;
import org.example.command.reader.CommandReaderFactory;
import org.example.model.Point;
import org.example.player.ComputerPlayer;
import org.example.player.Player;

public class SquareManya {

    public SquareManya() {

    }

    public static void run(String[] args) {
        System.out.println("Добро пожаловать в SquareManya");
        GameContext context = new GameContext();
        String scriptFile = args.length > 0 ? args[0] : null;

        try (CommandReader reader = CommandReaderFactory.create(scriptFile)) {
            while (!GameContext.shouldExit()) {
                Command c = null;
                try {
                    c = reader.readCommand();
                    if (c == null) break;
                    String result = c.execute(context);
                    if (result != null && !result.isBlank()) {
                        System.out.println(result);
                    }
                    while (!context.isGameOver() && context.getCurrentPlayer() instanceof ComputerPlayer) {
                        Player currentPlayer = context.getCurrentPlayer();
                        String resultComputeMove = currentPlayer.makeMove(context);
                        System.out.println(resultComputeMove);
                    }
                } catch (CommandParseException e) {
                    System.out.println("Неправильно написана команда: " + e.getMessage());
                } catch (IllegalStateException e) {
                    System.out.println("Команда недопустима в текущем контексте:" + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Плохо, разрабы получили по голове, бегут исправлять " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Фаталити: " + e.getMessage());
        }
    }
}
