package com.example.nttr.sparkling;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.UUID;

public class MultiresultActivity extends AppCompatActivity implements View.OnClickListener{

    TextView mScoreText;

    int score;
    double ido;
    double keido;
    boolean first = true;

    TextView textOne;
    TextView textTwo;
    TextView textThree;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiresult);

        mScoreText = (TextView) findViewById(R.id.MScoreResult);

        textOne = (TextView) findViewById(R.id.MrankOne);
        textTwo = (TextView) findViewById(R.id.MrankTwo);
        textThree = (TextView) findViewById(R.id.MrankThree);

        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        ido = intent.getDoubleExtra("ido", 0);
        keido = intent.getDoubleExtra("keido", 0);

        mScoreText.setText("Score : " + score);
    }


//    private DatabaseReference getMessageRef() {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        return database.getReference(MESSAGE_STORE); // MESSAGE_STORE = "message"
//    }

    private void sendMessage() {
        final GPSData data = new GPSData(ido, keido, score);
        String token = UUID.randomUUID().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("GPSranking");
        myRef.child(token).setValue(data);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Query query = myRef.orderByChild("sort_score");
        query.limitToFirst(1).addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d("aaa", "aaaaa");
                        int nScore = 0;
                        double nIdo = 0;
                        double nKeido = 0;
                        nScore = dataSnapshot.child("score").getValue(Integer.class);
                        nIdo = dataSnapshot.child("ido").getValue(Double.class);
                        nKeido = dataSnapshot.child("keido").getValue(Double.class);
                        textOne.setText("score : " + nScore);
                        textTwo.setText("緯度 : " + nIdo);
                        textThree.setText("経度: " + nKeido);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.d("aaa", "bbbbb");
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.d("aaa", "ccccc");
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        Log.d("aaa", "ddddd");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("aaa", "eeeee");
                    }
                }
        );
    }


    @Override
    public void onClick(View v) {
        if(first){
            first = false;
            sendMessage();
        }
    }
}
