package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

public class Recording extends AppCompatActivity {

    ListView simpleList;
    ArrayList<String> moveset = new ArrayList<String>();
    public static ArrayList<Game> gameList = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

//        saveData();


        setContentView(R.layout.activity_recording);
        simpleList = (ListView)findViewById(R.id.simpleListView);
        ArrayAdapter<Game> arrayAdapter = new ArrayAdapter<Game>(this, R.layout.activity_recording, R.id.textView, gameList);
        simpleList.setAdapter(arrayAdapter);

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Game selectedGame = gameList.get(position);
                System.out.println(selectedGame);
                MainActivity.inProgress = false;
                MainActivity.moveList = selectedGame.moves;
                finish();
            }

        });
    }

//    protected void saveData(){
//
//    }


}
