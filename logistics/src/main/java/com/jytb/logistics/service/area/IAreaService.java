package com.jytb.logistics.service.area;

import com.jytb.logistics.bean.area.Area;
import com.jytb.logistics.bean.area.City;
import com.jytb.logistics.bean.area.Province;
import com.jytb.logistics.bean.area.Street;

import java.util.List;

public interface IAreaService {

    /**
     * 获取全部省份
     * @return
     * @throws Exception
     */
    public List<Province> getAllProvinceList() throws Exception;

    /**
     * 通过省id查询省下的市
     * @param provinceId
     * @return
     * @throws Exception
     */
    public List<City> getCityListByProvinceId(long provinceId) throws Exception;

    /**
     * 通过城市id查询城市下的区
     * @param cityId
     * @return
     * @throws Exception
     */
    public List<Area> getAreaListByCityId(long cityId) throws Exception;

    /**
     * 通过区id查询区下的街道
     * @param areaId
     * @return
     * @throws Exception
     */
    public List<Street> getStreetListByAreaId(long areaId) throws Exception;

    /**
     * 通过id查询省份
     * @param provinceId
     * @return
     * @throws Exception
     */
    public Province findProvinceById(long provinceId) throws Exception;

    /**
     * 通过id查询城市
     * @param cityId
     * @return
     * @throws Exception
     */
    public City findCityById(long cityId) throws Exception;

    /**
     * 通过id查询区
     * @param areaId
     * @return
     * @throws Exception
     */
    public Area findAreaById(long areaId) throws Exception;

    /**
     * 通过id查询街道
     * @param streetId
     * @return
     * @throws Exception
     */
    public Street findStreetById(long streetId) throws Exception;
}
