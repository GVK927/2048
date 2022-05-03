package Game;

import java.util.Arrays;
import java.util.Random;

public class GameBoard {
    private static final int SIZE = 4;

    private int[][] board;
    private int points;

    private Random random;

    public GameBoard (){
        this.random = new Random();
        this.board = makeBoard();
        this.points = 0;
    }
    public int[][] getBoard () {
        return board;
    }
    public int getPoints () {
        return points;
    }
    public boolean move(Direction direction){
        boolean reverse_order = direction == Direction.DOWN || direction == Direction.RIGHT;
        int[][] t = new int[SIZE][SIZE];
        for(int i = 0; i < SIZE; i++){
            t[i] = Arrays.copyOf(board[i], SIZE);
        }
        if(direction == Direction.UP || direction == Direction.DOWN)
            for (int i = 0; i < SIZE; i++) setCol(i, shift(getCol(i), reverse_order));
        if(direction == Direction.LEFT || direction == Direction.RIGHT)
            for (int i = 0; i < SIZE; i++) setRow(i, shift(getRow(i), reverse_order));
        boolean flag = false;
        for(int i = 0; i < SIZE; i++)
            for(int j = 0; j < SIZE; j++)
                if (board[i][j] != t[i][j]) {
                    flag = true;
                    break;
                }
        if(flag) return updateBoard();
        return false;
    }

    private boolean updateBoard(){
        if(!isLose())
        for(;;){
            int x = random.nextInt(SIZE);
            int y = random.nextInt(SIZE);
            if(board[x][y] == 0) {
                board[x][y] = random.nextInt(10) < 9 ? 2 : 4;
                break;
            }
        }
        return isLose();
    }
    private boolean isLose(){
        for(int i = 0; i < SIZE; i++){
            for(int j = 1; j < SIZE; j++){
                if(board[i][j] == board[i][j - 1] || board[j][i] == board[j - 1][i]) return false;
            }
        }
        for(int[] i:board){
            for(int j:i){
                if(j == 0) return false;
            }
        }
        return true;
    }
    private int[] shift(int[] in, boolean reverse_order){
        int[] out = new int[SIZE];
        Arrays.fill(out, 0);
        if(reverse_order){
            int c = SIZE - 1;
            boolean flag = false;
            for(int i = SIZE - 1; i >= 0; i--){
                if(in[i] == 0) continue;
                if(!flag && c != SIZE - 1 && in[i] == out[c + 1]){
                    flag = true;
                    out[c + 1] += in[i];
                    points += out[c + 1];
                    continue;
                }
                out[c--] = in[i];
                flag = false;
            }
        }else{
            int c = 0;
            boolean flag = false;
            for(int i = 0; i < SIZE; i++){
                if(in[i] == 0) continue;
                if(!flag && c != 0 && in[i] == out[c - 1]){
                    flag = true;
                    out[c - 1] += in[i];
                    points += out[c - 1];
                    continue;
                }
                out[c++] = in[i];
                flag = false;
            }
        }
        return out;
    }
    private void setRow(int i, int[] row){
        board[i] = row;
    }
    private void setCol(int i, int[] col){
        for(int j = 0; j < SIZE; j++){
            board[j][i] = col[j];
        }
    }
    private int[] getRow(int i){
            return board[i];
    }
    private int[] getCol(int i){
        int[] out = new int[SIZE];
        for(int j = 0; j < SIZE; j++){
            out[j] = board[j][i];
        }
        return out;
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
