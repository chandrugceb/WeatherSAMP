package com.example.chand.weathersamp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by chand on 13-08-2017.
 */

public class InstallActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler
{

    Intent InstallIntent;
    TextView tvRoutineName;
    ImageView ivRoutineIcon;

    private ZXingScannerView zXingScannerView;

    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install);
        InstallIntent = this.getIntent();

        tvRoutineName = (TextView)findViewById(R.id.tvIcon_name);
        tvRoutineName.setText(InstallIntent.getStringExtra("IconText"));

        ivRoutineIcon = (ImageView)findViewById(R.id.ivIcon_Image);
        ivRoutineIcon.setImageResource(InstallIntent.getIntExtra("IconId", R.drawable.install_icon));

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }
        /*ivRoutineIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ScanIntent = new Intent(InstallActivity.this, ScanActivity.class);
                startActivityForResult(ScanIntent, REQUEST_CODE);
            }
        });*/
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    public void scan(View view)
    {
        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        Toast.makeText(getApplicationContext(), result.getText(),Toast.LENGTH_LONG).show();
        //zXingScannerView.resumeCameraPreview(this);
        zXingScannerView.stopCamera();
        finish();
    }
}
