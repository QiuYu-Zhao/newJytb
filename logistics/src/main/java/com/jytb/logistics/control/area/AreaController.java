package com.jytb.logistics.control.area;


import com.jytb.logistics.bean.area.Area;
import com.jytb.logistics.bean.area.City;
import com.jytb.logistics.bean.area.Street;
import com.jytb.logistics.service.area.IAreaService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zkl on 2019/06/08.
 */
@Controller
@RequestMapping("mk/area")
public class AreaController {
    private static final Logger logger = LoggerFactory.getLogger(AreaController.class);

    @Autowired
    private IAreaService areaService;

    @RequestMapping(value = "list", produces = "text/html;charset=UTF-8")
    public String list(ModelMap model) {
        return "logistics/logistics_list";
    }

    /**
     * 通过省的id查询所属该省的市
     * @param provinceId
     * @param request
     * @return
     */
    @RequestMapping(value = "getCityByProvince", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getCityByProvince(@RequestParam long provinceId, HttpServletRequest request) {
        List<City> cityList = new ArrayList<>();
        try {
            cityList = areaService.getCityListByProvinceId(provinceId);
        } catch (Exception e) {
            logger.error("查询城市失败", e);
        }
        JSONObject getObj = new JSONObject();
        getObj.put("cityList", cityList);
        return getObj.toString();

    }

    /**
     * 通过省的id查询所属该省的市
     * @param cityId
     * @param request
     * @return
     */
    @RequestMapping(value = "getAreaByCity", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getAreaByCity(@RequestParam long cityId, HttpServletRequest request) {
        List<Area> areaList = new ArrayList<>();
        try {
            areaList = areaService.getAreaListByCityId(cityId);
        } catch (Exception e) {
            logger.error("查询城市失败", e);
        }
        JSONObject getObj = new JSONObject();
        getObj.put("areaList", areaList);
        return getObj.toString();

    }


    /**
     * 通过区的id查询所属该区的街道
     * @param areaId
     * @param request
     * @return
     */
    @RequestMapping(value = "getStreetByArea", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getStreetByArea(@RequestParam long areaId, HttpServletRequest request) {
        List<Street> streetList = new ArrayList<>();
        try {
            streetList = areaService.getStreetListByAreaId(areaId);
        } catch (Exception e) {
            logger.error("查询城市失败", e);
        }
        JSONObject getObj = new JSONObject();
        getObj.put("streetList", streetList);
        return getObj.toString();

    }
}
