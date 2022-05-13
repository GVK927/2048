package com.example.a2048;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFullScreen();
        findViewById(R.id.new_game_button).setOnClickListener(l -> {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("isContinue", false);
            startActivity(intent);
        });
        findViewById(R.id.continue_button).setOnClickListener(l -> {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("isContinue", true);
            startActivity(intent);
        });
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