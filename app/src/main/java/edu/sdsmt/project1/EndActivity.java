package edu.sdsmt.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {

    public final static String PLAYER1_SCORE = "edu.sdsmt.project1.PLAYER1_SCORE";
    public final static String PLAYER2_SCORE = "edu.sdsmt.project1.PLAYER2_SCORE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        Intent intent = getIntent();

        TextView player1NameTextView = findViewById(R.id.player1FinalTextView);
        TextView player2NameTextView = findViewById(R.id.player2FinalTextView);
        TextView player1FinalScoreTextView = findViewById(R.id.player1FinalScoreTextView);
        TextView player2FinalScoreTextView = findViewById(R.id.player2FinalScoreTextView);

        String player1Name = intent.getStringExtra(GameActivity.PLAYER1_NAME);
        String player2Name = intent.getStringExtra(GameActivity.PLAYER2_NAME);
        player1NameTextView.setText(player1Name);
        player2NameTextView.setText(player2Name);

        int player1Score = intent.getIntExtra(PLAYER1_SCORE, 0);
        int player2Score = intent.getIntExtra(PLAYER2_SCORE, 0);
        player1FinalScoreTextView.setText(Integer.toString(player1Score));
        player2FinalScoreTextView.setText(Integer.toString(player2Score));
    }

    public void onReturnToStartButtonClick(View view) {
        this.finish();
    }
}