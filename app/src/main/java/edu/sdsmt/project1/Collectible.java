package edu.sdsmt.project1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;

import java.io.Serializable;

public class Collectible {

    private Bitmap collectBitmap;
    private Rect rect;
    private Parameters params;

    public Collectible(Context context, int id) {
        collectBitmap = BitmapFactory.decodeResource(context.getResources(), id);
        rect = new Rect();
        params = new Parameters();
        setRect();
    }

    public void move(float dx, float dy) {
        params.x += dx;
        params.y += dy;
        setRect();
    }

    private void setRect() {
        rect.set((int)params.x, (int)params.y, (int)params.x + (int)(collectBitmap.getWidth() * params.scale), (int)params.y+ (int)(collectBitmap.getHeight() * params.scale));
    }

    public boolean hit(float testX, float testY) {
        int pX = (int)((testX - params.x));
        int pY = (int)((testY - params.y));

        if(pX < 0 || pX >= collectBitmap.getWidth() ||
                pY < 0 || pY >= collectBitmap.getHeight()) {
            return false;
        }

        // We are within the rectangle of the piece.
        // Are we touching actual picture?
        return (collectBitmap.getPixel(pX, pY) & 0xff000000) != 0;
    }

    public Bitmap getCollectBitmap() {
        return collectBitmap;
    }

    public float getX() {
        return params.x;
    }

    public float getY() {
        return params.y;
    }

    public Rect getRect() {
        return rect;
    }

    private static class Parameters implements Serializable {
        public float x = 0;
        public float y = 0;
        public float scale = 1.0f;
        public float angle = 0;
    }

    public void saveCaptureState(String key, Bundle bundle) {
        bundle.putSerializable(key, params);
    }

    public void loadCaptureState(String key, Bundle bundle) {
        params = (Parameters)bundle.getSerializable(key);
        setRect();
    }
}
