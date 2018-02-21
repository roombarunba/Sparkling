package com.example.nttr.sparkling;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TitleActivity extends Activity implements View.OnClickListener{
    
    Button mStartButton;
    Button mGPSButton;

    private final int REQUEST_PERMISSION = 1000;

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        mStartButton = (Button) findViewById(R.id.startButton);
        mGPSButton = (Button) findViewById(R.id.buttonGPS);
//        asobu = (Button) findViewById(R.id.asobikata);
//        priv = (Button) findViewById(R.id.priv);
//        toRan = (Button) findViewById(R.id.toRanking);
    }

    public void startGame(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void startGPS(View v){
        // permission
        // Android 6, API 23以上でパーミッシンの確認
        if(Build.VERSION.SDK_INT >= 23){
            checkPermission();
        }
        else {
            locationActivity();
        }
    }

    // 位置情報許可の確認
    public void checkPermission() {
        // 既に許可している
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){

            locationActivity();
        }
        // 拒否していた場合
        else{
            requestLocationPermission();
        }
    }

    // 許可を求める
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(TitleActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSION);

        } else {
            Toast toast = Toast.makeText(this,
                    "許可されないとアプリが実行できませんーこのルートは通らないっぽい", Toast.LENGTH_SHORT);
            toast.show();

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,},
                    REQUEST_PERMISSION);

        }
    }

    // 結果の受け取り
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            // 使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationActivity();

            } else {
                // それでも拒否された時の対応
                Toast toast = Toast.makeText(this,
                        "位置情報へのアクセス許可がない場合はこのモードでは遊べません", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    // Intent でLocation
    private void locationActivity() {
        // LocationManager インスタンス生成
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        final boolean gpsEnabled
                = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!gpsEnabled) {
            // GPSを設定するように促す
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("せつめいしょ")
                    .setMessage("このモードで遊ぶためには、\n\n"
                            + "①　GPSをON\n" + "②　位置情報をWi-Fi、Bluethooth、モバイルネットワークから特定可能\n\n"
                            + "に設定する必要があります\n\n※通信環境もご確認ください")
                    .setPositiveButton("設定画面へ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            enableLocationSettings();
                        }
                    })
                    .create();
            alertDialog.show();
        }else{
            Intent intent = new Intent(getApplication(), MultiplayActivity.class);
            startActivity(intent);
        }
    }

    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }

    public void asobikata(View v){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("あそびかた")
                .setMessage("端末を炭酸ジュースに見立てて10秒間おもいっっっきり振ろう！\nより激しく振ってより激しく炭酸ジュースを爆発させた人の勝ちだ！！\n\n" +
                        "『ひとりであそぶ』モードでは世界規模のランキング戦！！\n\n" +
                        "『みんなであそぶ』モードでは、近くにいる人とのランキング戦が楽しめるぞ！！\n\n" +
                        "※「みんなであそぶ」モードでは\n" +
                        "①　GPSをON\n" + "②　位置情報をWi-Fi、Bluethooth、モバイルネットワークから特定可能\n"
                        + "に設定する必要があります\n\n" +
                        "※「みんなであそぶ」モード終了後30秒以内に再び「みんなであそぶ」モードを遊ぶと前回の記録が残る場合があります。その場合は昔の自分とも戦ってください。\n\n"
                        + "※ゲーム中は、端末を振ってぶつかる範囲に人がいないことを確認し、端末をしっかり握って遊んでください。\nこれを守らずに生じたいかなる損害に対しても責任を負いかねます。")
                .setPositiveButton("わかった！！！！", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        alertDialog.show();
    }

    public void priva(View v){
        Intent intent = new Intent(this, PrivacyActivity.class);
        startActivity(intent);
    }

    public void toRan(View v){
        Intent intent = new Intent(this, RankingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }
}
