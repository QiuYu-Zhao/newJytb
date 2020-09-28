package com.jytb.logistics.service.api.impl;

import co.chexiao.base.contract.dao.helper.MKDBHelper;
import com.jytb.logistics.bean.logistics.Logistics;
import com.jytb.logistics.service.api.IMobileLogisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 〈app物流单服务类〉
 *
 * @author hyz
 * @create 2019/6/9
 */
@Service
public class MobileLogisticsServiceImpl implements IMobileLogisticsService {

    private static final Logger logger =
            LoggerFactory.getLogger(MobileLogisticsServiceImpl.class);

    @Override
    public List<Logistics> getLogisticsListPageByCondition(String condition, int page, int pageSize, String orderBy) throws Exception {
        logger.info("app查询物流单，condition：" + condition);
        return (List<Logistics>)MKDBHelper.getDAOHelper().getListByPage(Logistics.class,condition,"*",page,pageSize,orderBy);
    }

    @Override
    public void updateLogistics(Logistics model) throws Exception {
        MKDBHelper.getDAOHelper().upateEntity(model);
    }
}