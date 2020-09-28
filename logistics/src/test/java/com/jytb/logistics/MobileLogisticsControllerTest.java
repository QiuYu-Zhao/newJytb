package com.jytb.logistics;

import co.chexiao.common.util.HttpClient;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 〈app物流测试类〉
 *
 * @author hyz
 * @create 2019/11/3
 */
public class MobileLogisticsControllerTest {

    @Test
    public void testMobileCreateLogistics() {
        String url = "http://localhost:8541/api/logistics/create?token=57cb10870ead4e0389de717aab0cb2e3";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("receiver", "物流单测试-收货人");
        paramMap.put("receiverTel", "111111111111");
        paramMap.put("freightCharge", "80.22");
        paramMap.put("receiverAddress", "1-测试");
        paramMap.put("insteadCharge", "22.35");
        paramMap.put("count", "20");
        paramMap.put("sender", "物流单测试-发货人");
        paramMap.put("senderTel", "22222222222");
        paramMap.put("remark", "测试单");
        paramMap.put("userId", "1140242179056256");

        String result = HttpClient.httpClientPost(url, paramMap, null);
        System.out.println(result);
    }
}