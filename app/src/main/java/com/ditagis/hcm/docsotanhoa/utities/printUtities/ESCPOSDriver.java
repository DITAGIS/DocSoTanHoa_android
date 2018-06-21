package com.ditagis.hcm.docsotanhoa.utities.printUtities;

import android.util.Log;

import java.io.BufferedOutputStream;
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

    public void initPrint(OutputStream bufferedOutputStream) {
        try {
            bufferedOutputStream.write(SWITCH_COMMAND);
            bufferedOutputStream.write(INIT);
            bufferedOutputStream.write(INTERNATIONALIZE);
        } catch (IOException e) {
            Log.e(tag, e.getMessage(), e);
        }
    }
public void printBarcode(OutputStream outputStream, String barcodeData){
        try{
            outputStream.write(BARCODE_TYPE_CODE39);
            outputStream.write(barcodeData.getBytes());
        }catch (IOException e){

        }
}

    public void changeFont(OutputStream outputStream, int font) {
        try {
            switch (font) {
                case 1:
                    outputStream.write(FONT_1);
                    break;
                case 2:
                    outputStream.write(FONT_2);
                    break;
                case 3:
                    outputStream.write(FONT_3);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void printLineAlignLeft(OutputStream bufferedOutputStream, String lineData) {
        try {
            bufferedOutputStream.write(ALIGN_LEFT);
            bufferedOutputStream.write(lineData.getBytes());
//            bufferedOutputStream.write(LINE_FEED);
        } catch (IOException e) {
            Log.e(tag, e.getMessage(), e);
        }
    }

    public void printLineAlignCenter(OutputStream bufferedOutputStream, String lineData) {
        try {
            bufferedOutputStream.write(ALIGN_CENTER);
            bufferedOutputStream.write(lineData.getBytes());
//            bufferedOutputStream.write(LINE_FEED);
        } catch (IOException e) {
            Log.e(tag, e.getMessage(), e);
        }
    }

    public void printLineAlignRight(OutputStream bufferedOutputStream, String lineData) {
        try {
            bufferedOutputStream.write(ALIGN_RIGHT);
            bufferedOutputStream.write(lineData.getBytes());
//            bufferedOutputStream.write(LINE_FEED);
        } catch (IOException e) {
            Log.e(tag, e.getMessage(), e);
        }
    }

    public void finishPrint(OutputStream bufferedOutputStream) {
        try {
            bufferedOutputStream.write(PAPER_FEED);
            bufferedOutputStream.write(PAPER_CUT);
        } catch (IOException e) {
            Log.e(tag, e.getMessage(), e);
        }
    }

    public void flushCommand(OutputStream bufferedOutputStream) {
        try {
            bufferedOutputStream.write(FLUSH_COMMAND);
            bufferedOutputStream.write(PAPER_FEED);
            bufferedOutputStream.write(PAPER_CUT);
        } catch (IOException e) {
            Log.e(tag, e.getMessage(), e);
        }
    }

}