package edu.sdsmt.project1;

import android.annotation.SuppressLint;
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
        game = new Game(getContext());
        game.setGameView(this);
    }

    /*
    @Override
    public boolean onTouchEvent(MotionEvent event){
        return game.onTouchEvent(this, event);
    }*/

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        game.draw(canvas);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event){
        return game.onTouchEvent(this, event);
    }

    public Game getGame(){
        return game;
    }

    /*public void saveGameState(Bundle bundle){
        game.saveGameState(bundle);
    }

    public void restoreGameState(Bundle bundle){
        game.restoreGameState(bundle);
    }*/
}
