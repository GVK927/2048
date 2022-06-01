package com.example.a2048;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder> {
    private final ArrayList<LeaderBoardPlayer> players;
    private final LayoutInflater inflater;

    public LeaderBoardAdapter(Context context, ArrayList<LeaderBoardPlayer> players){
        this.inflater = LayoutInflater.from(context);
        this.players = players;
    }

    @NonNull
    @Override
    public LeaderBoardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LeaderBoardAdapter.ViewHolder holder, int position) {
        LeaderBoardPlayer player = players.get(position);
        holder.nameView.setText(player.getName());
        holder.scoreView.setText(player.getScore()+"");
        holder.numberView.setText(position + 1 + "");
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView, scoreView, numberView;
        ViewHolder(View view){
            super(view);
            numberView = view.findViewById(R.id.numberView);
            scoreView = view.findViewById(R.id.userScore);
            nameView = view.findViewById(R.id.userName);
        }
    }
}
