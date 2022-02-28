package edu.sdsmt.project1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Game {



    private Bitmap backgroundBitmap = null;
    private GameView gameView;
    private float imageScale;
    private final static float SCALE_IN_VIEW = 1.0f;
    private float marginLeft;
    private float marginTop;
    private int numCollectibles = 15;
    // Public variables to reference
    public static final int PLAYER1_TURN = 1;
    public static final int PLAYER2_TURN = 2;
    public static final int GAME_OVER = -1;
    public static final int GAME_PLAYING = 0;

    public static final int NO_CAPTURE = 0;
    public static final int CIRCLE_CAPTURE = 1;
    public static final int RECTANGLE_CAPTURE = 2;
    public static final int LINE_CAPTURE = 3;

    private Parameters params;
    private Player player1;
    private Player player2;
    private Random random;

    // finish Capture in game class
    private Capture circleCapture;
    private Capture rectangleCapture;
    private Capture lineCapture;
    private ArrayList<Collectible> collectibles;


    private static final String GAME_PARAMS = "edu.sdsmt.project1.gameparams";
    private static final String PLAYER1_PARAMS = "edu.sdsmt.project1.player1params";
    private static final String PLAYER2_PARAMS = "edu.sdsmt.project1.player2params";
    private static final String CIRCLE_PARAMS = "edu.sdsmt.project1.circleparams";
    private static final String RECTANGLE_PARAMS = "edu.sdsmt.project1.rectangleparams";
    private static final String LINE_PARAMS = "edu.sdsmt.project1.lineparams";

    private static final String COLLECTIBLE_PARAMS = "edu.sdsmt.project1.captureparams";
    // constructor
    // context used to pass to collectibles and captures
    public Game(Context context) {
        params = new Parameters();
        player1 = new Player();
        player2 = new Player();
        random = new Random();
        // should either 1 or 2
        params.turn = random.nextInt(2) + 1;

        backgroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.space);
        collectibles = new ArrayList<>();

        // initialization for captures and collectibles.
        circleCapture = new Capture(context, R.drawable.circle); // using player image for testing
        rectangleCapture = new Capture(context, R.drawable.rectangle);
        lineCapture = new Capture(context, R.drawable.line);
        addCollectibleToList(context);

        shuffle();
    }

    public void addCollectibleToList(Context context){
        for(int i = 0; i < numCollectibles; i++) {
            collectibles.add(new Collectible(context, R.drawable.collectible, backgroundBitmap.getWidth(), backgroundBitmap.getHeight()));
        }
    }
    public  void shuffle(){
        for(Collectible collect : collectibles){
            collect.shuffle(random);
        }
    }

    public int getGameState() {

        // check if collectibles are all captured
        if (params.remainingRounds == 0 || allCollectiblesCaptured()) {
            return GAME_OVER;
        }
        else {
            return GAME_PLAYING;
        }
    }

    public int getPlayerTurn() {
        return params.turn;
    }

    public void setRounds(int rounds) {
        params.rounds = rounds;
        params.remainingRounds = rounds;
    }

    // Updates which player's turn it is and round remaining
    public void roundFinished() {
        // decrease rounds remaining
        params.remainingRounds -= 1;
        // switch turn
        if (params.turn == PLAYER1_TURN) {
            params.turn = PLAYER2_TURN;
        }
        else {
            params.turn = PLAYER1_TURN;
        }

    }

    public int getTotalRounds(){
        return params.rounds;
    }
    public int getRoundsRemaining() {
        return params.remainingRounds;
    }

    public int getRound() {
        return params.rounds - params.remainingRounds + 1;
    }

    public void setPlayersNames(String player1Name, String player2Name) {
        player1.setName(player1Name);
        player2.setName(player2Name);
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Capture getCapture() {

        switch (params.capture) {
            case NO_CAPTURE:
                return null;
            case CIRCLE_CAPTURE:
                return circleCapture;

            case RECTANGLE_CAPTURE:
                return rectangleCapture;

            case LINE_CAPTURE:
                return lineCapture;
        }

        return circleCapture;
    }

    public void setCapture(int capture) {
        if (capture < 4 && capture > 0) {
            params.capture = capture;
        }
    }

    public ArrayList<Collectible> getCollectibles() {
        return collectibles;
    }

    // capture functions still need to be finished with capture class and collectible class
    // Returns whether a collectible was captured by rectangle
    // update to take capture class
    public void capture() {
        Capture cap = circleCapture;
        switch (params.capture) {
            case NO_CAPTURE:
                return;
            case CIRCLE_CAPTURE:
                for (Collectible collect : collectibles) {
                    if (circleCapture.collisionTest(collect)) {
                        collect.capture();

                        if (params.turn == PLAYER1_TURN) {
                            player1.scored();
                        }
                        else {
                            player2.scored();
                        }
                    }
                }
                break;
            case RECTANGLE_CAPTURE:
                for (Collectible collect : collectibles) {
                    if (rectangleCapture.collisionTest(collect) && rectangleCaptureProb(rectangleCapture.getScale())) {
                        collect.capture();

                        if (params.turn == PLAYER1_TURN) {
                            player1.scored();
                        }
                        else {
                            player2.scored();
                        }
                    }
                }
                break;
            case LINE_CAPTURE:
                for (Collectible collect : collectibles) {
                    if (lineCapture.collisionTest(collect) && lineCaptureProb()) {
                        collect.capture();

                        if (params.turn == PLAYER1_TURN) {
                            player1.scored();
                        }
                        else {
                            player2.scored();
                        }
                    }
                }
                break;
        }
    }

    private boolean lineCaptureProb() {
        int prob = random.nextInt(4);

        if (prob == 3) {
            return false;
        }

        return true;
    }

    private boolean rectangleCaptureProb(float scale) {
        // Scale starts at 0.25
        int prob = (int)(random.nextInt(25) / scale);

        if (prob > 48) {
            return true;
        }
        return false;
    }

    private boolean allCollectiblesCaptured() {
        for (Collectible collect : collectibles) {
            if (!collect.isCaptured()) {
                return false;
            }
        }
        return true;
    }

    public void draw(Canvas canvas) {
        /**
         * Draw Background
         */
        if(backgroundBitmap == null) {
            return;
        }

        float wid = canvas.getWidth();
        float hit = canvas.getHeight();

        // What would be the scale to draw the where it fits both
        // horizontally and vertically?
        float scaleH = wid / backgroundBitmap.getWidth();
        float scaleV = hit / backgroundBitmap.getHeight();

        // Use the lesser of the two
        imageScale = scaleH < scaleV ? scaleH : scaleV;
        imageScale *= SCALE_IN_VIEW;
        // What is the scaled image size?
        float iWid = imageScale * backgroundBitmap.getWidth();
        float iHit = imageScale * backgroundBitmap.getHeight();

        // Determine the top and left margins to center
        marginLeft = (wid - iWid) / 2.0f;
        marginTop = (hit - iHit) / 2.0f;

        // And draw the bitmap
        canvas.save();
        canvas.translate(marginLeft,  marginTop);
        canvas.scale(imageScale, imageScale);
        canvas.drawBitmap(backgroundBitmap, 0,0, null);
        canvas.restore();

        for (Collectible collect : collectibles) {
            collect.setImageScale(imageScale);
        }
        // x range would be from marginLeft to marginLeft+imgWidth*imageScale
        // y range would be from marginTop to marginTop+imgHeight*imageScale
        /**
         * Draw Collectibles
         * */
        for(Collectible collect : collectibles){
            collect.draw(canvas, marginLeft, marginTop, imageScale, backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
        }

        /**
         * Drawing Test Capture Circle. Need To Decide When/Where to make this call
         * */
        switch (params.capture) {
            case NO_CAPTURE:
                break;
            case CIRCLE_CAPTURE:
                circleCapture.draw(canvas, marginLeft, marginTop, imageScale, backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
                break;
            case RECTANGLE_CAPTURE:
                rectangleCapture.draw(canvas, marginLeft, marginTop, imageScale, backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
                break;
            case LINE_CAPTURE:
                lineCapture.draw(canvas, marginLeft, marginTop, imageScale, backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
                break;
        }

        //rectangleCapture.draw(canvas, marginLeft, marginTop, imageScale, backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
        //lineCapture.draw(canvas, marginLeft, marginTop, imageScale, backgroundBitmap.getWidth(), backgroundBitmap.getHeight());

    }

    public boolean onTouchEvent(View gameView, MotionEvent event) {
        switch (params.capture) {
            case NO_CAPTURE:
                return true;
            case CIRCLE_CAPTURE:
                return circleCapture.onTouchEvent(gameView,event, marginLeft, marginTop, imageScale);
            case RECTANGLE_CAPTURE:
                return rectangleCapture.onTouchEvent(gameView,event, marginLeft, marginTop, imageScale);
            case LINE_CAPTURE:
                return lineCapture.onTouchEvent(gameView,event, marginLeft, marginTop, imageScale);
        }
        //return circleCapture.onTouchEvent(gameView,event, marginLeft, marginTop, imageScale);
        return rectangleCapture.onTouchEvent(gameView,event, marginLeft, marginTop, imageScale);
        //return lineCapture.onTouchEvent(gameView,event, marginLeft, marginTop, imageScale);
    }

    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }

    // class to serialize parameters
    private static class Parameters implements Serializable {
        // added rounds for now. Might not be needed
        private int rounds = 0;
        private int remainingRounds = 0;
        private int turn = 1;
        // which capture option we are using
        private int capture = RECTANGLE_CAPTURE;
        private float x = 0;
        private float y = 0;
    }

    public void saveGameState(Bundle bundle) {
        bundle.putSerializable(GAME_PARAMS, params);
        player1.savePlayer(PLAYER1_PARAMS, bundle);
        player2.savePlayer(PLAYER2_PARAMS, bundle);

        circleCapture.saveCaptureState(CIRCLE_PARAMS, bundle);
        rectangleCapture.saveCaptureState(RECTANGLE_PARAMS, bundle);
        lineCapture.saveCaptureState(LINE_PARAMS, bundle);
        int i = 0;
        for (Collectible collect : collectibles) {
            collect.saveCollectibleState(COLLECTIBLE_PARAMS + i, bundle);
            i++;
        }
    }

    public void restoreGameState(Bundle bundle) {
        params = (Parameters)bundle.getSerializable(GAME_PARAMS);
        player1.restorePlayer(PLAYER1_PARAMS, bundle);
        player2.restorePlayer(PLAYER2_PARAMS, bundle);

        circleCapture.loadCaptureState(CIRCLE_PARAMS, bundle);
        rectangleCapture.loadCaptureState(RECTANGLE_PARAMS, bundle);
        lineCapture.loadCaptureState(LINE_PARAMS, bundle);
        int i = 0;
        for (Collectible collect : collectibles) {
            collect.loadCollectibleState(COLLECTIBLE_PARAMS + i, bundle);
            i++;
        }

    }
}
