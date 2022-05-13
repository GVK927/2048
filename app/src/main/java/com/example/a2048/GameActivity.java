package com.example.a2048;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Game.GameBoard;

public class GameActivity extends AppCompatActivity{
    private GameBoard gameBoard;
    private GameView gameView;


    private TextView scoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        this.gameBoard = getIntent().getBooleanExtra("isContinue", false) ? loadGame() : new GameBoard();
        setContentView(R.layout.activity_game);
        this.scoreView = findViewById(R.id.scoreView);
        gameView = findViewById(R.id.gameView);
        gameView.setBoard(this.gameBoard);
        scoreView.setText(getString(R.string.score, gameBoard.getPoints()));
    }
    @Override
    public void onBackPressed(){
        saveGame();
        super.onBackPressed();
    }

    public void update(){
        scoreView.setText(getString(R.string.score, gameBoard.getPoints()));
        gameView.invalidate();
    }

    private void saveGame(){
        try{
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(new File(getFilesDir(), "save.dat")));
            outputStream.writeObject(gameBoard);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private GameBoard loadGame(){
        try{
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File(getFilesDir(), "save.dat")));
            return (GameBoard) inputStream.readObject();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
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
}
