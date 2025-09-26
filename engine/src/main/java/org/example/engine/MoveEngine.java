package org.example.engine;

import org.example.model.Board;
import org.example.model.Point;

import java.util.*;

public class MoveEngine {
    private final Random random = new Random();

    public Point generateMove(Board board, char myColor, char opponentColor) {
        Point winMove = findWinningMove(board, myColor);
        if (winMove != null) return winMove;
        Point blockMove = findWinningMove(board, opponentColor);
        if (blockMove != null) return blockMove;
        return findBestHeuristicMove(board, myColor, opponentColor);
    }

    private Point findWinningMove(Board board, char color) {
        for (Point p : board.getFreeCells()) {
            board.setCell(p, color);
            boolean wins = hasSquare(board, color);
            board.clearCell(p);
            if (wins) return p;
        }
        return null;
    }


    private boolean hasSquare(Board board, char color) {
        List<Point> cells = new ArrayList<>();
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (board.getCell(i, j) == color) {
                    cells.add(new Point(i, j));
                }
            }
        }
        int n = cells.size();
        if (n < 4) return false;

        for (int a = 0; a < n; a++) {
            for (int b = a + 1; b < n; b++) {
                for (int c = b + 1; c < n; c++) {
                    for (int d = c + 1; d < n; d++) {
                        if (isSquare(cells.get(a), cells.get(b), cells.get(c), cells.get(d))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    private boolean isSquare(Point p1, Point p2, Point p3, Point p4) {
        int[] d = new int[]{
                dist2(p1, p2), dist2(p1, p3), dist2(p1, p4),
                dist2(p2, p3), dist2(p2, p4), dist2(p3, p4)
        };
        Arrays.sort(d);
        return d[0] > 0 &&
                d[0] == d[1] && d[1] == d[2] && d[2] == d[3] &&
                d[4] == d[5] && d[4] == 2 * d[0];
    }

    private int dist2(Point a, Point b) {
        int dx = a.x() - b.x();
        int dy = a.y() - b.y();
        return dx * dx + dy * dy;
    }

    private Point findBestHeuristicMove(Board board, char myColor, char opponentColor) {
        List<Point> free = board.getFreeCells();
        if (free.isEmpty()) return null;

        int n = board.getSize();
        int bestScore = Integer.MIN_VALUE;
        List<Point> bestMoves = new ArrayList<>();

        for (Point p : free) {
            int score = 0;
            int cx = n / 2, cy = n / 2;
            score -= (Math.abs(p.x() - cx) + Math.abs(p.y() - cy));
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;
                    int nx = p.x() + dx, ny = p.y() + dy;
                    if (nx >= 0 && ny >= 0 && nx < n && ny < n) {
                        if (board.getCell(nx, ny) == myColor) score += 3;
                        if (board.getCell(nx, ny) == opponentColor) score += 1;
                    }
                }
            }

            board.setCell(p, myColor);
            if (createsThreat(board, myColor)) score += 5;
            board.clearCell(p);

            if (score > bestScore) {
                bestScore = score;
                bestMoves.clear();
                bestMoves.add(p);
            } else if (score == bestScore) {
                bestMoves.add(p);
            }
        }

        return bestMoves.get(random.nextInt(bestMoves.size()));
    }

    private boolean createsThreat(Board board, char color) {
        List<Point> cells = new ArrayList<>();
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (board.getCell(i, j) == color) {
                    cells.add(new Point(i, j));
                }
            }
        }
        if (cells.size() < 3) return false;

        for (int a = 0; a < cells.size(); a++) {
            for (int b = a + 1; b < cells.size(); b++) {
                for (int c = b + 1; c < cells.size(); c++) {
                    Point[] triple = {cells.get(a), cells.get(b), cells.get(c)};
                    Point missing = squareFourth(triple, board);
                    if (missing != null && board.isCellFree(missing)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Point squareFourth(Point[] triple, Board board) {
        for (int i = 0; i < 3; i++) {
            for (int j = i + 1; j < 3; j++) {
                Point p1 = triple[i], p2 = triple[j], p3 = triple[3 - i - j];
                int dx = p2.x() - p1.x();
                int dy = p2.y() - p1.y();

                Point candidate1 = new Point(p3.x() + dx, p3.y() + dy);
                Point candidate2 = new Point(p3.x() - dx, p3.y() - dy);

                if (inBounds(candidate1, board.getSize())) return candidate1;
                if (inBounds(candidate2, board.getSize())) return candidate2;
            }
        }
        return null;
    }

    private boolean inBounds(Point p, int size) {
        return p.x() >= 0 && p.y() >= 0 && p.x() < size && p.y() < size;
    }
}

