package com.example.nttr.sparkling;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.UUID;

public class ResultActivity extends Activity implements View.OnClickListener{

    TextView mScoreText;

    int score;
    String place;
    boolean first = true;

    TextView textOne;
    TextView textTwo;
    TextView textThree;

    int count = 0;

    Button sendRanking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mScoreText = (TextView) findViewById(R.id.ScoreResult);

        textOne = (TextView) findViewById(R.id.rankOne);
        textTwo = (TextView) findViewById(R.id.rankTwo);
        textThree = (TextView) findViewById(R.id.rankThree);

        sendRanking = (Button) findViewById(R.id.sendRanking);

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
        final SharedPreferences data = getSharedPreferences("uid", Context.MODE_PRIVATE);
        String uid = data.getString("uid", "");
        //テキスト入力を受け付けるビューを作成します。
        final EditText editView = new EditText(this);
        editView.setText(uid);
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("ユーザーネーム入力")
                //setViewにてビューを設定します。
                .setView(editView)
                .setPositiveButton("送信", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(first){
                            first = false;
                            place = editView.getText().toString();
                            SharedPreferences.Editor editor = data.edit();
                            editor.putString("uid", place);
                            editor.apply();
                            final Data data = new Data(place, score);
                            String token = UUID.randomUUID().toString();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("ranking");
                            myRef.child(token).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        sendRanking.setEnabled(false);
                                        sendRanking.setClickable(false);
                                        toastSuccess();
                                    }else{
                                        toastFail();
                                    }
                                }
                            });

//                            try {
//                                Thread.sleep(2000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }

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
                })
                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .show();
    }

    void toastSuccess(){
        Toast toast = Toast.makeText(this, "送信成功！", Toast.LENGTH_SHORT);
        toast.show();
    }

    void toastFail(){
        Toast toast = Toast.makeText(this, "送信失敗！もう一回押して！", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void toRanking(View v){
        Intent intent = new Intent(this,RankingActivity.class);
        startActivity(intent);
    }




    @Override
    public void onClick(View v) {

    }
}
