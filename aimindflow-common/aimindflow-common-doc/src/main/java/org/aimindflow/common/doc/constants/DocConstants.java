package org.aimindflow.common.doc.constants;

/**
 * 文档处理常量类
 *
 * @author HezaoHezao
 */
public class DocConstants {

    /**
     * 文档格式常量
     */
    public static class Format {
        /** Word文档格式 - docx */
        public static final String DOCX = "docx";
        /** Word文档格式 - doc */
        public static final String DOC = "doc";
        /** Excel文档格式 - xlsx */
        public static final String XLSX = "xlsx";
        /** Excel文档格式 - xls */
        public static final String XLS = "xls";
        /** PDF文档格式 */
        public static final String PDF = "pdf";
    }

    /**
     * 文档操作类型常量
     */
    public static class OperationType {
        /** 转换为PDF */
        public static final String CONVERT_TO_PDF = "convertToPdf";
        /** 提取文本 */
        public static final String EXTRACT_TEXT = "extractText";
        /** 合并PDF */
        public static final String MERGE_PDFS = "mergePdfs";
        /** 添加水印 */
        public static final String ADD_WATERMARK = "addWatermark";
    }

    /**
     * 水印相关常量
     */
    public static class Watermark {
        /** 默认水印字体大小 */
        public static final float DEFAULT_FONT_SIZE = 30.0f;
        /** 默认水印透明度 */
        public static final float DEFAULT_OPACITY = 0.3f;
        /** 默认水印旋转角度 */
        public static final float DEFAULT_ROTATION = -30.0f;
    }

    /**
     * 错误消息常量
     */
    public static class ErrorMessage {
        /** 不支持的文档格式 */
        public static final String UNSUPPORTED_FORMAT = "不支持的文档格式: %s";
        /** 文档转换失败 */
        public static final String CONVERSION_FAILED = "文档转换失败: %s";
        /** 文本提取失败 */
        public static final String TEXT_EXTRACTION_FAILED = "文本提取失败: %s";
        /** PDF合并失败 */
        public static final String PDF_MERGE_FAILED = "PDF合并失败: %s";
        /** 添加水印失败 */
        public static final String ADD_WATERMARK_FAILED = "添加水印失败: %s";
    }
}