package com.snozza.connect4;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Connect4 {

    private static final int ROWS = 6;
    private static final int COLUMNS = 7;
    private static final int DISCS_TO_WIN = 4;
    private static final String EMPTY = " ";
    private static final String RED = "R";
    private static final String GREEN = "G";
    private static final String DELIMITER = "|";

    private PrintStream outputChannel;
    private String winner = "";
    private String currentPlayer = "R";
    private String[][] board = new String[ROWS][COLUMNS];

    public Connect4(PrintStream out) {
        outputChannel = out;
        for (String[] row : board) {
            Arrays.fill(row, EMPTY);
        }
    }

    public String getCurrentPlayer() {
        outputChannel.printf("Player %s turn%n", currentPlayer);
        return currentPlayer;
    }

    private void printBoard() {
       for (int row = ROWS - 1; row >= 0; row--) {
           StringJoiner stringJoiner = new StringJoiner(DELIMITER, DELIMITER, DELIMITER);
           Stream.of(board[row]).forEachOrdered(stringJoiner::add);
           outputChannel.println(stringJoiner.toString());
       }
    }

    public int getNumberOfDiscs() {
        return IntStream.range(0, COLUMNS).map(this::getNumberOfDiscsInColumn).sum();
    }

    private void switchPlayer() {
        if (RED.equals(currentPlayer)) {
            currentPlayer = GREEN;
        } else {
            currentPlayer = RED;
        }
    }

    public boolean isFinished() {
        return getNumberOfDiscs() == ROWS * COLUMNS;
    }

    public int putDiscInColumn(int column) {
        checkColumn(column);
        int row = getNumberOfDiscsInColumn(column);
        checkInsertPosition(row, column);
        board[row][column] = getCurrentPlayer();
        printBoard();
        checkWinner(row, column);
        switchPlayer();
        return row;
    }

    private void checkInsertPosition(int row, int column) {
        if (row == ROWS) {
            throw new RuntimeException("No more room in column " + column);
        }
    }

    private void checkColumn(int column) {
        if (column < 0 || column >= COLUMNS) {
            throw new RuntimeException("Invalid column " + column);
        }
    }

    private int getNumberOfDiscsInColumn(int column) {
        return (int) IntStream.range(0, ROWS)
                    .filter(row -> !EMPTY.equals(board[row][column])).count();
    }

    public String getWinner() {
        return winner;
    }

    private void checkWinner(int row, int column) {
        String color = board[row][column];
        Pattern winPattern = Pattern.compile(".*" + color + "{" + DISCS_TO_WIN + "}.*");
        String vertical = IntStream.range(0, ROWS)
                .mapToObj(r -> board[r][column])
                .reduce(String::concat).get();
        System.out.println(vertical);
        if (winPattern.matcher(vertical).matches())
            winner = color;
    }
}