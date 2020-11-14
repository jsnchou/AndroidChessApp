package com.example.helloworld;

public class Pawn extends Piece {

    /**
     * Boolean indicating whether it is specific pawn's first move
     */
    public boolean firstMove = true;

    /**
     * Integer telling X value of white pawn's en passant shadow
     */
    public static int epwX = -1;

    /**
     * Integer telling Y value of white pawn's en passant shadow
     */
    public static int epwY = -1;

    /**
     * Integer telling X value of black pawn's en passant shadow
     */
    public static int epbX = -1;

    /**
     * Integer telling Y value of black pawn's en passant shadow
     */
    public static int epbY = -1;

    /**
     * String telling what piece to promote to
     */
    public static String promote;


    /**
     * Pawn constructor
     * @param x - integer telling x location
     * @param y - integer telling y location
     * @param color - String telling which side
     */
    public Pawn(int x, int y, String color) {
        super(x, y, color);
        // TODO Auto-generated constructor stub
    }

    /**
     * Pawn toString() method
     * @return String representing Pawn Class
     */
    public String toString() {
        return super.toString() + "p";
    }

    /**
     * Resets en passante at the start of player turn
     * @param turn - String representing which side's turn
     */
    public static void checkEnPassante(String turn){
        if (turn.equals("White's ")){
            epbX = -1;
            epbY = -1;
        } else {
            epwX = -1;
            epwY = -1;
        }
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
        if(this.firstMove)
            this.firstMove = false;

        this.x = fX;
        this.y = fY;

        board[sX][sY] = null;
        Piece captured = board[fX][fY];
        // promotes the pawn to whatever chosen role
        if ((this.color.equals("white") && fX == 0) || (this.color.equals("black") && fX == 7)){
            if (!MainActivity.checkPromotion){
                promote = null;
            } else {
                promote = MainActivity.promotion;
            }
            if (promote == null){
                board[fX][fY] = new Queen(fX, fY, this.color);
                promote = null;
                return captured;
            }
            if (promote.equals("n")){
                board[fX][fY] = new Knight(fX, fY, this.color);
            }
            if (promote.equals("q")){
                board[fX][fY] = new Queen(fX, fY, this.color);
            }
            if (promote.equals("r")){
                board[fX][fY] = new Rook(fX, fY, this.color);
            }
            if (promote.equals("b")){
                board[fX][fY] = new Bishop(fX, fY, this.color);
            }
        } else {
            board[fX][fY] = this;
        }
        promote = null;
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
    @Override
    public boolean isValidMove(Piece[][] board, int sX, int sY, int fX, int fY) {

        //System.out.println(sX + " " + sY + " " + fX + " " + fY);

        if(board[sX][sY] == null)
            return false;
//		if((fX == 0 && this.color.equals("white") && promote == null) || (fX == 7 && this.color.equals("black") && promote == null)){
//			return false;
//		}
        if(this.color.equals("white")) {
            if(firstMove) {
                if(sX - 2 == fX) {
                    if(sY == fY && board[fX][fY] == null && board[sX-1][fY] == null) {
                        // this sets up en passante for next turn
                        epwX = sX - 1;
                        epwY = sY;
                        return true;
                    }
                }
            }
            //move forward
            if(sX - 1 == fX && sY == fY) {
                if(board[fX][fY] != null)
                    return false;
                else
                    return true;
            }
            //capture piece
            else if((sX - 1 == fX && sY - 1 == fY) || (sX - 1 == fX && sY + 1 == fY)) {
                if(fX == epbX && fY == epbY){
                    Piece capturedPawn = board[fX+1][fY];
                    this.move(board, this.x, this.y, fX, fY);
                    board[fX + 1][fY] = null;
                    if(board[King.kwY][King.kwX].inCheck(board)){
                        this.move(board, this.x, this.y, sX, sY);
                        board[fX + 1][fY] = capturedPawn;
                        return false;
                    }
                    this.move(board, this.x, this.y, sX, sY);
                    return true;
                }
                if(board[fX][fY]!= null && board[fX][fY].color.equals("black"))
                    return true;
                else
                    return false;
            }

        }
        //black
        else {
            if(firstMove) {
                if(sX + 2 == fX) {
                    if(sY == fY && board[fX][fY] == null && board[sX+1][fY] == null) {
                        // this sets up en passante for next turn
                        epbX = sX + 1;
                        epbY = sY;
                        return true;
                    }
                }
            }
            if(sX + 1 == fX && sY == fY) {
                if(board[fX][fY] != null)
                    return false;
                else
                    return true;
            }
            //capture piece
            else if((sX + 1 == fX && sY + 1 == fY) || (sX + 1 == fX && sY - 1 == fY)) {
                if(fX == epwX && fY == epwY){
                    Piece capturedPawn = board[fX-1][fY];
                    this.move(board, this.x, this.y, fX, fY);
                    board[fX -1][fY] = null;
                    if(board[King.kbY][King.kbX].inCheck(board)){
                        this.move(board, this.x, this.y, sX, sY);
                        board[fX - 1][fY] = capturedPawn;
                        return false;
                    }
                    this.move(board,this.x,this.y, sX, sY);
                    return true;
                }
                if(board[fX][fY]!= null && board[fX][fY].color.equals("white"))
                    return true;
                else
                    return false;
            }
        }

        return false;


    }

}