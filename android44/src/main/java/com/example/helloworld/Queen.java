package com.example.helloworld;

public class Queen extends Piece {
    /**
     * Queen constructor
     * @param x - integer telling x location
     * @param y - integer telling y location
     * @param color - String telling which side
     */
    public Queen(int x, int y, String color) {
        super(x, y, color);
        // TODO Auto-generated constructor stub
    }
    /**
     * Queen toString() method
     * @return String representing Queen Class
     */
    public String toString() {
        return super.toString() + "Q";
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

        board[sX][sY] = null;
        Piece captured = board[fX][fY];
        board[fX][fY] = this;
        return captured;
    }
    /**
     * Check whether Piece is in check
     * @param board - Piece[][] of inputted board
     * @return tells whether piece is in check
     */
    @Override
    public boolean inCheck(Piece[][] board){
        return false;
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
    @Override
    public boolean isValidMove(Piece[][] board, int sX, int sY, int fX, int fY) {
        // TODO Auto-generated method stub

        if(board[sX][sY] == null)
            return false;

        try {
            if(board[sX][sY].color.equals(board[fX][fY].color))
                return false;
        }
        catch(Exception e) {};

        int x_diff = Math.abs(fX - sX);
        int y_diff = Math.abs(fY - sY);


        if((sX == fX) ^ (sY == fY)) {
            if(sX == fX) {
                do {
                    if(board[sX][sY] != null && !this.equals(board[sX][sY]) )
                        return false;
                    sY = sY < fY ? sY + 1 : sY - 1;
                }while(sY != fY);

            }
            else {
                do {
                    if(board[sX][sY] != null && !this.equals(board[sX][sY]))
                        return false;
                    sX = sX < fX ? sX + 1 : sX - 1;
                }while(sX != fX);
            }

            return true;
        }
        else if(x_diff == y_diff) {

            do {
                if(board[sX][sY] != null && !this.equals(board[sX][sY]))
                    return false;
                sX = sX < fX ? sX + 1 : sX - 1;
                sY = sY < fY ? sY + 1 : sY - 1;
            }while(sX != fX && sY != fY);

            return true;

        }

        return false;
    }

}
