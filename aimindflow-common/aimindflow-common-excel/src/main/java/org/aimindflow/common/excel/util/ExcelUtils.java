package org.aimindflow.common.excel.util;

import org.aimindflow.common.excel.service.ExcelService;
import org.aimindflow.common.excel.service.ExcelServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Excel工具类，提供便捷的Excel操作方法
 *
 * @author HezaoHezao
 */
@Component
public class ExcelUtils {

    private static ExcelServiceFactory excelServiceFactory;

    @Autowired
    public void setExcelServiceFactory(ExcelServiceFactory excelServiceFactory) {
        ExcelUtils.excelServiceFactory = excelServiceFactory;
    }

    /**
     * 基于模板导出Excel
     *
     * @param templatePath 模板路径
     * @param data         数据
     * @param outputStream 输出流
     * @return 是否成功
     */
    public static boolean exportExcel(String templatePath, Map<String, Object> data, OutputStream outputStream) {
        return getExcelService().exportExcel(templatePath, data, outputStream);
    }

    /**
     * 导出Excel（基于数据列表）
     *
     * @param sheetName    sheet名称
     * @param headers      表头
     * @param dataList     数据列表
     * @param outputStream 输出流
     * @param <T>          数据类型
     * @return 是否成功
     */
    public static <T> boolean exportExcel(String sheetName, String[] headers, List<T> dataList, OutputStream outputStream) {
        return getExcelService().exportExcel(sheetName, headers, dataList, outputStream);
    }

    /**
     * 导出Excel（基于对象列表）
     *
     * @param sheetName    sheet名称
     * @param dataList     数据列表
     * @param clazz        对象类型
     * @param outputStream 输出流
     * @param <T>          数据类型
     * @return 是否成功
     */
    public static <T> boolean exportExcel(String sheetName, List<T> dataList, Class<T> clazz, OutputStream outputStream) {
        return getExcelService().exportExcel(sheetName, dataList, clazz, outputStream);
    }

    /**
     * 导入Excel（返回对象列表）
     *
     * @param inputStream 输入流
     * @param clazz       对象类型
     * @param <T>         数据类型
     * @return 对象列表
     */
    public static <T> List<T> importExcel(InputStream inputStream, Class<T> clazz) {
        return getExcelService().importExcel(inputStream, clazz);
    }

    /**
     * 导入Excel（返回Map列表）
     *
     * @param inputStream 输入流
     * @param headers     表头
     * @return Map列表
     */
    public static List<Map<String, Object>> importExcel(InputStream inputStream, String[] headers) {
        return getExcelService().importExcel(inputStream, headers);
    }

    /**
     * 读取Excel单元格
     *
     * @param inputStream 输入流
     * @param sheetIndex  sheet索引
     * @param rowIndex    行索引
     * @param columnIndex 列索引
     * @return 单元格值
     */
    public static String readCell(InputStream inputStream, int sheetIndex, int rowIndex, int columnIndex) {
        return getExcelService().readCell(inputStream, sheetIndex, rowIndex, columnIndex);
    }

    /**
     * 读取Excel行
     *
     * @param inputStream 输入流
     * @param sheetIndex  sheet索引
     * @param rowIndex    行索引
     * @return 行数据
     */
    public static List<String> readRow(InputStream inputStream, int sheetIndex, int rowIndex) {
        return getExcelService().readRow(inputStream, sheetIndex, rowIndex);
    }

    /**
     * 读取Excel列
     *
     * @param inputStream 输入流
     * @param sheetIndex  sheet索引
     * @param columnIndex 列索引
     * @return 列数据
     */
    public static List<String> readColumn(InputStream inputStream, int sheetIndex, int columnIndex) {
        return getExcelService().readColumn(inputStream, sheetIndex, columnIndex);
    }

    /**
     * 获取Excel服务
     *
     * @return Excel服务
     */
    private static ExcelService getExcelService() {
        if (excelServiceFactory == null) {
            throw new IllegalStateException("ExcelServiceFactory未初始化");
        }
        return excelServiceFactory.getDefaultExcelService();
    }

    /**
     * 获取POI Excel服务
     *
     * @return POI Excel服务
     */
    public static ExcelService getPoiExcelService() {
        if (excelServiceFactory == null) {
            throw new IllegalStateException("ExcelServiceFactory未初始化");
        }
        return excelServiceFactory.getPoiExcelService();
    }

    /**
     * 获取EasyExcel服务
     *
     * @return EasyExcel服务
     */
    public static ExcelService getEasyExcelService() {
        if (excelServiceFactory == null) {
            throw new IllegalStateException("ExcelServiceFactory未初始化");
        }
        return excelServiceFactory.getEasyExcelService();
    }
}