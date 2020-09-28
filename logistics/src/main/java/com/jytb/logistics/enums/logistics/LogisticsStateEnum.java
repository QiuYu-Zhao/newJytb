package com.jytb.logistics.enums.logistics;

public enum LogisticsStateEnum {

    NOTSEND(1, "代发货"),
    SENDED(2, "已发货"),
    RECEIVED(3, "已到货"),
    CANCEL(-1, "已取消");


    private int code;
    private String desc;

    LogisticsStateEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(int code) {
        for (LogisticsStateEnum enu : LogisticsStateEnum.values())
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
