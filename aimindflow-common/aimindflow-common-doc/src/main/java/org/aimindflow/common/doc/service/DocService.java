package org.aimindflow.common.doc.service;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文档处理服务接口
 *
 * @author HezaoHezao
 */
public interface DocService {

    /**
     * 将文档转换为PDF
     *
     * @param inputStream  输入文档流
     * @param outputStream 输出PDF流
     * @param sourceFormat 源文档格式（如docx, xlsx, pptx等）
     * @return 是否转换成功
     */
    boolean convertToPdf(InputStream inputStream, OutputStream outputStream, String sourceFormat);

    /**
     * 从文档中提取文本内容
     *
     * @param inputStream  输入文档流
     * @param sourceFormat 源文档格式
     * @return 提取的文本内容
     */
    String extractText(InputStream inputStream, String sourceFormat);

    /**
     * 合并多个PDF文档
     *
     * @param inputStreams 多个PDF输入流
     * @param outputStream 合并后的输出流
     * @return 是否合并成功
     */
    boolean mergePdfs(InputStream[] inputStreams, OutputStream outputStream);

    /**
     * 添加水印到PDF文档
     *
     * @param inputStream  输入PDF流
     * @param outputStream 输出PDF流
     * @param watermarkText 水印文本
     * @return 是否添加成功
     */
    boolean addWatermark(InputStream inputStream, OutputStream outputStream, String watermarkText);
}