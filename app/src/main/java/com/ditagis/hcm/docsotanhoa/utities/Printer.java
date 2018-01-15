package com.ditagis.hcm.docsotanhoa.utities;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.entities.HoaDon;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
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
    DateFormat formatter_day_of_week = new SimpleDateFormat("E");
    Calendar cal = Calendar.getInstance();

    private BluetoothSocket mBluetoothSocket;
    private Context mContext;
    private ProgressDialog mBluetoothConnectProgressDialog;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private String mTuNgay, mDenNgay, mStaffName, mStaffPhone;
    private HoaDon mHoaDon;
    private double mTienNuoc;
    private int mNam;
    //    DecimalFormat df = new DecimalFormat("###.###.###,###");
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

    public BluetoothSocket getmBluetoothSocket() {
        return mBluetoothSocket;
    }

    public void setValue(int nam, String staffName, String staffPhone, HoaDon hoaDon, double tienNuoc) {
        mNam = nam;
        mStaffName = staffName;
        mStaffPhone = staffPhone;
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

    public boolean print() {
//        Thread t = new Thread() {
//            public void run() {
        try {
//            mBluetoothSocket = mBluetoothDevice
//                    .createRfcommSocketToServiceRecord(applicationUUID);
//            mBluetoothAdapter.cancelDiscovery();
//            mBluetoothSocket.connect();
//            mHandler.sendEmptyMessage(0);
            NumberFormat.getNumberInstance(Locale.CANADA).format(35634646);
            OutputStream os = mBluetoothSocket
                    .getOutputStream();
            cal.setTime(formatter_old.parse(mHoaDon.getThoiGian()));
            int[] dates = getDates(cal);

            int y = 160;
            StringBuilder builder = new StringBuilder();
            builder.append("! 0 200 200 1090 1\n" +

                    "CENTER\n" +
                    "TEXT 0 1 0 30 ------------------------------------\n" +
                    "TEXT 0 1 0 50 CTY CP CAP NUOC TAN HOA\n" + //font 0,4,7
                    "TEXT 0 1 0 80 95 PHAM HUU CHI, P12, Q5\n" +
                    "TEXT 7 1 0 100 PHIEU BAO C.SO & TIEN NUOC DU KIEN\n");
            builder.append(String.format("TEXT 0 1 0 %d KY %s/%s\n", y, mHoaDon.getKy(), mNam + ""));
            y += 20;
            builder.append(String.format("TEXT 0 1 0 %d (%s * %s)\n", y, mHoaDon.getTuNgay(), mHoaDon.getDenNgay()));

            builder.append("TEXT 7 0 0 200 ------------------------\n");
            y += 40;
            builder.append(String.format("BARCODE 128 1 1 50 0 %d %s\n", y, mHoaDon.getDanhBo()) +
                    "LEFT\n");
            y += 60;
            builder.append(String.format("TEXT 7 0 20 %d NHAN VIEN: %s\n", y, removeAccent(mStaffName)));
            y += 40;
            builder.append(String.format("TEXT 7 0 20 %d So dien thoai: %s\n", y, mStaffPhone));
            y += 40;
            builder.append(String.format("TEXT 7 0 20 %d KHACH HANG: %s\n", y,removeAccent(mHoaDon.getTenKhachHang())));
            y += 40;
            builder.append(String.format("TEXT 7 0 20 %d DIA CHI: %s\n", y, removeAccent(mHoaDon.getDiaChi())));
            y += 35;
            builder.append(String.format("TEXT 7 1 20 %d DANH BA: %s%11s%s\n", y, spaceDB(mHoaDon.getDanhBo()), "MLT: ", spaceMLT(mHoaDon.getMaLoTrinh())));
            y += 50;
            builder.append(String.format("TEXT 7 0 20 %d GIA BIEU: %s - DINH MUC: %s m3\n", y, mHoaDon.getGiaBieu(), mHoaDon.getDinhMuc()));
            y += 35;
            builder.append(String.format("TEXT 7 0 70 %d CHI SO MOI%23s\n", y, mHoaDon.getChiSoMoi()));
            y += 35;
            builder.append(String.format("TEXT 7 0 70 %d CHI SO CU%24s\n", y, mHoaDon.getChiSoCu()));
            y += 45;
            builder.append(String.format("TEXT 7 0 70 %d TIEU THU\n", y, mHoaDon.getTieuThuMoi()));
            builder.append(String.format("TEXT 7 1 70 %d         %25s m3\n", y, mHoaDon.getTieuThuMoi()));
            y += 55;
            builder.append(String.format("TEXT 7 0 70 %d TIEN NUOC%24s VND\n", y, NumberFormat.getNumberInstance(Locale.US).format(mTienNuoc))); //.0f
            y += 35;
            builder.append(String.format("TEXT 7 0 70 %d PHI BVMT%25s VND\n", y, NumberFormat.getNumberInstance(Locale.US).format(mTienNuoc / 10)));
            y += 35;
            builder.append(String.format("TEXT 7 0 70 %d THUE VAT%25s VND\n", y, NumberFormat.getNumberInstance(Locale.US).format(mTienNuoc / 20)));
            y += 35;
            builder.append(String.format("TEXT 7 0 230 %d %25s\n", y, "------------"));
            y += 10;
            builder.append(String.format("TEXT 7 1 40 %d TONG CONG%27s VND\n", y, NumberFormat.getNumberInstance(Locale.US).format(mTienNuoc * 115 / 100)) +
                    "CENTER\n");
            y += 50;
            builder.append(String.format("TEXT 7 0 0 %d ------------------------\n", y));
            y += 15;
            builder.append(String.format("TEXT 7 1 0 %d NGAY THU TIEN DU KIEN %s - %s\n", y, dates[0] + "", dates[1] + ""));
            y += 60;
            builder.append(String.format("TEXT 7 0 0 %d DIEN THOAI: 39 557 795 DE DUOC HUONG DAN\n", y));
            y += 40;
            builder.append(String.format("TEXT 7 1 0 %d PHIEU NAY KHONG CO GIA TRI THANH TOAN\n", y));
            y += 50;
            builder.append(String.format("TEXT 7 0 0 %d ------------------------\n", y));
            y += 20;
            builder.append(String.format("TEXT 7 0 80 %d Printed on: %s\n", y, formatter.format(formatter_old.parse(mHoaDon.getThoiGian()))));
            y += 48;
            builder.append(String.format("TEXT 7 0 0 %d ------------------------------\n", y) +
//                                    "FORM\n" +
                    "PRINT\n");

            os.write(builder.toString().getBytes());
            return true;
////
        } catch (Exception e) {
            Log.e("MainActivity", "Exe ", e);
        }
        return false;
    }

    private int[] getDates(Calendar cal) {
        int[] dates = new int[2];
        cal.add(Calendar.DATE, 4);
        while (cal.get(Calendar.DAY_OF_WEEK) > 6 || cal.get(Calendar.DAY_OF_WEEK) < 2)
            cal.add(Calendar.DATE, 1);
        dates[0] = cal.get(Calendar.DATE);


        cal.add(Calendar.DATE, 1);
        while (cal.get(Calendar.DAY_OF_WEEK) > 6 || cal.get(Calendar.DAY_OF_WEEK) < 2)
            cal.add(Calendar.DATE, 1);
        dates[1] = cal.get(Calendar.DATE);
        return dates;
    }

    public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }

    private String spaceMLT(String mlt) {
        String output = "";
        output = (mlt.substring(0, 2)).concat(" ").concat(mlt.substring(2, 4)).concat(" ").concat(mlt.substring(4));
        return output;
    }

    private String spaceDB(String danhBo) {
        String output = "";
        output = (danhBo.substring(0, 4)).concat(" ").concat(danhBo.substring(4, 7)).concat(" ").concat(danhBo.substring(7));
        return output;
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
