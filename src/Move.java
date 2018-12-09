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
     * Checks if this move can flip over any of the opponent's pieces
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
     * Determines the positions of opponents stones that need to be flipped over
     */
    private ArrayList<int[]> findPositsToFlip(int searchDown, int searchRight) {
        ArrayList<int[]> positions = new ArrayList<>();
        boolean tileOnBothSides = false;
        int r = row + searchDown;
        int c = col + searchRight;

        while (onBoard(r, c)) {
            int[] curPosition = { r, c };
            char stoneAtCurPos = board.findStoneAt(r, c);

            // This position belongs to opponent
            if (stoneAtCurPos != color && stoneAtCurPos != '0') {
                positions.add(curPosition);
            }
            // This position belongs to this player
            else if (stoneAtCurPos == color) {
                tileOnBothSides = true;
                break;
            }
            // This position is empty
            else {
                break;
            }
            r += searchDown;
            c += searchRight;
        }
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
     * Printable move represent.
     */
    public String toString() {
        return "Row: " + row + " Col: " + col;
    }
}
