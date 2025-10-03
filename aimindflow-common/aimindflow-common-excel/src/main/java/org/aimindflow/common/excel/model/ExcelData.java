package org.aimindflow.common.excel.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Excel数据模型
 *
 * @author HezaoHezao
 */
@Data
public class ExcelData {

    /**
     * 工作表名称
     */
    private String sheetName;

    /**
     * 表头
     */
    private List<String> headers;

    /**
     * 数据行
     */
    private List<List<Object>> rows;

    /**
     * 构造函数
     */
    public ExcelData() {
        this.headers = new ArrayList<>();
        this.rows = new ArrayList<>();
    }

    /**
     * 构造函数
     *
     * @param sheetName 工作表名称
     */
    public ExcelData(String sheetName) {
        this.sheetName = sheetName;
        this.headers = new ArrayList<>();
        this.rows = new ArrayList<>();
    }

    /**
     * 构造函数
     *
     * @param sheetName 工作表名称
     * @param headers   表头
     */
    public ExcelData(String sheetName, List<String> headers) {
        this.sheetName = sheetName;
        this.headers = headers;
        this.rows = new ArrayList<>();
    }

    /**
     * 添加表头
     *
     * @param header 表头
     */
    public void addHeader(String header) {
        this.headers.add(header);
    }

    /**
     * 添加数据行
     *
     * @param row 数据行
     */
    public void addRow(List<Object> row) {
        this.rows.add(row);
    }

    /**
     * 添加数据行
     *
     * @param rowData 数据行
     */
    public void addRow(Object... rowData) {
        List<Object> row = new ArrayList<>();
        for (Object data : rowData) {
            row.add(data);
        }
        this.rows.add(row);
    }

    /**
     * 获取数据行数
     *
     * @return 数据行数
     */
    public int getRowCount() {
        return this.rows.size();
    }

    /**
     * 获取表头数
     *
     * @return 表头数
     */
    public int getHeaderCount() {
        return this.headers.size();
    }

    /**
     * 清空数据
     */
    public void clear() {
        this.headers.clear();
        this.rows.clear();
    }
}