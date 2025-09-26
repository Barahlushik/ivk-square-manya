package org.example.model;


import java.util.*;

public class Board {
    private final int size;
    private final char[][] grid;
    private int filledCells = 0;

    public Board(int size) {
        if (size < 3) throw new IllegalArgumentException("Size must be >= 3");
        this.size = size;
        this.grid = new char[size][size];
    }

    public boolean isCellFree(int x, int y) {
        return grid[x][y] == 0;
    }

    public boolean isCellFree(Point p) {
        return isCellFree(p.x(), p.y());
    }

    public void setCell(Point p, char color) {
        if (!isCellFree(p.x(), p.y())) throw new IllegalArgumentException("Cell occupied: " + p);
        grid[p.x()][p.y()] = color;
        filledCells++;
    }

    public void clearCell(Point p) {
        if (grid[p.x()][p.y()] != 0) {
            grid[p.x()][p.y()] = 0;
            filledCells--;
        }
    }

    public List<Point> getFreeCells() {
        List<Point> free = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] == 0) free.add(new Point(i, j));
            }
        }
        return free;
    }

    public boolean isFull() {
        return filledCells == size * size;
    }


    public char getCell(int x, int y) {
        return grid[x][y];
    }

    public int getSize() {
        return size;
    }

    public String getBoardString() {
        StringBuilder board = new StringBuilder();
        for (int i = 0; i < size; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < size; j++) {
                char c = grid[i][j];
                sb.append(c == 0 ? '.' : c).append(' ');
            }
            board.append(sb.toString().trim());
            if (i < size - 1) {
                board.append('\n');
            }
        }
        return board.toString();
    }


    public boolean hasSquare(char color) {
        List<Point> pts = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] == color) pts.add(new Point(i, j));
            }
        }

        final int n = pts.size();
        if (n < 4) return false;

        for (int a = 0; a < n; a++) {
            Point p1 = pts.get(a);
            for (int b = a + 1; b < n; b++) {
                Point p2 = pts.get(b);

                int dx = p2.x() - p1.x();
                int dy = p2.y() - p1.y();

                if (dx == 0 && dy == 0) continue;


                int p3x = p1.x() - dy;
                int p3y = p1.y() + dx;
                int p4x = p2.x() - dy;
                int p4y = p2.y() + dx;

                if (inBounds(p3x, p3y) && inBounds(p4x, p4y)) {
                    if (grid[p3x][p3y] == color && grid[p4x][p4y] == color) {
                        return true;
                    }
                }

                int p5x = p1.x() + dy;
                int p5y = p1.y() - dx;
                int p6x = p2.x() + dy;
                int p6y = p2.y() - dx;

                if (inBounds(p5x, p5y) && inBounds(p6x, p6y)) {
                    if (grid[p5x][p5y] == color && grid[p6x][p6y] == color) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < size && y < size;
    }

}
