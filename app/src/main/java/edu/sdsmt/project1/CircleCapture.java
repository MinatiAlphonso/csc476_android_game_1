package edu.sdsmt.project1;

import android.content.Context;

public class CircleCapture extends Capture{

    public static final float CAPTURE_CHANCE = 0.8f;

    public CircleCapture(Context context, int id) {
        super(context, id);
        setScale(1.0f);
        setChance(CAPTURE_CHANCE);
    }
}
