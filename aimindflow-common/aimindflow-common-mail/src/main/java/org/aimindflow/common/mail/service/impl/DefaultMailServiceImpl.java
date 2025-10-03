package org.aimindflow.common.mail.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.mail.constant.MailConstants;
import org.aimindflow.common.mail.entity.MailInfo;
import org.aimindflow.common.mail.exception.MailException;
import org.aimindflow.common.mail.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 默认邮件服务实现类
 *
 * @author HezaoHezao
 */
@Slf4j
@Service
public class DefaultMailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired(required = false)
    private TemplateEngine templateEngine;

    @Autowired(required = false)
    private freemarker.template.Configuration freemarkerConfig;

    @Value("${spring.mail.username:}")
    private String from;

    @Override
    public void sendSimpleMail(String to, String subject, String content) {
        sendSimpleMail(to, null, null, subject, content);
    }

    @Override
    public void sendSimpleMail(String to, String cc, String subject, String content) {
        sendSimpleMail(to, cc, null, subject, content);
    }

    @Override
    public void sendSimpleMail(String to, String cc, String bcc, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to.split(","));
        if (StringUtils.hasText(cc)) {
            message.setCc(cc.split(","));
        }
        if (StringUtils.hasText(bcc)) {
            message.setBcc(bcc.split(","));
        }
        message.setSubject(subject);
        message.setText(content);
        message.setSentDate(new Date());

        try {
            mailSender.send(message);
            log.info("简单邮件发送成功，收件人：{}，主题：{}", to, subject);
        } catch (Exception e) {
            log.error("简单邮件发送失败，收件人：{}，主题：{}", to, subject, e);
            throw new MailException(MailConstants.ERROR_CODE_SEND_FAIL, MailConstants.ERROR_MESSAGE_SEND_FAIL, e);
        }
    }

    @Override
    public void sendHtmlMail(String to, String subject, String content) throws MessagingException {
        sendHtmlMail(to, null, null, subject, content);
    }

    @Override
    public void sendHtmlMail(String to, String cc, String subject, String content) throws MessagingException {
        sendHtmlMail(to, cc, null, subject, content);
    }

    @Override
    public void sendHtmlMail(String to, String cc, String bcc, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to.split(","));
        if (StringUtils.hasText(cc)) {
            helper.setCc(cc.split(","));
        }
        if (StringUtils.hasText(bcc)) {
            helper.setBcc(bcc.split(","));
        }
        helper.setSubject(subject);
        helper.setText(content, true);
        helper.setSentDate(new Date());

        try {
            mailSender.send(message);
            log.info("HTML邮件发送成功，收件人：{}，主题：{}", to, subject);
        } catch (Exception e) {
            log.error("HTML邮件发送失败，收件人：{}，主题：{}", to, subject, e);
            throw new MailException(MailConstants.ERROR_CODE_SEND_FAIL, MailConstants.ERROR_MESSAGE_SEND_FAIL, e);
        }
    }

    @Override
    public void sendAttachmentsMail(String to, String subject, String content, String filePath) throws MessagingException {
        sendAttachmentsMail(to, null, null, subject, content, filePath);
    }

    @Override
    public void sendAttachmentsMail(String to, String subject, String content, List<String> filePaths) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to.split(","));
        helper.setSubject(subject);
        helper.setText(content, true);
        helper.setSentDate(new Date());

        for (String filePath : filePaths) {
            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = file.getFilename();
            helper.addAttachment(fileName, file);
        }

        try {
            mailSender.send(message);
            log.info("带附件的邮件发送成功，收件人：{}，主题：{}", to, subject);
        } catch (Exception e) {
            log.error("带附件的邮件发送失败，收件人：{}，主题：{}", to, subject, e);
            throw new MailException(MailConstants.ERROR_CODE_SEND_FAIL, MailConstants.ERROR_MESSAGE_SEND_FAIL, e);
        }
    }

    @Override
    public void sendAttachmentsMail(String to, String subject, String content, File[] files) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to.split(","));
        helper.setSubject(subject);
        helper.setText(content, true);
        helper.setSentDate(new Date());

        for (File file : files) {
            FileSystemResource fileResource = new FileSystemResource(file);
            helper.addAttachment(file.getName(), fileResource);
        }

        try {
            mailSender.send(message);
            log.info("带附件的邮件发送成功，收件人：{}，主题：{}", to, subject);
        } catch (Exception e) {
            log.error("带附件的邮件发送失败，收件人：{}，主题：{}", to, subject, e);
            throw new MailException(MailConstants.ERROR_CODE_SEND_FAIL, MailConstants.ERROR_MESSAGE_SEND_FAIL, e);
        }
    }

    @Override
    public void sendAttachmentsMail(String to, String cc, String subject, String content, String filePath) throws MessagingException {
        sendAttachmentsMail(to, cc, null, subject, content, filePath);
    }

    @Override
    public void sendAttachmentsMail(String to, String cc, String bcc, String subject, String content, String filePath) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to.split(","));
        if (StringUtils.hasText(cc)) {
            helper.setCc(cc.split(","));
        }
        if (StringUtils.hasText(bcc)) {
            helper.setBcc(bcc.split(","));
        }
        helper.setSubject(subject);
        helper.setText(content, true);
        helper.setSentDate(new Date());

        FileSystemResource file = new FileSystemResource(new File(filePath));
        String fileName = file.getFilename();
        helper.addAttachment(fileName, file);

        try {
            mailSender.send(message);
            log.info("带附件的邮件发送成功，收件人：{}，主题：{}", to, subject);
        } catch (Exception e) {
            log.error("带附件的邮件发送失败，收件人：{}，主题：{}", to, subject, e);
            throw new MailException(MailConstants.ERROR_CODE_SEND_FAIL, MailConstants.ERROR_MESSAGE_SEND_FAIL, e);
        }
    }

    @Override
    public void sendInlineResourceMail(String to, String subject, String content, String rscPath, String rscId) throws MessagingException {
        sendInlineResourceMail(to, null, null, subject, content, rscPath, rscId);
    }

    @Override
    public void sendInlineResourceMail(String to, String cc, String subject, String content, String rscPath, String rscId) throws MessagingException {
        sendInlineResourceMail(to, cc, null, subject, content, rscPath, rscId);
    }

    @Override
    public void sendInlineResourceMail(String to, String cc, String bcc, String subject, String content, String rscPath, String rscId) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to.split(","));
        if (StringUtils.hasText(cc)) {
            helper.setCc(cc.split(","));
        }
        if (StringUtils.hasText(bcc)) {
            helper.setBcc(bcc.split(","));
        }
        helper.setSubject(subject);
        helper.setText(content, true);
        helper.setSentDate(new Date());

        FileSystemResource res = new FileSystemResource(new File(rscPath));
        helper.addInline(rscId, res);

        try {
            mailSender.send(message);
            log.info("带图片的邮件发送成功，收件人：{}，主题：{}", to, subject);
        } catch (Exception e) {
            log.error("带图片的邮件发送失败，收件人：{}，主题：{}", to, subject, e);
            throw new MailException(MailConstants.ERROR_CODE_SEND_FAIL, MailConstants.ERROR_MESSAGE_SEND_FAIL, e);
        }
    }

    @Override
    public void sendTemplateMail(String to, String subject, String template, Object model) throws MessagingException {
        sendTemplateMail(to, null, null, subject, template, model);
    }

    @Override
    public void sendTemplateMail(String to, String cc, String subject, String template, Object model) throws MessagingException {
        sendTemplateMail(to, cc, null, subject, template, model);
    }

    @Override
    public void sendTemplateMail(String to, String cc, String bcc, String subject, String template, Object model) throws MessagingException {
        // 默认使用Thymeleaf模板引擎
        String content = processThymeleafTemplate(template, model);
        sendHtmlMail(to, cc, bcc, subject, content);
    }

    @Override
    public void sendMail(MailInfo mailInfo) throws MessagingException {
        if (mailInfo == null) {
            throw new MailException(MailConstants.ERROR_CODE_PARAM_ERROR, "邮件信息不能为空");
        }

        if (!StringUtils.hasText(mailInfo.getTo())) {
            throw new MailException(MailConstants.ERROR_CODE_PARAM_ERROR, "收件人不能为空");
        }

        if (!StringUtils.hasText(mailInfo.getSubject())) {
            throw new MailException(MailConstants.ERROR_CODE_PARAM_ERROR, "邮件主题不能为空");
        }

        // 设置邮件ID
        if (!StringUtils.hasText(mailInfo.getMailId())) {
            mailInfo.setMailId(UUID.randomUUID().toString());
        }

        // 设置发件人
        if (!StringUtils.hasText(mailInfo.getFrom())) {
            mailInfo.setFrom(from);
        }

        // 设置发送时间
        if (mailInfo.getSendTime() == null) {
            mailInfo.setSendTime(new Date());
        }

        // 设置状态
        mailInfo.setStatus(MailConstants.SEND_STATUS_UNSENT);

        // 根据邮件类型发送邮件
        try {
            if (StringUtils.hasText(mailInfo.getTemplate())) {
                // 发送模板邮件
                String content;
                if (MailConstants.TEMPLATE_ENGINE_FREEMARKER.equals(mailInfo.getTemplateEngine())) {
                    content = processFreemarkerTemplate(mailInfo.getTemplate(), mailInfo.getTemplateModel());
                } else {
                    content = processThymeleafTemplate(mailInfo.getTemplate(), mailInfo.getTemplateModel());
                }
                mailInfo.setContent(content);
                mailInfo.setContentType(MailConstants.MAIL_TYPE_HTML);
            }

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailInfo.getFrom());
            helper.setTo(mailInfo.getTo().split(","));

            if (StringUtils.hasText(mailInfo.getCc())) {
                helper.setCc(mailInfo.getCc().split(","));
            }

            if (StringUtils.hasText(mailInfo.getBcc())) {
                helper.setBcc(mailInfo.getBcc().split(","));
            }

            helper.setSubject(mailInfo.getSubject());
            helper.setText(mailInfo.getContent(), MailConstants.MAIL_TYPE_HTML.equals(mailInfo.getContentType()));
            helper.setSentDate(mailInfo.getSendTime());

            // 添加附件
            if (mailInfo.getAttachmentPaths() != null && !mailInfo.getAttachmentPaths().isEmpty()) {
                for (String filePath : mailInfo.getAttachmentPaths()) {
                    FileSystemResource file = new FileSystemResource(new File(filePath));
                    String fileName = file.getFilename();
                    helper.addAttachment(fileName, file);
                }
            }

            if (mailInfo.getAttachments() != null && mailInfo.getAttachments().length > 0) {
                for (File file : mailInfo.getAttachments()) {
                    FileSystemResource fileResource = new FileSystemResource(file);
                    helper.addAttachment(file.getName(), fileResource);
                }
            }

            // 添加内联资源
            if (StringUtils.hasText(mailInfo.getInlineResourcePath()) && StringUtils.hasText(mailInfo.getInlineResourceId())) {
                FileSystemResource res = new FileSystemResource(new File(mailInfo.getInlineResourcePath()));
                helper.addInline(mailInfo.getInlineResourceId(), res);
            }

            // 设置回执
            if (mailInfo.getNeedReceipt() != null && mailInfo.getNeedReceipt()) {
                message.setHeader("Disposition-Notification-To", mailInfo.getFrom());
            }

            mailSender.send(message);
            mailInfo.setStatus(MailConstants.SEND_STATUS_SUCCESS);
            log.info("邮件发送成功，邮件ID：{}，收件人：{}，主题：{}", mailInfo.getMailId(), mailInfo.getTo(), mailInfo.getSubject());
        } catch (Exception e) {
            mailInfo.setStatus(MailConstants.SEND_STATUS_FAIL);
            mailInfo.setErrorMsg(e.getMessage());
            log.error("邮件发送失败，邮件ID：{}，收件人：{}，主题：{}", mailInfo.getMailId(), mailInfo.getTo(), mailInfo.getSubject(), e);
            throw new MailException(MailConstants.ERROR_CODE_SEND_FAIL, MailConstants.ERROR_MESSAGE_SEND_FAIL, e);
        }
    }

    /**
     * 处理Thymeleaf模板
     *
     * @param template 模板名称
     * @param model    模板参数
     * @return 处理后的内容
     */
    private String processThymeleafTemplate(String template, Object model) {
        if (templateEngine == null) {
            throw new MailException(MailConstants.ERROR_CODE_TEMPLATE_NOT_FOUND, "Thymeleaf模板引擎未配置");
        }

        Context context = new Context();
        if (model instanceof Map) {
            context.setVariables((Map<String, Object>) model);
        }

        try {
            return templateEngine.process(template, context);
        } catch (Exception e) {
            log.error("处理Thymeleaf模板失败，模板：{}", template, e);
            throw new MailException(MailConstants.ERROR_CODE_TEMPLATE_NOT_FOUND, "处理Thymeleaf模板失败", e);
        }
    }

    /**
     * 处理Freemarker模板
     *
     * @param template 模板名称
     * @param model    模板参数
     * @return 处理后的内容
     */
    private String processFreemarkerTemplate(String template, Object model) {
        if (freemarkerConfig == null) {
            throw new MailException(MailConstants.ERROR_CODE_TEMPLATE_NOT_FOUND, "Freemarker模板引擎未配置");
        }

        try {
            freemarker.template.Template freemarkerTemplate = freemarkerConfig.getTemplate(template + MailConstants.DEFAULT_TEMPLATE_SUFFIX_FREEMARKER);
            return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, model);
        } catch (Exception e) {
            log.error("处理Freemarker模板失败，模板：{}", template, e);
            throw new MailException(MailConstants.ERROR_CODE_TEMPLATE_NOT_FOUND, "处理Freemarker模板失败", e);
        }
    }
}