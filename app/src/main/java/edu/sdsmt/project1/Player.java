package edu.sdsmt.project1;

import android.os.Bundle;

import java.io.Serializable;

public class Player {

    private Parameters params;

    public Player() {
        params = new Parameters();
    }

    public String getName() {

        return params.name;
    }

    public void setName(String name) {
        params.name = name;
    }

    public int getScore() {
        return params.score;
    }

    // Increment the players score when the collect a item
    public void scored() {
        params.score += 1;
    }

    public static class Parameters implements Serializable {
        private int score = 0;
        private String name = null;

    }
    // Serialize and store parameters in bundle
    public void savePlayer(String key, Bundle bundle) {
        bundle.putSerializable(key, params);
    }
    // Load serializable
    public void restorePlayer(String key, Bundle bundle) {
        params = (Parameters)bundle.getSerializable(key);
    }
}
