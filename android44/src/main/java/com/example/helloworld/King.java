package com.example.helloworld;

public class King extends Piece {

    /**
     * Integer representing x location of white king
     */
    public static int kwX = 4;

    /**
     * Integer representing y location of white king
     */
    public static int kwY = 7;

    /**
     * Integer representing x location of black king
     */
    public static int kbX = 4;

    /**
     * Integer representing y location of black king
     */
    public static int kbY= 0;

    /**
     * String that keeps track of which turn
     */
    public static String turn = "White's ";


    /**
     * King constructor
     * @param x - integer telling x location
     * @param y - integer telling y location
     * @param color - String telling which side
     */
    public King(int x, int y, String color) {
        super(x, y, color);
        // TODO Auto-generated constructor stub
    }
    /**
     * King toString() method
     * @return String representing King Class
     */
    public String toString() {
        return super.toString() + "K";
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
    @Override
    public Piece move(Piece[][] board, int sX, int sY, int fX, int fY) {
        // TODO Auto-generated method stub
        this.x = fX;
        this.y = fY;

        if (this.color.equals("white")){
            kwX = fY;
            kwY = fX;
        } else {
            kbX = fY;
            kbY = fX;
        }
//		System.out.println(kwX + " " + kwY);
        board[sX][sY] = null;
        Piece captured = board[fX][fY];
        board[fX][fY] = this;
        return captured;

    }
    /**
     * Method that checks whether a move is acceptable before moving
     * @param board - Piece[][] of inputted board
     * @param sX - integer representing starting x value
     * @param sY - integer representing starting y value
     * @param fX - integer representing ending x value
     * @param fY - integer representing ending y value
     * @return boolean representing whether move is valid or not
     */

    public boolean isValidMove(Piece[][] board, int sX, int sY, int fX, int fY) {
        // TODO Auto-generated method stub
        int x_diff = Math.abs(fX - sX);
        int y_diff = Math.abs(fY - sY);

        try {
            if(board[sX][sY].color.equals(board[fX][fY].color))
                return false;
        }
        catch(Exception e) {};

        if (!haveMoved){
            if (sX == 0 && (fY == 2 || fY == 6) && turn.equals("Black's ")){
                if (fY == 2 && board[0][1] == null && board[0][2] == null && board[0][3] == null && board[0][0] != null && !board[0][0].haveMoved){
                    this.move(board, this.x , this.y, 0, 3);
                    if (this.inCheck(board)) {
                        this.move(board, this.x , this.y, 0, 4);
                        return false;
                    }
                    this.move(board, this.x, this.y, 0, 2);
                    if (this.inCheck(board)){
                        this.move(board,this.x, this.y, 0, 4);
                        return false;
                    }
                    this.move(board,this.x, this.y, 0, 4);
                    board[0][0].move(board, 0, 0, 0, 3);
                    return true;
                }
                if (fY == 6 && board [0][5] == null && board[0][6] == null && board[0][7] != null && !board[0][7].haveMoved){
                    this.move(board, this.x , this.y, 0, 5);
                    if (this.inCheck(board)) {
                        this.move(board, this.x , this.y, 0, 4);
                        return false;
                    }
                    this.move(board, this.x, this.y, 0, 6);
                    if (this.inCheck(board)){
                        this.move(board,this.x, this.y, 0, 4);
                        return false;
                    }
                    this.move(board,this.x, this.y, 0, 4);
                    board[0][7].move(board, 0, 7, 0, 5);
                    return true;
                }
            }
            if (sX == 7 && (fY == 2 || fY == 6) && turn.equals("White's ")){
                if (fY == 2 && board[7][1] == null && board[7][2] == null && board[7][3] == null && board[7][0] != null && !board[7][0].haveMoved){
                    this.move(board, this.x , this.y, 7, 3);
                    if (this.inCheck(board)) {
                        this.move(board, this.x , this.y, 7, 4);
                        return false;
                    }
                    this.move(board, this.x, this.y, 7, 2);
                    if (this.inCheck(board)){
                        this.move(board,this.x, this.y, 7, 4);
                        return false;
                    }
                    this.move(board,this.x, this.y, 7, 4);
                    board[7][0].move(board, 7, 0, 7, 3);
                    return true;
                }
                if (fY == 6 && board[7][5] == null && board[7][6] == null && board[7][7] != null && !board[7][7].haveMoved){
                    this.move(board, this.x , this.y, 7, 5);
                    if (this.inCheck(board)) {
                        this.move(board, this.x , this.y, 7, 4);
                        return false;
                    }
                    this.move(board, this.x, this.y, 7, 6);
                    if (this.inCheck(board)){
                        this.move(board,this.x, this.y, 7, 4);
                        return false;
                    }
                    this.move(board,this.x, this.y, 7, 4);
                    board[7][7].move(board, 7, 7, 7, 5);
                    return true;
                }
            }
        }

        if(x_diff == 1)
            return y_diff == 1 || y_diff == 0;

        if(y_diff == 1)
            return x_diff == 1 || x_diff == 0;


        return false;
    }
    /**
     * Check whether Piece is in check
     * @param board - Piece[][] of inputted board
     * @return tells whether piece is in check
     */
    public boolean inCheck(Piece[][] board) {
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                if(board[i][j] != null && this.color != board[i][j].color) {
                    if(board[i][j].isValidMove(board, board[i][j].x, board[i][j].y, this.x, this.y))
                        return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks whether King object is in Checkmate
     * @param board - Piece[][] of inputted board
     * @return boolean representing whether King object is in Checkmate
     */
    public boolean inCheckmate(Piece[][] board) {
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                if(board[i][j] != null && board[i][j].color.equals(this.color)) {
                    for(int a = 0; a < board.length; a++) {
                        for(int b = 0; b < board[a].length; b++) {
                            if(board[i][j].isValidMove(board, i, j, a, b)) {
                                Piece temp = board[i][j].move(board, i, j, a, b);
                                if(!this.inCheck(board)) {
                                    //System.out.println(i + " " + j + " " + a + " " + b);
                                    board[a][b].move(board, a, b, i, j);
                                    board[a][b] = temp;
                                    return false;
                                }
                                board[a][b].move(board, a, b, i, j);
                                board[a][b] = temp;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

}
