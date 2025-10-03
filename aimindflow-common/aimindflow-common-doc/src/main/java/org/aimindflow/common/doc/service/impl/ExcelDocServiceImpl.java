package org.aimindflow.common.doc.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.doc.service.DocService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

/**
 * Excel文档处理服务实现
 *
 * @author HezaoHezao
 */
@Slf4j
@Service
public class ExcelDocServiceImpl implements DocService {

    @Override
    public boolean convertToPdf(InputStream inputStream, OutputStream outputStream, String sourceFormat) {
        if (!"xlsx".equalsIgnoreCase(sourceFormat)) {
            log.error("不支持的源文档格式: {}", sourceFormat);
            return false;
        }

        try (Workbook workbook = new XSSFWorkbook(inputStream);
             PDDocument pdfDocument = new PDDocument()) {
            
            // 创建一个简单的PDF页面
            PDPage page = new PDPage(PDRectangle.A4);
            pdfDocument.addPage(page);
            
            // 提取文本内容
            String text = extractText(workbook);
            
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
            log.error("转换Excel文档到PDF失败", e);
            return false;
        }
    }

    @Override
    public String extractText(InputStream inputStream, String sourceFormat) {
        if (!"xlsx".equalsIgnoreCase(sourceFormat)) {
            log.error("不支持的源文档格式: {}", sourceFormat);
            return "";
        }

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            return extractText(workbook);
        } catch (IOException e) {
            log.error("提取Excel文档文本失败", e);
            return "";
        }
    }

    private String extractText(Workbook workbook) {
        StringBuilder text = new StringBuilder();
        
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            text.append("Sheet: ").append(sheet.getSheetName()).append("\n");
            
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cell.getCellType()) {
                        case STRING:
                            text.append(cell.getStringCellValue()).append("\t");
                            break;
                        case NUMERIC:
                            text.append(cell.getNumericCellValue()).append("\t");
                            break;
                        case BOOLEAN:
                            text.append(cell.getBooleanCellValue()).append("\t");
                            break;
                        case FORMULA:
                            text.append(cell.getCellFormula()).append("\t");
                            break;
                        default:
                            text.append("\t");
                    }
                }
                text.append("\n");
            }
            text.append("\n");
        }
        
        return text.toString();
    }

    @Override
    public boolean mergePdfs(InputStream[] inputStreams, OutputStream outputStream) {
        log.warn("Excel文档服务不支持合并PDF操作");
        return false;
    }

    @Override
    public boolean addWatermark(InputStream inputStream, OutputStream outputStream, String watermarkText) {
        log.warn("Excel文档服务不支持添加水印操作");
        return false;
    }
}