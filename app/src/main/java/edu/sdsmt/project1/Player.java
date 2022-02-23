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

    public void setX(float x) {
        params.x = x;
    }

    public void setY(float y) {
        params.y = y;
    }

    public void setScale(float scale) {
        params.scale = scale;
    }

    public float getX() {
        return params.x;
    }

    public float getY() {
        return params.y;
    }

    public float getScale() {
        return params.scale;
    }

    public void move(float dx, float dy) {
        params.x += dx;
        params.y += dy;
    }

    public void scale(float ds) {
        // might need to be multiplied?
        params.scale += ds;
    }

    public static class Parameters implements Serializable {
        private float x = 0;
        private float y = 0;
        private float scale = 0;
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
