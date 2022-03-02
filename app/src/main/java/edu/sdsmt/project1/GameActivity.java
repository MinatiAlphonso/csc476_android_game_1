package edu.sdsmt.project1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    //tag to identify information being passed to THIS activity
    public final static String PLAYER1_NAME = "edu.sdsmt.project1.PLAYER1_NAME";
    public final static String PLAYER2_NAME = "edu.sdsmt.project1.PLAYER2_NAME";
    public final static String ROUND_COUNT = "edu.sdsmt.project1.ROUND_COUNT";

    //gets the capture information back from the captureActivity
    public final static String RETURN_CAPTURE_MESSAGE = "edu.sdsmt.project1.RETURN_CAPTURE_MESSAGE";

    private TextView Player1Name = null;
    private TextView Player2Name = null;
    private TextView RoundCount = null;
    private TextView Turn = null;
    private Button Capture = null;
    private GameView gameView = null;

    private Game game;

    //activity launcher for captureActivity
    ActivityResultLauncher<Intent> captureResultLauncher;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle){
        super.onSaveInstanceState(bundle);
        gameView.saveGameState(bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Player1Name = (TextView) findViewById(R.id.textViewPlayer1Name);
        Player2Name = (TextView) findViewById(R.id.textViewPlayer2Name);
        RoundCount = (TextView) findViewById(R.id.textViewRoundCount);
        Turn = (TextView) findViewById(R.id.textViewTurn);
        Capture = (Button) findViewById(R.id.buttonCapture);
        gameView = (GameView) findViewById(R.id.gameView);

        game = gameView.getGame();
        //Get the message from the intent (StartActivity started up this activity)
        Intent intent = getIntent();
        game.setPlayersNames(intent.getStringExtra(PLAYER1_NAME),intent.getStringExtra(PLAYER2_NAME));
        game.setRounds(intent.getIntExtra(ROUND_COUNT,0));


        showGameStateInfo();

        Capture.setEnabled(false);

        //Info from CaptureActivity
        ActivityResultContracts.StartActivityForResult contract =
                new ActivityResultContracts.StartActivityForResult();
        captureResultLauncher =
                registerForActivityResult(contract, (result)->
                { int resultCode = result.getResultCode();
                    if (resultCode == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        game.setCapture(data.getIntExtra(RETURN_CAPTURE_MESSAGE, -1));
                    }});

        /**
         * Restore Game State
         */
        if(savedInstanceState != null){
            gameView.restoreGameState(savedInstanceState);
        }
    }

    private void showGameStateInfo() {
        Player1Name.setText(String.format("%s%s", getString(R.string.player1_text),game.getPlayer1().getName()));
        Player2Name.setText(String.format("%s%s", getString(R.string.player2_text),game.getPlayer2().getName()));
        RoundCount.setText(String.format("%s%s%s%s", getString(R.string.round_text), game.getRound(),"/",game.getTotalRounds()));
        Turn.setText(String.format("%s%s%s",getString(R.string.turn_text),"Player ",game.getPlayerTurn()));
    }

    public void onSelectCaptureOption(View view){
        Capture.setEnabled(true);
        Intent intent = new Intent(this, CaptureActivity.class);
        captureResultLauncher.launch(intent);
    }

    public void onCapture(View view){
        Capture.setEnabled(false);
        //reset the capture option
        game.setCapture(-1);
        //redraw the view
        gameView.invalidate();

        game.captureCollectibles();
        game.advanceTurn();
        if (game.getGameState() == Game.GAME_OVER) {
            // open end activity
            Log.d("GAME_STATE", "Game Over");
        }
    }


}