package org.example.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.example.engine.MoveEngine;
import org.example.dto.GameStateDto;
import org.example.dto.SimpleMoveDto;
import org.example.model.Board;
import org.example.model.Point;


@Path("/game")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MoveController {

    private final MoveEngine engine = new MoveEngine();

    @POST
    @Path("/move")
    public SimpleMoveDto generateMove(GameStateDto dto) {
        int size = dto.getSize();
        Board board = new Board(size);
        String data = dto.getData().replace("\n", "").replace("\r", "");
        if (data.length() != size * size) {
            throw new BadRequestException("Некорректная длина data: ожидалось " + (size * size));
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                char c = data.charAt(i * size + j);
                if (c != ' ' && c != '_' ) {
                    board.setCell(new Point(i, j), c);
                }
            }
        }

        char myColor = dto.getNextPlayerColor().charAt(0);
        char opponentColor = (Character.toLowerCase(myColor) == 'b') ? 'w' : 'b';
        Point move = engine.generateMove(board, myColor, opponentColor);
        return new SimpleMoveDto(move.x(), move.y(), String.valueOf(myColor));
    }
}