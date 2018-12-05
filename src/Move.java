//public class Move {
//    Board board;
//    Move(Board _board){
//        board = _board;
//    }
//
//    public boolean [][] calculateLegalMoves(char color){
//        boolean [][] rtv = {{false, false, false, false, false, false, false, false},
//                            {false, false, false, false, false, false, false, false},
//                            {false, false, false, false, false, false, false, false},
//                            {false, false, false, false, false, false, false, false},
//                            {false, false, false, false, false, false, false, false},
//                            {false, false, false, false, false, false, false, false},
//                            {false, false, false, false, false, false, false, false},
//                            {false, false, false, false, false, false, false, false}};
//
//        for (char [] row :this.defaultBoard) {
//            for (char disk: row) {
//
//
//            }
//        }
//
//
//
//    };
//
//
//}

import java.util.ArrayList;

public class Move {

    private int row;
    private int col;
    private char color;
    private Board board;

    public Move(int _row, int _col, char _color, Board _board) {
        this.row = _row;
        this.col = _col;
        this.color = _color;
        this.board = _board;
    }

    public char getColor() {
        return color;
    }

    public int[] getCoordinates() {
        int[] coordinates = { row, col };
        return coordinates;
    }

    /**
     * Validate this current move
     */
    public boolean isValid() {
        return board.findStoneAt(row, col) == '0' && flipAnyOver();
    }

    /**
     * Checks if this move would flip over any of the opponent's pieces
     * @return true if this move would flip over at least 1 opponent's piece
     * false otherwise
     */
    private boolean flipAnyOver() {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (findPositsToFlip(i, j).size() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Finds all of the positions that need to be flipped over as
     * a result of this move
     * @return
     */
    public ArrayList<int[]> allPosToFlip() {
        ArrayList<int[]> positions = new ArrayList<int[]>();
        for (int i = -1; i < 2; i++) {
            for(int j = -1; j < 2; j++) {
                for (int[] position : findPositsToFlip(i, j)) {
                    positions.add(position);
                }
            }
        }

        return positions;
    }

    /**
     * Determines the positions of opponents pieces that need to be flipped over
     * in a given direction out from this move.
     * @param searchDown
     *            +1 to go down, -1 to go up
     * @param searchRight
     *            +1 to go right, -1 to go left
     * @return
     */
    private ArrayList<int[]> findPositsToFlip(int searchDown, int searchRight) {
        ArrayList<int[]> positions = new ArrayList<int[]>();
        boolean tileOnBothSides = false;
        int r = row + searchDown;
        int c = col + searchRight;

        while (onBoard(r, c)) {
            int[] curPosition = { r, c };
            char peiceAtCurrentPos = board.findStoneAt(r, c);

            // This position belongs to opponent
            if (peiceAtCurrentPos != player && peiceAtCurrentPos != '-') {
                positions.add(curPosition);
            }
            // This position belongs to this player
            else if (peiceAtCurrentPos == player) {
                tileOnBothSides = true;
                break;
            }
            // This position doesn't belong to anyone
            else {
                break;
            }
            r += searchDown;
            c += searchRight;
        }
        // The player can only capture opponent pieces if the player has
        // a piece on both sides of this line of opponent pieces
        if (!tileOnBothSides) {
            positions.clear();
        }

        return positions;
    }

    /**
     * Check if the given position is on the board
     */
    private static boolean onBoard(int row, int col) {
        return row < 8 && col < 8 && row > -1 && col > -1;
    }

    /**
     * A representation of this move
     */
    public String toString() {
        return "Row: " + row + " Col: " + col;
    }
}
