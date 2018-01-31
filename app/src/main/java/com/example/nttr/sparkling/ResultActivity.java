package com.example.nttr.sparkling;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    TextView mScoreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mScoreText = (TextView) findViewById(R.id.ScoreResult);

        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);

        mScoreText.setText("Score : " + score);
    }


}
