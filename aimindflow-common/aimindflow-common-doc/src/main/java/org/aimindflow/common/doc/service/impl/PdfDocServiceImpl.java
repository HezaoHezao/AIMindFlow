package org.aimindflow.common.doc.service.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.doc.service.DocService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * PDF文档处理服务实现
 *
 * @author HezaoHezao
 */
@Slf4j
@Service
public class PdfDocServiceImpl implements DocService {

    @Override
    public boolean convertToPdf(InputStream inputStream, OutputStream outputStream, String sourceFormat) {
        if (!"pdf".equalsIgnoreCase(sourceFormat)) {
            log.error("不支持的源文档格式: {}", sourceFormat);
            return false;
        }

        try {
            // 对于PDF格式，直接复制到输出流
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            return true;
        } catch (IOException e) {
            log.error("复制PDF文档失败", e);
            return false;
        }
    }

    @Override
    public String extractText(InputStream inputStream, String sourceFormat) {
        if (!"pdf".equalsIgnoreCase(sourceFormat)) {
            log.error("不支持的源文档格式: {}", sourceFormat);
            return "";
        }

        try (PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (IOException e) {
            log.error("提取PDF文档文本失败", e);
            return "";
        }
    }

    @Override
    public boolean mergePdfs(InputStream[] inputStreams, OutputStream outputStream) {
        if (inputStreams == null || inputStreams.length == 0) {
            log.error("没有提供输入流");
            return false;
        }

        Document document = new Document();
        try {
            PdfCopy copy = new PdfCopy(document, outputStream);
            document.open();

            for (InputStream inputStream : inputStreams) {
                PdfReader reader = new PdfReader(inputStream);
                int n = reader.getNumberOfPages();
                for (int i = 1; i <= n; i++) {
                    PdfImportedPage page = copy.getImportedPage(reader, i);
                    copy.addPage(page);
                }
                reader.close();
            }

            document.close();
            return true;
        } catch (IOException | DocumentException e) {
            log.error("合并PDF文档失败", e);
            return false;
        }
    }

    @Override
    public boolean addWatermark(InputStream inputStream, OutputStream outputStream, String watermarkText) {
        try {
            PdfReader reader = new PdfReader(inputStream);
            PdfStamper stamper = new PdfStamper(reader, outputStream);
            
            // 获取页数
            int pageCount = reader.getNumberOfPages();
            
            // 设置水印字体
            Font font = FontFactory.getFont(FontFactory.HELVETICA, 30);
            
            // 为每一页添加水印
            for (int i = 1; i <= pageCount; i++) {
                PdfContentByte content = stamper.getUnderContent(i);
                
                // 设置水印透明度
                PdfGState gs = new PdfGState();
                gs.setFillOpacity(0.3f);
                content.setGState(gs);
                
                // 获取页面尺寸
                com.itextpdf.text.Rectangle pageSize = reader.getPageSize(i);
                float width = pageSize.getWidth();
                float height = pageSize.getHeight();
                
                // 添加水印
                content.beginText();
                content.setFontAndSize(BaseFont.createFont(), 30);
                content.showTextAligned(Element.ALIGN_CENTER, watermarkText, width / 2, height / 2, 45);
                content.endText();
            }
            
            stamper.close();
            reader.close();
            return true;
        } catch (IOException | DocumentException e) {
            log.error("添加水印失败", e);
            return false;
        }
    }
}