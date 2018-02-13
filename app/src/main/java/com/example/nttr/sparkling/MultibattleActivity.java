package com.example.nttr.sparkling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MultibattleActivity extends Activity implements View.OnClickListener{

    TextView myScore;

    TextView score1;
    TextView score2;
    TextView score3;

    TextView resultText;

    int score;
    double ido;
    double keido;
    long time;

    int count = 1;

    boolean mybool = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multibattle);

        myScore = (TextView) findViewById(R.id.myScore);
        score1 = (TextView) findViewById(R.id.Someone);
        score2 = (TextView) findViewById(R.id.SomeTwo);
        score3 = (TextView) findViewById(R.id.SomeThree);

        resultText = (TextView) findViewById(R.id.resultText);

        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        ido = intent.getDoubleExtra("ido", 0);
        keido = intent.getDoubleExtra("keido", 0);
        time = intent.getLongExtra("time", 0);

        myScore.setText("" + score);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("GPSranking");

        Query query = myRef.orderByChild("sort_score");
        query.addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d("aaa", "aaaaa");
                        int nScore = 0;
                        long nTime = 0;
                        nTime = dataSnapshot.child("time").getValue(Long.class);
                        if(Math.abs(time - nTime) > 60000){
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("GPSranking");
                            try{
                                String removeS = dataSnapshot.getKey();
                                myRef.child(removeS).removeValue();
                            }catch (Exception e){
                                Log.d("a", "すでに削除済み？");
                            }
                        }else{
                            nScore = dataSnapshot.child("score").getValue(Integer.class);
                            if(count == 1){
                                score1.setText("" + nScore);
                                if(nScore == score){
                                    resultText.setText("１位！！！");
                                    mybool = false;
                                }
                            }else if (count == 2){
                                score2.setText("" + nScore);
                                if(nScore == score && mybool){
                                    resultText.setText("２位！！！");
                                    mybool = false;
                                }
                            }else if(count == 3){
                                score3.setText("" + nScore);
                                if(nScore == score && mybool){
                                    resultText.setText("３位！！！");
                                    mybool = false;
                                }
                            }
                            count++;
                            if(count == 4 && mybool){
                                resultText.setText("圏外。。。");
                            }
                        }


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
        this.finish();
    }
}
