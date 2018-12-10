import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Player player = new Player();


    public static void main(String[] args) {

        boolean playAgain;
        System.out.println("You are White's");

        do{
            Board board = new Board();
            boolean gameOver = false;
            board.showGameInfo();
            // The turn loop
            for(;;){
                gameOver = player.takeTurn(board);

                board.showGameInfo();

                if (gameOver) {break;}
                player.switchPlayer();

            }

            System.out.println("Game Over!");
            System.out.println("Winner is " + board.winnerIs());



            System.out.print("Another Game?(y/n): ");
            scanner.nextLine();
            playAgain = scanner.nextLine().contains("y");

        }while (playAgain);
    }
}
