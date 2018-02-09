package com.example.nttr.sparkling;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
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

public class ResultActivity extends AppCompatActivity implements View.OnClickListener{

    TextView mScoreText;

    int score;
    String place;
    boolean first = true;

    TextView textOne;
    TextView textTwo;
    TextView textThree;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mScoreText = (TextView) findViewById(R.id.ScoreResult);

        textOne = (TextView) findViewById(R.id.rankOne);
        textTwo = (TextView) findViewById(R.id.rankTwo);
        textThree = (TextView) findViewById(R.id.rankThree);

        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        place = intent.getStringExtra("place");

        mScoreText.setText("Score : " + score);

        Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        vib.vibrate(2000);
    }


//    private DatabaseReference getMessageRef() {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        return database.getReference(MESSAGE_STORE); // MESSAGE_STORE = "message"
//    }

    public void sendMessage(View v) {
        if(first){
            first = false;
            final Data data = new Data(place, score);
            String token = UUID.randomUUID().toString();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("ranking");
            myRef.child(token).setValue(data);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Query query = myRef.orderByChild("sort_score");
            query.limitToFirst(3).addChildEventListener(
                    new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Log.d("aaa", "aaaaa");
                            int nScore = 0;
                            String nPlace = "";
                            nScore = dataSnapshot.child("score").getValue(Integer.class);
                            nPlace = dataSnapshot.child("place").getValue(String.class);
                            if(count == 0){
                                textOne.setText("score : " + nScore + ", place : " + nPlace);
                            }else if(count == 1){
                                textTwo.setText("score : " + nScore + ", place : " + nPlace);
                            }else if(count == 2){
                                textThree.setText("score : " + nScore + ", place : " + nPlace);
                                count = -1;
                            }
                            count++;
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
    }

    public void toRanking(View v){
        Intent intent = new Intent(this,RankingActivity.class);
        startActivity(intent);
    }




    @Override
    public void onClick(View v) {

    }
}
