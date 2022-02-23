package edu.sdsmt.project1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    //tag to identify information being passed to THIS activity
    public static final String PLAYER1_NAME = "edu.sdsmt.project1.PLAYER1_NAME";
    public static final String PLAYER2_NAME = "edu.sdsmt.project1.PLAYER2_NAME";
    public static final String ROUND_COUNT = "edu.sdsmt.project1.ROUND_COUNT";

    public final static String RETURN_CAPTURE_MESSAGE = "edu.sdsmt.project1.RETURN_CAPTURE_MESSAGE";

    ActivityResultLauncher<Intent> captureResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Get the message from the intent (StartActivity started up this activity)
        Intent intent = getIntent();
        TextView Player1Name = findViewById(R.id.textViewPlayer1Name);
        Player1Name.setText(String.format("%s%s", getString(R.string.player1_text),intent.getStringExtra(PLAYER1_NAME)));
        TextView Player2Name = findViewById(R.id.textViewPlayer2Name);
        Player2Name.setText(String.format("%s%s", getString(R.string.player2_text),intent.getStringExtra(PLAYER2_NAME)));
        TextView RoundCount = findViewById(R.id.textViewRoundCount);
        RoundCount.setText(String.format("%s%s", getString(R.string.round_text), intent.getIntExtra(ROUND_COUNT,0)));

        ActivityResultContracts.StartActivityForResult contract =
                new ActivityResultContracts.StartActivityForResult();
        captureResultLauncher =
                registerForActivityResult(contract, (result)->
                { int resultCode = result.getResultCode();
                    if (resultCode == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        int value = data.getIntExtra(RETURN_CAPTURE_MESSAGE,0);
                        // set something to value
                    }});

    }

    public void onCaptureOption(View view){
        Intent intent = new Intent(this, CaptureActivity.class);
        captureResultLauncher.launch(intent);
    }
}