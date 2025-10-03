package org.aimindflow.common.mail.service;

import org.aimindflow.common.mail.entity.MailInfo;

import javax.mail.MessagingException;
import java.io.File;
import java.util.List;

/**
 * 邮件服务接口
 *
 * @author HezaoHezao
 */
public interface MailService {

    /**
     * 发送简单文本邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    void sendSimpleMail(String to, String subject, String content);

    /**
     * 发送简单文本邮件（带抄送）
     *
     * @param to      收件人
     * @param cc      抄送人
     * @param subject 主题
     * @param content 内容
     */
    void sendSimpleMail(String to, String cc, String subject, String content);

    /**
     * 发送简单文本邮件（带抄送和密送）
     *
     * @param to      收件人
     * @param cc      抄送人
     * @param bcc     密送人
     * @param subject 主题
     * @param content 内容
     */
    void sendSimpleMail(String to, String cc, String bcc, String subject, String content);

    /**
     * 发送HTML邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content HTML内容
     * @throws MessagingException 消息异常
     */
    void sendHtmlMail(String to, String subject, String content) throws MessagingException;

    /**
     * 发送HTML邮件（带抄送）
     *
     * @param to      收件人
     * @param cc      抄送人
     * @param subject 主题
     * @param content HTML内容
     * @throws MessagingException 消息异常
     */
    void sendHtmlMail(String to, String cc, String subject, String content) throws MessagingException;

    /**
     * 发送HTML邮件（带抄送和密送）
     *
     * @param to      收件人
     * @param cc      抄送人
     * @param bcc     密送人
     * @param subject 主题
     * @param content HTML内容
     * @throws MessagingException 消息异常
     */
    void sendHtmlMail(String to, String cc, String bcc, String subject, String content) throws MessagingException;

    /**
     * 发送带附件的邮件
     *
     * @param to       收件人
     * @param subject  主题
     * @param content  内容
     * @param filePath 附件路径
     * @throws MessagingException 消息异常
     */
    void sendAttachmentsMail(String to, String subject, String content, String filePath) throws MessagingException;

    /**
     * 发送带附件的邮件
     *
     * @param to        收件人
     * @param subject   主题
     * @param content   内容
     * @param filePaths 附件路径列表
     * @throws MessagingException 消息异常
     */
    void sendAttachmentsMail(String to, String subject, String content, List<String> filePaths) throws MessagingException;

    /**
     * 发送带附件的邮件
     *
     * @param to     收件人
     * @param subject 主题
     * @param content 内容
     * @param files   附件列表
     * @throws MessagingException 消息异常
     */
    void sendAttachmentsMail(String to, String subject, String content, File[] files) throws MessagingException;

    /**
     * 发送带附件的邮件（带抄送）
     *
     * @param to       收件人
     * @param cc       抄送人
     * @param subject  主题
     * @param content  内容
     * @param filePath 附件路径
     * @throws MessagingException 消息异常
     */
    void sendAttachmentsMail(String to, String cc, String subject, String content, String filePath) throws MessagingException;

    /**
     * 发送带附件的邮件（带抄送和密送）
     *
     * @param to       收件人
     * @param cc       抄送人
     * @param bcc      密送人
     * @param subject  主题
     * @param content  内容
     * @param filePath 附件路径
     * @throws MessagingException 消息异常
     */
    void sendAttachmentsMail(String to, String cc, String bcc, String subject, String content, String filePath) throws MessagingException;

    /**
     * 发送带图片的邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容（需要包含<img src="cid:imageId"/>的HTML内容）
     * @param rscPath 图片路径
     * @param rscId   图片ID
     * @throws MessagingException 消息异常
     */
    void sendInlineResourceMail(String to, String subject, String content, String rscPath, String rscId) throws MessagingException;

    /**
     * 发送带图片的邮件（带抄送）
     *
     * @param to      收件人
     * @param cc      抄送人
     * @param subject 主题
     * @param content 内容（需要包含<img src="cid:imageId"/>的HTML内容）
     * @param rscPath 图片路径
     * @param rscId   图片ID
     * @throws MessagingException 消息异常
     */
    void sendInlineResourceMail(String to, String cc, String subject, String content, String rscPath, String rscId) throws MessagingException;

    /**
     * 发送带图片的邮件（带抄送和密送）
     *
     * @param to      收件人
     * @param cc      抄送人
     * @param bcc     密送人
     * @param subject 主题
     * @param content 内容（需要包含<img src="cid:imageId"/>的HTML内容）
     * @param rscPath 图片路径
     * @param rscId   图片ID
     * @throws MessagingException 消息异常
     */
    void sendInlineResourceMail(String to, String cc, String bcc, String subject, String content, String rscPath, String rscId) throws MessagingException;

    /**
     * 发送模板邮件
     *
     * @param to       收件人
     * @param subject  主题
     * @param template 模板名称
     * @param model    模板参数
     * @throws MessagingException 消息异常
     */
    void sendTemplateMail(String to, String subject, String template, Object model) throws MessagingException;

    /**
     * 发送模板邮件（带抄送）
     *
     * @param to       收件人
     * @param cc       抄送人
     * @param subject  主题
     * @param template 模板名称
     * @param model    模板参数
     * @throws MessagingException 消息异常
     */
    void sendTemplateMail(String to, String cc, String subject, String template, Object model) throws MessagingException;

    /**
     * 发送模板邮件（带抄送和密送）
     *
     * @param to       收件人
     * @param cc       抄送人
     * @param bcc      密送人
     * @param subject  主题
     * @param template 模板名称
     * @param model    模板参数
     * @throws MessagingException 消息异常
     */
    void sendTemplateMail(String to, String cc, String bcc, String subject, String template, Object model) throws MessagingException;

    /**
     * 发送邮件
     *
     * @param mailInfo 邮件信息
     * @throws MessagingException 消息异常
     */
    void sendMail(MailInfo mailInfo) throws MessagingException;
}