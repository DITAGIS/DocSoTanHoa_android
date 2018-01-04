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

import com.ditagis.hcm.docsotanhoa.entities.HoaDon;

import java.io.IOException;
import java.io.OutputStream;
import java.text.Normalizer;
import java.util.UUID;
import java.util.regex.Pattern;

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
    private  String mTuNgay, mDenNgay, mStaffName, mStaffSdt;
    private  HoaDon mHoaDon;
    private  double mTienNuoc;

    public static Printer getInstance() {
        if (instance == null)
            instance = new Printer();
        return instance;
    }

    public void setValue(String tuNgay, String denNgay, String staffName, String staffSdt, HoaDon hoaDon, double tienNuoc) {
        mTuNgay = tuNgay;
        mDenNgay = denNgay;
        mStaffName = staffName;
        mStaffSdt = staffSdt;
        mHoaDon = hoaDon;
        mTienNuoc = tienNuoc;
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

            String test2 = "! 0 200 200 930 1\n" +

                    "CENTER\n" +
                    "TEXT 0 1 0 50 ------------------------------------\n" +
                    "TEXT 0 1 0 70 CTY CP CAP NUOC TAN HOA\n" + //font 0,4,7
                    "TEXT 0 1 0 90 95 PHAM HUU CHI, P12, Q5\n" +
                    "TEXT 7 1 0 100 PHIEU BAO C.SO & TIEN NUOC DU KIEN\n" +
                    String.format("TEXT 0 1 0 160 KY %s/%s\n", mHoaDon.getKy(), mHoaDon.getNam() + "") +
                    String.format("TEXT 0 1 0 180 (%s * %s)\n", mTuNgay, mDenNgay) +
                    "TEXT 0 1 0 200 ------------------------\n" +
                    String.format("BARCODE 128 1 1 50 0 220 %s\n", mHoaDon.getDanhBo()) +
                    "LEFT\n" +
                    String.format("TEXT 7 0 20 280 NHAN VIEN: %s - %s\n", removeAccent(mStaffName), mStaffSdt) +
                    String.format("TEXT 7 0 20 310 KHACH HANG: %s\n", mHoaDon.getTenKhachHang()) +
                    String.format("TEXT 7 0 20 340 DIA CHI: %s\n", mHoaDon.getDiaChi()) +
                    String.format("TEXT 7 1 20 370 DANH BA: %s%15s%s\n", mHoaDon.getDanhBo(), "MLT: ", mHoaDon.getMaLoTrinh()) +
                    String.format("TEXT 7 0 20 420 GIA BIEU: %s - DINH MUC: %sm3\n", mHoaDon.getGiaBieu(), mHoaDon.getDinhMuc()) +

                    String.format("TEXT 7 0 70 450 CHI SO MOI%23s\n", mHoaDon.getChiSoMoi()) +
                    String.format("TEXT 7 0 70 480 CHI SO CU%24s\n", mHoaDon.getChiSoCu()) +
                    String.format("TEXT 7 0 70 510 TIEU THU%23sm3\n", mHoaDon.getTieuThuMoi()) +
                    String.format("TEXT 7 0 70 540 TIEN NUOC%21.0fVND\n", mTienNuoc) +
                    String.format("TEXT 7 0 70 570 PHI BVMT%22.0fVND\n", mTienNuoc / 10) +
                    String.format("TEXT 7 0 70 600 THUE VAT%22.0fVND\n", mTienNuoc / 20) +
                    String.format("TEXT 0 1 70 630 %25s\n", "------------") +

                    String.format("TEXT 7 1 40 640 TONG CONG%24.0fVND\n", mTienNuoc * 115 / 100) +
                    "CENTER\n" +
                    "TEXT 0 1 0 690 ------------------------\n" +

                    "TEXT 7 1 0 700 NGAY THU TIEN DU KIEN 22 - 23\n" +
                    "TEXT 7 0 0 750 DIEN THOAI: 39 557 795 DE DUOC HUONG DAN\n" +
                    "TEXT 7 1 0 770 PHIEU NAY KHONG CO GIA TRI THANH TOAN\n" +
                    "TEXT 0 1 0 820 ------------------------\n" +
                    String.format("TEXT 7 0 80 830 Printed on: %s\n", mHoaDon.getThoiGian()) +
//                                    "FORM\n" +
                    "PRINT\n";

            os.write(test2.getBytes());
////
        } catch (Exception e) {
            Log.e("MainActivity", "Exe ", e);
        }
//            }
//        };
//        t.start();
    }
    private String removeAccent(String s) {

        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
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
