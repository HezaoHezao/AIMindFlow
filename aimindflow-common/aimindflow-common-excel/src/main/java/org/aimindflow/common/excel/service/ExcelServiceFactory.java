package org.aimindflow.common.excel.service;

import org.aimindflow.common.excel.service.impl.EasyExcelServiceImpl;
import org.aimindflow.common.excel.service.impl.PoiExcelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Excel服务工厂类，用于获取不同的Excel服务实现
 *
 * @author HezaoHezao
 */
@Component
public class ExcelServiceFactory {

    private final PoiExcelServiceImpl poiExcelService;
    private final EasyExcelServiceImpl easyExcelService;

    @Autowired
    public ExcelServiceFactory(PoiExcelServiceImpl poiExcelService, EasyExcelServiceImpl easyExcelService) {
        this.poiExcelService = poiExcelService;
        this.easyExcelService = easyExcelService;
    }

    /**
     * 获取基于POI的Excel服务实现
     * 适用于：
     * 1. 需要基于模板导出Excel
     * 2. 需要更精细控制Excel格式
     * 3. 需要处理复杂Excel文件
     *
     * @return POI Excel服务实现
     */
    public ExcelService getPoiExcelService() {
        return poiExcelService;
    }

    /**
     * 获取基于EasyExcel的Excel服务实现
     * 适用于：
     * 1. 大数据量Excel处理（内存占用更小）
     * 2. 简单的Excel导入导出
     * 3. 基于注解的Excel处理
     *
     * @return EasyExcel服务实现
     */
    public ExcelService getEasyExcelService() {
        return easyExcelService;
    }

    /**
     * 获取默认的Excel服务实现
     * 默认使用POI实现，因为功能更全面
     *
     * @return 默认Excel服务实现
     */
    public ExcelService getDefaultExcelService() {
        return poiExcelService;
    }
}