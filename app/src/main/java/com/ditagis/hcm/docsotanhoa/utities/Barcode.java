package com.ditagis.hcm.docsotanhoa.utities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.MainActivity;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by ThanLe on 12/19/2017.
 */

public class Barcode {
    private static ZXingScannerView scannerView;
    private static Context mContext;
    private static View mRootView;
    private static Activity mActivity;
    private static AlertDialog.Builder mBuilder;
    private static AlertDialog mDialog;

    private Barcode() {

    }

    private static Barcode mInstance;

    public static Barcode getInstance(Activity activity, View rootView) {
        mActivity = activity;
        mContext = mActivity.getApplicationContext();
        mRootView = rootView;
        if (mInstance == null)
            mInstance = new Barcode();
        return mInstance;
    }

    public static void scanCode() {
        LayoutInflater inflater = LayoutInflater.from(mRootView.getContext());
        scannerView = new ZXingScannerView(mContext);
        scannerView.setResultHandler(new ZXingScannerResultHandle());
        mActivity.setContentView(scannerView);

//        mBuilder = new AlertDialog.Builder(mContext);
//
//        mBuilder.setView(dialogLayout);
//        mDialog = mBuilder.create();
//        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        mDialog.show();
        scannerView.startCamera();
    }

    public static class ZXingScannerResultHandle implements ZXingScannerView.ResultHandler {

        @Override
        public void handleResult(Result result) {
            String resultCode = result.getText();
            if (resultCode != null)

                Toast.makeText(mContext, resultCode, Toast.LENGTH_LONG).show();
//            mDialog.dismiss();
//            mActivity.setContentView(R.layout.activity_main);
//            setContentView(R.layout.activity_main);
            mActivity.startActivity(new Intent(mActivity,MainActivity.class));
            mActivity.finish();
            scannerView.stopCamera();
        }
    }
}
