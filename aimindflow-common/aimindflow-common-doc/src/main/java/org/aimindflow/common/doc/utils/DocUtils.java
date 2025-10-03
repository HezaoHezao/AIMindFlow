package org.aimindflow.common.doc.utils;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.doc.factory.DocServiceFactory;
import org.aimindflow.common.doc.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文档工具类
 *
 * @author HezaoHezao
 */
@Slf4j
@Component
public class DocUtils {

    private final DocServiceFactory docServiceFactory;

    @Autowired
    public DocUtils(DocServiceFactory docServiceFactory) {
        this.docServiceFactory = docServiceFactory;
    }

    /**
     * 将文档转换为PDF
     *
     * @param sourceFile 源文档文件
     * @param targetFile 目标PDF文件
     * @return 是否转换成功
     */
    public boolean convertToPdf(File sourceFile, File targetFile) {
        String sourceFormat = getFileExtension(sourceFile.getName());
        DocService docService = docServiceFactory.getDocService(sourceFormat);

        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(targetFile)) {
            return docService.convertToPdf(fis, fos, sourceFormat);
        } catch (IOException e) {
            log.error("转换文档到PDF失败", e);
            return false;
        }
    }

    /**
     * 从文档中提取文本内容
     *
     * @param file 文档文件
     * @return 提取的文本内容
     */
    public String extractText(File file) {
        String format = getFileExtension(file.getName());
        DocService docService = docServiceFactory.getDocService(format);

        try (FileInputStream fis = new FileInputStream(file)) {
            return docService.extractText(fis, format);
        } catch (IOException e) {
            log.error("提取文档文本失败", e);
            return "";
        }
    }

    /**
     * 合并多个PDF文件
     *
     * @param pdfFiles    PDF文件数组
     * @param targetFile  合并后的目标文件
     * @return 是否合并成功
     */
    public boolean mergePdfs(File[] pdfFiles, File targetFile) {
        DocService docService = docServiceFactory.getDocService("pdf");

        try (FileOutputStream fos = new FileOutputStream(targetFile)) {
            InputStream[] inputStreams = new InputStream[pdfFiles.length];
            
            for (int i = 0; i < pdfFiles.length; i++) {
                inputStreams[i] = new FileInputStream(pdfFiles[i]);
            }
            
            boolean result = docService.mergePdfs(inputStreams, fos);
            
            // 关闭所有输入流
            for (InputStream is : inputStreams) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error("关闭输入流失败", e);
                }
            }
            
            return result;
        } catch (IOException e) {
            log.error("合并PDF文件失败", e);
            return false;
        }
    }

    /**
     * 添加水印到PDF文档
     *
     * @param pdfFile       PDF文件
     * @param targetFile    添加水印后的目标文件
     * @param watermarkText 水印文本
     * @return 是否添加成功
     */
    public boolean addWatermark(File pdfFile, File targetFile, String watermarkText) {
        DocService docService = docServiceFactory.getDocService("pdf");

        try (FileInputStream fis = new FileInputStream(pdfFile);
             FileOutputStream fos = new FileOutputStream(targetFile)) {
            return docService.addWatermark(fis, fos, watermarkText);
        } catch (IOException e) {
            log.error("添加水印失败", e);
            return false;
        }
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName 文件名
     * @return 文件扩展名（不包含点）
     */
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        throw new IllegalArgumentException("文件名没有扩展名: " + fileName);
    }
}