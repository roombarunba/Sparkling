package com.example.nttr.sparkling;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class MultiresultActivity extends Activity implements View.OnClickListener{

//    TextView mScoreText;

    int score;
    double ido;
    double keido;
    boolean first = true;

//    TextView textOne;
//    TextView textTwo;
//    TextView textThree;
//    TextView textFour;
//    TextView textFive;

    int count = 1;

    long time;

    Button battleB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiresult);

//        mScoreText = (TextView) findViewById(R.id.MScoreResult);

//        textOne = (TextView) findViewById(R.id.MrankOne);
//        textTwo = (TextView) findViewById(R.id.MrankTwo);
//        textThree = (TextView) findViewById(R.id.MrankThree);
//        textFour = (TextView) findViewById(R.id.MrankFour);
//        textFive = (TextView) findViewById(R.id.MrankFive);

        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        ido = intent.getDoubleExtra("ido", 0);
        keido = intent.getDoubleExtra("keido", 0);

//        mScoreText.setText("Score : " + score);

        battleB = (Button) findViewById(R.id.battleButton);
        battleB.setEnabled(false);

        Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        vib.vibrate(2000);
    }


//    private DatabaseReference getMessageRef() {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        return database.getReference(MESSAGE_STORE); // MESSAGE_STORE = "message"
//    }

    private void sendMessage() {
        time = System.currentTimeMillis();
        final GPSData data = new GPSData(ido, keido, score, time);
        String token = UUID.randomUUID().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("GPSranking");
        myRef.child(token).setValue(data);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        battleB.setEnabled(true);
        battleB.setBackgroundColor(Color.GREEN);
        battleB.setTextColor(Color.BLUE);
    }

    public void battle(View v){
        finish();
        Intent intent = new Intent(this,MultibattleActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("ido", ido);
        intent.putExtra("keido", keido);
        intent.putExtra("time", time);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        if(first){
            first = false;
            sendMessage();
        }
    }
}
