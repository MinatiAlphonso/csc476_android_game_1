package edu.sdsmt.project1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class StartActivity extends AppCompatActivity {
    // Views to get the data
    private EditText Player1Input;
    private EditText Player2Input;
    private EditText RoundInput;
    // Strings to access the data in the intent
    public static final String PLAYER1_NAME = "edu.sdsmt.projet1player1name";
    public static final String PLAYER2_NAME = "edu.sdsmt.projet1player1name";
    public static final String ROUND_COUNT = "edu.sdsmt.projet1roundcount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        // assign the views
        Player1Input = findViewById(R.id.Player1TextInput);
        Player2Input = findViewById(R.id.Player2TextInput);
        RoundInput = findViewById(R.id.RoundsInput);


    }


    public void onPlay(View view) {
        // load the data from the views
        String p1Name = Player1Input.getText().toString();
        String p2Name = Player2Input.getText().toString();
        String rounds = RoundInput.getText().toString();

        if (p1Name.length() == 0 || p2Name.length() == 0 || rounds.length() == 0) {
            // If a view has no information display a dialog prompting them to enter it
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter Information!");
            builder.setMessage("Please enter both players names and the number of rounds to play.");
            builder.setPositiveButton(android.R.string.ok, null);

            AlertDialog alert = builder.create();
            alert.show();
        }
        else {
            // If all the data is entered put it in the intent
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra(PLAYER1_NAME, p1Name);
            intent.putExtra(PLAYER2_NAME, p2Name);
            intent.putExtra(ROUND_COUNT, Integer.parseInt(rounds));
            // launch the GameActivity
            startActivity(intent);
        }
    }
}