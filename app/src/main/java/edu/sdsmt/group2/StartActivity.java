package edu.sdsmt.group2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.EditText;

/*
Project 1 Grading

Group:
__Done__ 6pt No redundant activities
__Done__ 6pt How to play dialog
__Done__ 6pt Icons
__Done__ 6pt End activity
__Done__ 6pt Back button handled
How to open the "how to play dialog": Click the help button on the start activity

Individual:

	Play activity and custom view

		__Done__ 9pt Activity appearence
		__Done__ 16pt Static Custom View
		__Done__ 20pt Dynamic part of the Custom View
		__Done__ 15pt Rotation

	Welcome activity and Game Class

		__Done__ 13pt Welcome activity appearence
		__Done__ 20pt Applying capture rules
		__Done__ 12pt Game state
		__Done__ 15pt Rotation
		What is the probaility of the reactangle capture: 1 - the scale of the rectangle (starts at 0.50 scale which would be 50% capture)

	Capture activity and activity sequencing

		__Done__ 9pt Capture activity apearence
		__Done__ 16pt Player round sequencing
		__Done__ 20pt Move to next activity
		__Done__ 15pt Rotation


Please list any additional rules that may be needed to properly grade your project:
 */
public class StartActivity extends AppCompatActivity {
    // Views to get the data
    private EditText Player1Input;
    private EditText Player2Input;
    private EditText RoundInput;

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
            intent.putExtra(GameActivity.PLAYER1_NAME, p1Name);
            intent.putExtra(GameActivity.PLAYER2_NAME, p2Name);
            intent.putExtra(GameActivity.ROUND_COUNT, Integer.parseInt(rounds));
            // launch the GameActivity
            startActivity(intent);
        }
    }

    public void onHelp(View view) {
        // create new web view
        WebView webView = new WebView(this);
        // set contents
        webView.loadData(getString(R.string.Help_paragraph), "text/html", "UTF-8");
        // create dialog, set title and add web view to dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(webView);
        builder.setTitle("Asteroids!");
        builder.setPositiveButton(android.R.string.ok, null);

        // show dialog
        AlertDialog alert = builder.create();
        alert.show();
        // get dialog window
        Window dialogWindow = alert.getWindow();
        // set dialog height to match parent and fill screen for easier reading
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(layoutParams);
    }
}