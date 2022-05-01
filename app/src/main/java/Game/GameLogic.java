package Game;

import java.util.Arrays;
import java.util.Random;

class GameLogic {
    private static final int SIZE = 4;

    private int[][] board;
    private int points;

    private Random random;

    GameLogic(){
        this.random = new Random();
        this.board = makeBoard();
        this.points = 0;
    }
    public boolean updateBoard(){
        boolean lose_flag = true;
        for(int[] i:board){
            for(int j:i){
                if(j == 0){
                    lose_flag = false;
                    break;
                }
            }
        }
        for(;;){
            int x = random.nextInt();
            int y = random.nextInt();
            if(board[x][y] == 0) {
                board[x][y] = random.nextInt(10) < 9 ? 2 : 4;
                break;
            }
        }
        for(int[] i:board){
            for(int j:i){
                if(j == 0){
                    lose_flag = false;
                    break;
                }
            }
        }
        return lose_flag;
    }
    private void moveUp(){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 3; j++){
                if(board[j][i] == board[j + 1][i] || board[j][i] == 0){
                    board[j][i] += board[j + 1][i];
                    for(int k = j + 1; k < 3; k++){
                        board[k][i] = board[k + 1][i];
                    }
                    board[3][i] = 0;
                }
            }
        }
    }


    private int[][] makeBoard(){
        int[][] board = new int[SIZE][SIZE];
        for(int[] i:board){
            Arrays.fill(i, 0);
        }
        //90% - начинаем с двойки, 10% - с четверки
        board[random.nextInt(SIZE)][random.nextInt(SIZE)] = random.nextInt(10) < 9 ? 2 : 4;
        return board;
    }
}
