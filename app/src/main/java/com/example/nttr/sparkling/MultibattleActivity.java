package com.example.nttr.sparkling;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        myScore = (TextView) findViewById(R.id.myScoreR);
        score1 = (TextView) findViewById(R.id.Someone);
        score2 = (TextView) findViewById(R.id.SomeTwo);
        score3 = (TextView) findViewById(R.id.SomeThree);

        resultText = (TextView) findViewById(R.id.resultText);

        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        ido = intent.getDoubleExtra("ido", 0);
        keido = intent.getDoubleExtra("keido", 0);
        time = intent.getLongExtra("time", 0);

        myScore.setText("あなたのスコア：" + score);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef2 = database.getReference("GPSranking");

        Query query = myRef2.orderByChild("sort_score");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int nScore = 0;
                long nTime = 0;
                double nIdo = 0;
                double nKeido = 0;
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    try{
                        nTime = data.child("time").getValue(Long.class);
                        nIdo = data.child("ido").getValue(Double.class);
                        nKeido = data.child("keido").getValue(Double.class);
                        if(Math.abs(time - nTime) > 60000){
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("GPSranking");
                            try{
                                String removeS = data.getKey();
                                myRef.child(removeS).removeValue();
                            }catch (Exception e){
                                Log.d("a", "すでに削除済み？");
                            }
                        }else if (Math.abs(time - nTime) <= 32000 && Math.abs(ido - nIdo) <= 0.001
                                && Math.abs(keido - nKeido) <= 0.001){
                            nScore = data.child("score").getValue(Integer.class);
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
                    }catch (Exception e){
                        Log.d("sparkling","nullpo");
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("GPSranking");
                        try{
                            String removeS = data.getKey();
                            myRef.child(removeS).removeValue();
                        }catch (Exception eee){
                            Log.d("a", "すでに削除済み？");
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        query.addChildEventListener(
//                new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        Log.d("aaa", "aaaaa");
//
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                        Log.d("aaa", "bbbbb");
//                    }
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//                        Log.d("aaa", "ccccc");
//                    }
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                        Log.d("aaa", "ddddd");
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        Log.d("aaa", "eeeee");
//                    }
//                }
//        );
    }

    @Override
    public void onClick(View v) {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
