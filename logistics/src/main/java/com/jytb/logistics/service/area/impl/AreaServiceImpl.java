package com.jytb.logistics.service.area.impl;

import co.chexiao.base.contract.dao.helper.MKDBHelper;
import com.jytb.logistics.bean.area.Area;
import com.jytb.logistics.bean.area.City;
import com.jytb.logistics.bean.area.Province;
import com.jytb.logistics.bean.area.Street;
import com.jytb.logistics.service.area.IAreaService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 〈地区服务类〉
 *
 * @author hyz
 * @create 2019/6/8
 */
@Service
public class AreaServiceImpl implements IAreaService {

    @Override
    public List<Province> getAllProvinceList() throws Exception {
        return (List<Province>)MKDBHelper.getDAOHelper().getListByCustom(Province.class,"*","1=1","");
    }

    @Override
    public List<City> getCityListByProvinceId(long provinceId) throws Exception {
        String condition = "province_id = " + provinceId;
        return (List<City>)MKDBHelper.getDAOHelper().getListByCustom(City.class,"*",condition,"");
    }

    @Override
    public List<Area> getAreaListByCityId(long cityId) throws Exception {
        String condition = "city_id = " + cityId;
        return (List<Area>)MKDBHelper.getDAOHelper().getListByCustom(Area.class,"*",condition,"");
    }

    @Override
    public List<Street> getStreetListByAreaId(long areaId) throws Exception {
        String condition = "area_id = " + areaId;
        return (List<Street>)MKDBHelper.getDAOHelper().getListByCustom(Street.class,"*",condition,"");
    }

    @Override
    public Province findProvinceById(long provinceId) throws Exception {
        return (Province) MKDBHelper.getDAOHelper().get(Province.class,provinceId);
    }

    @Override
    public City findCityById(long cityId) throws Exception {
        return (City) MKDBHelper.getDAOHelper().get(City.class,cityId);
    }

    @Override
    public Area findAreaById(long areaId) throws Exception {
        return (Area) MKDBHelper.getDAOHelper().get(Area.class,areaId);
    }

    @Override
    public Street findStreetById(long streetId) throws Exception {
        return (Street) MKDBHelper.getDAOHelper().get(Street.class,streetId);
    }
}