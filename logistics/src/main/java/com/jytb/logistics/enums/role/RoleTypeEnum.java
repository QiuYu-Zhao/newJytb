package com.jytb.logistics.enums.role;

public enum RoleTypeEnum {

    ADMIN("ADMIN", "管理员"),
    PC("PC", "PC"),
    APP("APP", "APP");


    private String code;
    private String desc;

    RoleTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(String code) {
        for (RoleTypeEnum enu : RoleTypeEnum.values())
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
