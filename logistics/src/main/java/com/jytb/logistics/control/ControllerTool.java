package com.jytb.logistics.control;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by h on 2017/3/16.
 */
public class ControllerTool {

    public Page createPage(String aoData) {
        String sEcho = null;
        int iDisplayStart = 0; // 起始索引
        int iDisplayLength = 10; // 每页显示的行数

        JSONArray jsonarray = JSONArray.fromObject(aoData);
        for (int i = 0, l = jsonarray.size(); i < l; i++) {
            JSONObject obj = (JSONObject) jsonarray.get(i);
            if ("sEcho".equals(obj.get("name")))
                sEcho = obj.get("value").toString();

            if ("iDisplayStart".equals(obj.get("name"))) {
                iDisplayStart = obj.getInt("value");

                if ("iDisplayLength".equals(obj.get("name")))
                    iDisplayLength = obj.getInt("value");
            }
        }
        Page page = new Page();
        page.iDisplayStart = iDisplayStart;
        page.sEcho = sEcho;
        page.iDisplayLength = iDisplayLength;
        return page;
    }

    public static class Page {
        public String sEcho = null;
        public int iDisplayStart = 0; // 起始索引
        public int iDisplayLength = 10; // 每页显示的行数

        public int getCurrentPage(){
            return (iDisplayStart / iDisplayLength) + 1;
        }
    }
}