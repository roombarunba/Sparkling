package com.example.nttr.sparkling;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class MultiresultActivity extends Activity implements View.OnClickListener, LocationListener{

    int score;
    double ido;
    double keido;
    boolean first = true;

    int count = 1;

    long time;

    Button send;
    Button battleB;

    private LocationManager locationManager;
    private static final int MinTime = 1000;
    private static final float MinDistance = 1;

    CountDown countDown;
    TextView multiCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiresult);

        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        ido = intent.getDoubleExtra("ido", 0);
        keido = intent.getDoubleExtra("keido", 0);

        send = (Button) findViewById(R.id.sendButton);

        battleB = (Button) findViewById(R.id.battleButton);
        battleB.setEnabled(false);

        multiCount = (TextView) findViewById(R.id.MultiCount);

        // LocationManager インスタンス生成
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if(ido == 0 && keido == 0){
            startGPS();
            send.setEnabled(false);
        }else {
            multiCount.setText("みんなの準備ができたら\n送信を押してね");
        }

        Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        vib.vibrate(2000);
    }

    private void sendMessage() {
        multiCount.setText("送信中…");
        time = System.currentTimeMillis();
        final GPSData data = new GPSData(ido, keido, score, time);
        String token = UUID.randomUUID().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("GPSranking");
        myRef.child(token).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    battleB.setEnabled(true);
                    battleB.setBackgroundColor(Color.GREEN);
                    battleB.setTextColor(Color.BLUE);
                    send.setEnabled(false);
                    send.setClickable(false);
                    toastSuccess();
                    countDownM();
                }else{
                    toastFail();
                }
            }
        });


    }

    void countDownM(){
        long countNumber = 30000;
        long interval = 60;
        countDown = new CountDown(countNumber, interval, this);
        countDown.start();
    }

    void toastSuccess(){
        Toast toast = Toast.makeText(this, "送信成功！", Toast.LENGTH_SHORT);
        toast.show();
    }

    void toastFail(){
        Toast toast = Toast.makeText(this, "送信失敗！もう一回押して！", Toast.LENGTH_SHORT);
        toast.show();
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

    protected void startGPS() {
        Log.d("MultiplayActivity", "gpsEnabled");
        final boolean gpsEnabled
                = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!gpsEnabled) {
            // GPSを設定するように促す
            Log.d("kokoni","konaihazu");
            // GPSを設定するように促す
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("位置が取得できません")
                    .setMessage("このモードで遊ぶためには、\n\n"
                            + "①　GPSをON\n" + "②　位置情報をWi-Fi、Bluethooth、モバイルネットワークから特定可能\n\n"
                            + "に設定する必要があります")
                    .setPositiveButton("設定画面へ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            enableLocationSettings();
                        }
                    })
                    .create();
            alertDialog.show();
        }

        if (locationManager != null) {
            Log.d("LocationActivity", "locationManager.requestLocationUpdates");
            // バックグラウンドから戻ってしまうと例外が発生する場合がある
            try {
                // minTime = 1000msec, minDistance = 50m
                if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)!=
                        PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION)!=
                                PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        MinTime, MinDistance, this);
            } catch (Exception e) {
                e.printStackTrace();

                Toast toast = Toast.makeText(this,
                        "例外が発生、位置情報のPermissionを許可していますか？", Toast.LENGTH_SHORT);
                toast.show();

                //MainActivityに戻す
                finish();
            }
        } else {
            Log.d("Sp","startGPS");
        }

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
        try{
            stopGPS();
        }catch (NullPointerException e){
            Log.d("Ecatch", "nullpo");
        }
        try{
            countDown.cancel();
        }catch (NullPointerException e){
            Log.d("Ecatch", "nullpo");
        }
    }

    private void stopGPS(){
        if (locationManager != null) {
            Log.d("LocationActivity", "onStop()");
            // update を止める
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(this);
        } else {
            Log.d("Sp","stopGPS");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        String location_s = "";
        location_s += "----------\n";
        location_s += "Latitude=" + String.valueOf(location.getLatitude()) + "\n";
        location_s += "Longitude=" + String.valueOf(location.getLongitude()) + "\n";
        location_s += "Accuracy=" + String.valueOf(location.getAccuracy()) + "\n";
        location_s += "Altitude=" + String.valueOf(location.getAltitude()) + "\n";
        location_s += "Time=" + String.valueOf(location.getTime()) + "\n";
        location_s += "Speed=" + String.valueOf(location.getSpeed()) + "\n";
        location_s += "Bearing=" + String.valueOf(location.getBearing()) + "\n";
        location_s += "----------\n";

        ido = location.getLatitude();
        keido = location.getLongitude();

        multiCount.setText("みんなの準備ができたら\n送信を押してね");
        send.setEnabled(true);
        stopGPS();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }


    class CountDown extends CountDownTimer {

        MultiresultActivity multiresultActivity;

        public CountDown(long millisInFuture, long countDownInterval, MultiresultActivity multiresultActivity) {
            super(millisInFuture, countDownInterval);
            this.multiresultActivity = multiresultActivity;
        }

        @Override
        public void onFinish() {
            // 完了
            // 画面繊維
//            finish();
//            Intent intent = new Intent(multiresultActivity, MultiresultActivity.class);
//            intent.putExtra("score", score);
//            intent.putExtra("ido", ido);
//            intent.putExtra("keido", keido);
//            startActivity(intent);
            multiCount.setText("参加締め切り！");
        }

        // インターバルで呼ばれる
        @Override
        public void onTick(long millisUntilFinished) {
            // 残り時間を分、秒、ミリ秒に分割
            long mm = millisUntilFinished / 1000 / 60;
            long ss = millisUntilFinished / 1000 % 60;
            long ms = millisUntilFinished - ss * 1000 - mm * 1000 * 60;
            //timerText.setText(String.format("%1$02d:%2$02d.%3$03d", mm, ss, ms));

            multiCount.setText("参加締め切りまで\n残り" + ss + "秒");
        }
    }
}
