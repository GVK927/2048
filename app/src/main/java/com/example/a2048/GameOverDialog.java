package com.example.a2048;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.prefs.Preferences;

public class GameOverDialog extends DialogFragment {

    private GameActivity gameActivity;

    public GameOverDialog(GameActivity gameActivity) {
        super();
        this.gameActivity = gameActivity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.game_over_dialog, null, false);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_rounded_background);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        view.findViewById(R.id.buttonOK).setOnClickListener(view1 -> onButtonPressed());
        return dialog;
    }
    public void onButtonPressed(){
        writeMaxScore();
        gameActivity.newGame();
        getDialog().cancel();
    }

    private void writeMaxScore() {
        SharedPreferences preferences = this.gameActivity.getSharedPreferences("score", Context.MODE_PRIVATE);
        int currentPoints = gameActivity.getGameBoard().getPoints();
        if (currentPoints > preferences.getInt("score", 0))
            preferences.edit().putInt("score", currentPoints).apply();
    }
}
