package com.example.a2048;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.VectorDrawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.HashMap;

import Game.Direction;
import Game.GameBoard;

class GameView extends View {
    private int CELL_SIZE;
    private int OFFSET;
    private int BOARD_SIZE;

    private GestureDetector gesture_detector;
    private GameBoard board;
    private final VectorDrawable boardImg;
    private final HashMap<Integer, VectorDrawable> cellImages = new HashMap<>();
    private GameView self;

    {
        cellImages.put(2, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell2));
        cellImages.put(4, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell4));
        cellImages.put(8, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell8));
        cellImages.put(16, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell16));
        cellImages.put(32, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell32));
        cellImages.put(64, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell64));
        cellImages.put(128, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell128));
        cellImages.put(256, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell256));
        cellImages.put(512, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell512));
        cellImages.put(1024, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell1024));
        cellImages.put(2048, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell2048));
        cellImages.put(4096, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell4096));
        cellImages.put(8192, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell8192));
        cellImages.put(16384, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell16384));
        cellImages.put(32768, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell32768));
        cellImages.put(65536, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell65536));
        cellImages.put(131072, (VectorDrawable) getResources().getDrawable(R.drawable.ic_cell131072));

        this.boardImg = (VectorDrawable) getResources().getDrawable(R.drawable.ic_gameboard);
    }

    public GameView(Context context, AttributeSet set) {
        super(context, set);
        self = this;
        CELL_SIZE = cellImages.get(2).getIntrinsicWidth();
        BOARD_SIZE = boardImg.getIntrinsicWidth();
        OFFSET = (int)(((double)(BOARD_SIZE - CELL_SIZE * 4)) / 5);
        gesture_detector = new GestureDetector(null, new SwipeDetector());
    }
    public GameView(Context context){
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackground(boardImg);
        canvas.translate(resizeCords(OFFSET), resizeCords(OFFSET));
        canvas.save();
        for (int i = 0; i < board.getBoard().length; i++) {
            canvas.translate(0, resizeCords((OFFSET + CELL_SIZE) * i));
            for (int j = 0; j < board.getBoard().length; j++) {
                if (board.getBoard()[i][j] != 0) {
                    cellImages.get(board.getBoard()[i][j]).setBounds(0, 0, resizeCords(CELL_SIZE), resizeCords(CELL_SIZE));
                    cellImages.get(board.getBoard()[i][j]).draw(canvas);
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

    public void setBoard(GameBoard board) {
        this.board = board;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.gesture_detector.onTouchEvent(event);
    }
    private class SwipeDetector extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipe(Direction.RIGHT);
                        } else {
                            onSwipe(Direction.LEFT);
                        }
                    }
                    result = true;
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipe(Direction.DOWN);
                    } else {
                        onSwipe(Direction.UP);
                    }
                }
                result = true;

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }

        private void onSwipe(Direction direction) {
            if (board.move(direction)) {
                GameOverDialog gameOverDialog = new GameOverDialog((GameActivity) self.getContext());
                gameOverDialog.show(((AppCompatActivity)getContext()).getSupportFragmentManager(), "");
            }
            ((GameActivity)getContext()).update();
        }
    }
}
