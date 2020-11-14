package com.example.helloworld;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Game implements Serializable {

    private static final long serialVersionUID = 4033017482271211409L;

    public ArrayList<String> moves;
    public String name;
    public Date date;

    public Game(ArrayList<String> moves, String name, Date date) {
        this.moves = moves;
        this.name = name;
        this.date = date;
        // TODO Auto-generated constructor stub
    }
    @Override
    public String toString() {
        return name + " " + date;
    }
}
