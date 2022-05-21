package com.example.a2048;

import android.app.AlertDialog;
import android.app.Dialog;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

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
        view.findViewById(R.id.buttonOK).setOnClickListener(view1 -> onButtonPressed());
        return dialog;
    }
    public void onButtonPressed(){
        gameActivity.newGame();
        getDialog().cancel();
    }
}
