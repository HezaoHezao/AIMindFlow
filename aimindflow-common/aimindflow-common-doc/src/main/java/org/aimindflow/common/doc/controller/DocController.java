package org.aimindflow.common.doc.controller;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.core.domain.R;
import org.aimindflow.common.doc.factory.DocServiceFactory;
import org.aimindflow.common.doc.service.DocService;
import org.aimindflow.common.doc.utils.DocUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 文档处理控制器
 *
 * @author HezaoHezao
 */
@Slf4j
@RestController
@RequestMapping("/doc")
public class DocController {

    private final DocUtils docUtils;
    private final DocServiceFactory docServiceFactory;

    @Autowired
    public DocController(DocUtils docUtils, DocServiceFactory docServiceFactory) {
        this.docUtils = docUtils;
        this.docServiceFactory = docServiceFactory;
    }

    /**
     * 将文档转换为PDF
     *
     * @param file     文档文件
     * @param response HTTP响应
     * @return 操作结果
     */
    @PostMapping("/convert-to-pdf")
    public R<?> convertToPdf(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        if (file.isEmpty()) {
            return R.fail("请选择要转换的文件");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            return R.fail("文件名不能为空");
        }

        try {
            // 创建临时目录
            Path tempDir = Files.createTempDirectory("doc_convert_");
            File sourceFile = tempDir.resolve(originalFilename).toFile();
            String pdfFileName = UUID.randomUUID() + ".pdf";
            File targetFile = tempDir.resolve(pdfFileName).toFile();

            // 保存上传的文件
            file.transferTo(sourceFile);

            // 转换为PDF
            boolean success = docUtils.convertToPdf(sourceFile, targetFile);
            if (!success) {
                return R.fail("文档转换失败");
            }

            // 设置响应头
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=" + pdfFileName);

            // 将PDF文件写入响应
            try (InputStream is = new FileInputStream(targetFile);
                 OutputStream os = response.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }

            // 删除临时文件
            sourceFile.delete();
            targetFile.delete();
            Files.delete(tempDir);

            return R.ok();
        } catch (IOException e) {
            log.error("文档转换失败", e);
            return R.fail("文档转换失败: " + e.getMessage());
        }
    }

    /**
     * 从文档中提取文本
     *
     * @param file 文档文件
     * @return 提取的文本内容
     */
    @PostMapping("/extract-text")
    public R<String> extractText(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return R.fail("请选择要提取文本的文件");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            return R.fail("文件名不能为空");
        }

        try {
            // 创建临时文件
            Path tempDir = Files.createTempDirectory("doc_extract_");
            File sourceFile = tempDir.resolve(originalFilename).toFile();

            // 保存上传的文件
            file.transferTo(sourceFile);

            // 提取文本
            String text = docUtils.extractText(sourceFile);

            // 删除临时文件
            sourceFile.delete();
            Files.delete(tempDir);

            return R.ok(text);
        } catch (IOException e) {
            log.error("文本提取失败", e);
            return R.fail("文本提取失败: " + e.getMessage());
        }
    }
}