package com.example.nttr.sparkling;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.UUID;

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

        sendMessage("place", score);
    }


//    private DatabaseReference getMessageRef() {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        return database.getReference(MESSAGE_STORE); // MESSAGE_STORE = "message"
//    }

    private void sendMessage(String place, int score) {
        final Data data = new Data(place, score);
        String token = UUID.randomUUID().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ranking");
        myRef.child(token).setValue(data);

        Query query = myRef.orderByChild("sort_score");
        query.limitToFirst(10).addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d("aaa", "aaaaa");
                        int nScore = dataSnapshot.child("score").getValue(Integer.class);
                        String nPlace = dataSnapshot.child("place").getValue(String.class);
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
