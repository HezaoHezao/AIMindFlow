package org.aimindflow.common.doc.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.doc.service.DocService;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Word文档处理服务实现
 *
 * @author HezaoHezao
 */
@Slf4j
@Service
public class WordDocServiceImpl implements DocService {

    @Override
    public boolean convertToPdf(InputStream inputStream, OutputStream outputStream, String sourceFormat) {
        if (!"docx".equalsIgnoreCase(sourceFormat)) {
            log.error("不支持的源文档格式: {}", sourceFormat);
            return false;
        }

        try (XWPFDocument document = new XWPFDocument(inputStream);
             PDDocument pdfDocument = new PDDocument()) {
            
            // 创建一个简单的PDF页面
            PDPage page = new PDPage(PDRectangle.A4);
            pdfDocument.addPage(page);
            
            // 提取文本内容
            String text = extractText(document);
            
            // 将文本写入PDF
            try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, 700);
                contentStream.showText(text.length() > 100 ? text.substring(0, 100) + "..." : text);
                contentStream.endText();
            }
            
            pdfDocument.save(outputStream);
            return true;
        } catch (IOException e) {
            log.error("转换Word文档到PDF失败", e);
            return false;
        }
    }

    @Override
    public String extractText(InputStream inputStream, String sourceFormat) {
        if (!"docx".equalsIgnoreCase(sourceFormat)) {
            log.error("不支持的源文档格式: {}", sourceFormat);
            return "";
        }

        try (XWPFDocument document = new XWPFDocument(inputStream)) {
            return extractText(document);
        } catch (IOException e) {
            log.error("提取Word文档文本失败", e);
            return "";
        }
    }

    private String extractText(XWPFDocument document) {
        try (XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            return extractor.getText();
        } catch (IOException e) {
            log.error("提取Word文档文本失败", e);
            return "";
        }
    }

    @Override
    public boolean mergePdfs(InputStream[] inputStreams, OutputStream outputStream) {
        log.warn("Word文档服务不支持合并PDF操作");
        return false;
    }

    @Override
    public boolean addWatermark(InputStream inputStream, OutputStream outputStream, String watermarkText) {
        try (PDDocument document = PDDocument.load(inputStream)) {
            for (PDPage page : document.getPages()) {
                try (PDPageContentStream contentStream = new PDPageContentStream(
                        document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                    
                    // 设置水印文本
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 30);
                    contentStream.setNonStrokingColor(200, 200, 200); // 浅灰色
                    
                    // 计算页面中心位置
                    PDRectangle pageSize = page.getMediaBox();
                    float centerX = pageSize.getWidth() / 2;
                    float centerY = pageSize.getHeight() / 2;
                    
                    // 在页面中心添加水印
                    contentStream.newLineAtOffset(centerX - (watermarkText.length() * 8), centerY);
                    contentStream.showText(watermarkText);
                    contentStream.endText();
                }
            }
            
            document.save(outputStream);
            return true;
        } catch (IOException e) {
            log.error("添加水印失败", e);
            return false;
        }
    }
}