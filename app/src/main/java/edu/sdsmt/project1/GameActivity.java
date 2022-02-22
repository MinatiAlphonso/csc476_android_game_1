package edu.sdsmt.project1;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class GameActivity extends AppCompatActivity {

    //tag to identify information being passed to THIS activity
    public static final String PLAYER1_NAME = "edu.sdsmt.project1player1name";
    public static final String PLAYER2_NAME = "edu.sdsmt.project1player2name";
    public static final String ROUND_COUNT = "edu.sdsmt.project1roundcount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Get the message from the intent (StartActivity started up this activity)
        Intent intent = getIntent();
        TextView player1name = findViewById(R.id.player1name);
        player1name.setText(String.format("%s%s", getString(R.string.player1_text),intent.getStringExtra(PLAYER1_NAME)));
        TextView player2name = findViewById(R.id.player2name);
        player2name.setText(String.format("%s%s", getString(R.string.player2_text),intent.getStringExtra(PLAYER2_NAME)));
        TextView roundCount = findViewById(R.id.roundCount);
        roundCount.setText(String.format("%s%s", getString(R.string.round_text), intent.getIntExtra(ROUND_COUNT,0)));
    }
}