package edu.sdsmt.project1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    private TextView Player1Name = null;
    private TextView Player2Name = null;
    private TextView RoundCount = null;
    private TextView Turn = null;
    private Button SelectCaptureOption = null;
    private Button Capture = null;
    private GameView gameView = null;

    private Game game;
    private Boolean isCaptureEnabled = false;

    //tag to identify information being passed to THIS activity
    public static final String PLAYER1_NAME = "edu.sdsmt.project1.PLAYER1_NAME";
    public static final String PLAYER2_NAME = "edu.sdsmt.project1.PLAYER2_NAME";
    public static final String ROUND_COUNT = "edu.sdsmt.project1.ROUND_COUNT";

    //gets the capture information back from the captureActivity
    public final static String RETURN_CAPTURE_MESSAGE = "edu.sdsmt.project1.RETURN_CAPTURE_MESSAGE";

    //activity launcher for captureActivity
    ActivityResultLauncher<Intent> captureResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Player1Name = (TextView) findViewById(R.id.textViewPlayer1Name);
        Player2Name = (TextView) findViewById(R.id.textViewPlayer2Name);
        RoundCount = (TextView) findViewById(R.id.textViewRoundCount);
        Turn = (TextView) findViewById(R.id.textViewTurn);
        SelectCaptureOption = (Button) findViewById(R.id.buttonSelectCaptureOption);
        Capture = (Button) findViewById(R.id.buttonCapture);
        gameView = (GameView) findViewById(R.id.gameView);

        game = gameView.getGame();
        //Get the message from the intent (StartActivity started up this activity)
        Intent intent = getIntent();
        game.setPlayersNames(intent.getStringExtra(PLAYER1_NAME),intent.getStringExtra(PLAYER2_NAME));
        game.setRounds(intent.getIntExtra(ROUND_COUNT,0));


        Player1Name.setText(String.format("%s%s", getString(R.string.player1_text),game.getPlayer1().getName()));
        Player2Name.setText(String.format("%s%s", getString(R.string.player2_text),game.getPlayer2().getName()));
        RoundCount.setText(String.format("%s%s%s%s", getString(R.string.round_text), game.getRound(),"/",game.getTotalRounds()));
        Turn.setText(String.format("%s%s%s",getString(R.string.turn_text),"Player ",game.getPlayerTurn()));

        Capture.setEnabled(isCaptureEnabled);

        //Info from CaptureActivity
        ActivityResultContracts.StartActivityForResult contract =
                new ActivityResultContracts.StartActivityForResult();
        captureResultLauncher =
                registerForActivityResult(contract, (result)->
                { int resultCode = result.getResultCode();
                    if (resultCode == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String captureValue = data.getStringExtra(RETURN_CAPTURE_MESSAGE);
                    }});
    }

    public void onSelectCaptureOption(View view){
        isCaptureEnabled = true;
        Capture.setEnabled(isCaptureEnabled);
        Intent intent = new Intent(this, CaptureActivity.class);
        captureResultLauncher.launch(intent);
    }

    public void onCapture(View view){
        isCaptureEnabled = false;
        Capture.setEnabled(isCaptureEnabled);

    }

    /*protected void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);

        gameView.saveGameState(bundle);
    }*/
}