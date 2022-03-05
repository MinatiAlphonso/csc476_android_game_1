package edu.sdsmt.project1;

import android.content.Context;

public class CircleCapture extends Capture{

    public static final float CAPTURE_CHANCE = 1.0f;

    public CircleCapture(Context context, int id) {
        super(context, id);
        setScale(1.0f);
        setChance(CAPTURE_CHANCE);
    }
}
