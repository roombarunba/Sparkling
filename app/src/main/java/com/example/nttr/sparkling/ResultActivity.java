package com.example.nttr.sparkling;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class ResultActivity extends Activity implements View.OnClickListener{

    TextView mScoreText;

    int score;
    String place;
    boolean first = true;

    Button sendRanking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mScoreText = (TextView) findViewById(R.id.ScoreResult);

        sendRanking = (Button) findViewById(R.id.sendRanking);

        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        place = intent.getStringExtra("place");

        mScoreText.setText("スコア：" + score);

        Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        vib.vibrate(2000);
    }

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
                            sendRanking.setEnabled(false);
                            sendRanking.setClickable(false);
                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.blue_g);
                            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                            sendRanking.setBackground(drawable);
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
        first = true;
        sendRanking.setEnabled(true);
        sendRanking.setClickable(true);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.blue);
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        sendRanking.setBackground(drawable);
    }

    public void toRanking(View v){
        Intent intent = new Intent(this,RankingActivity.class);
        startActivity(intent);
    }




    @Override
    public void onClick(View v) {

    }
}
