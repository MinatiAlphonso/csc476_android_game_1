package edu.sdsmt.project1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class GameView extends View {

    private Game game;

    //game area background
    Bitmap backgroundBitmap = null;
    private float imageScale = 1;
    private float marginLeft = 0;
    private float marginTop = 0;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.space);
        //game = new Game(getContext());
        //game.setGameView(this);
    }

    /*
    @Override
    public boolean onTouchEvent(MotionEvent event){
        return game.onTouchEvent(this, event);
    }*/

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        //should the drawing of the background be done in game.draw() too?
        drawBackground(canvas);
        //game.draw(canvas);
    }

    public void drawBackground(Canvas canvas){
        if(backgroundBitmap == null) {
            return;
        }

        float wid = getWidth();
        float hit = getHeight();

        // What would be the scale to draw the where it fits both
        // horizontally and vertically?
        float scaleH = wid / backgroundBitmap.getWidth();
        float scaleV = hit / backgroundBitmap.getHeight();

        // Use the lesser of the two
        imageScale = scaleH < scaleV ? scaleH : scaleV;

        // What is the scaled image size?
        float iWid = imageScale * backgroundBitmap.getWidth();
        float iHit = imageScale * backgroundBitmap.getHeight();

        // Determine the top and left margins to center
        marginLeft = (wid - iWid) / 2;
        marginTop = (hit - iHit) / 2;

        // And draw the bitmap
        canvas.save();
        canvas.translate(marginLeft,  marginTop);
        canvas.scale(imageScale, imageScale);
        canvas.drawBitmap(backgroundBitmap, 0, 0, null);
        canvas.restore();
    }

    public Game getGame(){
        return game;
    }

    public void saveGameState(Bundle bundle){
        game.saveGameState(bundle);
    }

    public void restoreGameState(Bundle bundle){
        game.restoreGameState(bundle);
    }
}
