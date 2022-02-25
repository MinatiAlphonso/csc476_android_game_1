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
    //
//    private void setRect() {
//        rect.set((int)params.x, (int)params.y, (int)params.x + (int)(collectBitmap.getWidth() * params.scale), (int)params.y+ (int)(collectBitmap.getHeight() * params.scale));
//    }

    private void setRect() {
        rect.set((int)params.x, (int)params.y, (int)params.x + collectBitmap.getWidth(), (int)params.y + collectBitmap.getHeight());
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
        public float scale = 0.25f;
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
