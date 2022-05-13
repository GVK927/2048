package com.example.a2048;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.VectorDrawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


import androidx.core.view.GestureDetectorCompat;


import java.util.HashMap;

import Game.GameBoard;

class GameView extends View {
    private int CELL_SIZE;
    private int OFFSET;
    private int BOARD_SIZE;

    private GestureDetector gesture_detector;

    private GameBoard board;
    private final VectorDrawable boardImg;
    private final HashMap<Integer, VectorDrawable> cellImgs = new HashMap<>();

    {
        cellImgs.put(2, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell2));
        cellImgs.put(4, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell4));
        cellImgs.put(8, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell8));
        cellImgs.put(16, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell16));
        cellImgs.put(32, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell32));
        cellImgs.put(64, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell64));
        cellImgs.put(128, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell128));
        cellImgs.put(256, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell256));
        cellImgs.put(512, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell512));
        cellImgs.put(1024, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell1024));
        cellImgs.put(2048, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell2048));
        cellImgs.put(4096, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell4096));
        cellImgs.put(8192, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell8192));
        cellImgs.put(16384, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell16384));
        cellImgs.put(32768, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell32768));
        cellImgs.put(65536, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell65536));
        cellImgs.put(131072, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell131072));

        this.boardImg = (VectorDrawable) getResources().getDrawable(R.drawable.ic_gameboard);
    }

    public GameView(Context context, AttributeSet set) {
        super(context, set);
        CELL_SIZE = cellImgs.get(2).getIntrinsicWidth();
        BOARD_SIZE = boardImg.getIntrinsicWidth();
        OFFSET = (int)(((double)(BOARD_SIZE - CELL_SIZE * 4)) / 5);
        gesture_detector = new GestureDetector(null, new SwipeDetector());
    }
    public GameView(Context context){
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gesture_detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackground(boardImg);
        canvas.translate(resizeCords(OFFSET), resizeCords(OFFSET));
        canvas.save();
        for(int i = 0; i < board.getBoard().length; i++){
            canvas.translate(0, resizeCords((OFFSET + CELL_SIZE) * i));
            for(int j = 0; j < board.getBoard().length; j++){
                if(board.getBoard()[i][j] != 0) {
                    cellImgs.get(board.getBoard()[i][j]).setBounds(0, 0, resizeCords(CELL_SIZE), resizeCords(CELL_SIZE));
                    cellImgs.get(board.getBoard()[i][j]).draw(canvas);
                }
                canvas.translate(resizeCords(OFFSET + CELL_SIZE), 0);
            }
            canvas.restore();
            canvas.save();
        }
        canvas.restore();
    }


    private int resizeCords(int input) {
        return (int)(input * ((double)getWidth() / boardImg.getIntrinsicWidth()));
    }

    public GameBoard getBoard() {
        return board;
    }
    public void setBoard(GameBoard board) {
        this.board = board;
    }

    class SwipeDetector extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            System.out.println("DOWN");
            return true;
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            System.out.println("FLING");
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                    result = true;
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                }
                result = true;

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }

        public void onSwipeRight() {
            System.out.println("RIGHT");
        }
        public void onSwipeLeft() {
            System.out.println("LEFT");
        }
        public void onSwipeTop() {
            System.out.println("TOP");
        }
        public void onSwipeBottom() {
            System.out.println("BOTTOM");
        }
    }
}
