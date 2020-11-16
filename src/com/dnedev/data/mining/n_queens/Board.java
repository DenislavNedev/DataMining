package com.dnedev.data.mining.n_queens;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {

    private final int ONE = 1;
    private final int NUMBER_OF_QUEENS = 3;

    private List<Integer> queens;
    private List<Integer> mainDiagonalConflicts;
    private List<Integer> secondaryDiagonalConflicts;
    private List<Integer> columnsConflicts;
    private int boardSize;
    private Random randomGenerator;

    public Board(int n) {
        this.boardSize = n;
        this.randomGenerator = new Random();
        initBoard(this.boardSize);
    }

    private void initLists(int boardSize) {
        this.queens = new ArrayList<>(boardSize);
        this.mainDiagonalConflicts = new ArrayList<>(2 * boardSize - ONE);
        this.secondaryDiagonalConflicts = new ArrayList<>(2 * boardSize - ONE);
        this.columnsConflicts = new ArrayList<>(boardSize);

        for (int i = 0; i < boardSize; i++) {
            this.columnsConflicts.add(i, 0);
        }

        for (int i = 0; i < (2 * boardSize - 1); i++) {
            this.mainDiagonalConflicts.add(i, 0);
            this.secondaryDiagonalConflicts.add(i, 0);
        }
    }

    public void solve() {
        int moves = 0;
        while (getTotalConflicts() > 0) {
            if (moves > this.boardSize / 2) {
                initBoard(this.boardSize);
                moves = 0;
                continue;
            }

            makeMove();
            moves++;
        }

        if (this.boardSize <= 20) {
            printQueens(this.queens, this.boardSize);
        }
    }


    private void initBoard(int boardSize) {
        long startTime = System.nanoTime();

        initLists(boardSize);
        for (int i = 0; i < this.boardSize; i++) {
            int row = randomGenerator.nextInt(boardSize);
            while (queens.contains(row) && queens.size() != this.boardSize) {
                row = randomGenerator.nextInt(boardSize);
            }
            queens.add(i, row);
            updateConflictsForQueen(i, row, 1);
        }

        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Init time in milliseconds : " + totalTime / 1000000);
    }

    private void makeMove() {
        int maxConflicts = Integer.MIN_VALUE;
        List<Integer> queensForSwap = new ArrayList<>();

        //Find number max conflicts
        for (int x : queens) {
            int currentConflicts = getConflictsAtPosition(x, queens.get(x));
            if (currentConflicts > maxConflicts) {
                maxConflicts = currentConflicts;
            }
        }

        //Find queens with max conflicts
        for (int x : queens) {
            int currentConflicts = getConflictsAtPosition(x, queens.get(x));
            if (currentConflicts == maxConflicts) {
                queensForSwap.add(x);
            }
        }

        int worstQueenIndex = queensForSwap.get(randomGenerator.nextInt(queensForSwap.size()));

        int minimumConflicts = Integer.MAX_VALUE;

        for (int i = 0; i < this.boardSize; i++) {
            int currentConflicts = getConflictsAtPosition(worstQueenIndex, i);
            if (currentConflicts < minimumConflicts) {
                minimumConflicts = currentConflicts;
            }
        }

        List<Integer> moveCandidates = new ArrayList<>();

        for (int i = 0; i < this.boardSize; i++) {
            int currentConflicts = getConflictsAtPosition(worstQueenIndex, i);
            if (currentConflicts == minimumConflicts) {
                moveCandidates.add(i);
            }
        }

        int movePosition = moveCandidates.get(randomGenerator.nextInt(moveCandidates.size()));

        updateConflictsForQueen(worstQueenIndex, queens.get(worstQueenIndex), -1);
        updateConflictsForQueen(worstQueenIndex, movePosition, 1);

        queens.set(worstQueenIndex, movePosition);
    }

    private int getMainDiagonal(int x, int y) {
        return (this.boardSize - ONE - x + y);
    }

    private int getSecondaryDiagonal(int x, int y) {
        return x + y;
    }

    private int getConflictsAtPosition(int x, int y) {
        return columnsConflicts.get(y) +
                mainDiagonalConflicts.get(getMainDiagonal(x, y)) +
                secondaryDiagonalConflicts.get(getSecondaryDiagonal(x, y)) -
                NUMBER_OF_QUEENS;
    }

    private int getTotalConflicts() {
        return getConflicts(columnsConflicts)
                + getConflicts(mainDiagonalConflicts)
                + getConflicts(secondaryDiagonalConflicts);
    }

    private int getConflicts(List<Integer> list) {
        int conflicts = 0;

        for (int currentQueens : list) {
            if (currentQueens > 1) {
                conflicts += (currentQueens - 1);
            }
        }

        return conflicts;
    }

    private void updateConflictsForQueen(int x, int y, int factor) {
        columnsConflicts.set(y, columnsConflicts.get(y) + factor);
        int currentMainDiagonal = getMainDiagonal(x, y);
        mainDiagonalConflicts.set(currentMainDiagonal, mainDiagonalConflicts.get(currentMainDiagonal) + factor);
        int currentSecondaryDiagonal = getSecondaryDiagonal(x, y);
        secondaryDiagonalConflicts.set(currentSecondaryDiagonal, secondaryDiagonalConflicts.get(currentSecondaryDiagonal) + factor);
    }

    private void printQueens(List<Integer> queens, int boardSize) {
        System.out.println();

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (j == queens.get(i)) {
                    System.out.print("1 ");
                } else {
                    System.out.print("0 ");
                }
            }
            System.out.println();
        }
    }
}