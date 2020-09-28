package com.jytb.logistics.enums.logistics;

/**
 * 物流单拨打电话状态
 */
public enum LogisticsTelStateEnum {

    NOTTEL(0, "未拨打"),
    TEL(1, "已拨打");


    private int code;
    private String desc;

    LogisticsTelStateEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(int code) {
        for (LogisticsTelStateEnum enu : LogisticsTelStateEnum.values())
            if (enu.getCode() == code)
                return enu.getDesc();
        return "";
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
