package com.example.nttr.sparkling;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    SensorManager mSensorManager;
    Sensor mAccSensor;

    TextView mTextX;
    TextView mTextY;
    TextView mTextZ;
    TextView mScoreText;

    double beforeX = 0;
    double beforeY = 0;
    double beforeZ = 0;

    boolean first = true;

    int score = 0;

    TextView mTimeText;

    CountDown countDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mAccSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mTextX = (TextView) findViewById(R.id.textX);
        mTextY = (TextView) findViewById(R.id.textY);
        mTextZ = (TextView) findViewById(R.id.textZ);
        mScoreText = (TextView) findViewById(R.id.scoreText);
        mTimeText = (TextView) findViewById(R.id.timeText);

        long countNumber = 1000;
        long interval = 10;
        countDown = new CountDown(countNumber, interval, this);
        countDown.start();
    }

    // 加速度センサーの値に変化があった時に呼ばれる
    @Override
    public void onSensorChanged(SensorEvent event) {
        double nx = event.values[0];
        double ny = event.values[1];
        double nz = event.values[2];
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            Log.d("加速度", "x = " + nx
                    + ", y = " + ny
                    + ", z = " + nz);
        }

        mTextX.setText("" + nx);
        mTextY.setText("" + ny);
        mTextZ.setText("" + nz);

        if(first){
            first = false;
        }else{
            double sx = Math.abs(nx - beforeX);
            double sy = Math.abs(ny - beforeY);
            double sz = Math.abs(nz - beforeZ);
            score += Math.sqrt((sx*sx) + (sy*sy) + (sz*sz));
        }

        beforeX = nx;
        beforeY = ny;
        beforeZ = nz;

        mScoreText.setText("" + (int)score);
    }

    // 加速度センサーの精度が変更された時に呼ばれる
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(this,mAccSensor, SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    protected void onPause() {
        super.onPause();

        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            countDown.cancel();
        }catch (NullPointerException e){
            Log.d("Ecatch", "nullpo");
        }
    }

    class CountDown extends CountDownTimer {

        MainActivity mainActivity;

        CountDown(long millisInFuture, long countDownInterval, MainActivity mainActivity) {
            super(millisInFuture, countDownInterval);
            this.mainActivity = mainActivity;
        }

        @Override
        public void onFinish() {
            // 完了
            // 画面繊維
            finish();
            Intent intent = new Intent(mainActivity, ResultActivity.class);
            intent.putExtra("score", score);
            startActivity(intent);
        }

        // インターバルで呼ばれる
        @Override
        public void onTick(long millisUntilFinished) {
            // 残り時間を分、秒、ミリ秒に分割
            //long mm = millisUntilFinished / 1000 / 60;
            //long ss = millisUntilFinished / 1000 % 60;
            //long ms = millisUntilFinished - ss * 1000 - mm * 1000 * 60;
            //timerText.setText(String.format("%1$02d:%2$02d.%3$03d", mm, ss, ms));

            mTimeText.setText("" + millisUntilFinished);
        }
    }
}
