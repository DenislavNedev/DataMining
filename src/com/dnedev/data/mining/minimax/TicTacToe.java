package com.dnedev.data.mining.minimax;

import java.util.Scanner;

public class TicTacToe {
    private static final int BOARD_SIZE = 3;
    private static final String EMPTY_SPACE = "_";
    private static final String HUMAN_SYMBOL = "O";
    private static final String COMPUTER_SYMBOL = "X";
    private static final String TIE = "Tie";

    private String[][] board;
    private Scanner scanner;

    public TicTacToe() {
        this.scanner = new Scanner(System.in);
        initBoard();
    }

    public void startGame() {
        System.out.println("Enter 0 for you to be first, and 1 for the computer: ");

        int isHumanFirst = Integer.parseInt(scanner.nextLine());

        if (isHumanFirst == 0) {
            while (!checkGameStatus().isGameFinished()) {
                humanMove();
                System.out.println("Your's move: ");
                printBoard();
                if (checkGameStatus().isGameFinished()) {
                    break;
                }
                computerMove();
                System.out.println("Computer's move: ");
                printBoard();
            }
        } else {
            while (!checkGameStatus().isGameFinished()) {
                computerMove();
                System.out.println("Computer's move: ");
                printBoard();
                if (checkGameStatus().isGameFinished()) {
                    break;
                }
                humanMove();
                System.out.println("Your's move: ");
                printBoard();
            }
        }

        System.out.println("Winner: " + checkGameStatus().getWinner());
    }

    private void humanMove() {
        System.out.println("Row: ");
        int row = Integer.parseInt(scanner.nextLine());
        System.out.println("Column: ");
        int column = Integer.parseInt(scanner.nextLine());
        if (board[row - 1][column - 1].equals(EMPTY_SPACE)) {
            board[row - 1][column - 1] = HUMAN_SYMBOL;
        } else {
            System.out.println("Enter empty position!");
            humanMove();
        }
    }

    private void computerMove() {
        int bestScore = Integer.MIN_VALUE;
        int row = 0;
        int column = 0;

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j].equals(EMPTY_SPACE)) {
                    board[i][j] = COMPUTER_SYMBOL;
                    int score = minMax(board, false, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
                    board[i][j] = EMPTY_SPACE;
                    if (score > bestScore) {
                        bestScore = score;
                        row = i;
                        column = j;
                    }
                }
            }
        }

        board[row][column] = COMPUTER_SYMBOL;
    }

    private void initBoard() {
        this.board = new String[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = EMPTY_SPACE;
            }
        }
    }

    private void printBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private Pair checkGameStatus() {

        //Horizontal
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (isLine(board[i][0], board[i][1], board[i][2])) {
                return new Pair(true, board[i][0]);
            }
        }

        //Vertical
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (isLine(board[0][i], board[1][i], board[2][i])) {
                return new Pair(true, board[0][i]);
            }
        }

        //Diagonal
        if (isLine(board[0][0], board[1][1], board[2][2])) {
            return new Pair(true, board[0][0]);
        }

        if (isLine(board[2][0], board[1][1], board[0][2])) {
            return new Pair(true, board[2][0]);
        }

        int emptySpaces = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j].equals(EMPTY_SPACE)) {
                    emptySpaces++;
                }
            }
        }

        if (emptySpaces == 0) {
            return new Pair(true, TIE);
        }
        return new Pair(false, null);
    }

    private boolean isLine(String a, String b, String c) {
        return a.equals(b) && b.equals(c) && !a.equals(EMPTY_SPACE);
    }

    private int minMax(String[][] board, boolean isMaximizing, int alpha, int beta, int depth) {
        String winner = checkGameStatus().getWinner();
        if (winner != null) {
            return getScore(winner, depth);
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            outerLoop:
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (board[i][j].equals(EMPTY_SPACE)) {
                        board[i][j] = COMPUTER_SYMBOL;
                        int score = minMax(board, false, alpha, beta, depth++);
                        board[i][j] = EMPTY_SPACE;
                        bestScore = Math.max(score, bestScore);
                        alpha = Math.max(alpha, bestScore);
                        if (beta <= alpha) break outerLoop;
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            outerLoop:
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (board[i][j].equals(EMPTY_SPACE)) {
                        board[i][j] = HUMAN_SYMBOL;
                        int score = minMax(board, true, alpha, beta, depth++);
                        board[i][j] = EMPTY_SPACE;
                        bestScore = Math.min(score, bestScore);
                        beta = Math.min(beta, bestScore);
                        if (beta <= alpha) break outerLoop;
                    }
                }
            }
            return bestScore;
        }
    }

    private int getScore(String player, int depth) {
        switch (player) {
            case HUMAN_SYMBOL:
                return -10 + depth;
            case COMPUTER_SYMBOL:
                return 10 - depth;
        }
        return 0;
    }
}