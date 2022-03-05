package edu.sdsmt.group2;

import android.content.Context;

public class LineCapture extends Capture {

    public static final float CAPTURE_CHANCE = 0.75f;

    public LineCapture(Context context, int id) {
        super(context, id);
        setChance(CAPTURE_CHANCE);
    }
}
