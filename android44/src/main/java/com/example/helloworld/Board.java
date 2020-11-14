package com.example.helloworld;

public class Board {
    /**
     * Initializes the chess board
     * @return returns 2d array of Piece objects
     */
    public static Piece[][] initializeBoard() {
        Piece[][] board = new Piece[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (row == 1) {
                    board[row][col] = new Pawn(row, col, "black");
                } else if (row == 6) {
                    board[row][col] = new Pawn(row, col, "white");
                } else if (row == 0 || row == 7) {
                    String color;
                    if (row == 0) {
                        color = "black";
                    } else {
                        color = "white";
                    }
                    if (col == 0 || col == 7) {
                        board[row][col] = new Rook(row, col, color);
                    }
                    if (col == 1 || col == 6) {
                        board[row][col] = new Knight(row, col, color);
                    }
                    if (col == 2 || col == 5) {
                        board[row][col] = new Bishop(row, col, color);
                    }
                    if (col == 3) {
                        board[row][col] = new Queen(row, col, color);
                    }
                    if (col == 4) {
                        board[row][col] = new King(row, col, color);
                    }
                }
            }

        }
        return board;
    }

    /**
     * Prints the given 2D Piece array
     * @param board 2D array of Pieces to be printed
     */
    public static void printBoard(Piece[][] board){
        System.out.println();
        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){
                if (board[row][col] == null) {
                    if ((row + col) % 2 == 0) {
                        System.out.print("  ");
                    } else {
                        System.out.print("##");
                    }
                } else {
                    System.out.print(board[row][col]);
                }
                System.out.print(" ");
            }
            System.out.println(8 - row);
        }
        System.out.println(" a  b  c  d  e  f  g  h");
        System.out.println();
    }
}
