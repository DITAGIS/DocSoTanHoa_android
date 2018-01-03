package com.ditagis.hcm.docsotanhoa.utities;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by ThanLe on 1/3/2018.
 */

public class Printer {

    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    BluetoothAdapter mBluetoothAdapter;
    private Context mContext;
    private ProgressDialog mBluetoothConnectProgressDialog;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static Printer instance = null;

    public static Printer getInstance() {
        if (instance == null)
            instance = new Printer();
        return instance;
    }

    public void initialize(BluetoothDevice bluetoothDevice, BluetoothAdapter bluetoothAdapter, Context context) {
        this.mBluetoothDevice = bluetoothDevice;
        this.mBluetoothAdapter = bluetoothAdapter;
        this.mContext = context;
    }
    public void initialize(BluetoothSocket bluetoothSocket, Context context) {
      this.mBluetoothSocket = bluetoothSocket;
        this.mContext = context;
    }
    private Printer() {
    }

    public BluetoothAdapter getmBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    public void setmBluetoothAdapter(BluetoothAdapter mBluetoothAdapter) {
        this.mBluetoothAdapter = mBluetoothAdapter;
    }

    public void print() {
//        Thread t = new Thread() {
//            public void run() {
        try {
//            mBluetoothSocket = mBluetoothDevice
//                    .createRfcommSocketToServiceRecord(applicationUUID);
//            mBluetoothAdapter.cancelDiscovery();
//            mBluetoothSocket.connect();
//            mHandler.sendEmptyMessage(0);
            OutputStream os = mBluetoothSocket
                    .getOutputStream();
            String test = "! 0 200 200 210 1\r\n"
                    + "TEXT 4 0 30 40 This is a CPCL test.\r\n"  //type, font, size, x position, y position, data
//                            + "FORM\r\n"
                    + "PRINT\r\n";
            String test1 =
                    "! 0 200 200 310 1\n" +
                            "CENTER\n" +
                            "TEXT 4 1 0 50 RECEIPT THAN\n" +
                            "TEXT 4 0 0 150 \\\\\n" +

//                            "FORM\n" +
                            "PRINT\n";
            String test2 = "! 0 200 200 210 1\r\n" +

                    "CENTER\r\n" +

                    "BARCODE 128 1 1 50 0 130 123456789\r\n" +
                    "PRINT\r\n";
            os.write(test2.getBytes());

//                    String BILL = "";
//
//                    BILL = "-------------------------------\\n"
//                            + " CTY CP CAP NUOC TAN HOA "
//                            + "        95 PHAM HUU CHI, P12, Q5\n"
//                            + "  PHIEU BAO C.SO &  TIEN NUOC DU KIEN     \n " +
//                            "           KY 11/2017    \n" +
//                            "   (16/11/2017 * 15/12/2017)   \n" +
//                            "        --------------------     \n";
//                                    os.write(BILL.getBytes());
////
        } catch (Exception e) {
            Log.e("MainActivity", "Exe ", e);
        }
//            }
//        };
//        t.start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(mContext.getApplicationContext(), "Đã kết nối", Toast.LENGTH_SHORT).show();
        }
    };


    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
//            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
//            Log.d(TAG, "CouldNotCloseSocket");
        }
    }
}
