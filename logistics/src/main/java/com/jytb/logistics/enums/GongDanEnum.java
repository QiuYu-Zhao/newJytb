package com.jytb.logistics.enums;

public enum GongDanEnum {

    QUERY("query", "待审核"),
    MAKSURE("makesure", "待调度"),
    WORK("work", "待接单"),
    RECEIVE("receive", "待测试"),
    INSTALL("install", "安装中"),
    TEST("test", "待验收"),
    CONFIRM("confirm", "已完成"),
    BACKCUSTOER("backCustomer", "退回客户"),
    BACKINSTALL("backInstall", "退回安装公司");


    private String code;
    private String desc;

    GongDanEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(String code) {
        for (GongDanEnum enu : GongDanEnum.values())
            if (enu.getCode().equals(code))
                return enu.getDesc();
        return "";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
