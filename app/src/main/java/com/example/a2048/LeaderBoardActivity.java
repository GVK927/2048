package com.example.a2048;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class LeaderBoardActivity extends AppCompatActivity {
    public static final String DATABASE_URL = "https://project-2048game-default-rtdb.europe-west1.firebasedatabase.app/";
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance(DATABASE_URL);
    private TextView currentNickTextView;
    private Button accountButton;
    private RecyclerView leadersList;
    private ArrayList<LeaderBoardPlayer> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaders);
        leadersList = findViewById(R.id.leaders_list);
        accountButton = findViewById(R.id.accountButton);
        currentNickTextView = findViewById(R.id.currentNickTextView);

        players = loadPlayers();
        leadersList.setAdapter(new LeaderBoardAdapter(this, players));
        if (auth.getCurrentUser() != null) {
            currentNickTextView.setText(auth.getCurrentUser().getDisplayName());
            initLogoutButton();
        } else {
            currentNickTextView.setText(R.string.Offline);
            initLoginButton();
        }
    }    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            int localMaxScore = getSharedPreferences("score", Context.MODE_PRIVATE)
                    .getInt("score", 0);
            database.getReference()
                    .child("users")
                    .child(Objects.requireNonNull(auth.getUid()))
                    .setValue(new LeaderBoardPlayer(localMaxScore, Objects.requireNonNull(auth.getCurrentUser())
                            .getDisplayName()));
            initLogoutButton();
        }
    }

    private void initLogoutButton() {
        accountButton.setText(R.string.Logout);
        currentNickTextView.setText(Objects.requireNonNull(auth.getCurrentUser()).getDisplayName());
        accountButton.setOnClickListener(l -> {
            auth.signOut();
            getSharedPreferences("score", Context.MODE_PRIVATE).edit().putInt("score", 0).apply();
            currentNickTextView.setText(R.string.Offline);
            initLoginButton();
        });
    }

    private void initLoginButton() {
        accountButton.setText(R.string.Log_in_sign_up);
        accountButton.setOnClickListener(l -> {
            List<AuthUI.IdpConfig> providers = Collections.singletonList(
                    new AuthUI.IdpConfig.EmailBuilder().build());
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build();
            signInLauncher.launch(signInIntent);
        });
    }

    private void tryUpdateCurrentUser() {
        SharedPreferences preferences = getSharedPreferences("score", Context.MODE_PRIVATE);
        int localMaxScore = preferences.getInt("score", 0);
        database.getReference()
                .child("users")
                .child(Objects.requireNonNull(auth.getCurrentUser())
                        .getUid())
                .child("score")
                .get().addOnCompleteListener(task -> {
            int onlineMaxScore = 0;
            if (task.isSuccessful() && task.getResult().getValue() != null) {
                onlineMaxScore = task.getResult().getValue(Integer.class);
            }
            if (localMaxScore > onlineMaxScore)
                database.getReference()
                        .child("users")
                        .child(auth.getCurrentUser()
                                .getUid())
                        .child("score")
                        .setValue(localMaxScore);
        });

    }

    private void updatePlayersList() {
        Collections.sort(players, (x1, x2) -> Integer.compare(x1.getScore(), x2.getScore()));
    }

    private ArrayList<LeaderBoardPlayer> loadPlayers() {
        if (auth.getCurrentUser() != null)
            tryUpdateCurrentUser();
        players = new ArrayList<>();
        database.getReference().child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                players.add(snapshot.getValue(LeaderBoardPlayer.class));
                updatePlayersList();
                leadersList.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (players.contains(snapshot.getValue(LeaderBoardPlayer.class))) {
                    players.set(players.indexOf(snapshot.getValue(LeaderBoardPlayer.class)), snapshot.getValue(LeaderBoardPlayer.class));
                    updatePlayersList();
                    leadersList.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                players.remove(snapshot.getValue(LeaderBoardPlayer.class));
                updatePlayersList();
                leadersList.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                players = loadPlayers();
                updatePlayersList();
                leadersList.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return players;
    }
}
