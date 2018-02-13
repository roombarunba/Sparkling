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
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class MultiresultActivity extends Activity implements View.OnClickListener, LocationListener{

//    TextView mScoreText;

    int score;
    double ido;
    double keido;
    boolean first = true;

//    TextView textOne;
//    TextView textTwo;
//    TextView textThree;
//    TextView textFour;
//    TextView textFive;

    int count = 1;

    long time;

    Button send;
    Button battleB;

    private LocationManager locationManager;
    private static final int MinTime = 1000;
    private static final float MinDistance = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiresult);

//        mScoreText = (TextView) findViewById(R.id.MScoreResult);

//        textOne = (TextView) findViewById(R.id.MrankOne);
//        textTwo = (TextView) findViewById(R.id.MrankTwo);
//        textThree = (TextView) findViewById(R.id.MrankThree);
//        textFour = (TextView) findViewById(R.id.MrankFour);
//        textFive = (TextView) findViewById(R.id.MrankFive);

        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        ido = intent.getDoubleExtra("ido", 0);
        keido = intent.getDoubleExtra("keido", 0);

        send = (Button) findViewById(R.id.sendButton);

        battleB = (Button) findViewById(R.id.battleButton);
        battleB.setEnabled(false);

        // LocationManager インスタンス生成
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if(ido == 0 && keido == 0){
            startGPS();
            send.setEnabled(false);
        }

//        mScoreText.setText("Score : " + score);



        Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        vib.vibrate(2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ido == 0 && keido == 0){
            send.setEnabled(false);
        }else{
            send.setEnabled(true);
        }
    }

    //    private DatabaseReference getMessageRef() {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        return database.getReference(MESSAGE_STORE); // MESSAGE_STORE = "message"
//    }

    private void sendMessage() {
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
                }else{
                    toastMan();
                }
            }
        });


    }

    void toastMan(){
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

        send.setEnabled(true);
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
}
