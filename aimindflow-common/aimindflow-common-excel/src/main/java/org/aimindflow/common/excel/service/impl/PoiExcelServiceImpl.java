package org.aimindflow.common.excel.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.excel.service.ExcelService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于POI的Excel服务实现
 *
 * @author HezaoHezao
 */
@Slf4j
@Service
public class PoiExcelServiceImpl implements ExcelService {

    @Override
    public boolean exportExcel(String templatePath, Map<String, Object> data, OutputStream outputStream) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(templatePath);
             Workbook workbook = WorkbookFactory.create(is)) {

            // 遍历所有工作表
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                // 遍历所有行
                for (int j = 0; j <= sheet.getLastRowNum(); j++) {
                    Row row = sheet.getRow(j);
                    if (row != null) {
                        // 遍历所有单元格
                        for (int k = 0; k < row.getLastCellNum(); k++) {
                            Cell cell = row.getCell(k);
                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                String cellValue = cell.getStringCellValue();
                                // 替换模板中的占位符
                                for (Map.Entry<String, Object> entry : data.entrySet()) {
                                    String placeholder = "${" + entry.getKey() + "}";
                                    if (cellValue.contains(placeholder)) {
                                        cellValue = cellValue.replace(placeholder, entry.getValue().toString());
                                        cell.setCellValue(cellValue);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            workbook.write(outputStream);
            return true;
        } catch (IOException e) {
            log.error("导出Excel失败", e);
            return false;
        }
    }

    @Override
    public <T> boolean exportExcel(String sheetName, String[] headers, List<T> dataList, OutputStream outputStream) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName);

            // 创建表头样式
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // 创建表头
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // 填充数据
            int rowNum = 1;
            for (T data : dataList) {
                Row row = sheet.createRow(rowNum++);
                if (data instanceof Map) {
                    // 处理Map类型数据
                    Map<?, ?> map = (Map<?, ?>) data;
                    int colNum = 0;
                    for (String header : headers) {
                        Cell cell = row.createCell(colNum++);
                        Object value = map.get(header);
                        setCellValue(cell, value);
                    }
                } else {
                    // 处理普通对象
                    Field[] fields = data.getClass().getDeclaredFields();
                    int colNum = 0;
                    for (Field field : fields) {
                        if (colNum >= headers.length) {
                            break;
                        }
                        field.setAccessible(true);
                        Cell cell = row.createCell(colNum++);
                        try {
                            Object value = field.get(data);
                            setCellValue(cell, value);
                        } catch (IllegalAccessException e) {
                            log.error("获取字段值失败", e);
                            cell.setCellValue("");
                        }
                    }
                }
            }

            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return true;
        } catch (IOException e) {
            log.error("导出Excel失败", e);
            return false;
        }
    }

    @Override
    public <T> boolean exportExcel(String sheetName, List<T> dataList, Class<T> clazz, OutputStream outputStream) {
        // 获取类的所有字段作为表头
        Field[] fields = clazz.getDeclaredFields();
        String[] headers = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            headers[i] = fields[i].getName();
        }

        return exportExcel(sheetName, headers, dataList, outputStream);
    }

    @Override
    public <T> List<T> importExcel(InputStream inputStream, Class<T> clazz) {
        List<T> dataList = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            // 获取表头
            Row headerRow = sheet.getRow(0);
            int cellCount = headerRow.getLastCellNum();
            String[] headers = new String[cellCount];
            for (int i = 0; i < cellCount; i++) {
                Cell cell = headerRow.getCell(i);
                headers[i] = cell.getStringCellValue();
            }

            // 获取数据
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                try {
                    T obj = clazz.newInstance();
                    for (int j = 0; j < cellCount; j++) {
                        Cell cell = row.getCell(j);
                        if (cell == null) {
                            continue;
                        }

                        String fieldName = headers[j];
                        try {
                            Field field = clazz.getDeclaredField(fieldName);
                            field.setAccessible(true);
                            Object value = getCellValue(cell, field.getType());
                            field.set(obj, value);
                        } catch (NoSuchFieldException e) {
                            log.warn("字段不存在: {}", fieldName);
                        }
                    }
                    dataList.add(obj);
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error("创建对象实例失败", e);
                }
            }
        } catch (IOException e) {
            log.error("导入Excel失败", e);
        }

        return dataList;
    }

    @Override
    public List<Map<String, Object>> importExcel(InputStream inputStream, String[] headers) {
        List<Map<String, Object>> dataList = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            // 获取数据
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                Map<String, Object> data = new HashMap<>();
                for (int j = 0; j < headers.length; j++) {
                    Cell cell = row.getCell(j);
                    if (cell == null) {
                        data.put(headers[j], null);
                    } else {
                        data.put(headers[j], getCellValue(cell, String.class));
                    }
                }
                dataList.add(data);
            }
        } catch (IOException e) {
            log.error("导入Excel失败", e);
        }

        return dataList;
    }

    @Override
    public String readCell(InputStream inputStream, int sheetIndex, int rowIndex, int columnIndex) {
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                return null;
            }
            Cell cell = row.getCell(columnIndex);
            if (cell == null) {
                return null;
            }
            return getCellValue(cell, String.class).toString();
        } catch (IOException e) {
            log.error("读取Excel单元格失败", e);
            return null;
        }
    }

    @Override
    public List<String> readRow(InputStream inputStream, int sheetIndex, int rowIndex) {
        List<String> rowData = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                return rowData;
            }

            for (int i = 0; i < row.getLastCellNum(); i++) {
                Cell cell = row.getCell(i);
                if (cell == null) {
                    rowData.add("");
                } else {
                    rowData.add(getCellValue(cell, String.class).toString());
                }
            }
        } catch (IOException e) {
            log.error("读取Excel行失败", e);
        }

        return rowData;
    }

    @Override
    public List<String> readColumn(InputStream inputStream, int sheetIndex, int columnIndex) {
        List<String> columnData = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);

            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    columnData.add("");
                    continue;
                }

                Cell cell = row.getCell(columnIndex);
                if (cell == null) {
                    columnData.add("");
                } else {
                    columnData.add(getCellValue(cell, String.class).toString());
                }
            }
        } catch (IOException e) {
            log.error("读取Excel列失败", e);
        }

        return columnData;
    }

    /**
     * 设置单元格值
     *
     * @param cell  单元格
     * @param value 值
     */
    private void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue(value.toString());
        }
    }

    /**
     * 获取单元格值
     *
     * @param cell      单元格
     * @param valueType 值类型
     * @return 单元格值
     */
    private Object getCellValue(Cell cell, Class<?> valueType) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (valueType == Integer.class) {
                    return (int) cell.getNumericCellValue();
                } else if (valueType == Long.class) {
                    return (long) cell.getNumericCellValue();
                } else if (valueType == Double.class) {
                    return cell.getNumericCellValue();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}