//public class Main {
//    public static void main(String[] args) {
//
//	// write your code here
//    }
//}

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class Main {
    private Scanner scnr = new Scanner(System.in);
    private Player player = new Player();


    public void main(String[] args) {

        boolean playAgain;
        char curPlayer, otherPlayer;


        // Give human the option to go first
        System.out.println("You are W's");

        // Human is always W's
        // curPlayer will go first.
//        if (scnr.nextLine().contains("y")) {
//            curPlayer = 'X';
//            otherPlayer = 'O';
//        } else {
//            curPlayer = 'O';
//            otherPlayer = 'X';
//        }

        // The game loop
        do{
            Board theBoard = new Board();
            boolean gameOver = false;
            theBoard.showGameInfo();

            // The turn loop
            do {
                gameOver = player.takeTurn(theBoard);

                theBoard.showGameInfo();
                player.switchPlayer();
            } while (!gameOver);

            // Ask if the Human would like to play again
            System.out.print("Play again?(y/n): ");
            scnr.nextLine();
            playAgain = scnr.nextLine().contains("y");

        }while (playAgain);
    }
}
