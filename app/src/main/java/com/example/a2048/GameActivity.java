package com.example.a2048;


import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import Game.GameBoard;

public class GameActivity extends AppCompatActivity{
    private GameBoard gameBoard;


    private TextView scoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.scoreView = findViewById(R.id.scoreView);
        setFullScreen();
        this.gameBoard = getIntent().getBooleanExtra("isContinue", false)?loadGame():new GameBoard();
        setContentView(R.layout.activity_game);
        ((GameView)findViewById(R.id.gameView)).setBoard(this.gameBoard);
    }

    private void setFullScreen(){
        getWindow().getDecorView().setSystemUiVisibility(
                  View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private GameBoard loadGame(){
        return null;
    }
    private void update(){
        scoreView.setText("Score: " + gameBoard.getPoints());
        scoreView.invalidate();

    }


}
