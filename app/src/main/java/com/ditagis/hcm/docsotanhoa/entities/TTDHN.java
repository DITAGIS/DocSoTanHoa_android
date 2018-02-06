package com.ditagis.hcm.docsotanhoa.entities;

/**
 * Created by ThanLe on 2/6/2018.
 */

public class TTDHN {
    private String code;
    private String TTDHN;

    public TTDHN(String code, String TTDHN) {
        this.code = code;
        this.TTDHN = TTDHN;
    }

    public TTDHN() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTTDHN() {
        return TTDHN;
    }

    public void setTTDHN(String TTDHN) {
        this.TTDHN = TTDHN;
    }
}
