package com.ditagis.hcm.docsotanhoa;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by ThanLe on 12/20/2017.
 */

public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    protected static String upcCodeValue;
    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_scanner);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(com.google.zxing.Result rawResult) {
        // Do something with the result here
        Log.v("result", rawResult.getText()); // Prints scan results
        Log.v("Scanner", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
        Intent data = new Intent();
        upcCodeValue = rawResult.getText();
        data.setData(Uri.parse(upcCodeValue));
        mScannerView.stopCamera();
        setResult(RESULT_OK, data);
        this.finish();
    }
}
