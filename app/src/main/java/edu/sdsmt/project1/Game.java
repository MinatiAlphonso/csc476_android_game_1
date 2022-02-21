package edu.sdsmt.project1;

import android.os.Bundle;

import java.io.Serializable;

public class Game {
    // Public variables to reference
    public static final int PLAYER1_TURN = 1;
    public static final int PLAYER2_TURN = 2;
    public static final int GAME_OVER = -1;

    // constructor
    public Game() {

    }

    public int getGameState() {
        return 0;
    }

    public int getPlayerTurn() {
        return 0;
    }

    public void setRounds() {

    }
    // Updates which player's turn it is and round remaining
    public void roundFinished() {

    }

    public int getRoundsRemaining() {
        return 0;
    }
    // Returns whether a collectible was captured by rectangle
    public boolean rectangleCaptured(float scale) {
        // rectangle probability varies based on the scale
        return false;
    }

    public boolean circleCaptured() {
        return true;
    }

    public boolean lineCaptured() {
        return false;
    }

    // class to serialize parameters
    private static class parameters implements Serializable {

    }

    public void saveGameState(Bundle bundle) {

    }

    public void restoreGameState(Bundle bundle) {

    }
}
