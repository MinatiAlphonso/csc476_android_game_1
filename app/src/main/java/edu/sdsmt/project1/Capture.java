package edu.sdsmt.project1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;

import java.io.Serializable;

/**
 * This is a starting point for a class for a coverPic. It includes functions to
 * load the coverPic image and to do collision detection against another coverPic.
 */
public class Capture {
    private Bitmap captureBitmap;
    private Rect rect;
    private Rect overlap = new Rect();
    private Parameters params;

    public Capture(Context context, int id) {
        captureBitmap = BitmapFactory.decodeResource(context.getResources(), id);
        rect = new Rect();
        params = new Parameters();
        setRect();

    }

    public void move(float dx, float dy) {
        params.x += dx;
        params.y += dy;
        setRect();
    }
    // version with scale
//    private void setRect() {
//        // might not handle rotation or scale?
//        rect.set((int)params.x, (int)params.y, (int)params.x + (int)(captureBitmap.getWidth() * params.scale), (int)params.y+ (int)(captureBitmap.getHeight() * params.scale));
//    }

    private void setRect() {
        rect.set((int)params.x, (int)params.y, (int)params.x + captureBitmap.getWidth(), (int)params.y + captureBitmap.getHeight());
    }

    public boolean hit(float testX, float testY) {
        int pX = (int)((testX - params.x));
        int pY = (int)((testY - params.y));

        if(pX < 0 || pX >= captureBitmap.getWidth() ||
                pY < 0 || pY >= captureBitmap.getHeight()) {
            return false;
        }

        // We are within the rectangle of the piece.
        // Are we touching actual picture?
        return (captureBitmap.getPixel(pX, pY) & 0xff000000) != 0;
    }

    /**
     * Collision detection between two coverPics. This object is
     * compared to the one referenced by other
     * @param other collectible to compare to.
     * @return True if there is any overlap between the two coverPics.
     */
    public boolean collisionTest(Collectible other) {
        // Do the rectangles overlap?
        if(!Rect.intersects(rect, other.getRect())) {
            return false;
        }

        // Determine where the two images overlap
        overlap.set(rect);

        if (!overlap.intersect(other.getRect())) {
            return false;
        }

        // We have overlap. Now see if we have any pixels in common
        for(int r=overlap.top; r<overlap.bottom;  r++) {
            int aY = (int)((r - params.y));
            int bY = (int)((r - other.getY()));

            for(int c=overlap.left;  c<overlap.right;  c++) {

                int aX = (int)((c - params.x));
                int bX = (int)((c - other.getX()));

                if( (captureBitmap.getPixel(aX, aY) & 0x80000000) != 0 &&
                        (other.getCollectBitmap().getPixel(bX, bY) & 0x80000000) != 0) {
                    //Log.i("collision", "Overlap " + r + "," + c);
                    return true;
                }
            }
        }

        return false;
    }

    public Bitmap getCollectBitmap() {
        return captureBitmap;
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
