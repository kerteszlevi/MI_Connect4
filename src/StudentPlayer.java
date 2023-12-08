import static java.lang.Math.*;

import java.util.HashMap;
import java.util.Random;


public class StudentPlayer extends Player{
    private int depth = 6;
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

        //if(getRandomBoolean()) depth = 6;
        //else depth = 8;
    }

    private HashMap<Long, Integer> transpositionTable = new HashMap<>();
    private ZobristHash zobristHash = new ZobristHash(boardSize[0], boardSize[1]);

    @Override
    public int step(Board board) {
        int bestScore = -999999;
        int bestMove = -1;
        int previousPlayer = board.getLastPlayerIndex();
        Board boardCopy = new Board(board);
        for(int possibleMove: board.getValidSteps()){
            boardCopy.step(playerIndex, possibleMove);
            int score = minimax(boardCopy, depth, false, previousPlayer, -999999, 999999);
            if(bestScore<score){
                bestScore = score;
                bestMove = possibleMove;
            }
            boardCopy = new Board(board);
        }
        if(bestMove == -1) {
            System.out.println();
        }
        return bestMove;
    }
    //Minmax functions:

    public int minimax(Board position, int depth, boolean isMaximizing, int previousPlayerIndex, int alpha, int beta){
        long positionKey = zobristHash.hash(position.getState());

        if (transpositionTable.containsKey(positionKey)) {
            return transpositionTable.get(positionKey);
        }

        if(depth == 0 || position.gameEnded()){
            if(isMaximizing && position.gameEnded()) return -999;
            if(!isMaximizing && position.gameEnded()) return 999;
            return evaluate(position);

        }
        if(isMaximizing){
            int maxEval = -999999;
            Board positionCopy = new Board(position);
            for(int possibleColumn : positionCopy.getValidSteps()){
                positionCopy.step(playerIndex, possibleColumn);
                int eval = minimax(positionCopy, depth-1,false, previousPlayerIndex, alpha, beta);
                maxEval = max(maxEval, eval);
                //alpha-beta pruning
                alpha = max(alpha, eval);
                if(beta<=alpha) break;

                positionCopy = new Board(position);
            }
            transpositionTable.put(positionKey, maxEval);
            return maxEval;
        }else{
            int minEval = 999999;
            Board positionCopy = new Board(position);
            for(int possibleColumn : positionCopy.getValidSteps()){
                positionCopy.step(previousPlayerIndex, possibleColumn);
                int eval = minimax(positionCopy, depth-1,true,previousPlayerIndex, alpha, beta);
                minEval = min(minEval, eval);

                beta = min(beta, eval);
                if(beta<=alpha) break;

                positionCopy = new Board(position);
            }
            transpositionTable.put(positionKey, minEval);
            return minEval;
        }
    }
    //régi butább kiértékelőfüggvény
    public int evaluate2(Board state){
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

    //Kiértékelő függvén, ami a tábla egy pozícióját értékeli ki.
    public int evaluate(Board state){
        int[][] stateCopy = arrayCopy2d(state.getState());
        int[][] scoreCopy = arrayCopy2d(scoreTable);
        for(int row = 0; row < stateCopy.length; row++){
            for(int col = 0; col < state.getState()[row].length;col++){
                //minden pozícióra
                if(stateCopy[row][col] == 0 || stateCopy[row][col] == state.getLastPlayerIndex()) continue;
                else{
                    //horizontális, vertikális és átlós irányok
                    for(int rowindex = row-nToConnect+1;rowindex<row+nToConnect;rowindex++) {
                        for(int colindex = col-nToConnect+1;colindex<col+nToConnect;colindex++){
                            if(  (rowindex == row||colindex == col||abs(rowindex - row) == abs(colindex-col)  ) && indexCheck(rowindex, colindex)) scoreCopy[rowindex][colindex] -=1;
                        }
                    }
                }
            }
        }
        return arraySum2d(scoreCopy);
    }


    //Helper functions:

    //A pont táblát összegzi.
    public int arraySum2d(int[][] array) {
        int sum = 0;
        for (int[] row : array) {
            for (int value : row) {
                sum += value;
            }
        }
        return sum;
    }
    //a beépített arraycopy segítségével egy 2d int tömböt másol
    public int[][] arrayCopy2d(int[][] arrayToCopy){
        int noRows = arrayToCopy.length;
        int noColumns = arrayToCopy[0].length;
        int [][] copy = new int[noRows][noColumns];
        for(int i = 0; i<noRows;i++){
            System.arraycopy(arrayToCopy[i], 0, copy[i], 0, noColumns);
        }
        return copy;
    }
    //ellenőrzi, hogy az átadott index a táblán belül található-e.
    public boolean indexCheck(int rowToCheck, int columnToCheck){
        return rowToCheck >= 0 && (boardSize[0] - 1) >= rowToCheck && columnToCheck >= 0 && (boardSize[1] - 1) >= columnToCheck;
    }
    //TODO:tetszőleges tábla méretre implementálni kell
    public int[][] generateScoreTable(int numberOfRows, int numberOfColumns){
        return new int[0][0];
    }
    public boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }

    public class ZobristHash {
        private long[][] table;

        public ZobristHash(int rows, int cols) {
            table = new long[rows][cols];
            Random random = new Random();

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    table[i][j] = random.nextLong();
                }
            }
        }

        public long hash(int[][] board) {
            long h = 0;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] != 0) {
                        h ^= table[i][j];
                    }
                }
            }
            return h;
        }
    }
}
