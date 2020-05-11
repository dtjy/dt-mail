package com.dt.utils.service.impl;

import com.dt.utils.job.ScheduleTask;
import com.dt.utils.service.MailSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author jiangyao
 * @Date 2020/4/17 9:55
 **/
@Service
public class MailSendServiceImpl implements MailSendService {

    private final static Logger logger = LoggerFactory.getLogger(MailSendServiceImpl.class);

    @Autowired
    private JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送纯文本的简单邮件
     * @param to
     * @param subject
     * @param content
     */
    @Override
    public void sendSimpleMail(String to, String subject, String content){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        try {
            sender.send(message);
            logger.info("测试邮件。");
        } catch (Exception e) {
            logger.error("测试邮件发生异常！", e);
        }
    }
}
