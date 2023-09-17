package com.ruoyi.demo.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;


/**
 * 邮件发送案例
 *
 * @author Michelle.Chung
 */
@Validated
@RestController
@RequestMapping("/demo/mail")
public class MailDemoController {

    @Autowired
    private MailService mailService;

    /**
     * 发送邮件
     *
     * @param to      接收人
     * @param subject 标题
     * @param text    内容
     */
    @GetMapping("/sendSimpleMessage")
    public R<Void> sendSimpleMessage(String to, String subject, String text) {
        mailService.sendText(to, subject, text);
        return R.ok();
    }

    /**
     * 发送邮件（带附件）
     *
     * @param to       接收人
     * @param subject  标题
     * @param text     内容
     * @param filePath 附件路径
     */
    @GetMapping("/sendMessageWithAttachment")
    public R<Void> sendMessageWithAttachment(String to, String subject, String text, String filePath) {
        mailService.sendText(to, subject, text, new File(filePath));
        return R.ok();
    }

}
