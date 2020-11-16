package com.dnedev.data.mining.n_queens;

public class Main {

    public static void main(String[] args) {

        long startTime = System.nanoTime();

        Board board = new Board(10000);
        board.solve();

        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Execution time in milliseconds : " + totalTime / 1000000);

    }
}
