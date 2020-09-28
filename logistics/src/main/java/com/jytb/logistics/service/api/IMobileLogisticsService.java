package com.jytb.logistics.service.api;

import com.jytb.logistics.bean.logistics.Logistics;

import java.util.List;

public interface IMobileLogisticsService {

    public List<Logistics> getLogisticsListPageByCondition(String condition, int page, int pageSize, String orderBy) throws Exception;

    public void updateLogistics(Logistics model) throws Exception;
}
