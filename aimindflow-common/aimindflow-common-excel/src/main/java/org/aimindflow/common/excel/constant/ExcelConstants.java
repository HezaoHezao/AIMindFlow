package org.aimindflow.common.excel.constant;

/**
 * Excel常量类
 *
 * @author HezaoHezao
 */
public class ExcelConstants {

    /**
     * Excel文件类型
     */
    public static final String EXCEL_XLS = "xls";
    public static final String EXCEL_XLSX = "xlsx";

    /**
     * Excel内容类型
     */
    public static final String CONTENT_TYPE_XLS = "application/vnd.ms-excel";
    public static final String CONTENT_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    /**
     * 默认Sheet名称
     */
    public static final String DEFAULT_SHEET_NAME = "Sheet1";

    /**
     * 默认列宽
     */
    public static final int DEFAULT_COLUMN_WIDTH = 15;

    /**
     * 最大列宽
     */
    public static final int MAX_COLUMN_WIDTH = 255;

    /**
     * 默认行高
     */
    public static final short DEFAULT_ROW_HEIGHT = 400;

    /**
     * 默认字体
     */
    public static final String DEFAULT_FONT_NAME = "宋体";

    /**
     * 默认字体大小
     */
    public static final short DEFAULT_FONT_SIZE = 11;

    /**
     * 错误消息
     */
    public static final String ERROR_FILE_TYPE = "不支持的Excel文件类型";
    public static final String ERROR_FILE_READ = "Excel文件读取失败";
    public static final String ERROR_FILE_WRITE = "Excel文件写入失败";
    public static final String ERROR_TEMPLATE_NOT_FOUND = "Excel模板文件不存在";
    public static final String ERROR_DATA_FORMAT = "Excel数据格式错误";
    public static final String ERROR_CELL_TYPE = "Excel单元格类型错误";
    public static final String ERROR_EXPORT = "Excel导出失败";
    public static final String ERROR_IMPORT = "Excel导入失败";

    /**
     * 私有构造函数，防止实例化
     */
    private ExcelConstants() {
        throw new IllegalStateException("Constant class");
    }
}