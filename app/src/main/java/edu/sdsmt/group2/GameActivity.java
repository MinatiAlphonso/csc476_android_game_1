package edu.sdsmt.group2;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    //tag to identify information being passed to THIS activity
    public final static String PLAYER1_NAME = "edu.sdsmt.group2.PLAYER1_NAME";
    public final static String PLAYER2_NAME = "edu.sdsmt.group2.PLAYER2_NAME";
    public final static String ROUND_COUNT = "edu.sdsmt.group2.ROUND_COUNT";

    //gets the capture information back from the captureActivity
    public final static String RETURN_CAPTURE_MESSAGE = "edu.sdsmt.group2.RETURN_CAPTURE_MESSAGE";

    private TextView Player1Name = null;
    private TextView Player2Name = null;
    private TextView RoundCount = null;
    private TextView Turn = null;
    private Button Capture = null;
    private GameView gameView = null;

    private Game game;

    private Boolean isCaptureEnabled = false;

    //activity launcher for captureActivity
    ActivityResultLauncher<Intent> captureResultLauncher;

    private static final String IS_CAPTURE_ENABLED = "isCaptureEnabled";

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle){
        super.onSaveInstanceState(bundle);

        bundle.putBoolean(IS_CAPTURE_ENABLED, isCaptureEnabled);

        gameView.saveGameState(bundle);
    }

    private void updateUI(){
        Capture.setEnabled(isCaptureEnabled);
        Player1Name.setText(String.format("%s%s", getString(R.string.player1_text),game.getPlayer1().getName()));
        Player2Name.setText(String.format("%s%s", getString(R.string.player2_text),game.getPlayer2().getName()));
        RoundCount.setText(String.format("%s%s%s%s", getString(R.string.round_text), game.getRound(),"/",game.getTotalRounds()));
        Turn.setText(String.format("%s%s%s",getString(R.string.turn_text),"Player ",game.getPlayerTurn()));
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
            isCaptureEnabled = savedInstanceState.getBoolean(IS_CAPTURE_ENABLED);
            gameView.restoreGameState(savedInstanceState);
        }
        updateUI();
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
        game.captureCollectibles(); //this is causing the game to either crash or move to the start screen

        //reset the capture option
        game.setCapture(-1);

        //redraw the view
        gameView.invalidate();

        game.advanceTurn();
        updateUI();

        if (game.getGameState() == Game.GAME_OVER) {
            openEndActivity();
            this.finish();// close GameActivity so EndActivity will reveal StartActivity on closing
        }
    }

    private void openEndActivity() {
        Intent intent = new Intent(this, EndActivity.class);
        intent.putExtra(PLAYER1_NAME, Player1Name.getText());
        intent.putExtra(PLAYER2_NAME, Player2Name.getText());
        intent.putExtra(EndActivity.PLAYER1_SCORE, game.getPlayer1().getScore());
        intent.putExtra(EndActivity.PLAYER2_SCORE, game.getPlayer2().getScore());
        startActivity(intent);
    }

    @Override
    public void onBackPressed()//GRADING: BACK
    {
        DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int option) {
                if(option == DialogInterface.BUTTON_POSITIVE) {
                    finish();
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String dialogMessage = "Both players' progress will be lost. Are you sure you want to quit the game?";
        builder.setMessage(dialogMessage)
                .setPositiveButton("Yes", dialogListener)
                .setNegativeButton("No", dialogListener)
                .show();
    }


}