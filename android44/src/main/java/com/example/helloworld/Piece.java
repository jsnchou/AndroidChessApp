package com.example.helloworld;

public abstract class Piece {

    /**
     * Integer for current x location
     */
    public int x;

    /**
     * Integer for current y location
     */
    public int y;

    /**
     * String indicating color
     */
    public String color;

    /**
     * Boolean for whether piece has move
     */
    public boolean haveMoved;

    /**
     * Piece constructor
     * @param x - integer telling starting x location
     * @param y - integer telling starting y location
     * @param color - String telling which side
     */
    public Piece(int x, int y, String color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    /**
     * Piece toString() method
     * @return String representing Piece Class color
     */
    public String toString() {
        return this.color.substring(0,1);
    }

    /**
     * Method that moves the piece from starting location to ending location
     * @param board - Piece[][] of inputted board
     * @param sX - integer representing starting x value
     * @param sY - integer representing starting y value
     * @param fX - integer representing ending x value
     * @param fY - integer representing ending y value
     * @return Piece object representing captured piece
     */
    public abstract Piece move(Piece[][] board, int sX, int sY, int fX, int fY);
    /**
     * Method that checks whether a move is acceptable before moving
     * @param board - Piece[][] of inputted board
     * @param sX - integer representing starting x value
     * @param sY - integer representing starting y value
     * @param fX - integer representing ending x value
     * @param fY - integer representing ending y value
     * @return boolean representing whether move is valid or not
     */
    public abstract boolean isValidMove(Piece[][] board, int sX, int sY, int fX, int fY);
    /**
     * Check whether Piece is in check
     * @param board - Piece[][] of inputted board
     * @return tells whether piece is in check
     */
    public abstract boolean inCheck(Piece[][] board);
}
