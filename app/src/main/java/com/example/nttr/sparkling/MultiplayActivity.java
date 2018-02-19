package com.example.nttr.sparkling;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MultiplayActivity extends Activity implements SensorEventListener, LocationListener{

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

    String place = "place";

    private LocationManager locationManager;

    double ido = 0;
    double keido = 0;

    boolean start = false;

    private static final int MinTime = 1000;
    private static final float MinDistance = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplay);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mAccSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mTextX = (TextView) findViewById(R.id.MtextX);
        mTextY = (TextView) findViewById(R.id.MtextY);
        mTextZ = (TextView) findViewById(R.id.MtextZ);
        mScoreText = (TextView) findViewById(R.id.MscoreText);
        mTimeText = (TextView) findViewById(R.id.MtimeText);

        // LocationManager インスタンス生成
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        long countNumber = 12100;
        long interval = 10;
        countDown = new CountDown(countNumber, interval, this);
        countDown.start();
        startGPS();
    }

    // 加速度センサーの値に変化があった時に呼ばれる
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(start){
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

        if (locationManager != null) {
            Log.d("LocationActivity", "locationManager.removeUpdates");
            // update を止める
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) !=
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
            locationManager.removeUpdates(this);
        } else {
            Log.d("Sp","onPause");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            countDown.cancel();
        }catch (NullPointerException e){
            Log.d("Ecatch", "nullpo");
        }

        try{
            stopGPS();
        }catch (NullPointerException e){
            Log.d("Ecatch", "nullpo");
        }

        try{
            mSensorManager.unregisterListener(this);
        }catch (NullPointerException e){
            Log.d("Ecatch", "nullpo");
        }
    }

    protected void startGPS() {
        Log.d("MultiplayActivity", "gpsEnabled");
        final boolean gpsEnabled
                = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!gpsEnabled) {
            // GPSを設定するように促す
            Log.d("kokoni","konaihazu");
            enableLocationSettings();
        }

        if (locationManager != null) {
            Log.d("LocationActivity", "locationManager.requestLocationUpdates");
            // バックグラウンドから戻ってしまうと例外が発生する場合がある
            try {
                // minTime = 1000msec, minDistance = 50m
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)!=
                        PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this,
                                Manifest.permission.ACCESS_COARSE_LOCATION)!=
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

    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }

    private void stopGPS(){
        if (locationManager != null) {
            Log.d("LocationActivity", "onStop()");
            // update を止める
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) !=
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
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.d("Sp","LocationProvider.AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("Sp","LocationProvider.OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("Sp","LocationProvider.TEMPORARILY_UNAVAILABLE");
                break;
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("pro", "pro_en");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("pro", "pro_dis");
    }

    class CountDown extends CountDownTimer {

        MultiplayActivity multiplayActivity;

        CountDown(long millisInFuture, long countDownInterval, MultiplayActivity multiplayActivity) {
            super(millisInFuture, countDownInterval);
            this.multiplayActivity = multiplayActivity;
        }

        @Override
        public void onFinish() {
            // 完了
            // 画面繊維
            finish();
            Intent intent = new Intent(multiplayActivity, MultiresultActivity.class);
            intent.putExtra("score", score);
            intent.putExtra("ido", ido);
            intent.putExtra("keido", keido);
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

            if(millisUntilFinished <= 10000){
                start = true;
                mTimeText.setText("" + millisUntilFinished);
            }else if (millisUntilFinished <= 10700){
                mTimeText.setText("開始まで 1");
            }else if (millisUntilFinished <= 11400){
                mTimeText.setText("開始まで 2");
            }else if (millisUntilFinished <= 12500){
                mTimeText.setText("開始まで 3");
            }else{
                Log.d("spark", "なんすか");
            }
        }
    }
}
