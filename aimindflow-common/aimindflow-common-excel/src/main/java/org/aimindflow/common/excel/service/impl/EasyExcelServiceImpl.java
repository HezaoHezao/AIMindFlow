package org.aimindflow.common.excel.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.excel.service.ExcelService;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于EasyExcel的Excel服务实现
 *
 * @author HezaoHezao
 */
@Slf4j
@Service
public class EasyExcelServiceImpl implements ExcelService {

    @Override
    public boolean exportExcel(String templatePath, Map<String, Object> data, OutputStream outputStream) {
        log.warn("EasyExcel不支持基于模板的导出，请使用PoiExcelService");
        return false;
    }

    @Override
    public <T> boolean exportExcel(String sheetName, String[] headers, List<T> dataList, OutputStream outputStream) {
        try {
            // 将数据转换为Map列表
            List<Map<String, Object>> mapList = new ArrayList<>();
            for (T data : dataList) {
                if (data instanceof Map) {
                    mapList.add((Map<String, Object>) data);
                } else {
                    log.warn("EasyExcel导出非Map对象时，请使用带Class参数的方法");
                    return false;
                }
            }

            // 创建表头
            List<List<String>> headList = new ArrayList<>();
            for (String header : headers) {
                List<String> head = new ArrayList<>();
                head.add(header);
                headList.add(head);
            }

            // 创建数据列表
            List<List<Object>> dataRows = new ArrayList<>();
            for (Map<String, Object> map : mapList) {
                List<Object> row = new ArrayList<>();
                for (String header : headers) {
                    row.add(map.get(header));
                }
                dataRows.add(row);
            }

            // 导出Excel
            EasyExcel.write(outputStream)
                    .sheet(sheetName)
                    .head(headList)
                    .doWrite(dataRows);

            return true;
        } catch (Exception e) {
            log.error("EasyExcel导出失败", e);
            return false;
        }
    }

    @Override
    public <T> boolean exportExcel(String sheetName, List<T> dataList, Class<T> clazz, OutputStream outputStream) {
        try {
            EasyExcel.write(outputStream, clazz)
                    .sheet(sheetName)
                    .doWrite(dataList);
            return true;
        } catch (Exception e) {
            log.error("EasyExcel导出失败", e);
            return false;
        }
    }

    @Override
    public <T> List<T> importExcel(InputStream inputStream, Class<T> clazz) {
        List<T> dataList = new ArrayList<>();

        try {
            EasyExcel.read(inputStream, clazz, new AnalysisEventListener<T>() {
                @Override
                public void invoke(T data, AnalysisContext context) {
                    dataList.add(data);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                    log.info("Excel解析完成，共{}条数据", dataList.size());
                }
            }).sheet().doRead();
        } catch (Exception e) {
            log.error("EasyExcel导入失败", e);
        }

        return dataList;
    }

    @Override
    public List<Map<String, Object>> importExcel(InputStream inputStream, String[] headers) {
        List<Map<String, Object>> dataList = new ArrayList<>();

        try {
            EasyExcel.read(inputStream, new AnalysisEventListener<Map<Integer, String>>() {
                @Override
                public void invoke(Map<Integer, String> data, AnalysisContext context) {
                    Map<String, Object> rowData = new HashMap<>();
                    for (int i = 0; i < headers.length; i++) {
                        rowData.put(headers[i], data.get(i));
                    }
                    dataList.add(rowData);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                    log.info("Excel解析完成，共{}条数据", dataList.size());
                }
            }).sheet().doRead();
        } catch (Exception e) {
            log.error("EasyExcel导入失败", e);
        }

        return dataList;
    }

    @Override
    public String readCell(InputStream inputStream, int sheetIndex, int rowIndex, int columnIndex) {
        List<List<String>> data = new ArrayList<>();

        try {
            EasyExcel.read(inputStream, new AnalysisEventListener<Map<Integer, String>>() {
                @Override
                public void invoke(Map<Integer, String> rowData, AnalysisContext context) {
                    if (context.readRowHolder().getRowIndex() == rowIndex) {
                        List<String> row = new ArrayList<>();
                        for (int i = 0; i <= columnIndex; i++) {
                            row.add(rowData.getOrDefault(i, ""));
                        }
                        data.add(row);
                    }
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                }
            }).sheet(sheetIndex).doRead();

            if (!data.isEmpty() && data.get(0).size() > columnIndex) {
                return data.get(0).get(columnIndex);
            }
        } catch (Exception e) {
            log.error("EasyExcel读取单元格失败", e);
        }

        return null;
    }

    @Override
    public List<String> readRow(InputStream inputStream, int sheetIndex, int rowIndex) {
        List<List<String>> data = new ArrayList<>();

        try {
            EasyExcel.read(inputStream, new AnalysisEventListener<Map<Integer, String>>() {
                @Override
                public void invoke(Map<Integer, String> rowData, AnalysisContext context) {
                    if (context.readRowHolder().getRowIndex() == rowIndex) {
                        List<String> row = new ArrayList<>();
                        int maxColumnIndex = rowData.keySet().stream().mapToInt(Integer::intValue).max().orElse(-1);
                        for (int i = 0; i <= maxColumnIndex; i++) {
                            row.add(rowData.getOrDefault(i, ""));
                        }
                        data.add(row);
                    }
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                }
            }).sheet(sheetIndex).doRead();

            if (!data.isEmpty()) {
                return data.get(0);
            }
        } catch (Exception e) {
            log.error("EasyExcel读取行失败", e);
        }

        return new ArrayList<>();
    }

    @Override
    public List<String> readColumn(InputStream inputStream, int sheetIndex, int columnIndex) {
        List<String> columnData = new ArrayList<>();

        try {
            EasyExcel.read(inputStream, new AnalysisEventListener<Map<Integer, String>>() {
                @Override
                public void invoke(Map<Integer, String> rowData, AnalysisContext context) {
                    columnData.add(rowData.getOrDefault(columnIndex, ""));
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                }
            }).sheet(sheetIndex).doRead();
        } catch (Exception e) {
            log.error("EasyExcel读取列失败", e);
        }

        return columnData;
    }
}