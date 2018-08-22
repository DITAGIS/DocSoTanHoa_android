package com.ditagis.hcm.docsotanhoa.utities;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.entities.HoaDon;
import com.ditagis.hcm.docsotanhoa.utities.printUtities.PrinterCommands;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
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
    static final int LEFT_ALIGN = 0;
    private static final int CENTER_ALIGN = 1;
    private static final int RIGHT_ALIGN = 2;
    private static Printer instance = null;
    BluetoothDevice mBluetoothDevice;
    BluetoothAdapter mBluetoothAdapter;
    DateFormat formatter_old = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy");
    DateFormat formatter_day_of_week = new SimpleDateFormat("E");
    Calendar cal = Calendar.getInstance();
    private static OutputStream outputStream;
    private BluetoothSocket mBluetoothSocket;
    private Context mContext;
    private ProgressDialog mBluetoothConnectProgressDialog;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private String address;
    private String mTuNgay, mDenNgay, mStaffName, mStaffPhone;
    private HoaDon mHoaDon;
    private double mTienNuoc;
    private int mNam;

    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    InputStream mmInputStream;
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

    private void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
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

//    public void initialize(BluetoothDevice bluetoothDevice, BluetoothAdapter bluetoothAdapter, BluetoothSocket bluetoothSocket, Context context) {
//        this.mBluetoothDevice = bluetoothDevice;
//        this.address = this.mBluetoothDevice.getAddress();
//        this.mBluetoothAdapter = bluetoothAdapter;
//        this.mBluetoothSocket = bluetoothSocket;
//        this.mContext = context;
//    }

    public void initialize(BluetoothSocket bluetoothSocket, Context context) {
        this.mBluetoothSocket = bluetoothSocket;

        this.mContext = context;
        try {
            this.mmInputStream = mBluetoothSocket.getInputStream();
            beginListenForData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BluetoothAdapter getmBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    public void setmBluetoothAdapter(BluetoothAdapter mBluetoothAdapter) {
        this.mBluetoothAdapter = mBluetoothAdapter;
    }

//    public boolean print() {
//        try {
//            cal.setTime(formatter_old.parse(mHoaDon.getThoiGian()));
//            int[] dates = getDates(cal);
//            outputStream = mBluetoothSocket.getOutputStream();
//            byte[] Chuoi = {0x1b};
//            outputStream.write(("EZ\n" +
//                    "{PRINT:\n" +
//                    "@5,0:TIMNR,HMULT1,VMULT1|  CÔNG TY CPCN CHỢ LỚN|\n" +
//                    "@30,0:TIMNR,HMULT1,VMULT1|   97 Phạm Hữu Chí, P.12, Q.5|\n" +
//                    "@55,0:TIMNR,HMULT2,VMULT2|BIÊN NHẬN THU|\n" +
//                    "@100,0:TIMNR,HMULT2,VMULT2|    TIỀN NƯỚC|\n" +
//                    "@150,0:TIMNR,HMULT1,VMULT1|Kỳ:|\n" +
//                    "@150,300:TIMNR,HMULT1,VMULT1|7/2018|\n" +
//                    "@175,0:TIMNR,HMULT1,VMULT1|Từ ngày:|\n" +
//                    "@175,140:TIMNR,HMULT1,VMULT1|06/06/2018-06/07/2018|\n" +
//                    "@200,0:TIMNR,HMULT1,VMULT1|DB:|\n" +
//                    "@200,218:TIMNR,HMULT1,VMULT1|0614-679-0075|\n" +
//                    "@225,0:TIMNR,HMULT1,VMULT1|KH:|\n" +
//                    "@225,43:TIMNR,HMULT1,VMULT1|DAO THI THANH HANG|\n" +
//                    "@300,0:TIMNR,HMULT1,VMULT1|Đ/Chỉ:|\n" +
//                    "@300,76:TIMNR,HMULT1,VMULT1|79/34A17C TAN HOA DONG|\n" +
//                    "@325,0:TIMNR,HMULT1,VMULT1|GB:|\n" +
//                    "@325,44:TIMNR,HMULT1,VMULT1|11|\n" +
//                    "@325,110:TIMNR,HMULT1,VMULT1|DM:|\n" +
//                    "@325,154:TIMNR,HMULT1,VMULT1|44|\n" +
//                    "@350,0:TIMNR,HMULT1,VMULT1|Mlt:|\n" +
//                    "@350,275:TIMNR,HMULT1,VMULT1|1700113|\n" +
//                    "@375,0:TIMNR,HMULT1,VMULT1|Số HĐ:|\n" +
//                    "@375,88:TIMNR,HMULT1,VMULT1|20189108257|\n" +
//                    "@400,0:TIMNR,HMULT1,VMULT1|Cs Cũ:|\n" +
//                    "@400,320:TIMNR,HMULT1,VMULT1|167|\n" +
//                    "@425,0:TIMNR,HMULT1,VMULT1|Cs mới:|\n" +
//                    "@425,320:TIMNR,HMULT1,VMULT1|183|\n" +
//                    "@450,0:TIMNR,HMULT1,VMULT1|Tiêu thụ:|\n" +
//                    "@450,309:TIMNR,HMULT1,VMULT1|16m3|\n" +
//                    "}\n"
//            ).getBytes());
//            outputStream.write(Chuoi);
////            ESCPOSDriver escposDriver = new ESCPOSDriver(outputStream);
////            String msgLeft = "Left";
////            msgLeft += "\n";
////            String msgCenter = "Center";
////            msgCenter += "\n";
////            String msgRight = "Right";
////            msgRight += "\n";
////
////            //Initialize
////            escposDriver.initPrint();
////            escposDriver.changeFont( 1);
////            escposDriver.printLineText( msgLeft);
////            escposDriver.printLineAlignLeft( msgLeft);
////            escposDriver.changeFont( 2);
////            escposDriver.printLineAlignCenter( msgCenter);
////            escposDriver.changeFont( 3);
////            escposDriver.printLineAlignRight( msgRight);
////            escposDriver.printBarcode( "13031230034");
////
////            escposDriver.flushCommand();
//
//            outputStream.flush();
//
////            printNewLine();
//////            printCustom("CONG TY CPCN TAN HOA", 0, RIGHT_ALIGN);
//////            printCustom("CONG TY CPCN TAN HOA", 1, CENTER_ALIGN);
////            printCustom("CONG TY CPCN TAN HOA", 2, LEFT_ALIGN);
////            printCustom("CONG TY CPCN TAN HOA", 21, LEFT_ALIGN);
////            printCustom("CONG TY CPCN TAN HOA", 22, LEFT_ALIGN);
////            outputStream.write("ESC ".getBytes());
////            outputStream.write(InitializePrinter.getBytes());
////            outputStream.write("Here is some normal text.".getBytes());
////            outputStream.write((BoldOn + "Here is some bold text." + BoldOff).getBytes());
////            outputStream.write((DoubleOn + "Here is some large text." + DoubleOff).getBytes());
////            final String message = "Example message\n";
////// Default format:
////            writeWithFormat(message.getBytes(), new Formatter().get(), Formatter.leftAlign());
////// Bold format center:
////            writeWithFormat(message.getBytes(), new Formatter().bold().get(), Formatter.centerAlign());
////// Bold underlined format with right alignment:
////            writeWithFormat(message.getBytes(), new Formatter().bold().underlined().get(), Formatter.rightAlign());
////            printCustom("95 PHAM HUU CHI, P12, Q5", 0, CENTER_ALIGN);
////            printCustom("PHIEU BAO C.SO & TIEN NUOC DU KIEN", 2, CENTER_ALIGN);
//            printNewLine();
////            printBill();
//            return true;
//////
//        } catch (Exception e) {
//            Log.e("MainActivity", "Exe ", e);
//        }
//        return false;
//    }

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

            int y = 100;
            StringBuilder builder = new StringBuilder();
            builder.append("! 0 200 200 1000 1\n" +

                    "CENTER\n" +

                    "TEXT 0 1 0 0 CTY CP CAP NUOC TAN HOA\n" + //font 0,4,7
                    "TEXT 0 1 0 30 95 PHAM HUU CHI, P12, Q5\n" +
                    "TEXT 7 1 0 50 PHIEU BAO C.SO & TIEN NUOC DU KIEN\n");
            builder.append(String.format("TEXT 0 1 0 %d KY %s/%s\n", y, mHoaDon.getKy(), mNam + ""));
            y += 20;
            builder.append(String.format("TEXT 0 1 0 %d (%s * %s)\n", y, mHoaDon.getTuNgay(), mHoaDon.getDenNgay()));

            builder.append("TEXT 7 0 0 135 -------------------------------\n");
            y += 40;
            builder.append(String.format("BARCODE 128 1 1 50 0 %d %s\n", y, mHoaDon.getDanhBo()) +
                    "LEFT\n");
            y += 60;
            builder.append(String.format("TEXT 7 0 20 %d NHAN VIEN: %s\n", y, removeAccent(mStaffName)));
            y += 40;
            builder.append(String.format("TEXT 7 0 20 %d So dien thoai: %s\n", y, mStaffPhone));
            y += 40;
            builder.append(String.format("TEXT 7 0 20 %d KHACH HANG: %s\n", y, removeAccent(mHoaDon.getTenKhachHang())));
            y += 40;
            builder.append(String.format("TEXT 7 0 20 %d DIA CHI: %s\n", y, removeAccent(mHoaDon.getDiaChi())));
            y += 45;
            builder.append(String.format("TEXT 7 0 20 %d DANH BA: %s%11s%s\n", y, "             ", "MLT: ", ""));
            y -= 20;
            builder.append(String.format("TEXT 7 1 20 %d          %s%11s%s\n", y, spaceDB(mHoaDon.getDanhBo()), " ", spaceMLT(mHoaDon.getMaLoTrinh())));
            y += 50;
            builder.append(String.format("TEXT 7 0 20 %d GIA BIEU: %s - DINH MUC: %s m3\n", y, mHoaDon.getGiaBieu(), mHoaDon.getDinhMuc()));
            y += 35;
            builder.append(String.format("TEXT 7 0 70 %d CHI SO MOI%23s\n", y, mHoaDon.getChiSoMoi()));
            y += 35;
            builder.append(String.format("TEXT 7 0 70 %d CHI SO CU%24s\n", y, mHoaDon.getChiSoCu()));
            y += 40;
            builder.append(String.format("TEXT 7 0 70 %d TIEU THU\n", y, mHoaDon.getTieuThuMoi()));
            y -= 10;
            builder.append(String.format("TEXT 7 1 70 %d         %25s m3\n", y, mHoaDon.getTieuThuMoi()));
            y += 55;
            builder.append(String.format("TEXT 7 0 70 %d TIEN NUOC%24s VND\n", y, NumberFormat.getNumberInstance(Locale.US).format(mTienNuoc))); //.0f
            y += 35;

            double BVMT = 0, VAT = mTienNuoc / 20;
            if (!mHoaDon.getGiaBieu().equals("52"))
                BVMT = mTienNuoc / 10;
            builder.append(String.format("TEXT 7 0 70 %d PHI BVMT%25s VND\n", y, NumberFormat.getNumberInstance(Locale.US).format(BVMT)));
            y += 35;
            builder.append(String.format("TEXT 7 0 70 %d THUE VAT%25s VND\n", y, NumberFormat.getNumberInstance(Locale.US).format(VAT)));
            y += 35;
            builder.append(String.format("TEXT 7 0 230 %d %25s\n", y, "-----------"));
            y += 10;
            builder.append(String.format("TEXT 7 1 40 %d TONG CONG%27s VND\n", y, NumberFormat.getNumberInstance(Locale.US).format(mTienNuoc + BVMT + VAT)) +
                    "CENTER\n");
            y += 50;
            builder.append(String.format("TEXT 7 0 0 %d -------------------------------\n", y));
            y += 15;
            builder.append(String.format("TEXT 7 1 0 %d NGAY THU TIEN DU KIEN %s -> %s\n", y, String.format("%02d", dates[0]), String.format("%02d", dates[1])));
            y += 60;
            builder.append(String.format("TEXT 7 0 0 %d DIEN THOAI: 39 557 795 DE DUOC HUONG DAN\n", y));
            y += 40;
            builder.append(String.format("TEXT 7 1 0 %d PHIEU NAY KHONG CO GIA TRI THANH TOAN\n", y));
            y += 50;
            builder.append(String.format("TEXT 7 0 0 %d -------------------------------\n", y));
            y += 20;
            builder.append(String.format("TEXT 7 0 80 %d Printed on: %s\n", y, formatter.format(formatter_old.parse(mHoaDon.getThoiGian()))));
            y += 30;
            builder.append(String.format("TEXT 7 0 0 %d -------------------------------\n", y) +
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

    protected void printBill() {
        try {
            outputStream = mBluetoothSocket
                    .getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //print command
        try {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            byte[] printformat = new byte[]{0x1B, 0x21, 0x03};
            outputStream.write(printformat);


            printCustom("Fair Group BD", 2, 1);
            printCustom("Pepperoni Foods Ltd.", 0, 1);
//                printPhoto(R.drawable.ic_icon_pos);
            printCustom("H-123, R-123, Dhanmondi, Dhaka-1212", 0, 1);
            printCustom("Hot Line: +88000 000000", 0, 1);
            printCustom("Vat Reg : 0000000000,Mushak : 11", 0, 1);
            String dateTime[] = getDateTime();
            printText(leftRightAlign(dateTime[0], dateTime[1]));
            printText(leftRightAlign("Qty: Name", "Price "));
            printCustom(new String(new char[32]).replace("\0", "."), 0, 1);
            printText(leftRightAlign("Total", "2,0000/="));
            printNewLine();
            printCustom("Thank you for coming & we look", 0, 1);
            printCustom("forward to serve you again", 0, 1);
            printNewLine();
            printNewLine();

            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean writeWithFormat(byte[] buffer, final byte[] pFormat, final byte[] pAlignment) {
        try {
            // Notify printer it should be printed with given alignment:
            outputStream.write(pAlignment);
            // Notify printer it should be printed in the given format:
            outputStream.write(pFormat);
            // Write the actual data:
            outputStream.write(buffer, 0, buffer.length);

            // Share the sent message back to the UI Activity
//            App.getInstance().getHandler().obtainMessage(MESSAGE_WRITE, buffer.length, -1, buffer).sendToTarget();
            return true;
        } catch (IOException e) {
//            Log.e(TAG, "Exception during write", e);
            return false;
        }
    }

    //print custom
    private void printCustom(String msg, int size, int align) {
        //Print config "mode"
        byte[] cc = new byte[]{0x1B, 0x21, 0x03};  // 0- normal size text
        byte[] cc1 = new byte[]{0x1B, 0x21, 0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B, 0x21, 0x08};  // 1- only bold text

        byte[] bb2 = new byte[]{0x1B, 0x21, 0x10}; // 3- bold with large text
        byte[] bb21 = new byte[]{0x1B, 0x21, 0x15}; // 3- bold with large text
        byte[] bb22 = new byte[]{0x1B, 0x21, 0x18}; // 3- bold with large text
        byte[] bb3 = new byte[]{0x1B, 0x21, 0x20}; // 2- bold with medium text
        byte[] bb4 = new byte[]{0x1B, 0x21, 0x30}; // 2- bold with medium text
        try {

            switch (align) {
                case LEFT_ALIGN:
                    //left align
                    outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case CENTER_ALIGN:
                    //center align
                    outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case RIGHT_ALIGN:
                    //right align
                    outputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
            switch (size) {
                case 0:
                    outputStream.write(cc1);
                    break;
                case 1:
                    outputStream.write(bb);
                    break;
                case 2:
                    outputStream.write(bb2);
                    break;
                case 21:
                    outputStream.write(bb21);
                    break;
                case 22:
                    outputStream.write(bb22);
                    break;
                case 3:
                    outputStream.write(bb3);
                    break;
                case 4:
                    outputStream.write(bb4);
                    break;
            }

            outputStream.write(msg.getBytes());
            outputStream.write(PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //print photo
    public void printPhoto(int img) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(),
                    img);
            if (bmp != null) {
                byte[] command = Utils.decodeBitmap(bmp);
                outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(command);
            } else {
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    //print unicode
    public void printUnicode() {
        try {
            outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(Utils.UNICODE_TEXT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //print new line
    private void printNewLine() {
        try {
            outputStream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void resetPrint() {
        try {
            outputStream.write(PrinterCommands.ESC_FONT_COLOR_DEFAULT);
            outputStream.write(PrinterCommands.FS_FONT_ALIGN);
            outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
            outputStream.write(PrinterCommands.ESC_CANCEL_BOLD);
            outputStream.write(PrinterCommands.LF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //print text
    private void printText(String msg) {
        try {
            // Print normal text
            outputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //print byte[]
    private void printText(byte[] msg) {
        try {
            // Print normal text
            outputStream.write(msg);
            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String leftRightAlign(String str1, String str2) {
        String ans = str1 + str2;
        if (ans.length() < 31) {
            int n = (31 - str1.length() + str2.length());
            ans = str1 + new String(new char[n]).replace("\0", " ") + str2;
        }
        return ans;
    }


    private String[] getDateTime() {
        final Calendar c = Calendar.getInstance();
        String dateTime[] = new String[2];
        dateTime[0] = c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR);
        dateTime[1] = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        return dateTime;
    }

}
