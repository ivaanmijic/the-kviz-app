package com.example.kviz.webSocket;

public class UserData {
    private String name;
    private int points;

    public UserData() {
        this.points = 0;
    }

    public void setName(String name){this.name = name;}

    public void addPoints(int points) {
        this.points += points;
    }
    public int getPoints() {
        return points;
    }
}
