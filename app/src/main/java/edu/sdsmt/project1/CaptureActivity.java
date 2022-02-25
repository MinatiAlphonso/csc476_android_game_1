package edu.sdsmt.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CaptureActivity extends AppCompatActivity {

    private String captureType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
    }

    public void onRectClick(View view) {
        captureType = "RECTANGLE";
        close();
    }

    public void onCircleClick(View view) {
        captureType = "CIRCLE";
        close();
    }

    public void onLineClick(View view) {
        captureType = "LINE";
        close();
    }

    private void close() {
        Intent intent = new Intent();
        intent.putExtra(GameActivity.RETURN_CAPTURE_MESSAGE, captureType);
        setResult(RESULT_OK, intent);
        finish();
    }

}