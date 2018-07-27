package com.ditagis.hcm.docsotanhoa;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class Main2Activity extends AppCompatActivity {
    // will show the statuses like bluetooth open, close or data sent
    TextView myLabel;

    // will enable user to enter any text to be printed
    EditText myTextbox;

    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button openButton = (Button) findViewById(R.id.open);
        Button sendButton = (Button) findViewById(R.id.send);
        Button closeButton = (Button) findViewById(R.id.close);
// text label and input box
        myLabel = (TextView) findViewById(R.id.label);
        myTextbox = (EditText) findViewById(R.id.entry);
        try {
            // more codes will be here
            openButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        findBT();
                        openBT();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            // send data typed by the user to be printed
            sendButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        sendData();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            // close bluetooth connection
            closeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        closeBT();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // this will find a bluetooth printer device
    void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                myLabel.setText("No bluetooth adapter available");
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName().equals("PR2-000780A8EDF5")) {
                        mmDevice = device;
                        break;
                    }
                }
            }

            myLabel.setText("Bluetooth device found.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // tries to open a connection to the bluetooth printer device
    void openBT() throws IOException {
        try {

            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

            myLabel.setText("Bluetooth Opened");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * after opening a connection to bluetooth printer device,
     * we have to listen and check if a data were sent to be printed.
     */
    void beginListenForData() {
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
                                                myLabel.setText(data);
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

    // this will send text data to be printed by the bluetooth printer
    void sendDataFinally() throws IOException {
        try {

            // the text typed by the user
            byte[] Chuoi = {0x1b};
            String msg = myTextbox.getText().toString();
            msg += "\n";
            mmOutputStream.write(Chuoi);
//            mmOutputStream.write(msg.getBytes());
            mmOutputStream.write(("EZ\n" +
                    "{PRINT:\n" +
                    "@5,0:TIMNR,HMULT1,VMULT1|  CÔNG TY CPCN CHỢ LỚN|\n" +
                    "@30,0:TIMNR,HMULT1,VMULT1|   97 Phạm Hữu Chí, P.12, Q.5|\n" +
                    "@55,0:TIMNR,HMULT2,VMULT2|BIÊN NHẬN THU|\n" +
                    "@100,0:TIMNR,HMULT2,VMULT2|    TIỀN NƯỚC|\n" +
                    "@150,0:TIMNR,HMULT1,VMULT1|Kỳ:|\n" +
                    "@150,300:TIMNR,HMULT1,VMULT1|7/2018|\n" +
                    "@175,0:TIMNR,HMULT1,VMULT1|Từ ngày:|\n" +
                    "@175,140:TIMNR,HMULT1,VMULT1|06/06/2018-06/07/2018|\n" +
                    "@200,0:TIMNR,HMULT1,VMULT1|DB:|\n" +
                    "@200,218:TIMNR,HMULT1,VMULT1|0614-679-0075|\n" +
                    "@225,0:TIMNR,HMULT1,VMULT1|KH:|\n" +
                    "@225,43:TIMNR,HMULT1,VMULT1|DAO THI THANH HANG|\n" +
                    "@300,0:TIMNR,HMULT1,VMULT1|Đ/Chỉ:|\n" +
                    "@300,76:TIMNR,HMULT1,VMULT1|79/34A17C TAN HOA DONG|\n" +
                    "@325,0:TIMNR,HMULT1,VMULT1|GB:|\n" +
                    "@325,44:TIMNR,HMULT1,VMULT1|11|\n" +
                    "@325,110:TIMNR,HMULT1,VMULT1|DM:|\n" +
                    "@325,154:TIMNR,HMULT1,VMULT1|44|\n" +
                    "@350,0:TIMNR,HMULT1,VMULT1|Mlt:|\n" +
                    "@350,275:TIMNR,HMULT1,VMULT1|1700113|\n" +
                    "@375,0:TIMNR,HMULT1,VMULT1|Số HĐ:|\n" +
                    "@375,88:TIMNR,HMULT1,VMULT1|20189108257|\n" +
                    "@400,0:TIMNR,HMULT1,VMULT1|Cs Cũ:|\n" +
                    "@400,320:TIMNR,HMULT1,VMULT1|167|\n" +
                    "@425,0:TIMNR,HMULT1,VMULT1|Cs mới:|\n" +
                    "@425,320:TIMNR,HMULT1,VMULT1|183|\n" +
                    "@450,0:TIMNR,HMULT1,VMULT1|Tiêu thụ:|\n" +
                    "@450,309:TIMNR,HMULT1,VMULT1|16m3|\n" +
                    "}\n"
            ).getBytes());
            mmOutputStream.write(Chuoi);
//            mmOutputStream.write(("EZ\n" +
//                    "{PRINT:\n" +
//                    "@475,0:TIMNR,HMULT1,VMULT1|Giá bán:|\n" +
//                    "@475,275:TIMNR,HMULT1,VMULT1|84.800đ|\n" +
//                    "@500,0:TIMNR,HMULT1,VMULT1|Thuế:|\n" +
//                    "@500,286:TIMNR,HMULT1,VMULT1|4.240đ|\n" +
//                    "@525,0:TIMNR,HMULT1,VMULT1|Phí:|\n" +
//                    "@525,286:TIMNR,HMULT1,VMULT1|8.480đ|\n" +
//                    "@550,0:TIMNR,HMULT1,VMULT1|Tổng cộng:|\n" +
//                    "@550,275:TIMNR,HMULT1,VMULT1|97.520đ|\n" +
//                    "@600,0:TIMNR,HMULT1,VMULT1|Nhân viên thu:|\n" +
//                    "@600,286:TIMNR,HMULT1,VMULT1|Quầy 2|\n" +
//                    "}\n" +
//                    "{LP}").getBytes());
            mmOutputStream.write(("EZ\n" +
                    "{PRINT:\n" +
                    "@5,0:TIMNR,HMULT1,VMULT1|Giá bán:|\n" +
                    "@5,275:TIMNR,HMULT1,VMULT1|84.800đ|\n" +
                    "@30,0:TIMNR,HMULT1,VMULT1|Thuế:|\n" +
                    "@30,286:TIMNR,HMULT1,VMULT1|4.240đ|\n" +
                    "@55,0:TIMNR,HMULT1,VMULT1|Phí:|\n" +
                    "@55,286:TIMNR,HMULT1,VMULT1|8.480đ|\n" +
                    "@80,0:TIMNR,HMULT1,VMULT1|Tổng cộng:|\n" +
                    "@80,275:TIMNR,HMULT1,VMULT1|97.520đ|\n" +
                    "@105,0:TIMNR,HMULT1,VMULT1|Nhân viên thu:|\n" +
                    "@105,286:TIMNR,HMULT1,VMULT1|Quầy 2|\n" +
                    "}\n" +
                    "{LP}").getBytes());


            // tell the user data were sent
            myLabel.setText("Data sent.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendData() throws IOException {
        try {

            // the text typed by the user
            byte[] Chuoi = {0x1B};
//            byte[] Chuoi = "0x1B".getBytes();
            String msg = myTextbox.getText().toString();
            msg += "\n";

            mmOutputStream.write(Chuoi);
            mmOutputStream.write(("EZ\n" +
                    "{PRINT:\n" +
//                    "@5,0:TIMNR,HMULT1,VMULT1|  CÔNG TY CPCN CHỢ LỚN|\n" +
//                    "@5,1:TIMNR,HMULT1,VMULT1|  CÔNG TY CPCN CHỢ LỚN|\n" +
                    "@10,0:TIMNR,HMULT1,VMULT1|Tiền nước: " + padLeft("2 000đ", 22) + "|\n" +
                    "@30,0:TIMNR,HMULT1,VMULT1|Tiền nước: " + padLeft("20 000đ", 22) + "|\n" +
                    "@50,0:TIMNR,HMULT1,VMULT1|Tiền nước: " + padLeft("20 007 777đ", 22) + "|\n" +
                    "@70,0:TIMNR,HMULT1,VMULT1|Tiền nước: " + padLeft("1 003 300đ", 22) + "|\n" +
                    "@90,0:TIMNR,HMULT1,VMULT1|Tiền nước: " + padLeft("200 000đ", 22) + "|\n" +
//                    "@5,2:TIMNR,HMULT1,VMULT1|  CÔNG TY CPCN CHỢ LỚN|\n" +
                    "}\n"
            ).getBytes());
//            mmOutputStream.write(NotEmphasis);
//            mmOutputStream.write(Chuoi);
//
//            mmOutputStream.write(("EZ\n" +
//                    "{PRINT:\n" +
//                    "@5,0:TIMNR,HMULT1,VMULT1|Giá bán:|\n" +
//                    "}\n" +
//                    "{LP}").getBytes());


            // tell the user data were sent
            myLabel.setText("Data sent.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s).replace(" ","  ");
    }

    // close the connection to bluetooth printer.
    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            myLabel.setText("Bluetooth Closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
