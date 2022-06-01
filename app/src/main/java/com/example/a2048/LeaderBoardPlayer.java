package com.example.a2048;

public class LeaderBoardPlayer {
    private int score;
    private String name;

    public LeaderBoardPlayer(int score, String name){
        this.name = name;
        this.score = score;
    }
    public LeaderBoardPlayer(){}
    public int getScore() {
        return score;
    }
    public String getName() {
        return name;
    }
}
