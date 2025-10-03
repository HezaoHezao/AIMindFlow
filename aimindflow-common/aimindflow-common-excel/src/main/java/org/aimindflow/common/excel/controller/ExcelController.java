package org.aimindflow.common.excel.controller;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.core.domain.R;
import org.aimindflow.common.excel.constant.ExcelConstants;
import org.aimindflow.common.excel.service.ExcelService;
import org.aimindflow.common.excel.service.ExcelServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel控制器
 *
 * @author HezaoHezao
 */
@Slf4j
@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private ExcelServiceFactory excelServiceFactory;

    /**
     * 导出Excel示例
     *
     * @param response HTTP响应
     */
    @GetMapping("/export/example")
    public void exportExample(HttpServletResponse response) {
        try {
            // 设置响应头
            String fileName = URLEncoder.encode("示例Excel", StandardCharsets.UTF_8.name());
            response.setContentType(ExcelConstants.CONTENT_TYPE_XLSX);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + ".xlsx");

            // 准备数据
            String[] headers = {"姓名", "年龄", "性别", "邮箱"};
            List<Map<String, Object>> dataList = new ArrayList<>();

            for (int i = 1; i <= 10; i++) {
                Map<String, Object> data = new HashMap<>();
                data.put("姓名", "用户" + i);
                data.put("年龄", 20 + i);
                data.put("性别", i % 2 == 0 ? "男" : "女");
                data.put("邮箱", "user" + i + "@example.com");
                dataList.add(data);
            }

            // 导出Excel
            ExcelService excelService = excelServiceFactory.getDefaultExcelService();
            excelService.exportExcel(ExcelConstants.DEFAULT_SHEET_NAME, headers, dataList, response.getOutputStream());
        } catch (IOException e) {
            log.error("导出Excel示例失败", e);
        }
    }

    /**
     * 导入Excel
     *
     * @param file Excel文件
     * @return 导入结果
     */
    @PostMapping("/import")
    public R<List<Map<String, Object>>> importExcel(@RequestParam("file") MultipartFile file) {
        try {
            // 检查文件类型
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || (!originalFilename.endsWith(".xlsx") && !originalFilename.endsWith(".xls"))) {
                return R.fail("请上传Excel文件");
            }

            // 导入Excel
            String[] headers = {"姓名", "年龄", "性别", "邮箱"};
            ExcelService excelService = excelServiceFactory.getDefaultExcelService();
            List<Map<String, Object>> dataList = excelService.importExcel(file.getInputStream(), headers);

            return R.ok(dataList);
        } catch (IOException e) {
            log.error("导入Excel失败", e);
            return R.fail("导入Excel失败: " + e.getMessage());
        }
    }

    /**
     * 下载Excel模板
     *
     * @return Excel模板
     */
    @GetMapping("/template")
    public ResponseEntity<byte[]> downloadTemplate() {
        try {
            // 准备数据
            String[] headers = {"姓名", "年龄", "性别", "邮箱"};
            List<Map<String, Object>> dataList = new ArrayList<>();

            // 添加一行示例数据
            Map<String, Object> data = new HashMap<>();
            data.put("姓名", "示例用户");
            data.put("年龄", 25);
            data.put("性别", "男");
            data.put("邮箱", "example@example.com");
            dataList.add(data);

            // 导出Excel
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ExcelService excelService = excelServiceFactory.getDefaultExcelService();
            excelService.exportExcel("模板", headers, dataList, outputStream);

            // 设置响应头
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.parseMediaType(ExcelConstants.CONTENT_TYPE_XLSX));
            String fileName = URLEncoder.encode("Excel导入模板", StandardCharsets.UTF_8.name());
            responseHeaders.setContentDispositionFormData("attachment", fileName + ".xlsx");

            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            log.error("下载Excel模板失败", e);
            return ResponseEntity.badRequest().build();
        }
    }
}