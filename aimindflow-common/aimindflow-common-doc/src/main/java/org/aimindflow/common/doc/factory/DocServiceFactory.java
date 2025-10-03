package org.aimindflow.common.doc.factory;

import org.aimindflow.common.doc.service.DocService;
import org.aimindflow.common.doc.service.impl.ExcelDocServiceImpl;
import org.aimindflow.common.doc.service.impl.PdfDocServiceImpl;
import org.aimindflow.common.doc.service.impl.WordDocServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 文档服务工厂类
 *
 * @author HezaoHezao
 */
@Component
public class DocServiceFactory {

    private final WordDocServiceImpl wordDocService;
    private final ExcelDocServiceImpl excelDocService;
    private final PdfDocServiceImpl pdfDocService;

    @Autowired
    public DocServiceFactory(WordDocServiceImpl wordDocService, 
                            ExcelDocServiceImpl excelDocService, 
                            PdfDocServiceImpl pdfDocService) {
        this.wordDocService = wordDocService;
        this.excelDocService = excelDocService;
        this.pdfDocService = pdfDocService;
    }

    /**
     * 根据文档格式获取对应的文档服务
     *
     * @param format 文档格式（如docx, xlsx, pdf等）
     * @return 文档服务实例
     */
    public DocService getDocService(String format) {
        if (format == null || format.isEmpty()) {
            throw new IllegalArgumentException("文档格式不能为空");
        }

        format = format.toLowerCase();
        switch (format) {
            case "docx":
            case "doc":
                return wordDocService;
            case "xlsx":
            case "xls":
                return excelDocService;
            case "pdf":
                return pdfDocService;
            default:
                throw new IllegalArgumentException("不支持的文档格式: " + format);
        }
    }
}