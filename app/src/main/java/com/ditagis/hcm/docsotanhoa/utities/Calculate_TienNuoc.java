package com.ditagis.hcm.docsotanhoa.utities;

/**
 * Created by ThanLe on 11/22/2017.
 */

public class Calculate_TienNuoc {
    private String mGB;
    private int mDM;
    private int tieuThu, mTieuThuSH, mTieuThuSX, mTieuThuDV, mTieuThuHC;
    private double mTienNuoc;
    private int mSH, mSX, mDV, mHC;
    private int SHTM, SHVM1, SHVM2;
    private static
    int DON_GIA_SHTM = 5300;
    private static
    int DON_GIA_SHVM1 = 10200;
    private static
    int DON_GIA_SHVM2 = 11400;
    private static
    int DON_GIA_SX = 9600;
    private static
    int DON_GIA_DV = 16900;
    private static
    int DON_GIA_HC = 10300;
    private static
    int DON_GIA_SH_NUOC_NGOAI = DON_GIA_SHVM2;

    private Calculate_TienNuoc() {
    }

    private static Calculate_TienNuoc _instance;

    public static Calculate_TienNuoc getInstance() {

        if (_instance == null)
            _instance = new Calculate_TienNuoc();
        return _instance;

    }

    public int getLNSH() {
        return mTieuThuSH;
    }

    public int getLNHCSN() {
        return mTieuThuHC;
    }

    public int getLNSX() {
        return mTieuThuSX;
    }

    public int getLNDV() {
        return mTieuThuDV;
    }

    public double getmTienNuoc() {
        return mTienNuoc;
    }

    public int getGTGT() {
        return doubleToInt(mTienNuoc * 5 / 100);
    }

    public int getBVMT() {
        return doubleToInt(mTienNuoc * 0.9);
    }

    public long getTongTien() {
        return (long) (getmTienNuoc() + getGTGT() + getBVMT());
    }

    private void reset() {
        SHTM = SHVM1 = SHVM2 = mTieuThuSH = mTieuThuSX = mTieuThuDV = mTieuThuHC = 0;
    }

    private int getSHTM() {
        SHTM = mTieuThuSH - mDM > 0 ? mDM : mTieuThuSH;
        return SHTM;
    }

    private int getSHVM() {
        return mTieuThuSH - SHTM;
    }

    private int getSHVM1() {
        SHVM1 = mTieuThuSH - 1.5 * mDM > 0 ? mDM / 2 : mTieuThuSH - SHTM;
        return SHVM1;
    }

    private int getSHVM2() {
        SHVM2 = mTieuThuSH - SHTM - SHVM1;
        return SHVM2;
    }

    private boolean hasRatio() {
        return mSH > 0 || mSX > 0 || mDV > 0 || mHC > 0;
    }

    private boolean hasSHVM() {
        return SHTM >= mDM;
    }

    private boolean hasSHVM2() {
        return SHVM1 >= (double) mDM / 2;
    }

    private void calculateSHTM_VM1_VM2() {
        SHTM = getSHTM();
        if (hasSHVM()) {
            SHVM1 = getSHVM1();
            if (hasSHVM2())
                SHVM2 = getSHVM2();
        }
    }

    private void calculateSHTM_VM() {
        SHTM = getSHTM();
        if (hasSHVM())
            SHVM1 = getSHVM();
    }

    private int calculateTienNuoc_TM_VM1_VM2() {
        return SHTM * DON_GIA_SHTM + SHVM1 * DON_GIA_SHVM1 + SHVM2 * DON_GIA_SHVM2;
    }

    private int calculateSH() {
        return mTieuThuSH * DON_GIA_SHVM2;
    }

    private int calculateSX() {
        return mTieuThuSX * DON_GIA_SX;
    }

    private int calculateDV() {
        return mTieuThuDV * DON_GIA_DV;
    }

    private int calculateHC() {
        return mTieuThuHC * DON_GIA_HC;
    }


    public double calculate(int tieuthu, String gb, String dm, int sh, int sx, int dv, int hc) {
        this.mGB = gb;
        mDM = Integer.parseInt(dm);
        mSH = sh;
        mSX = sx;
        mDV = dv;
        mHC = hc;
        this.tieuThu = tieuthu;
        switch (mGB) {
            case "11":
                reset();
                if (mDM == 0) {
                    mTienNuoc = tieuThu * DON_GIA_SHVM2;
                } else {
                    mTieuThuSH = tieuThu;
                    calculateSHTM_VM1_VM2();
                    mTienNuoc = calculateTienNuoc_TM_VM1_VM2();
                }
                break;
            case "12":
            case "22":
            case "32":
            case "42":
                mTieuThuSX = tieuthu;
                mTienNuoc = calculateSX();

                break;
            case "13":
            case "23":
            case "33":
            case "43":
                mTieuThuDV = tieuthu;
                mTienNuoc = calculateDV();

                break;
            case "14":
            case "24":
                reset();
                if (hasRatio()) {

                    mTieuThuSH = doubleToInt((double) mSH / 100 * tieuThu);
                    mTieuThuSX = doubleToInt((double) mSX / 100 * tieuThu);
                    calculateSHTM_VM1_VM2();
                    mTienNuoc = calculateTienNuoc_TM_VM1_VM2() + calculateSX();
                } else {
                    mTieuThuSH = tieuThu;
                    calculateSHTM_VM();
                    mTieuThuSX = SHVM1;
                    mTienNuoc = SHTM * DON_GIA_SHTM + calculateSX();
                }
                break;
            case "15":
            case "25":
                reset();
                if (hasRatio()) {

                    mTieuThuSH = doubleToInt((double) mSH / 100 * tieuThu);
                    mTieuThuDV = doubleToInt((double) mDV / 100 * tieuThu);
                    calculateSHTM_VM1_VM2();
                    mTienNuoc = calculateTienNuoc_TM_VM1_VM2() + calculateDV();
                } else {
                    mTieuThuSH = tieuThu;
                    calculateSHTM_VM();
                    mTieuThuDV = SHVM1;
                    mTienNuoc = SHTM * DON_GIA_SHTM + calculateDV();
                }
                break;
            case "16":
            case "26":
                reset();
                if (mSH > 0) {// có đủ 3 tỉ lệ

                    mTieuThuSH = doubleToInt((double) mSH / 100 * tieuThu);
                    mTieuThuDV = doubleToInt((double) mDV / 100 * tieuThu);
                    mTieuThuSX = doubleToInt((double) mSX / 100 * tieuThu);

                    calculateSHTM_VM1_VM2();
                    mTienNuoc = calculateTienNuoc_TM_VM1_VM2() + calculateSX() + calculateDV();
                } else {
                    if (tieuThu >= mDM) {
                        mTieuThuSH = mDM;
                        mTieuThuSX = doubleToInt((double) mSX / 100 * (tieuThu - mDM));
                        mTieuThuDV = doubleToInt((double) mDV / 100 * (tieuThu - mDM));
                    } else {
                        mTieuThuSH = tieuThu;
                    }
                    mTienNuoc = calculateSH() + calculateSX() + calculateDV();
                }
                break;
            case "17":
            case "27":
                SHTM = tieuthu;
                mTienNuoc = tieuThu * DON_GIA_SHTM;

                break;
            case "18":
            case "28":
            case "38":
                reset();
                if (hasRatio()) {
                    mTieuThuSH = doubleToInt((double) mSH / 100 * tieuThu);
                    mTieuThuHC = doubleToInt((double) mHC / 100 * tieuThu);
                    tieuThu = mTieuThuSH;
                    calculateSHTM_VM1_VM2();
                    mTienNuoc = calculateTienNuoc_TM_VM1_VM2() + calculateHC();
                } else {
                    mTieuThuSH = tieuthu;
                    calculateSHTM_VM();
                    mTieuThuHC = SHVM1;
                    mTienNuoc = SHTM * DON_GIA_SHTM + calculateHC();
                }
                break;
            case "19":
            case "29":
            case "39":
                reset();
                mTieuThuSH = doubleToInt((double) mSH / 100 * tieuThu);
                mTieuThuSX = doubleToInt((double) mSX / 100 * tieuThu);
                mTieuThuDV = doubleToInt((double) mDV / 100 * tieuThu);
                mTieuThuHC = doubleToInt((double) mHC / 100 * tieuThu);
                calculateSHTM_VM1_VM2();
                mTienNuoc = calculateTienNuoc_TM_VM1_VM2() + calculateSX() + calculateDV() + calculateHC();
                break;
            case "21":
                reset();
                mTieuThuSH = tieuThu;
                calculateSHTM_VM1_VM2();
                mTienNuoc = calculateTienNuoc_TM_VM1_VM2();
                break;
            case "31":
                mTieuThuHC = tieuthu;
                mTienNuoc = tieuThu * DON_GIA_HC;
                break;
            case "34":
                reset();
                if (mSX == 0 && mHC == 0)
                    mTieuThuSX = tieuthu;
                else {
                    mTieuThuSX = doubleToInt((double) mSX / 100 * tieuThu);
                    mTieuThuHC = doubleToInt((double) mHC / 100 * tieuThu);
                }
                mTienNuoc = calculateSX() + calculateHC();
                break;
            case "35":
                reset();
                if (mDV == 0 && mHC == 0)
                    mTieuThuDV = tieuthu;
                else {
                    mTieuThuDV = doubleToInt((double) mDV / 100 * tieuThu);
                    mTieuThuHC = doubleToInt((double) mHC / 100 * tieuThu);
                }
                mTienNuoc = calculateDV() + calculateHC();
                break;
            case "36":
                reset();
                mTieuThuSX = doubleToInt((double) mSX / 100 * tieuThu);
                mTieuThuDV = doubleToInt((double) mDV / 100 * tieuThu);
                mTieuThuHC = doubleToInt((double) mHC / 100 * tieuThu);
                mTienNuoc = calculateSX() + calculateDV() + calculateHC();
                break;

            case "51":
                reset();
                mTieuThuSH = tieuThu;
                calculateSHTM_VM1_VM2();
                mTienNuoc = 0.9 * (SHTM * DON_GIA_SHTM + SHVM1 * DON_GIA_SHVM1 + SHVM2 * DON_GIA_SHVM2);
                break;
            case "52":
                mTieuThuSX = tieuthu;
                mTienNuoc = 0.9 * calculateSX();
                break;
            case "53":
                mTieuThuDV = tieuthu;
                mTienNuoc = 0.9 * calculateDV();
                break;
            case "54":
                mTieuThuHC = tieuthu;
                mTienNuoc = 0.9 * calculateHC();
                break;
            case "59":
                reset();
                if (mSH == 0 && mDV == 0 && mSX == 0 && mHC == 0)
                    mTieuThuSH = tieuthu;
                else {
                    mTieuThuSH = doubleToInt((double) mSH / 100 * tieuThu);
                    mTieuThuSX = doubleToInt((double) mSX / 100 * tieuThu);
                    mTieuThuDV = doubleToInt((double) mDV / 100 * tieuThu);
                    mTieuThuHC = doubleToInt((double) mHC / 100 * tieuThu);
                }
                calculateSHTM_VM1_VM2();
                mTienNuoc = 0.9 * (calculateTienNuoc_TM_VM1_VM2() + calculateSX() + calculateDV() + calculateHC());
                break;
            case "68":
                if (mSH == 0 && mDV == 0)
                    mTieuThuSH = tieuthu;
                else {
                    mTieuThuSH = doubleToInt((double) mSH / 100 * tieuThu);
                    mTieuThuDV = doubleToInt((double) mDV / 100 * tieuThu);
                }
                calculateSHTM_VM1_VM2();
                mTienNuoc = 0.9 * (calculateTienNuoc_TM_VM1_VM2() + calculateDV());
                break;
            default:
                mTienNuoc = 0;
                break;

        }
        return mTienNuoc;
    }

    private int doubleToInt(double number) {

        return (int) Math.round(number);
    }

}