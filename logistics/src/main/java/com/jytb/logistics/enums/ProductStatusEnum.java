package com.jytb.logistics.enums;

/**
 * 成品信息Enum
 * Created by 刘玉林
 * Date: 2018-04-10 11:31
 */
public enum ProductStatusEnum {
    //成品类型product_type
    PRODUCT_TYPE_CONVENTIONAL(1,"常规品"),
    PRODUCT_TYPE_SAMPLE(2,"样品"),
    PRODUCT_TYPE_NEW(3,"新品"),

    //设备厂商manufacturer
    MANUFACTURER_BSJ(1,"CX-S01"),
    MANUFACTURER_HDH(2,"CX-S05"),
    MANUFACTURER_KKS(3,"CX-S08"),
    MANUFACTURER_WDF(4,"CX-S03"),

    //使用状态use_state
    USE_STATE_TEST(0,"测试中"),
    USE_STATE_USE(1,"使用中"),
    USE_STATE_TEARDOWN(2,"已拆除"),
    USE_STATE_SPLIT(3,"已拆分"),

    //损坏状态bad_state
    BAD_STATE_NORMAL(0,"正常"),
    BAD_STATE_CONVENTIONAL_RETURN(1,"常规品已返厂"),
    BAD_STATE_DAMAGED_RETURN(2,"报损品已返厂"),
    BAD_STATE_SCRAPPED(3,"报废品"),

    //损坏原因bad_reason
    BAD_REASON_CPU(1,"主板有问题"),
    BAD_REASON_FREQUENCY(2,"频率有误"),
    BAD_REASON_BATTERY(3,"电池有问题"),
    BAD_REASON_SWITCH(4,"开关有问题"),
    BAD_REASON_NO_RESPONSE(5,"通电后没反应"),
    BAD_REASON_OTHER(6,"其他"),

    //是否修复is_repair
    IS_REPAIR_TRUE(1,"已修复"),
    IS_REPAIR_FALSE(0,"未修复");

    private int code;
    private String desc;

    ProductStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getProductTypeDesc(int code){
        String productTypeDesc = "";
        if(code==ProductStatusEnum.PRODUCT_TYPE_CONVENTIONAL.getCode()){
            productTypeDesc = ProductStatusEnum.PRODUCT_TYPE_CONVENTIONAL.getDesc();
        }else if(code==ProductStatusEnum.PRODUCT_TYPE_SAMPLE.getCode()){
            productTypeDesc = ProductStatusEnum.PRODUCT_TYPE_SAMPLE.getDesc();
        }else{
            productTypeDesc = ProductStatusEnum.PRODUCT_TYPE_NEW.getDesc();
        }
        return productTypeDesc;
    }

    public static String getManufacturerDesc(int code){
        String manufacturerDesc = "";
        if(code == ProductStatusEnum.MANUFACTURER_BSJ.getCode()){
            manufacturerDesc = "博实结";
        }else if(code == ProductStatusEnum.MANUFACTURER_HDH.getCode()){
            manufacturerDesc = "好的货";
        }else if(code == ProductStatusEnum.MANUFACTURER_KKS.getCode()){
            manufacturerDesc = "康凯斯";
        }else if(code == ProductStatusEnum.MANUFACTURER_WDF.getCode()){

        }
        return manufacturerDesc;
    }

    public static String getUseStateDesc(int code){
        String useStateDesc = "";
        if(code == ProductStatusEnum.USE_STATE_TEST.getCode()){
            useStateDesc = ProductStatusEnum.USE_STATE_TEST.getDesc();
        }else if(code == ProductStatusEnum.USE_STATE_USE.getCode()){
            useStateDesc = ProductStatusEnum.USE_STATE_USE.getDesc();
        }else if(code == ProductStatusEnum.USE_STATE_TEARDOWN.getCode()){
            useStateDesc = ProductStatusEnum.USE_STATE_TEARDOWN.getDesc();
        }else{
            useStateDesc = ProductStatusEnum.USE_STATE_SPLIT.getDesc();
        }
        return useStateDesc;
    }

    public static String getBadStateDesc(int code){
        String badStateDesc = "";
        if(code == ProductStatusEnum.BAD_STATE_NORMAL.getCode()){
            badStateDesc = ProductStatusEnum.BAD_STATE_NORMAL.getDesc();
        }else if(code == ProductStatusEnum.BAD_STATE_CONVENTIONAL_RETURN.getCode()){
            badStateDesc = ProductStatusEnum.BAD_STATE_CONVENTIONAL_RETURN.getDesc();
        }else if(code == ProductStatusEnum.BAD_STATE_DAMAGED_RETURN.getCode()){
            badStateDesc = ProductStatusEnum.BAD_STATE_DAMAGED_RETURN.getDesc();
        }else{
            badStateDesc = ProductStatusEnum.BAD_STATE_SCRAPPED.getDesc();
        }
        return badStateDesc;
    }

    public static String getBadReasonDesc(int code){
        String badReasonDesc = "";
        if(code == ProductStatusEnum.BAD_REASON_CPU.getCode()){
            badReasonDesc = ProductStatusEnum.BAD_REASON_CPU.getDesc();
        }else if(code == ProductStatusEnum.BAD_REASON_FREQUENCY.getCode()){
            badReasonDesc = ProductStatusEnum.BAD_REASON_FREQUENCY.getDesc();
        }else if(code == ProductStatusEnum.BAD_REASON_BATTERY.getCode()){
            badReasonDesc = ProductStatusEnum.BAD_REASON_BATTERY.getDesc();
        }else if(code == ProductStatusEnum.BAD_REASON_SWITCH.getCode()){
            badReasonDesc = ProductStatusEnum.BAD_REASON_SWITCH.getDesc();
        }else if(code == ProductStatusEnum.BAD_REASON_NO_RESPONSE.getCode()){
            badReasonDesc = ProductStatusEnum.BAD_REASON_NO_RESPONSE.getDesc();
        }else{
            badReasonDesc = ProductStatusEnum.BAD_REASON_OTHER.getDesc();
        }
        return badReasonDesc;
    }

    public static String getIsRepairDesc(int code){
        String isRepairDesc = "";
        if(code == ProductStatusEnum.IS_REPAIR_TRUE.getCode()){
            isRepairDesc = ProductStatusEnum.IS_REPAIR_TRUE.getDesc();
        }else{
            isRepairDesc = ProductStatusEnum.IS_REPAIR_FALSE.getDesc();
        }
        return isRepairDesc;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
