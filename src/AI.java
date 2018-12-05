import java.util.ArrayList;

public class AI {
    public static int MAX_SEARCH_DEPTH = 12;

    /**
     * An AI technique that finds the move that gives the greatest chance of
     * winning if all moves after this move were made randomly by both players.
     *
     * @param player The char of the player making this move
     * @return The Move that is found to be good
     */
    public static Move findMoveMonteCarlo(Player player, Board board) {
        ArrayList<Move> possibleMoves = board.findPosMoves(player.curPlayer);
        Move bestMove = possibleMoves.get(0);

        // Run through the different moves the current player can make
        double highScore = 0;
        for (Move posMove : possibleMoves) {
            Board newBoard = new Board(board);
            newBoard.makeMove(posMove);

            // Finish this board randomly many times to get an estimate
            // of how good it is
            double moveScore = scoreTheBoard(player, newBoard);
            if (moveScore > highScore) {
                highScore = moveScore;
                bestMove = posMove;
            }
        }
//		if(player=='O')
//			System.out.println("Estimated chance Human Wins: " + (100 - highScore) + "%");
        return bestMove;
    }

    /**
     * Estimate how often this board leads to a win if played out randomly.
     * Score returned is on a scale 0 - 100.
     */
    private static double scoreTheBoard(Player player, Board board) {
        double winCount = 0;
        final int TRIALS = 4000;
        for (int i = 0; i < TRIALS; i++) {
            Board newBoard = new Board(board);
            Player tempPlayer = player.copy();
            newBoard.finishRandomly(tempPlayer);
            if (newBoard.winnerIs(player)) {
                winCount += 1;
            }
        }
        return (winCount / TRIALS) * 100;
    }

    /**
     * Identifies the move with the best worst case scenario. It should never
     * be called when the player has no legal moves on this Board.
     * If no legal move is available, it should be the other player's turn,
     * or the game should be over.
     *
     * @return The best Move available. Returns null if there are no
     * legal moves from this state.
     */
    public static Move alphaBetaSearch(Board board, Player player) {
        Move bestMove = null;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        boolean getFirstMove = MAX_SEARCH_DEPTH==0;

        for (Move pos : board.findPosMoves(player.curPlayer)) {
            if (getFirstMove) return pos;
            Board newState = new Board(board);
            Player tempPlayer = player.copy();
            newState.makeMove(pos);
            int val = minValue(newState,tempPlayer, MAX_SEARCH_DEPTH-1, alpha, beta);
            if (val > alpha) {
                alpha = val;
                bestMove = pos;
            }
        }

        if(bestMove == null) {
            System.out.println("absearch was called on a board that had no moves");
        }
        return bestMove;
    }

    /**
     * Returns the largest value of its children, or the score difference
     * of this Board.
     */
    public static int maxValue(Board state, Player player, int depth, int alpha, int beta) {
        if (depth == 0) return state.scoreDifference(player);
        int bestVal = Integer.MIN_VALUE;
        for (Move pos : state.findPosMoves(player.curPlayer)) {
            Board newState = new Board(state);
            newState.makeMove(pos);
            int val = minValue(newState,player, depth-1, alpha, beta);
            // update bestVal
            bestVal = (val > bestVal) ? val : bestVal;
            // update alpha
            if (val > alpha) {
                alpha = val;
                // Check if we can prune
                if(alpha >= beta) {
                    return alpha;
                }
            }

        }
        // No legal move was available so bestVal was never changed
        if(bestVal==Integer.MIN_VALUE) {
            //char otherPlayer = Main.switchPlayer(player);

            // If the other player can go, the value of this node will be
            // the worst instead of the best for this player
            if(state.findPosMoves(player.otherPlayer).size() > 0) {
                return minValue(state, player, depth, alpha, beta);
            }
            // Otherwise return the score difference for the current state
            return state.scoreDifference(player);
        }

        return bestVal;
    }

    /**
     * Returns the smallest value of its children, or the score difference
     *  of this Board.
     */
    public static int minValue(Board state,Player player, int depth, int alpha, int beta) {
        if (depth == 0) return state.scoreDifference(player);
        int bestVal = Integer.MAX_VALUE;
        //char otherPlayer = Main.switchPlayer(player);
        for (Move pos : state.findPosMoves(player.otherPlayer)) {
            Board newState = new Board(state);
            newState.makeMove(pos);
            int val = maxValue(newState, player, depth-1, alpha, beta);
            // update bestVal
            bestVal = (val < bestVal) ? val : bestVal;
            // update beta
            if (val < beta) {
                beta = val;
                // Check if we can prune
                if(beta <= alpha) {
                    return beta;
                }
            }
        }
        // No legal move was available so bestVal was never changed
        if(bestVal==Integer.MAX_VALUE) {

            // If the given player can go, the value of this node will be
            // the best instead of the worst for them.
            if(state.findPosMoves(player.curPlayer).size() > 0) {
                return maxValue(state, player, depth, alpha, beta);
            }
            // Otherwise return the score difference for the current state
            return state.scoreDifference(player);
        }

        return bestVal;
    }
}