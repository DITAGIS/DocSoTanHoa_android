package com.ditagis.hcm.docsotanhoa.utities.printUtities;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;

public class ESCPOSDriver {

    private static String tag = ESCPOSDriver.class.getSimpleName();

    private static final byte[] LINE_FEED = {0x0A};
    private static final byte[] PAPER_FEED = {27, 0x4A, (byte) 0xFF};
    private static final byte[] PAPER_CUT = {0x1D, 0x56, 0x1};
    private static final byte[] ALIGN_LEFT = {0x1B, 0x61, 0x00};
    private static final byte[] ALIGN_CENTER = {0x1B, 0x61, 0x01};
    private static final byte[] ALIGN_RIGHT = {0x1B, 0x61, 0x02};
    private static final byte[] BOLD_ON = {0x1B, 0x45, 1};

    private static final byte[] BOLD_OFF = {0x1B, 0x45, 0};
    private static final byte[] FONT_1 = {0x1B, 0x77, 0x28};
    private static final byte[] FONT_2 = {0x1B, 0x77, 0x29};
    private static final byte[] FONT_3 = {0x1B, 0x77, 0x21};

    private static final byte[] INTERNATIONALIZE = {0x1B, 0x52, 0x16};

    private static final byte[] BARCODE_TYPE_CODE39 = {0x45};

    private static final byte[] INIT = {0x1B, 0x40};
    private static final byte[] STANDARD_MODE = {0x1B, 0x53};
    private static final byte[] SWITCH_COMMAND = {0x1B, 0x69, 0x61, 0x00};
    private static final byte[] FLUSH_COMMAND = {(byte) 0xFF, 0x0C};
    private OutputStream mOutputStream;

    public ESCPOSDriver(OutputStream mOutPutStream) {
        this.mOutputStream = mOutPutStream;
    }

    public void initPrint() {
        try {
            mOutputStream.write(SWITCH_COMMAND);
            mOutputStream.write(INIT);
            mOutputStream.write(INTERNATIONALIZE);
        } catch (IOException e) {
            Log.e(tag, e.getMessage(), e);
        }
    }

    public void printBarcode( String barcodeData) {
        try {
            mOutputStream.write(BARCODE_TYPE_CODE39);
            mOutputStream.write(barcodeData.getBytes());
        } catch (IOException e) {

        }
    }

    public void changeFont(int font) {
        try {
            switch (font) {
                case 1:
                    mOutputStream.write(FONT_1);
                    break;
                case 2:
                    mOutputStream.write(FONT_2);
                    break;
                case 3:
                    mOutputStream.write(FONT_3);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void printLineText( String lineData) {
        try {
            mOutputStream.write(lineData.getBytes());
//            bufferedOutputStream.write(LINE_FEED);
        } catch (IOException e) {
            Log.e(tag, e.getMessage(), e);
        }
    }

    public void printLineAlignLeft( String lineData) {
        try {
            mOutputStream.write(ALIGN_LEFT);
            mOutputStream.write(lineData.getBytes());
//            bufferedOutputStream.write(LINE_FEED);
        } catch (IOException e) {
            Log.e(tag, e.getMessage(), e);
        }
    }

    public void printLineAlignCenter( String lineData) {
        try {
            mOutputStream.write(ALIGN_CENTER);
            mOutputStream.write(lineData.getBytes());
//            bufferedOutputStream.write(LINE_FEED);
        } catch (IOException e) {
            Log.e(tag, e.getMessage(), e);
        }
    }

    public void printLineAlignRight( String lineData) {
        try {
            mOutputStream.write(ALIGN_RIGHT);
            mOutputStream.write(lineData.getBytes());
//            bufferedOutputStream.write(LINE_FEED);
        } catch (IOException e) {
            Log.e(tag, e.getMessage(), e);
        }
    }

    public void finishPrint() {
        try {
            mOutputStream.write(PAPER_FEED);
            mOutputStream.write(PAPER_CUT);
        } catch (IOException e) {
            Log.e(tag, e.getMessage(), e);
        }
    }

    public void flushCommand() {
        try {
            mOutputStream.write(FLUSH_COMMAND);
            mOutputStream.write(PAPER_FEED);
            mOutputStream.write(PAPER_CUT);
        } catch (IOException e) {
            Log.e(tag, e.getMessage(), e);
        }
    }

}