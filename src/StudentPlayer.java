public class StudentPlayer extends Player{
    private final int depth = 5;
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
    private int maxScore = 0;
    private int minScore = 0;



    public StudentPlayer(int playerIndex, int[] boardSize, int nToConnect) {
        super(playerIndex, boardSize, nToConnect);
        int height = boardSize[0];
        int width = boardSize[1];

        //possible 4-s if the board is empty
        int vertical = (height - nToConnect + 1) * width;
        int horizontal = (width - nToConnect + 1) * height;
        int diagonal = (width - nToConnect + 1) * (height - nToConnect + 1) * 2;

        //minScore = (vertical + horizontal + diagonal * 2)*-1;
        //maxScore = vertical + horizontal + diagonal * 2;
    }

    @Override
    public int step(Board board) {
        for(int col = 0; col <boardSize[1]; col++){

        }
        return 0;
    }
    //Minmax functions:
    public int evaluate(int[][] state){
        int score = 0;

        for(int row = 0; row < state.length; row++){
            for(int col = 0; col < state[row].length;col++){
                if(state[row][col] == 0) continue;
                if(state[row][col] == playerIndex){
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
