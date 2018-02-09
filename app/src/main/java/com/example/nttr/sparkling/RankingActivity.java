package com.example.nttr.sparkling;

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

public class RankingActivity extends AppCompatActivity implements View.OnClickListener{

    TextView u1;
    TextView u2;
    TextView u3;
    TextView u4;
    TextView u5;
    TextView u6;
    TextView u7;
    TextView u8;
    TextView u9;
    TextView u10;

    TextView s1;
    TextView s2;
    TextView s3;
    TextView s4;
    TextView s5;
    TextView s6;
    TextView s7;
    TextView s8;
    TextView s9;
    TextView s10;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        u1 = (TextView) findViewById(R.id.u1);
        u2 = (TextView) findViewById(R.id.u2);
        u3 = (TextView) findViewById(R.id.u3);
        u4 = (TextView) findViewById(R.id.u4);
        u5 = (TextView) findViewById(R.id.u5);
        u6 = (TextView) findViewById(R.id.u6);
        u7 = (TextView) findViewById(R.id.u7);
        u8 = (TextView) findViewById(R.id.u8);
        u9 = (TextView) findViewById(R.id.u9);
        u10 = (TextView) findViewById(R.id.u10);

        s1 = (TextView) findViewById(R.id.s1);
        s2 = (TextView) findViewById(R.id.s2);
        s3 = (TextView) findViewById(R.id.s3);
        s4 = (TextView) findViewById(R.id.s4);
        s5 = (TextView) findViewById(R.id.s5);
        s6 = (TextView) findViewById(R.id.s6);
        s7 = (TextView) findViewById(R.id.s7);
        s8 = (TextView) findViewById(R.id.s8);
        s9 = (TextView) findViewById(R.id.s9);
        s10 = (TextView) findViewById(R.id.s10);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ranking");

        Query query = myRef.orderByChild("sort_score");
        query.limitToFirst(10).addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d("aaa", "aaaaa");
                        int nScore = 0;
                        String nPlace = "";
                        nScore = dataSnapshot.child("score").getValue(Integer.class);
                        nPlace = dataSnapshot.child("place").getValue(String.class);
                        if(count == 0){
                            u1.setText(nPlace);
                            s1.setText("" + nScore);
                        }else if(count == 1){
                            u2.setText(nPlace);
                            s2.setText("" + nScore);
                        }else if(count == 2){
                            u3.setText(nPlace);
                            s3.setText("" + nScore);
                        }else if(count == 3){
                            u4.setText(nPlace);
                            s4.setText("" + nScore);
                        }else if(count == 4){
                            u5.setText(nPlace);
                            s5.setText("" + nScore);
                        }else if(count == 5){
                            u6.setText(nPlace);
                            s6.setText("" + nScore);
                        }else if(count == 6){
                            u7.setText(nPlace);
                            s7.setText("" + nScore);
                        }else if(count == 7){
                            u8.setText(nPlace);
                            s8.setText("" + nScore);
                        }else if(count == 8){
                            u9.setText(nPlace);
                            s9.setText("" + nScore);
                        }else if(count == 9){
                            u10.setText(nPlace);
                            s10.setText("" + nScore);
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


    @Override
    public void onClick(View v) {
        this.finish();
    }
}
