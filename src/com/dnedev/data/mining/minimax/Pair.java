package com.dnedev.data.mining.minimax;

public class Pair {
    private boolean isGameFinished;
    private String winner;

    public Pair(boolean isGameFinished, String winner) {
        this.isGameFinished = isGameFinished;
        this.winner = winner;
    }

    public boolean isGameFinished() {
        return isGameFinished;
    }

    public String getWinner() {
        return winner;
    }
}
