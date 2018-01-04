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
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by ThanLe on 1/3/2018.
 */

public class Printer {

    private static Printer instance = null;
    BluetoothDevice mBluetoothDevice;
    BluetoothAdapter mBluetoothAdapter;
    DateFormat formatter_old = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy");
    private BluetoothSocket mBluetoothSocket;
    private Context mContext;
    private ProgressDialog mBluetoothConnectProgressDialog;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private String mTuNgay, mDenNgay, mStaffName, mStaffSdt;
    private HoaDon mHoaDon;
    private double mTienNuoc;
    private int mNam;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(mContext.getApplicationContext(), "Đã kết nối", Toast.LENGTH_SHORT).show();
        }
    };

    private Printer() {
    }

    public static Printer getInstance() {
        if (instance == null)
            instance = new Printer();
        return instance;
    }

    public void setValue(int nam, String staffName, String staffSdt, HoaDon hoaDon, double tienNuoc) {
        mNam = nam;
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
//            DecimalFormat decimalFormat = new DecimalFormat("#. ###");
            int y = 160;
            StringBuilder builder = new StringBuilder();
            builder.append("! 0 200 200 960 1\n" +

                    "CENTER\n" +
                    "TEXT 0 1 0 50 ------------------------------------\n" +
                    "TEXT 0 1 0 70 CTY CP CAP NUOC TAN HOA\n" + //font 0,4,7
                    "TEXT 0 1 0 90 95 PHAM HUU CHI, P12, Q5\n" +
                    "TEXT 7 1 0 100 PHIEU BAO C.SO & TIEN NUOC DU KIEN\n");
            builder.append(String.format("TEXT 0 1 0 %d KY %s/%s\n", y, mHoaDon.getKy(), mNam + ""));
            y += 20;
            builder.append(String.format("TEXT 0 1 0 %d (%s * %s)\n", y, mHoaDon.getTuNgay(), mHoaDon.getDenNgay()));

            builder.append("TEXT 0 1 0 200 ------------------------\n");
            y += 30;
            builder.append(String.format("BARCODE 128 1 1 50 0 %d %s\n", y, mHoaDon.getDanhBo()) +
                    "LEFT\n");
            y += 60;
            builder.append(String.format("TEXT 7 0 20 %d NHAN VIEN: %s - %s\n", y, removeAccent(mStaffName), mStaffSdt));
            y += 40;
            builder.append(String.format("TEXT 7 0 20 %d KHACH HANG: %s\n", y, mHoaDon.getTenKhachHang()));
            y += 40;
            builder.append(String.format("TEXT 7 0 20 %d DIA CHI: %s\n", y, mHoaDon.getDiaChi()));
            y += 40;
            builder.append(String.format("TEXT 7 1 20 %d DANH BA: %s%15s%s\n", y, mHoaDon.getDanhBo(), "MLT: ", mHoaDon.getMaLoTrinh()));
            y += 50;
            builder.append(String.format("TEXT 7 0 20 %d GIA BIEU: %s - DINH MUC: %sm3\n", y, mHoaDon.getGiaBieu(), mHoaDon.getDinhMuc()));
            y += 30;
            builder.append(String.format("TEXT 7 0 70 %d CHI SO MOI%23s\n", y, mHoaDon.getChiSoMoi()));
            y += 30;
            builder.append(String.format("TEXT 7 0 70 %d CHI SO CU%24s\n", y, mHoaDon.getChiSoCu()));
            y += 30;
            builder.append(String.format("TEXT 7 0 70 %d TIEU THU%23sm3\n", y, mHoaDon.getTieuThuMoi()));
            y += 30;
            builder.append(String.format("TEXT 7 0 70 %d TIEN NUOC%21.0f VND\n", y, mTienNuoc));
            y += 30;
            builder.append(String.format("TEXT 7 0 70 %d PHI BVMT%22.0f VND\n", y, mTienNuoc / 10));
            y += 30;
            builder.append(String.format("TEXT 7 0 70 %d THUE VAT%22.0f VND\n", y, mTienNuoc / 20));
            y += 30;
            builder.append(String.format("TEXT 0 1 70 %d %25s\n", y, "------------"));
            y += 10;
            builder.append(String.format("TEXT 7 1 40 %d TONG CONG%24.0f VND\n", y, mTienNuoc * 115 / 100) +
                    "CENTER\n");
            y += 50;
            builder.append(String.format("TEXT 0 1 0 %d ------------------------\n", y));
            y += 10;
            builder.append(String.format("TEXT 7 1 0 %d NGAY THU TIEN DU KIEN 22 - 23\n", y));
            y += 60;
            builder.append(String.format("TEXT 7 0 0 %d DIEN THOAI: 39 557 795 DE DUOC HUONG DAN\n", y));
            y += 30;
            builder.append(String.format("TEXT 7 1 0 %d PHIEU NAY KHONG CO GIA TRI THANH TOAN\n", y));
            y += 60;
            builder.append(String.format("TEXT 0 1 0 %d ------------------------\n", y));
            y += 20;
            builder.append(String.format("TEXT 7 0 80 %d Printed on: %s\n", y, formatter.format(formatter_old.parse(mHoaDon.getThoiGian()))) +
//                                    "FORM\n" +
                    "PRINT\n");

            os.write(builder.toString().getBytes());
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

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
//            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
//            Log.d(TAG, "CouldNotCloseSocket");
        }
    }
}
