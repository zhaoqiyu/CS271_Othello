//public class Board {
//    public int sizeOfBoard = 8;
//    public char [][] board ;
//    private char [][] defaultBoard =   {{'0','0','0','0','0','0','0','0'},
//                                        {'0','0','0','0','0','0','0','0'},
//                                        {'0','0','0','0','0','0','0','0'},
//                                        {'0','0','0','W','B','0','0','0'},
//                                        {'0','0','0','B','W','0','0','0'},
//                                        {'0','0','0','0','0','0','0','0'},
//                                        {'0','0','0','0','0','0','0','0'},
//                                        {'0','0','0','0','0','0','0','0'}};
//
//    Board(char [][] customBoard){
//        if(customBoard == null){
//            this.board = defaultBoard;
//        }else{
//            this.board = customBoard;
//        }
//    }
//
//    public int [] calculateScore(){
//        int whiteScore = 0;
//        int blackScore = 0;
//
//        for (char [] row :this.defaultBoard) {
//            for (char disk: row) {
//                switch (disk){
//                    case 'W':
//                        whiteScore++;
//                        break;
//                    case 'B':
//                        blackScore++;
//                        break;
//                    case '0':
//                        break;
//                }
//            }
//        }
//        int [] rtv = {blackScore, whiteScore};
//        return rtv;
//    }
//
//}

import java.util.ArrayList;
import java.util.Random;

public class Board {

    private char[][] board = {{'0','0','0','0','0','0','0','0'},
                              {'0','0','0','0','0','0','0','0'},
                                        {'0','0','0','0','0','0','0','0'},
                                        {'0','0','0','W','B','0','0','0'},
                                        {'0','0','0','B','W','0','0','0'},
                                        {'0','0','0','0','0','0','0','0'},
                                        {'0','0','0','0','0','0','0','0'},
                                        {'0','0','0','0','0','0','0','0'}};

    /**
     * Creates a gameboard with an identical board as another gameboard
     *
     * @param original
     *            the gameboard to copy
     */
    public Board(Board original) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                this.board[row][col] = original.board[row][col];
            }
        }
    }

    public Board() {

    }

    public char findStoneAt(int row, int col) {
        return board[row][col];
    }

    /**
     * Print out the gameboard
     */
    public void show() {
        int i = 0;
        System.out.println("  0 1 2 3 4 5 6 7");
        for (char[] row : board) {
            System.out.print(i + " ");
            for (char position : row) {
                System.out.print(position + " ");
            }
            System.out.println();
            i++;
        }
    }

    /**
     * Make a move.
     *
     * @return boolean true if the move was made
     */
    public boolean makeMove(Move theMove) {
        // Check if that position is valid
        if (theMove.isValid()) {
            // Place the players piece
            int[] coordinates = theMove.getCoordinates();
            board[coordinates[0]][coordinates[1]] = theMove.getColor();

            // Change all other necessary positions
            for (int[] position : theMove.allPosToFlip()) {
                int row = position[0];
                int col = position[1];
                board[row][col] = theMove.getColor();
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * @return true if the given player has a larger score
     */
    public boolean winnerIs(Player player) {
        if (count(player.curPlayer) > count(player.otherPlayer)) {
            return true;
        }
        return false;
    }

    /**
     * print out the current score
     */
    public void showScore() {
        System.out.println("Black: " + count('B'));
        System.out.println("White: " + count('W'));
    }

    /**
     * Print current board in terminal
     */
    public void showGameInfo() {
        show();
        showScore();
        System.out.println("===============================");
    }

    /**
     * Count how many times a character occurs on the board
     *
     * @param color
     *            The char to look for
     * @return int the number of times the char occurs
     */
    public int count(char color) {
        int count = 0;
        for (char[] row : board) {
            for (char position : row) {
                if (position == color)
                    count++;
            }
        }
        return count;
    }

    public int scoreDifference(Player player) {
        return count(player.curPlayer) - count(player.otherPlayer);
    }

    /**
     * Makes random valid moves until the gameboard is finished and neither player
     * can make a move
     *
     * @param player
     *            The duplicated player object
     */
    public void finishRandomly(Player player) {
        //Start from otherPlayer
        //char player = Othello.switchPlayer(playerJustWent);
        player.switchPlayer();
        ArrayList<Move> possibleMoves = this.findPosMoves(player.curPlayer);

        // Keep make moves as long as one of the players can go
        while (possibleMoves.size() != 0 || this.findPosMoves(player.otherPlayer).size() != 0) {

            if (possibleMoves.size() > 0) {
                Move selectedMove = selectRandom(possibleMoves);
                this.makeMove(selectedMove);
            }
            player.switchPlayer();

            //playerJustWent = player;
            //player = Othello.switchPlayer(player);
            possibleMoves = this.findPosMoves(player.curPlayer);
        }
    }

    /**
     * Randomly selects and returns an element from the ArrayList
     *
     * @param theList
     *            ArrayList
     */
    public <T> T selectRandom(ArrayList<T> theList) {
        Random randGen = new Random();
        int randNum = randGen.nextInt(theList.size());
        return theList.get(randNum);
    }

    /**
     * @return An ArrayList of all the possible moves the given player can make
     */
    public ArrayList<Move> findPosMoves(char player) {
        ArrayList<Move> possibleMoves = new ArrayList<Move>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Move posMove = new Move(row, col, player, this);
                if (posMove.isValid()) {
                    possibleMoves.add(posMove);
                }
            }
        }
        return possibleMoves;
    }

}
 