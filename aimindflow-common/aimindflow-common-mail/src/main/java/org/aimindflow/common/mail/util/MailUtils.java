package org.aimindflow.common.mail.util;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.mail.entity.MailInfo;
import org.aimindflow.common.mail.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.io.File;
import java.util.List;

/**
 * 邮件工具类
 *
 * @author HezaoHezao
 */
@Slf4j
@Component
public class MailUtils {

    private static MailUtils instance;

    @Autowired
    private MailService mailService;

    @PostConstruct
    public void init() {
        instance = this;
        instance.mailService = this.mailService;
    }

    /**
     * 发送简单文本邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    public static void sendSimpleMail(String to, String subject, String content) {
        instance.mailService.sendSimpleMail(to, subject, content);
    }

    /**
     * 发送简单文本邮件（带抄送）
     *
     * @param to      收件人
     * @param cc      抄送人
     * @param subject 主题
     * @param content 内容
     */
    public static void sendSimpleMail(String to, String cc, String subject, String content) {
        instance.mailService.sendSimpleMail(to, cc, subject, content);
    }

    /**
     * 发送简单文本邮件（带抄送和密送）
     *
     * @param to      收件人
     * @param cc      抄送人
     * @param bcc     密送人
     * @param subject 主题
     * @param content 内容
     */
    public static void sendSimpleMail(String to, String cc, String bcc, String subject, String content) {
        instance.mailService.sendSimpleMail(to, cc, bcc, subject, content);
    }

    /**
     * 发送HTML邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content HTML内容
     * @throws MessagingException 消息异常
     */
    public static void sendHtmlMail(String to, String subject, String content) throws MessagingException {
        instance.mailService.sendHtmlMail(to, subject, content);
    }

    /**
     * 发送HTML邮件（带抄送）
     *
     * @param to      收件人
     * @param cc      抄送人
     * @param subject 主题
     * @param content HTML内容
     * @throws MessagingException 消息异常
     */
    public static void sendHtmlMail(String to, String cc, String subject, String content) throws MessagingException {
        instance.mailService.sendHtmlMail(to, cc, subject, content);
    }

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
    public static void sendHtmlMail(String to, String cc, String bcc, String subject, String content) throws MessagingException {
        instance.mailService.sendHtmlMail(to, cc, bcc, subject, content);
    }

    /**
     * 发送带附件的邮件
     *
     * @param to       收件人
     * @param subject  主题
     * @param content  内容
     * @param filePath 附件路径
     * @throws MessagingException 消息异常
     */
    public static void sendAttachmentsMail(String to, String subject, String content, String filePath) throws MessagingException {
        instance.mailService.sendAttachmentsMail(to, subject, content, filePath);
    }

    /**
     * 发送带附件的邮件
     *
     * @param to        收件人
     * @param subject   主题
     * @param content   内容
     * @param filePaths 附件路径列表
     * @throws MessagingException 消息异常
     */
    public static void sendAttachmentsMail(String to, String subject, String content, List<String> filePaths) throws MessagingException {
        instance.mailService.sendAttachmentsMail(to, subject, content, filePaths);
    }

    /**
     * 发送带附件的邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     * @param files   附件列表
     * @throws MessagingException 消息异常
     */
    public static void sendAttachmentsMail(String to, String subject, String content, File[] files) throws MessagingException {
        instance.mailService.sendAttachmentsMail(to, subject, content, files);
    }

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
    public static void sendAttachmentsMail(String to, String cc, String subject, String content, String filePath) throws MessagingException {
        instance.mailService.sendAttachmentsMail(to, cc, subject, content, filePath);
    }

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
    public static void sendAttachmentsMail(String to, String cc, String bcc, String subject, String content, String filePath) throws MessagingException {
        instance.mailService.sendAttachmentsMail(to, cc, bcc, subject, content, filePath);
    }

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
    public static void sendInlineResourceMail(String to, String subject, String content, String rscPath, String rscId) throws MessagingException {
        instance.mailService.sendInlineResourceMail(to, subject, content, rscPath, rscId);
    }

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
    public static void sendInlineResourceMail(String to, String cc, String subject, String content, String rscPath, String rscId) throws MessagingException {
        instance.mailService.sendInlineResourceMail(to, cc, subject, content, rscPath, rscId);
    }

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
    public static void sendInlineResourceMail(String to, String cc, String bcc, String subject, String content, String rscPath, String rscId) throws MessagingException {
        instance.mailService.sendInlineResourceMail(to, cc, bcc, subject, content, rscPath, rscId);
    }

    /**
     * 发送模板邮件
     *
     * @param to       收件人
     * @param subject  主题
     * @param template 模板名称
     * @param model    模板参数
     * @throws MessagingException 消息异常
     */
    public static void sendTemplateMail(String to, String subject, String template, Object model) throws MessagingException {
        instance.mailService.sendTemplateMail(to, subject, template, model);
    }

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
    public static void sendTemplateMail(String to, String cc, String subject, String template, Object model) throws MessagingException {
        instance.mailService.sendTemplateMail(to, cc, subject, template, model);
    }

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
    public static void sendTemplateMail(String to, String cc, String bcc, String subject, String template, Object model) throws MessagingException {
        instance.mailService.sendTemplateMail(to, cc, bcc, subject, template, model);
    }

    /**
     * 发送邮件
     *
     * @param mailInfo 邮件信息
     * @throws MessagingException 消息异常
     */
    public static void sendMail(MailInfo mailInfo) throws MessagingException {
        instance.mailService.sendMail(mailInfo);
    }
}