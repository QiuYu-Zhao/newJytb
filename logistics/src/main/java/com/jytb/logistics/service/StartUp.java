package com.jytb.logistics.service;

import co.chexiao.base.contract.dao.helper.MKDBHelper;
import co.chexiao.base.contract.dao.helper.RedisHelper;
import co.chexiao.common.util.PropertiesUtil;
import co.chexiao.common.util.UniqueIDUtil;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 目的用作参数的初始化
 * Created by lzj on 2017/3/21.
 */
public class StartUp {

    private static final Logger logger = Logger.getLogger(StartUp.class);


    public void init() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream path1 = loader.getResourceAsStream("ini.properties");
        PropertiesUtil.init(path1);
        MKDBHelper.init(loader.getResource("db/mkdb.properties").getPath());
        try {
            UniqueIDUtil.init();
        } catch (Exception e) {
            logger.error("获取idworker出错，用classpath下的ini.properties配置",e);
        } finally {
            if(null != path1) {
                try {
                    path1.close();
                } catch (IOException e) {
                    logger.error("关闭文件流出错");
                }
            }
        }
    }
}
