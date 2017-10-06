package com.example.chand.weathersamp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by chand on 13-08-2017.
 */

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler
{
    private ZXingScannerView zXingScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
     super.onCreate(savedInstanceState);
     zXingScannerView = new ZXingScannerView(this);
     setContentView(R.layout.activity_scan);
     ViewGroup contentFrame = (ViewGroup)findViewById(R.id.content_frame);
     contentFrame.addView(zXingScannerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        Intent intent = new Intent();
        intent.putExtra("scannedText", result.getText());
        //Toast.makeText(getApplicationContext(), result.getText() + "Scan .............",Toast.LENGTH_LONG).show();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
