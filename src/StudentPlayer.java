import static java.lang.Math.max;
import static java.lang.Math.min;

public class StudentPlayer extends Player{
    private int depth = 5;
    private int maxPossibleScore;
    //scoring table for a board with the height of 6 and width of 7 where you need to
    // connect 4 dots. Currently have to be calculated manually. Currently this
    private final int[][] scoreTable={
            {3, 4, 5,  7,  5,  4, 3},
            {4, 6, 8,  10, 8,  6, 4},
            {5, 8, 11, 13, 11, 8, 5},
            {5, 8, 11, 13, 11, 8, 5},
            {4, 6, 8,  10, 8,  6, 4},
            {3, 4, 5,  7,  5,  4, 3}
    };



    public StudentPlayer(int playerIndex, int[] boardSize, int nToConnect) {
        super(playerIndex, boardSize, nToConnect);
        int height = boardSize[0];
        int width = boardSize[1];

        //possible 4-s if the board is empty
        int vertical = (height - nToConnect + 1) * width;
        int horizontal = (width - nToConnect + 1) * height;
        int diagonal = (width - nToConnect + 1) * (height - nToConnect + 1) * 2;

        maxPossibleScore = vertical + horizontal + diagonal;
        //minScore = (vertical + horizontal + diagonal * 2)*-1;
        //maxScore = vertical + horizontal + diagonal * 2;
    }

    @Override
    public int step(Board board) {
        int bestScore = -999999;
        int bestMove = -1;
        int previousPlayer = board.getLastPlayerIndex();
        Board boardCopy = new Board(board);
        for(int possibleMove: board.getValidSteps()){
            boardCopy.step(playerIndex,possibleMove);
            int score = minimax(boardCopy, depth, false, previousPlayer);
            if(bestScore<score){
                bestScore = score;
                bestMove = possibleMove;
            }
            boardCopy = new Board(board);
        }
        return bestMove;
    }
    //Minmax functions:

    public int minimax(Board position, int depth, boolean isMaximizing, int previousPlayerIndex){
        if(depth == 0 || position.gameEnded()){
            return evaluate(position);
        }
        if(isMaximizing){
            int maxEval = -999999;
            Board positionCopy = new Board(position);
            for(int possibleColumn : positionCopy.getValidSteps()){
                positionCopy.step(playerIndex, possibleColumn);
                int eval = minimax(positionCopy, depth-1,false, previousPlayerIndex);
                maxEval = max(maxEval, eval);
                positionCopy = new Board(position);
            }
            return maxEval;
        }else{
            int minEval = 999999;
            Board positionCopy = new Board(position);
            for(int possibleColumn : positionCopy.getValidSteps()){
                positionCopy.step(previousPlayerIndex, possibleColumn);
                int eval = minimax(positionCopy, depth-1,true,previousPlayerIndex);
                minEval = min(minEval, eval);
                positionCopy = new Board(position);
            }
            return minEval;
        }
    }
    public int evaluate(Board state){
        int score = 0;
        for(int row = 0; row < state.getState().length; row++){
            for(int col = 0; col < state.getState()[row].length;col++){
                if(state.getState()[row][col] == 0) continue;
                if(state.getState()[row][col] == playerIndex){
                    // Ha mi vagyunk a pozícióban akkor növeljük,
                    score += scoreTable[row][col];
                }else{
                    // ha az ellenfél akkor csökkentjük a pontszámot.
                    score -= scoreTable[row][col];
                }
            }
        }
        return score;
    }
    public int evaluate2(Board state){

        return 0;
    }


    //Helper functions:
    public int arraySum2d(int[][] array) {
        int sum = 0;
        for (int[] row : array) {
            for (int value : row) {
                sum += value;
            }
        }
        return sum;
    }
}
