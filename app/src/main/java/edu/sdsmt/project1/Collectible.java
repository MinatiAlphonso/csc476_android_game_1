package edu.sdsmt.project1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;
import java.util.Random;

public class Collectible {

    private Bitmap collectBitmap;
    private Rect rect;
    private Parameters params;
    Paint paint;

    public Collectible(Context context, int id, int width, int height) {
        collectBitmap = BitmapFactory.decodeResource(context.getResources(), id);
        rect = new Rect();
        params = new Parameters();
        params.backgroundHeight = height;
        params.backgroundWidth = width;
        params.id = id;
        setRect();
        // debugging paint to draw collision boxes
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.purple_200));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
    }

    public void move(float dx, float dy) {
        params.x += dx;
        params.y += dy;
        setRect();
    }

    private void setRect() {
        rect.set((int)(params.x * params.backgroundHeight * params.imageScale - collectBitmap.getWidth() * params.scale / 2), (int)(params.y * params.backgroundWidth * params.imageScale - collectBitmap.getHeight() * params.scale / 2), (int)(params.x * params.backgroundHeight * params.imageScale) + (int)(collectBitmap.getWidth() * params.scale / 2), (int)(params.y  * params.backgroundWidth * params.imageScale) + (int)(collectBitmap.getHeight() * params.scale / 2));
    }


    public Bitmap getCollectBitmap() {
        return collectBitmap;
    }

    public void setX(float x){
        params.x = x;
    }
    public void setY(float y){
        params.y = y;
    }
    public float getX() {
        return params.x * params.backgroundHeight * params.imageScale - collectBitmap.getWidth() * params.scale / 2;
    }

    public float getY() {
        return params.y * params.backgroundWidth * params.imageScale - collectBitmap.getHeight() * params.scale / 2;
    }

    public Rect getRect() {
        return rect;
    }

    public void setImageScale(float scale) {
        params.imageScale = scale;
        setRect();
    }

    public float getScale() {
        return params.scale;
    }

    public boolean isCaptured() {
        return params.captured;
    }

    public void capture() {
        params.captured = true;
    }

    public void draw(Canvas canvas, float marginLeft, float marginTop, float imageScale, float imgWid, float imgHit) {
        /**
         * Draw Collectible
         */
        if (!params.captured) {
            canvas.save();
            canvas.translate(marginLeft + (params.x * imgWid * imageScale), marginTop + (params.y * imgHit * imageScale));
            canvas.scale(params.scale, params.scale);
            canvas.translate(-collectBitmap.getWidth() / 2.0f, -collectBitmap.getHeight() / 2.0f);
            canvas.drawBitmap(collectBitmap, 0, 0, null);
            canvas.restore();
        }
    }

    public void shuffle(Random rand) {
        params.x = rand.nextFloat();
        params.y = rand.nextFloat();
        setRect();
    }

    private static class Parameters implements Serializable {
        public float x = 0;
        public float y = 0;
        public int id = -1;
        public float scale = 0.25f;
        public float angle = 0;
        public boolean captured = false;
        public int backgroundHeight = 0;
        public int backgroundWidth = 0;
        public float imageScale = 0;
    }

    public void saveCollectibleState(String key, Bundle bundle) {
        bundle.putSerializable(key, params);
    }

    public void loadCollectibleState(String key, Bundle bundle) {
        params = (Parameters)bundle.getSerializable(key);
        setRect();
    }
}
