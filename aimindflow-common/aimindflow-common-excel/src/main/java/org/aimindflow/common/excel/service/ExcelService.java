package org.aimindflow.common.excel.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Excel服务接口
 *
 * @author HezaoHezao
 */
public interface ExcelService {

    /**
     * 导出Excel（基于模板）
     *
     * @param templatePath 模板路径
     * @param data         数据
     * @param outputStream 输出流
     * @return 是否成功
     */
    boolean exportExcel(String templatePath, Map<String, Object> data, OutputStream outputStream);

    /**
     * 导出Excel（基于数据列表）
     *
     * @param sheetName    工作表名称
     * @param headers      表头
     * @param dataList     数据列表
     * @param outputStream 输出流
     * @param <T>          数据类型
     * @return 是否成功
     */
    <T> boolean exportExcel(String sheetName, String[] headers, List<T> dataList, OutputStream outputStream);

    /**
     * 导出Excel（基于对象列表，使用注解）
     *
     * @param sheetName    工作表名称
     * @param dataList     数据列表
     * @param clazz        数据类型
     * @param outputStream 输出流
     * @param <T>          数据类型
     * @return 是否成功
     */
    <T> boolean exportExcel(String sheetName, List<T> dataList, Class<T> clazz, OutputStream outputStream);

    /**
     * 导入Excel（返回对象列表）
     *
     * @param inputStream 输入流
     * @param clazz       数据类型
     * @param <T>         数据类型
     * @return 数据列表
     */
    <T> List<T> importExcel(InputStream inputStream, Class<T> clazz);

    /**
     * 导入Excel（返回Map列表）
     *
     * @param inputStream 输入流
     * @param headers     表头
     * @return 数据列表
     */
    List<Map<String, Object>> importExcel(InputStream inputStream, String[] headers);

    /**
     * 读取Excel单元格数据
     *
     * @param inputStream 输入流
     * @param sheetIndex  工作表索引
     * @param rowIndex    行索引
     * @param columnIndex 列索引
     * @return 单元格数据
     */
    String readCell(InputStream inputStream, int sheetIndex, int rowIndex, int columnIndex);

    /**
     * 读取Excel行数据
     *
     * @param inputStream 输入流
     * @param sheetIndex  工作表索引
     * @param rowIndex    行索引
     * @return 行数据
     */
    List<String> readRow(InputStream inputStream, int sheetIndex, int rowIndex);

    /**
     * 读取Excel列数据
     *
     * @param inputStream 输入流
     * @param sheetIndex  工作表索引
     * @param columnIndex 列索引
     * @return 列数据
     */
    List<String> readColumn(InputStream inputStream, int sheetIndex, int columnIndex);
}