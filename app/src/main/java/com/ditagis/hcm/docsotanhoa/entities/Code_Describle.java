package com.ditagis.hcm.docsotanhoa.entities;

/**
 * Created by ThanLe on 12/6/2017.
 */

public class Code_Describle {
    private String code;
    private String describe;

    public Code_Describle(String code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
