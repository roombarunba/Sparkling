package com.example.nttr.sparkling;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TitleActivity extends AppCompatActivity implements View.OnClickListener{
    
    Button mStartButton;
    Button mGPSButton;

    private final int REQUEST_PERMISSION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        
        mStartButton = (Button) findViewById(R.id.startButton);
        mGPSButton = (Button) findViewById(R.id.buttonGPS);
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
        Intent intent = new Intent(getApplication(), MultiplayActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }
}
