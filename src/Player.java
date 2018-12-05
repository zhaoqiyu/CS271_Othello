import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Player {
    //This class is used to keep tracking who is the current player

    private Scanner scnr = new Scanner(System.in);

    public char curPlayer = 'B'; //AI always go first

    public char otherPlayer = 'W';

    //Switch player
    public void switchPlayer() {
        char temp = this.curPlayer;
        this.curPlayer = this.otherPlayer;
        this.otherPlayer = temp;
    }

    /**
     * Allow player to enter coordinate for next move
     */

    public Move getMove(char player, Board theBoard) {
        // Ask for row
        int row = -1;
        while (row < 0 || row > 7) {
            System.out.print("Row: ");
            row = scnr.nextInt();
        }
        // Ask for column
        int col = -1;
        while (col < 0 || col > 7) {
            System.out.print("Col: ");
            col = scnr.nextInt();
        }
        Move theMove = new Move(row, col, player, theBoard);
        if (theMove.isValid()) {
            return theMove;
        } else {
            System.out.println("Invalid location");
            // Ask again
            return getMove(player, theBoard);
        }
    }
    /**
     * Runs through one turn in the game. Returns True if the game is
     * over (neither player can go).
     */
    public boolean takeTurn(Board theBoard) {
        boolean gameOver = false;

        // Check if the current player can play
        ArrayList<Move> curPlayerMoves = theBoard.findPosMoves(curPlayer);
        boolean curPlayerCanPlay = curPlayerMoves.size() > 0;

        if (!curPlayerCanPlay) {
            String person = (curPlayer == 'W') ? "You" : "Computer";
            System.out.println(person + " could not go");

            // If neither player can go, the game is over
            //switchPlayer
            ArrayList<Move> othPlayerMoves = theBoard.findPosMoves(otherPlayer);
            gameOver = othPlayerMoves.size() == 0;
        }
        // Current player is human
        else if (curPlayer == 'W') {
            Move chosenMove = getMove(curPlayer, theBoard);
            theBoard.makeMove(chosenMove);
            System.out.println("You selected " + chosenMove);
        }
        // Current player is computer
        else if (curPlayer == 'B') {
            Move chosenMove;
            int openSpaces = 64-(theBoard.count('X') + theBoard.count('O'));
            if(openSpaces <= AI.MAX_SEARCH_DEPTH) {
                chosenMove = AI.alphaBetaSearch(theBoard, curPlayer);
            }
            else{
                chosenMove = AI.findMoveMonteCarlo(curPlayer, theBoard);
            }
            theBoard.makeMove(chosenMove);
            System.out.println("Computer selected " + chosenMove);
        }
        return gameOver;
    }

}