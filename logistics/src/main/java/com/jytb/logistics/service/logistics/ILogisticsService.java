package com.jytb.logistics.service.logistics;

import com.jytb.logistics.bean.logistics.Logistics;

import java.util.List;

/**
 * 〈物流单服务类〉
 *
 * @author hyz
 * @create 2019/6/8
 */
public interface ILogisticsService {

    /**
     * 通过条件查询物流单列表，分页查询
     * @param condition
     * @param currentPage
     * @param pageSize
     * @param orderBy
     * @return
     * @throws Exception
     */
    public List<Logistics> getLogisticsListPageByCondition(String condition, int currentPage, int pageSize, String orderBy) throws Exception;

    /**
     * 根据条件查询物流单数量
     * @param condition
     * @return
     * @throws Exception
     */
    public int getLogisticsCountByCondition(String condition) throws Exception;

    /**
     * 插入物流单
     * @param logistics
     * @throws Exception
     */
    public void insertLogistics(Logistics logistics) throws Exception;

    /**
     * 分配物流单
     * @param logisticsIds
     * @throws Exception
     */
    public void sendLogistics(String logisticsIds) throws Exception;

    /**
     * 分配所有物流单
     * @throws Exception
     */
    public void sendAllLogistics() throws Exception;

    /**
     * 通过id查询物流单
     * @param logisticsId
     * @return
     * @throws Exception
     */
    public Logistics findLogisticsById(long logisticsId) throws Exception;

    /**
     * 通过id删除物流单
     * @param logisticsId
     * @throws Exception
     */
    public void deleteLogisticsById(long logisticsId) throws Exception;

    /**
     * 通过id作废物流单
     * @param logisticsId
     * @throws Exception
     */
    public void cancelLogisticsById(long logisticsId) throws Exception;

    /**
     * 查询大于date的运费和代收总计
     * @param date
     * @return
     * @throws Exception
     */
    public List<Object[]> getTotalCharge(String date) throws Exception;

    /**
     * 更新物流单
     * @param logistics
     * @throws Exception
     */
    public void updateLogistics(Logistics logistics) throws Exception;

    /**
     * 删除30天前的数据
     * @throws Exception
     */
    public void deleteHsitoryData() throws Exception;

    /**
     * 生成发货编号
     *
     * @return
     * @throws Exception
     */
    public String getSystemNum() throws Exception;
}
