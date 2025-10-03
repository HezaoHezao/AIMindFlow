package org.aimindflow.common.mail.controller;

import org.aimindflow.common.core.domain.R;
import org.aimindflow.common.mail.entity.MailInfo;
import org.aimindflow.common.mail.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 邮件控制器
 *
 * @author HezaoHezao
 */
@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    /**
     * 发送简单文本邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     * @return 结果
     */
    @PostMapping("/simple")
    public R<Void> sendSimpleMail(@RequestParam String to, @RequestParam String subject, @RequestParam String content) {
        mailService.sendSimpleMail(to, subject, content);
        return R.ok();
    }

    /**
     * 发送HTML邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content HTML内容
     * @return 结果
     */
    @PostMapping("/html")
    public R<Void> sendHtmlMail(@RequestParam String to, @RequestParam String subject, @RequestParam String content) {
        try {
            mailService.sendHtmlMail(to, subject, content);
            return R.ok();
        } catch (MessagingException e) {
            return R.fail("发送HTML邮件失败：" + e.getMessage());
        }
    }

    /**
     * 发送带附件的邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     * @param files   附件列表
     * @return 结果
     */
    @PostMapping("/attachment")
    public R<Void> sendAttachmentsMail(@RequestParam String to, @RequestParam String subject, @RequestParam String content, @RequestParam("files") MultipartFile[] files) {
        try {
            // 将MultipartFile转换为File
            File[] attachments = new File[files.length];
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                Path tempDir = Files.createTempDirectory("mail_attachments");
                File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();
                file.transferTo(tempFile);
                attachments[i] = tempFile;
            }

            mailService.sendAttachmentsMail(to, subject, content, attachments);
            return R.ok();
        } catch (MessagingException | IOException e) {
            return R.fail("发送带附件的邮件失败：" + e.getMessage());
        }
    }

    /**
     * 发送带图片的邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容（需要包含<img src="cid:imageId"/>的HTML内容）
     * @param image   图片
     * @return 结果
     */
    @PostMapping("/inline")
    public R<Void> sendInlineResourceMail(@RequestParam String to, @RequestParam String subject, @RequestParam String content, @RequestParam("image") MultipartFile image) {
        try {
            // 将MultipartFile转换为File
            Path tempDir = Files.createTempDirectory("mail_images");
            File tempFile = tempDir.resolve(image.getOriginalFilename()).toFile();
            image.transferTo(tempFile);

            String rscId = UUID.randomUUID().toString();
            mailService.sendInlineResourceMail(to, subject, content, tempFile.getAbsolutePath(), rscId);
            return R.ok();
        } catch (MessagingException | IOException e) {
            return R.fail("发送带图片的邮件失败：" + e.getMessage());
        }
    }

    /**
     * 发送模板邮件
     *
     * @param to       收件人
     * @param subject  主题
     * @param template 模板名称
     * @param params   模板参数（JSON格式）
     * @return 结果
     */
    @PostMapping("/template")
    public R<Void> sendTemplateMail(@RequestParam String to, @RequestParam String subject, @RequestParam String template, @RequestParam Map<String, Object> params) {
        try {
            mailService.sendTemplateMail(to, subject, template, params);
            return R.ok();
        } catch (MessagingException e) {
            return R.fail("发送模板邮件失败：" + e.getMessage());
        }
    }

    /**
     * 发送邮件
     *
     * @param mailInfo 邮件信息
     * @return 结果
     */
    @PostMapping("/send")
    public R<Void> sendMail(@RequestBody MailInfo mailInfo) {
        try {
            mailService.sendMail(mailInfo);
            return R.ok();
        } catch (MessagingException e) {
            return R.fail("发送邮件失败：" + e.getMessage());
        }
    }

    /**
     * 测试邮件
     *
     * @param to 收件人
     * @return 结果
     */
    @GetMapping("/test")
    public R<Void> testMail(@RequestParam String to) {
        try {
            // 发送简单文本邮件
            mailService.sendSimpleMail(to, "测试邮件", "这是一封测试邮件，请勿回复。");

            // 发送HTML邮件
            String content = "<html><body><h1>测试HTML邮件</h1><p>这是一封测试HTML邮件，请勿回复。</p></body></html>";
            mailService.sendHtmlMail(to, "测试HTML邮件", content);

            // 发送模板邮件
            Map<String, Object> model = new HashMap<>();
            model.put("username", "测试用户");
            model.put("message", "这是一封测试模板邮件，请勿回复。");
            mailService.sendTemplateMail(to, "测试模板邮件", "test", model);

            return R.ok();
        } catch (MessagingException e) {
            return R.fail("测试邮件发送失败：" + e.getMessage());
        }
    }
}