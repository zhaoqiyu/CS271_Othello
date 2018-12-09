import java.util.ArrayList;

import java.lang.Math;

public class AI {
    public static int MAX_SEARCH_DEPTH = 12;

    /**
     * Monte Carlo search will be used as the backup for minimax in the case it's exceeding the
     * Search depth limit.
     */
    public static Move findMoveMonteCarlo(Player player, Board board) {
        //Find all the possible moves for current player
        ArrayList<Move> possibleMoves = board.findPosMoves(player.curPlayer);
        Move bestMove = possibleMoves.get(0);

        double highScore = 0;
        for (Move posMove : possibleMoves) {
            Board newBoard = new Board(board);
            newBoard.makeMove(posMove);

            //Finish the board randomly and eval the score we can get
            double moveScore = scoreTheBoard(player, newBoard);
            //Update the optimal solution
            if (moveScore > highScore) {
                highScore = moveScore;
                bestMove = posMove;
            }
        }
        return bestMove;
    }

    /**
     * Iterate through certain number of trails to get a estimation win rate score
     */
    private static double scoreTheBoard(Player player, Board board) {
        double winCount = 0;
        final int TRIALS = 3000;
        for (int i = 0; i < TRIALS; i++) {
            Board newBoard = new Board(board);
            Player tempPlayer = player.copy();
            newBoard.finishRandomly(tempPlayer);
            if (newBoard.winnerIs(player)) {
                winCount++;
            }
        }
        return (winCount / TRIALS) * 100;
    }

    /**
     * Implementation of Minimax algorithm with alpha beta pruning
     */
    public static Move alphaBetaPruning(Board board, Player player) {
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


    public static int maxValue(Board state, Player player, int depth, int alpha, int beta) {
        if (depth == 0) return state.scoreDifference(player);
        int bestVal = Integer.MIN_VALUE;
        for (Move pos : state.findPosMoves(player.curPlayer)) {
            Board tempBoard = new Board(state);
            tempBoard.makeMove(pos);
            int val = minValue(tempBoard,player, depth-1, alpha, beta);
            bestVal = Math.max(val,bestVal);
            if (val > alpha) {
                alpha = val;
                // Check if we can prune
                if(alpha >= beta) {
                    return alpha;
                }
            }

        }
        // No legal move
        if(bestVal==Integer.MIN_VALUE) {
            if(state.findPosMoves(player.otherPlayer).size() > 0) {
                return minValue(state, player, depth, alpha, beta);
            }
            return state.scoreDifference(player);
        }

        return bestVal;
    }

    public static int minValue(Board state,Player player, int depth, int alpha, int beta) {
        if (depth == 0) return state.scoreDifference(player);
        int bestVal = Integer.MAX_VALUE;
        for (Move pos : state.findPosMoves(player.otherPlayer)) {
            Board tempBoard = new Board(state);
            tempBoard.makeMove(pos);
            int val = maxValue(tempBoard, player, depth-1, alpha, beta);
            bestVal = Math.min(val, bestVal);
            if (val < beta) {
                beta = val;
                if(beta <= alpha) {
                    return beta;
                }
            }
        }
        // No legal move
        if(bestVal==Integer.MAX_VALUE) {
            if(state.findPosMoves(player.curPlayer).size() > 0) {
                return maxValue(state, player, depth, alpha, beta);
            }
            return state.scoreDifference(player);
        }

        return bestVal;
    }
}