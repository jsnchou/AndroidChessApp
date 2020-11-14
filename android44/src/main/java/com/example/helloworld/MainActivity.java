package com.example.helloworld;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;




public class MainActivity extends AppCompatActivity{

    static Piece[][] board = Board.initializeBoard();
    static String turn = "White's ";
    String motive = "start";
    int identifier = 0;

    public static boolean checkPromotion = false;

    static int sPosY = -1;
    static int sPosX = -1;
    static int fPosY = -1;
    static int fPosX = -1;

    int playMove;


    ImageButton promote1;
    ImageButton promote2;
    ImageButton promote3;
    ImageButton promote4;

    Button drawButton;

    boolean whiteDraw = false;
    boolean blackDraw = false;
    static boolean inProgress = true;

    boolean undoAvailable = false;

    private String m_Text = "";

    static String promotion;

    int counter = 0;
    static HashMap<Character, Integer> mapX = new HashMap<>();
    static HashMap<Character, Integer> mapY = new HashMap<>();

    public static ArrayList<String> moveList = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int counter = 0;
        for(char c = 'a'; c <= 'h'; c++)
            mapX.put(c, counter++);

        counter = 7;
        for(char c = '1'; c <= '8'; c++)
            mapY.put(c, counter--);

        rebaseBoard();
        int id = getResources().getIdentifier("promote1", "id", getPackageName());
        promote1 = findViewById(id);
        id = getResources().getIdentifier("promote2", "id", getPackageName());
        promote2 = findViewById(id);
        id = getResources().getIdentifier("promote3", "id", getPackageName());
        promote3 = findViewById(id);
        id = getResources().getIdentifier("promote4", "id", getPackageName());
        promote4 = findViewById(id);
        drawButton = findViewById(getResources().getIdentifier("draw", "id", getPackageName()));

        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        try {
            Gson gson = new Gson();
            String json = mPrefs.getString("Game List", null);
            Type type = new TypeToken<ArrayList<Game>>() {}.getType();
            ArrayList<Game> obj = gson.fromJson(json, type);
            for (int i = 0; i < obj.size(); i++){
                Recording.gameList.add(obj.get(i));
//                System.out.println(obj.get(i));
            }
        } catch (Exception e){

        }

    }
    @Override
    protected void onResume(){
        super.onResume();
        if (!inProgress) {
            turn = "White's ";
            board = Board.initializeBoard();
            inProgress = false;
            playMove = 0;
            Button nt = (Button) findViewById(R.id.nextTurn);
            nt.setVisibility(View.VISIBLE);
            rebaseBoard();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    public void boardClick(View view){

//        System.out.println(view.getTag());
        //TextView info = findViewById(R.id.information);

        if(inProgress) {
            if (motive.contentEquals("start")) {


                sPosX = mapY.get(view.getTag().toString().charAt(1));
                sPosY = mapX.get(view.getTag().toString().charAt(0));
//                System.out.println("sPosY: " + sPosY + " sPosX: " + sPosX);
                identifier = view.getId();
                if (board[sPosX][sPosY] != null && board[sPosX][sPosY].color.contentEquals(turn.substring(0, 5).toLowerCase())) {
//                    System.out.println(turn);
                    motive = "end";
                }
                if (board[sPosX][sPosY] instanceof Pawn && (board[sPosX][sPosY].color.contentEquals("white")) && (sPosX == 1)) {
                    for (int i = 0; i < 8; i++) {
                        if (board[sPosX][sPosY].isValidMove(board, sPosX, sPosY, 0, i)) {
                            promote1.setImageDrawable(getResources().getDrawable(R.drawable.white_queen));
                            promote2.setImageDrawable(getResources().getDrawable(R.drawable.white_rook));
                            promote3.setImageDrawable(getResources().getDrawable(R.drawable.white_bishop));
                            promote4.setImageDrawable(getResources().getDrawable(R.drawable.white_knight));
                        }
                    }
                }
                if (board[sPosX][sPosY] instanceof Pawn && (board[sPosX][sPosY].color.contentEquals("black")) && (sPosX == 6)) {
                    for (int i = 0; i < 8; i++) {
                        if (board[sPosX][sPosY].isValidMove(board, sPosX, sPosY, 7, i)) {
                            promote1.setImageDrawable(getResources().getDrawable(R.drawable.black_queen));
                            promote2.setImageDrawable(getResources().getDrawable(R.drawable.black_rook));
                            promote3.setImageDrawable(getResources().getDrawable(R.drawable.black_bishop));
                            promote4.setImageDrawable(getResources().getDrawable(R.drawable.black_knight));
                        }
                    }
                }
            } else if (motive.contentEquals("end")) {

                promote1.setImageDrawable(getResources().getDrawable(R.drawable.transparent));
                promote2.setImageDrawable(getResources().getDrawable(R.drawable.transparent));
                promote3.setImageDrawable(getResources().getDrawable(R.drawable.transparent));
                promote4.setImageDrawable(getResources().getDrawable(R.drawable.transparent));
                fPosX = mapY.get(view.getTag().toString().charAt(1));
                fPosY = mapX.get(view.getTag().toString().charAt(0));
//                System.out.println("fPosY: " + fPosY + " fPosX: " + fPosX);
                if (checkBoard(board, sPosX, sPosY, fPosX, fPosY)) {
                    if (promotion != null)
                        moveList.add(findViewById(identifier).getTag() + " " + view.getTag() + " " + promotion);
                    else
                        moveList.add(findViewById(identifier).getTag() + " " + view.getTag());

//                    System.out.println("Previous Moves:");
                    for (int i = 0; i < moveList.size(); i++){
//                        System.out.println(moveList.get(i));
                    }
                    if (turn.equals("White's ")) {
                        whiteDraw = false;
                        if (blackDraw == true) {
                            drawButton.setText("draw?");
                        } else {
                            drawButton.setText("draw");
                        }
                    } else {
                        if (whiteDraw == true) {
                            drawButton.setText("draw?");
                        } else {
                            drawButton.setText("draw");
                        }
                        blackDraw = false;
                    }
                    TextView info = findViewById(R.id.information);
                    if (info.getText().toString().equals("White's turn."))
                        info.setText("Black's turn.");
                    else if(info.getText().toString().equals("Black's turn."))
                        info.setText("White's turn.");
                    undoAvailable = true;
                }
                promotion = null;
                motive = "start";
                rebaseBoard();
            }
            //rebaseBoard();
//            Board.printBoard(board);
        }
    }

    public void promotionClick(View view){
        promotion = (String) view.getTag();
    }



    public boolean checkBoard (Piece[][] board, int sPosX, int sPosY, int fPosX, int fPosY){
        checkPromotion = true;

        if (board[sPosX][sPosY] == null){
            return false;
        } else if (!(board[sPosX][sPosY].color.equals(turn.substring(0, 5).toLowerCase()))){
            return false;
        } else if (!board[sPosX][sPosY].isValidMove(board, sPosX, sPosY, fPosX, fPosY)){
            return false;
        } else {
            Piece captured = null;
            try{
                captured = board[sPosX][sPosY].move(board, sPosX, sPosY, fPosX, fPosY);
            }
            catch(Exception E) {};
            checkPromotion = false;
            if (turn.equals("White's ")){

                if (board[King.kwY][King.kwX].inCheck(board)) {
                    board[fPosX][fPosY].move(board, fPosX, fPosY, sPosX, sPosY);
                    board[fPosX][fPosY] = captured;
                    return false;
                }
            } else {
                if (board[King.kbY][King.kbX].inCheck(board)) {

                    board[fPosX][fPosY].move(board, fPosX, fPosY, sPosX, sPosY);
                    board[fPosX][fPosY] = captured;
                    return false;
                }
            }
            checkPromotion = true;
        }
        board[fPosX][fPosY].haveMoved = true;
        Pawn.checkEnPassante(turn);
        turn = turn.equals("White's ") ? "Black's " : "White's ";
        King.turn = turn;
        checkPromotion = false;
        if (turn.equals("White's ") && board[King.kwY][King.kwX].inCheck(board)){
            if(((King)board[King.kwY][King.kwX]).inCheckmate(board)) {
//                System.out.println("CHECKMATE 1");
//                System.out.println("Checkmate\nBlack Wins");
                TextView info = findViewById(R.id.information);
                info.setText("Checkmate. Black wins.");
                endGame();
                inProgress = false;
                return true;
            }
//            System.out.println("Check");
        }
        if (turn.equals("Black's ")&& board[King.kbY][King.kbX].inCheck(board)) {
            if(((King)board[King.kbY][King.kbX]).inCheckmate(board)) {
//                System.out.println("CHECKMATE 2");
//                System.out.println("Checkmate\nWhite Wins");
                TextView info = findViewById(R.id.information);
                info.setText("Checkmate. White wins.");
                endGame();
                inProgress = false;
                return true;
            }
//            System.out.println("Check");
        }
        checkPromotion = true;
        return true;
    }


    public void drawClick(View view){
        TextView info = findViewById(R.id.information);

        if(drawButton.getText().toString().equals("draw?")){
            info.setText("Draw.");
            inProgress = false;
            Button ng = (Button) findViewById(R.id.newGame);
            ng.setVisibility(View.VISIBLE);
            Button play = (Button) findViewById(R.id.playBack);
            play.setVisibility(View.VISIBLE);
            return;
        }
        if (turn.contentEquals("White's ")){
            if (blackDraw == true){
//                System.out.println("draw!");
            }
            whiteDraw = true;
        } else {
            if (whiteDraw == true){
//                System.out.println("draw!");
            }
            blackDraw = true;
        }

    }

    public void resignClick(View view){
        TextView info = findViewById(R.id.information);
        inProgress = false;
        if(turn.contentEquals("White's "))
            info.setText("White resigns. Black wins.");
        else
            info.setText("Black resigns. White wins.");

//        System.out.println("End Moves:");
        for (int i = 0; i < moveList.size(); i++){
//            System.out.println(moveList.get(i));
        }

        endGame();

    }

    public void endGame(){
        Button ng = (Button) findViewById(R.id.newGame);
        ng.setVisibility(View.VISIBLE);
        Button play = (Button) findViewById(R.id.playBack);
        play.setVisibility(View.VISIBLE);
    }
    public void aiClick(View view){
        if (inProgress) {
            if (turn.equals("White's ")) {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (board[i][j] != null && board[i][j].color.equals("white")) {
                            for (int a = 0; a < 8; a++) {
                                for (int b = 0; b < 8; b++) {
//                                    System.out.println("i: " + i + " j: " + j + " a: " + a + " b: " + b);
                                    //if (board[i][j].isValidMove(board, i, j, a, b)) {
                                    if (checkBoard(board, i, j, a, b)) {
                                        //board[i][j].move(board, i, j, a, b);
                                        rebaseBoard();
                                        TextView info = findViewById(R.id.information);
                                        info.setText("Black's turn.");
                                        turn = "Black's ";
                                        moveList.add("" + "abcdefgh".charAt(j) + (8 - i) + " " + "abcdefgh".charAt(b) + (8 - a));
                                        return;
                                    }

                                }
                            }
                        }

                    }
                }
            } else {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (board[i][j] != null && board[i][j].color.equals("black")) {
                            for (int a = 0; a < 8; a++) {
                                for (int b = 0; b < 8; b++) {
//                                    System.out.println("i: " + i + " j: " + j + " a: " + a + " b: " + b);
                                    //if(board[i][j].isValidMove(board, i, j, a, b)){
                                    if (checkBoard(board, i, j, a, b)) {
                                        //board[i][j].move(board, i, j, a, b);
                                        rebaseBoard();
                                        turn = "White's ";
                                        TextView info = findViewById(R.id.information);
                                        info.setText("White's turn.");
                                        moveList.add("" + "abcdefgh".charAt(j) + (8 - i) + " " + "abcdefgh".charAt(b) + (8 - a));
                                        return;
                                    }
                                }
                            }
                        }

                    }
                }

            }
        }
    }

    public void nextTurn(View view){
        if (playMove < moveList.size()) {
            int sPosY = -1;
            int sPosX = -1;
            int fPosY = -1;
            int fPosX = -1;
            String sPos;
            String fPos;
            String pawnPromote = null;
            boolean illegal = false;

            String move;

            if (turn.equals("White's ") && board[King.kwY][King.kwX].inCheck(board)) {
                if (((King) board[King.kwY][King.kwX]).inCheckmate(board)) {
//                    System.out.println("CHECKMATE 3");
//                    System.out.println("Checkmate\nBlack Wins");
                    TextView info = findViewById(R.id.information);
                    info.setText("Checkmate. Black wins.");
                    endGame();
                    inProgress = false;
                    return;
                }
//                System.out.println("Check");
            }
            if (turn.equals("Black's ") && board[King.kbY][King.kbX].inCheck(board)) {
                if (((King) board[King.kbY][King.kbX]).inCheckmate(board)) {
//                    System.out.println("CHECKMATE 4");
//                    System.out.println("Checkmate\nWhite Wins");
                    TextView info = findViewById(R.id.information);
                    info.setText("Checkmate. White wins.");
                    endGame();
                    inProgress = false;
                    return;
                }
//                System.out.println("Check");
            }


            do {
//                System.out.print(turn + "move: ");
                move = moveList.get(playMove);

                try {
                    sPos = move.substring(0, move.indexOf(" "));
                    fPos = move.substring(move.indexOf(" ") + 1);
                } catch (Exception e) {
                    illegal = true;
                    continue;
                }
                ;
                pawnPromote = null;
                if (move.length() > 6) {
                    pawnPromote = move.substring(6, 7);
                }

                Pawn.promote = pawnPromote;

                if (mapX.containsKey((sPos.charAt(0)))) {
                    sPosY = mapX.get(sPos.charAt(0));
                } else {
                    illegal = true;
                    continue;
                }

                if (mapY.containsKey(sPos.charAt(1))) {
                    sPosX = mapY.get(sPos.charAt(1));
                } else {
                    illegal = true;
                }
                if (mapX.containsKey(fPos.charAt(0))) {
                    fPosY = mapX.get(fPos.charAt(0));
                } else {
                    illegal = true;
                }
                if (mapY.containsKey(fPos.charAt(1))) {
                    fPosX = mapY.get(fPos.charAt(1));
                } else {
                    illegal = true;
                }
                if (board[sPosX][sPosY] == null) {
                    illegal = true;
                } else if (!(board[sPosX][sPosY].color.equals(turn.substring(0, 5).toLowerCase()))) {
                    illegal = true;
                } else if (!board[sPosX][sPosY].isValidMove(board, sPosX, sPosY, fPosX, fPosY)) {
                    illegal = true;
                } else {
                    Piece captured = null;
                    try {
                        captured = board[sPosX][sPosY].move(board, sPosX, sPosY, fPosX, fPosY);
                    } catch (Exception E) {
                    }
                    ;
                    if (turn.equals("White's ")) {

                        if (board[King.kwY][King.kwX].inCheck(board)) {
                            if (board[fPosX][fPosY] instanceof King) {

                            }
                            board[fPosX][fPosY].move(board, fPosX, fPosY, sPosX, sPosY);

                            board[fPosX][fPosY] = captured;
                            illegal = true;
                        }
                    } else {
                        if (board[King.kbY][King.kbX].inCheck(board)) {

                            board[fPosX][fPosY].move(board, fPosX, fPosY, sPosX, sPosY);
                            board[fPosX][fPosY] = captured;
                            illegal = true;
                        }
                    }
                }

            } while (illegal);

            board[fPosX][fPosY].haveMoved = true;
//            Board.printBoard(board);
            Pawn.checkEnPassante(turn);
            turn = turn.equals("White's ") ? "Black's " : "White's ";

            King.turn = turn;
            playMove++;
            rebaseBoard();
        } else {
////            System.out.println("Done");
        }
    }

    public void playClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input Game Title");


        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                Recording.gameList.add(new Game(moveList, m_Text, new Date()));
                SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(Recording.gameList);
                prefsEditor.putString("Game List", json);
                prefsEditor.apply();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        builder.show();


    }



    public void recordingClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("View Recordings");

        builder.setPositiveButton("View Sorted by Name", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Collections.sort(Recording.gameList, new Comparator<Game>() {
                    @Override
                    public int compare(Game o1, Game o2) {
                        return o1.name.compareTo(o2.name);
                    }
                });
                Intent intent = new Intent(MainActivity.this, Recording.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("View Sorted by Date", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Collections.sort(Recording.gameList, new Comparator<Game>() {
                    @Override
                    public int compare(Game o1, Game o2) {
                        return o1.date.compareTo(o2.date);
                    }
                });
                Intent intent = new Intent(MainActivity.this, Recording.class);
                startActivity(intent);
            }
        });
        builder.setNeutralButton("Clear Recordings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Recording.gameList.clear();
                SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(Recording.gameList);
                prefsEditor.putString("Game List", json);
                prefsEditor.apply();
            }
        });

        builder.show();
    }

    public void undoClick(View view){
        if(inProgress) {
            if (undoAvailable) {
                board = Board.initializeBoard();
//                Board.printBoard(board);
                String move;
                turn = "White's ";

                int moveSet = 0;
                while (moveSet < moveList.size() - 1) {

                    int sPosY = -1;
                    int sPosX = -1;
                    int fPosY = -1;
                    int fPosX = -1;
                    String sPos;
                    String fPos;
                    String pawnPromote = null;
                    boolean first = true;
                    boolean illegal = false;

                    if (turn.equals("White's ") && board[King.kwY][King.kwX].inCheck(board)) {
                        if (((King) board[King.kwY][King.kwX]).inCheckmate(board)) {

//                            System.out.println("Checkmate\nBlack Wins");
                            TextView info = findViewById(R.id.information);
                            info.setText("Checkmate. Black wins.");
                            endGame();
                            inProgress = false;
                            return;
                        }
//                        System.out.println("Check");
                    }
                    if (turn.equals("Black's ") && board[King.kbY][King.kbX].inCheck(board)) {
                        if (((King) board[King.kbY][King.kbX]).inCheckmate(board)) {
//                            System.out.println("Checkmate\nWhite Wins");
                            TextView info = findViewById(R.id.information);
                            info.setText("Checkmate. White wins.");
                            endGame();
                            inProgress = false;
                            return;
                        }
////                        System.out.println("Check");
                    }


                    do {
//                        System.out.print(turn + "move: ");
                        move = moveList.get(moveSet);

                        try {
                            sPos = move.substring(0, move.indexOf(" "));
                            fPos = move.substring(move.indexOf(" ") + 1);
                        } catch (Exception e) {
                            illegal = true;
                            first = false;
                            continue;
                        }
                        ;
                        pawnPromote = null;
                        if (move.length() > 6) {
                            pawnPromote = move.substring(6, 7);
                        }

                        Pawn.promote = pawnPromote;

                        if (mapX.containsKey((sPos.charAt(0)))) {
                            sPosY = mapX.get(sPos.charAt(0));
                        } else {
                            illegal = true;
                            continue;
                        }

                        if (mapY.containsKey(sPos.charAt(1))) {
                            sPosX = mapY.get(sPos.charAt(1));
                        } else {
                            illegal = true;
                        }
                        if (mapX.containsKey(fPos.charAt(0))) {
                            fPosY = mapX.get(fPos.charAt(0));
                        } else {
                            illegal = true;
                        }
                        if (mapY.containsKey(fPos.charAt(1))) {
                            fPosX = mapY.get(fPos.charAt(1));
                        } else {
                            illegal = true;
                        }
                        if (board[sPosX][sPosY] == null) {
                            illegal = true;
                        } else if (!(board[sPosX][sPosY].color.equals(turn.substring(0, 5).toLowerCase()))) {
                            illegal = true;
                        } else if (!board[sPosX][sPosY].isValidMove(board, sPosX, sPosY, fPosX, fPosY)) {
                            illegal = true;
                        } else {
                            Piece captured = null;
                            try {
                                captured = board[sPosX][sPosY].move(board, sPosX, sPosY, fPosX, fPosY);
                            } catch (Exception E) {
                            }
                            ;
                            if (turn.equals("White's ")) {

                                if (board[King.kwY][King.kwX].inCheck(board)) {
                                    if (board[fPosX][fPosY] instanceof King) {

                                    }
                                    board[fPosX][fPosY].move(board, fPosX, fPosY, sPosX, sPosY);

                                    board[fPosX][fPosY] = captured;
                                    illegal = true;
                                }
                            } else {
                                if (board[King.kbY][King.kbX].inCheck(board)) {

                                    board[fPosX][fPosY].move(board, fPosX, fPosY, sPosX, sPosY);
                                    board[fPosX][fPosY] = captured;
                                    illegal = true;
                                }
                            }
                        }

                        first = false;

                    } while (illegal);

                    board[fPosX][fPosY].haveMoved = true;
//                    Board.printBoard(board);
                    Pawn.checkEnPassante(turn);
                    turn = turn.equals("White's ") ? "Black's " : "White's ";

                    King.turn = turn;
                    moveSet++;

                }
                moveList.remove(moveList.size() - 1);
                TextView info = findViewById(R.id.information);
                if (turn.contentEquals("Black's "))
                    info.setText("Black's turn.");
                else
                    info.setText("White's turn.");
                rebaseBoard();
            }
            undoAvailable = false;
        }
    }

    public void newGameClick(View view){
        inProgress = true;
        Button ng = (Button) findViewById(R.id.newGame);
        ng.setVisibility(View.INVISIBLE);
        Button play = (Button) findViewById(R.id.playBack);
        play.setVisibility(View.INVISIBLE);
        Button next = (Button) findViewById(R.id.nextTurn);
        next.setVisibility(View.INVISIBLE);
        board = Board.initializeBoard();
        rebaseBoard();
        TextView info = findViewById(R.id.information);
        info.setText("White's turn.");
        turn = "White's ";
        whiteDraw = false;
        blackDraw = false;
        drawButton.setText("draw");
        moveList.clear();

    }

    public void rebaseBoard(){


        for (int i = 0; i < 8; i++){
            for (int j = 1; j <= 8; j++){
                int id = getResources().getIdentifier("abcdefgh".charAt(i) + "" + j, "id", getPackageName());
                ImageButton button = findViewById(id);
                if (board[8-j][i] == null){
                    button.setImageDrawable(getResources().getDrawable(R.drawable.transparent));
                } else if(board[8 - j][i] instanceof King){
                    if (board[8 - j][i].color == "white")
                        button.setImageDrawable(getResources().getDrawable(R.drawable.white_king));
                    else
                        button.setImageDrawable(getResources().getDrawable(R.drawable.black_king));
                } else if(board[8 - j][i] instanceof Pawn){
                    if (board[8 - j][i].color == "white")
                        button.setImageDrawable(getResources().getDrawable(R.drawable.white_pawn));
                    else
                        button.setImageDrawable(getResources().getDrawable(R.drawable.black_pawn));
                } else if(board[8 - j][i] instanceof Queen){
                    if (board[8 - j][i].color == "white")
                        button.setImageDrawable(getResources().getDrawable(R.drawable.white_queen));
                    else
                        button.setImageDrawable(getResources().getDrawable(R.drawable.black_queen));
                } else if(board[8 - j][i] instanceof Rook){
                    if (board[8 - j][i].color == "white")
                        button.setImageDrawable(getResources().getDrawable(R.drawable.white_rook));
                    else
                        button.setImageDrawable(getResources().getDrawable(R.drawable.black_rook));
                } else if(board[8 - j][i] instanceof Knight){
                    if (board[8 - j][i].color == "white")
                        button.setImageDrawable(getResources().getDrawable(R.drawable.white_knight));
                    else
                        button.setImageDrawable(getResources().getDrawable(R.drawable.black_knight));
                } else if(board[8 - j][i] instanceof Bishop){
                    if (board[8 - j][i].color == "white")
                        button.setImageDrawable(getResources().getDrawable(R.drawable.white_bishop));
                    else
                        button.setImageDrawable(getResources().getDrawable(R.drawable.black_bishop));
                }
            }
        }


    }

}