package edu.sdsmt.project1;

import android.os.Bundle;

import java.io.Serializable;

public class Player {

    public Player() {

    }

    public String getName() {
        return "";
    }

    public void setName(String name) {

    }

    public int getScore() {
        return 0;
    }

    // Increment the players score when the collect a item
    public void scored() {

    }

    public static class Parameters implements Serializable {

    }
    // Serialize and store parameters in bundle
    public void savePlayer(Bundle bundle) {

    }
    // Load serializable
    public void restorePlayer(Bundle bundle) {

    }
}
