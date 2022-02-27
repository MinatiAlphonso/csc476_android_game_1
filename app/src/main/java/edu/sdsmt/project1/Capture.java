package edu.sdsmt.project1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

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
    private Paint paint;
    /**
     * First touch status
     */
    private Touch touch1 = new Touch();

    /**
     * Second touch status
     */
    private Touch touch2 = new Touch();

    public Capture(Context context, int id) {
        captureBitmap = BitmapFactory.decodeResource(context.getResources(), id);
        rect = new Rect();
        params = new Parameters();
        setRect();
        // debugging paint for collision boxes
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.purple_200));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
    }

    /*public void move(float dx, float dy) {
        params.x += dx;
        params.y += dy;
        setRect();
    }*/
    // version with scale
    private void setRect() {
        // might not handle rotation or scale?
        rect.set((int)params.x, (int)params.y, (int)params.x + (int)(captureBitmap.getWidth() * params.scale), (int)params.y + (int)(captureBitmap.getHeight() * params.scale));
    }

//    private void setRect() {
//        rect.set((int)params.x, (int)params.y, (int)params.x + captureBitmap.getWidth(), (int)params.y + captureBitmap.getHeight());
//    }

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
    public boolean collisionTest(Collectible other, int width, int height) {
        // collision works as long as the collectible and capture are not scaled

        // Do the rectangles overlap?
        if(Rect.intersects(rect, other.getRect())) {
            return true;
        }

        // Determine where the two images overlap
        overlap.set(rect);

        if (!overlap.intersect(other.getRect())) {
            return false;
        }

        // We have overlap. Now see if we have any pixels in common
        for(int r=overlap.top; r<overlap.bottom;  r++) {
            int aY = (int)((r - params.y));
            int bY = (int)((r - other.getY() * height));

            for(int c=overlap.left;  c<overlap.right;  c++) {

                int aX = (int)((c - params.x));
                int bX = (int)((c - other.getX() * width));

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

    public void draw(Canvas canvas, float marginLeft, float marginTop, float imageScale, int width, int height) {
        canvas.save();
        canvas.translate(marginLeft+params.x, marginTop+params.y);
        canvas.scale(params.scale,params.scale);
        canvas.rotate(params.angle);
        canvas.drawBitmap(captureBitmap,0,0,null);
        canvas.restore();
        // debugging draw to show collision boxes
        canvas.save();
        canvas.drawRect(rect, paint);
        canvas.restore();
    }

    public boolean onTouchEvent(View gameView, MotionEvent event, float marginLeft, float marginTop, float imageScale) {
        int id = event.getPointerId(event.getActionIndex());

        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touch1.id = id;
                touch2.id = -1;
                getPositions(gameView,event,marginLeft,marginTop,imageScale);
                touch1.copyToLast();
                return true;

            case MotionEvent.ACTION_POINTER_DOWN:
                if(touch1.id >= 0 && touch2.id < 0) {
                    touch2.id = id;
                    getPositions(gameView,event,marginLeft,marginTop,imageScale);
                    touch2.copyToLast();
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                touch1.id = -1;
                touch2.id = -1;
                gameView.invalidate();
                return true;

            case MotionEvent.ACTION_POINTER_UP:
                if(id == touch2.id) {
                    touch2.id = -1;
                } else if(id == touch1.id) {
                    // Make what was touch2 now be touch1 by
                    // swapping the objects.
                    Touch t = touch1;
                    touch1 = touch2;
                    touch2 = t;
                    touch2.id = -1;
                }
                gameView.invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:
                getPositions(gameView, event, marginLeft, marginTop, imageScale);
                move();
                return true;
        }
        return false;
    }

    /**
     * Get the positions for the two touches and put them
     * into the appropriate touch objects.
     * @param event the motion event
     * @param marginLeft
     * @param marginTop
     * @param imageScale
     */
    private void getPositions(View gameView,  MotionEvent event, float marginLeft, float marginTop, float imageScale) {
        for(int i=0;  i<event.getPointerCount();  i++) {

            // Get the pointer id
            int id = event.getPointerId(i);

            // Convert to image coordinates
            float x = (event.getX(i) - marginLeft) / imageScale;
            float y = (event.getY(i) - marginTop) / imageScale;

            if(id == touch1.id) {
                touch1.copyToLast();
                touch1.x = x;
                touch1.y = y;
            } else if(id == touch2.id) {
                touch2.copyToLast();
                touch2.x = x;
                touch2.y = y;
            }
        }
        setRect();
        gameView.invalidate();
    }

    /**
     * Handle movement of the touches
     */
    private void move() {
        // If no touch1, we have nothing to do
        // This should not happen, but it never hurts
        // to check.
        if(touch1.id < 0) {
            return;
        }

        if(touch1.id >= 0) {
            // At least one touch
            // We are moving
            touch1.computeDeltas();
            Log.i("X1", String.valueOf(params.x));
            Log.i("Y1", String.valueOf(params.y));
            params.x += touch1.dX;
            params.y += touch1.dY;
            Log.i("X2", String.valueOf(params.x));
            Log.i("Y2", String.valueOf(params.y));
        }
        if(touch2.id >= 0) {
            // Two touches

            /*
             * Rotation
             */

            float angle1 = angle(touch1.lastX, touch1.lastY, touch2.lastX, touch2.lastY);
            float angle2 = angle(touch1.x, touch1.y, touch2.x, touch2.y);
            float da = angle2 - angle1;
            rotate(da, touch1.x, touch1.y);

            /*
             * Scaling
             */
            float length1 = length(touch1.lastX, touch1.lastY, touch2.lastX, touch2.lastY);
            float length2 = length(touch1.x, touch1.y, touch2.x, touch2.y);
            scale(length2 / length1, touch1.x, touch1.y);

        }
    }
    /**
     * Rotate the image around the point x1, y1
     * @param dAngle Angle to rotate in degrees
     * @param x1 rotation point x
     * @param y1 rotation point y
     */
    public void rotate(float dAngle, float x1, float y1) {
        params.angle += dAngle;

        // Compute the radians angle
        double rAngle = Math.toRadians(dAngle);
        float ca = (float) Math.cos(rAngle);
        float sa = (float) Math.sin(rAngle);
        float xp = (params.x - x1) * ca - (params.y - y1) * sa + x1;
        float yp = (params.x - x1) * sa + (params.y - y1) * ca + y1;

        params.x = xp;
        params.y = yp;
    }

    /**
     * Rotate the image around the point x1, y1
     *
     * @param scale percentage to scale
     * @param x1    scale point x
     * @param y1    scale point y
     */
    public void scale(float scale, float x1, float y1) {
        params.scale *= scale;

        // Compute a vector to hatX, hatY
        float dx = params.x - x1;
        float dy = params.y - y1;

        // Compute scaled hatX, hatY
        params.x = x1 + dx * scale;
        params.y = y1 + dy * scale;
    }
    /**
     * Determine the angle for two touches
     * @param x1 Touch 1 x
     * @param y1 Touch 1 y
     * @param x2 Touch 2 x
     * @param y2 Touch 2 y
     * @return computed angle in degrees
     */
    private float angle(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.toDegrees(Math.atan2(dy, dx));
    }

    /**
     * Determine the distance between two touches
     *
     * @param x1 Touch 1 x
     * @param y1 Touch 1 y
     * @param x2 Touch 2 x
     * @param y2 Touch 2 y
     * @return computed distance
     */
    private float length(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
    private static class Parameters implements Serializable {
        public float x = 0;
        public float y = 0;
        public float scale = 0.5f;
        public float angle = 0;
    }
    /**
     * Local class to handle the touch status for one touch.
     * We will have one object of this type for each of the
     * two possible touches.
     */
    private class Touch {
        /**
         * Touch id
         */
        public int id = -1;

        /**
         * Current x location
         */
        public float x = 0;

        /**
         * Current y location
         */
        public float y = 0;

        /**
         * Previous x location
         */
        public float lastX = 0;

        /**
         * Previous y location
         */
        public float lastY = 0;

        /**
         * Change in x value from previous
         */
        public float dX = 0;

        /**
         * Change in y value from previous
         */
        public float dY = 0;

        /**
         * Copy the current values to the previous values
         */
        public void copyToLast() {
            lastX = x;
            lastY = y;
        }

        /**
         * Compute the values of dX and dY
         */
        public void computeDeltas() {
            dX = x - lastX;
            dY = y - lastY;
        }
    }

    public void saveCaptureState(String key, Bundle bundle) {
        bundle.putSerializable(key, params);
    }

    public void loadCaptureState(String key, Bundle bundle) {
        params = (Parameters)bundle.getSerializable(key);
        setRect();
    }
}
