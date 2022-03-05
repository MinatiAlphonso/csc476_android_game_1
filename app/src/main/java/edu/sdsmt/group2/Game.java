package edu.sdsmt.group2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    private Bitmap backgroundBitmap;
    private GameView gameView;
    private float imageScale;
    private final static float SCALE_IN_VIEW = 1.0f;
    private float marginLeft;
    private float marginTop;

    // Public variables to reference
    public static final int GAME_OVER = -1;
    public static final int GAME_PLAYING = 0;
    public static final int RECTANGLE_CAPTURE = 0;
    public static final int CIRCLE_CAPTURE = 1;
    public static final int LINE_CAPTURE = 2;

    // class to serialize parameters
    private static class Parameters implements Serializable {
        // added rounds for now. Might not be needed
        public int rounds = 0;
        public int remainingRounds = 0;
        public int turn = 1;
        public int capture = -1;//default is no capture option selected
        public float x = 0;
        public float y = 0;
        public float angle = 0;
        public float scale = 0.25f;
        public int collectibles = 15;
    }
    private Parameters params;
    private Player player1;
    private Player player2;
    private Random random;

    private Capture selectedCapture = null;//default is no capture option selected
    // finish Capture in game class
    private final Capture circleCapture;
    private final Capture rectangleCapture;
    private final Capture lineCapture;
    private ArrayList<Collectible> collectibles;

    // add collectibles here
    private static final String GAME_PARAMS = "edu.sdsmt.group2.GAME_PARAMS";
    private static final String PLAYER1_PARAMS = "edu.sdsmt.group2.PLAYER1_PARAMS";
    private static final String PLAYER2_PARAMS = "edu.sdsmt.group2.PLAYER2_PARAMS";

    private static final String COLLECTIBLE_PARAMS = "edu.sdsmt.group2.captureparams";
    // constructor
    // context used to pass to collectibles and captures
    public Game(Context context) {
        params = new Parameters();
        player1 = new Player();
        player2 = new Player();
        random = new Random();
        // should either 1 or 2
        params.turn = 1;

        backgroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.space);
        collectibles = new ArrayList<>();

        // initialization for captures and collectibles.
        rectangleCapture = new RectangleCapture(context, R.drawable.rectangle_concentric);
        circleCapture = new CircleCapture(context, R.drawable.circle_concentric);
        lineCapture = new LineCapture(context, R.drawable.line);

        circleCapture.setScalable(false);
        circleCapture.setScale(0.25f);
        lineCapture.setScalable(false);
        lineCapture.setScale(0.50f);

        addCollectibleToList(context);

        shuffle();
    }

    public void addCollectibleToList(Context context){
        for(int i = 0; i < params.collectibles; i++) {
            // added background image dimensions so that the get x and y functions can return absolute location for collisions
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
        if (params.remainingRounds <= 0 || allCollectiblesCaptured()) {
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

    public int getTotalRounds(){
        return params.rounds;
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
        return selectedCapture;
    }

    /**
     * This function sets the capture option for the current turn.
     * -1 means that capture option has not been selected.
     * 0 refers to the rectangle capture
     * 1 refers to the circle capture
     * 2 refers to the line capture
     * @param capture
     */
    public void setCapture(int capture) {
        if (capture >= -1 && capture < CaptureActivity.CaptureType.values().length) {
            params.capture = capture;
            Boolean needsReset = true;
            float tempX = 0, tempY = 0, tempAngle = 0;
            if(selectedCapture != null){
                needsReset = false;
                tempX = selectedCapture.getX();
                tempY = selectedCapture.getY();
                tempAngle = selectedCapture.getAngle();
            }
            switch (params.capture) {
                case RECTANGLE_CAPTURE:
                    selectedCapture = rectangleCapture;
                    if(needsReset){selectedCapture.setScale(0.25f);}
                    break;

                case CIRCLE_CAPTURE:
                    selectedCapture = circleCapture;
                    break;

                case LINE_CAPTURE:
                    selectedCapture = lineCapture;
                    break;

                default:
                    selectedCapture = null;
            }
            if(selectedCapture != null){
                selectedCapture.setX(tempX);
                selectedCapture.setY(tempY);
                selectedCapture.setAngle(tempAngle);
                if(needsReset){
                    selectedCapture.setX(0);
                    selectedCapture.setY(0);
                    selectedCapture.setAngle(tempAngle);
                }
            }

        }
    }

    public ArrayList<Collectible> getCollectibles() {
        return collectibles;
    }

    /**
     * This function determines whether each collectible in collectibles
     * should be collected based on the current capture shape's probability
     * of collection. It then removes them from the list
     */
    public void captureCollectibles() {
        ArrayList<Collectible> capturedCollectibles = new ArrayList<>();
        // for every collectible in collectibles
        for (int colIndex = 0; colIndex < getCollectibles().size(); colIndex++) {
            // if the collectible overlaps with the capture shape
            Collectible col = getCollectibles().get(colIndex);
            if (getCapture().collisionTest(col)
                    && random.nextFloat() < getCapture().getChance()) {
                // add it to the list of overlapping collectibles, given the collection chance
                capturedCollectibles.add(col);
            }
        }
        // remove the captured collectibles from collectibles
        getCollectibles().removeAll(capturedCollectibles);
        // increase the current player's score by the number of collectibles captured
        getCurrentPlayer().scored(capturedCollectibles.size());
    }

    /**
     * Returns the player whose turn it is
     * */
    private Player getCurrentPlayer() {
        Player currentPlayer;
        if (getPlayerTurn() == 1) {
            currentPlayer = getPlayer1();
        } else {
            currentPlayer = getPlayer2();
        }
        return currentPlayer;
    }

    /**
     * Advances to the next players turn, unless both players have already
     * played, in which case the next round will begin
     */
    public void advanceTurn() {
        params.turn++;
        if (params.turn > 2) {
            advanceRound();
        }
    }

    /**
     * Begins the next round
     */
    public void advanceRound() {
        params.remainingRounds--;
        params.turn = 1;
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
         * Calculations
         */

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

        // set the collectibles so that the get x and y functions return absolute position for collision bounds
        for (Collectible collect : collectibles) {
            collect.setImageScale(imageScale);
        }


        /**
         * Draw the Space Background
         */
        if(backgroundBitmap != null) {
            drawBackground(canvas,marginLeft,marginTop,imageScale);
        }

        /**
         * Draw Collectibles
         * */
        for(Collectible collect : collectibles){
            if(collect != null){
                collect.draw(canvas, marginLeft, marginTop, imageScale, backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
            }
        }

        /**
         * Drawing the Capture Option
         * */

        if(selectedCapture != null){
            selectedCapture.draw(canvas, marginLeft, marginTop, imageScale);
        }

    }

    public void drawBackground(Canvas canvas, float marginLeft, float marginTop, float imageScale){
        canvas.save();
        canvas.translate(marginLeft,  marginTop);
        canvas.scale(imageScale, imageScale);
        canvas.drawBitmap(backgroundBitmap, 0,0, null);
        canvas.restore();
    }

    public boolean onTouchEvent(View gameView, MotionEvent event) {
        if(getCapture() != null) {
            return selectedCapture.onTouchEvent(gameView, event, marginLeft, marginTop, imageScale);
        }
        return false;
    }

    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }


    public void saveGameState(Bundle bundle) {
        if(selectedCapture!=null){
            params.x = selectedCapture.getX();
            params.y = selectedCapture.getY();
            params.angle = selectedCapture.getAngle();
            params.scale = selectedCapture.getScale();
        }
        params.collectibles = collectibles.size();
        bundle.putSerializable(GAME_PARAMS, params);
        player1.savePlayer(PLAYER1_PARAMS, bundle);
        player2.savePlayer(PLAYER2_PARAMS, bundle);
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
        setCapture(params.capture);
        if(selectedCapture!=null){
            selectedCapture.setX(params.x);
            selectedCapture.setY(params.y);
            selectedCapture.setAngle(params.angle);
            selectedCapture.setScale(params.scale);
        }
        collectibles.clear();
        addCollectibleToList(gameView.getContext());
        int i = 0;
        for (Collectible collect : collectibles) {
            collect.loadCollectibleState(COLLECTIBLE_PARAMS + i, bundle);
            i++;
        }
    }

}