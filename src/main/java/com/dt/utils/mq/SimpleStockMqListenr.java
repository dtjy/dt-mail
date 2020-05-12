package com.dt.utils.mq;

import com.alibaba.fastjson.JSONObject;
import com.dt.utils.model.SimpleStockMqVO;
import com.dt.utils.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.Properties;

/**
 * @Author jiangyao
 * @Date 2020/4/18 0:12
 **/
@Component
public class SimpleStockMqListenr {

    public static final String SIMPLE_STOCK_MQ_TOPIC = "SIMPLE_STOCK";

    private final static Logger LOGGER = LoggerFactory.getLogger(SimpleStockMqListenr.class);

    @Autowired
    RedisUtil redisUtil;

    @JmsListener(destination = SIMPLE_STOCK_MQ_TOPIC,containerFactory = "jmsListenerContainerTopic")
    public void consumerMessage(String text) throws Exception{

        SimpleStockMqVO simpleStockMqVO = JSONObject.parseObject(text,SimpleStockMqVO.class);
        LOGGER.info(simpleStockMqVO.toString());
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
//        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", simpleStockMqVO.getMailHost());
//        props.setProperty("mail.smtp.port", simpleStockMqVO.getSmtpPort());
        //阿里云服务器禁用25端口，所以服务器上改为465端口
        props.put("mail.smtp.socketFactory.port", "465");
	    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	    props.setProperty("mail.smtp.socketFactory.fallback", "false");
	    props.setProperty("mail.smtp.socketFactory.port", "465");

        Session session = Session.getInstance(props);
        Message msg = getMimeMessage(session, simpleStockMqVO);
        Transport transport = session.getTransport();
        transport.connect(simpleStockMqVO.getMailCode(), simpleStockMqVO.getPassword());
        transport.sendMessage(msg,msg.getAllRecipients());
        transport.close();
//        redisUtil.incr(simpleStockMqVO.getMailCode(),1);
//        redisUtil.incr(simpleStockMqVO.getMailCode()+"_"+ LocalDate.now().toString().replace("-",""),1);
//        redisUtil.incr("mail_send_count",1);

        redisUtil.hincr("send_mail_count",simpleStockMqVO.getMailCode(),1);
        redisUtil.hincr("send_mail_count",simpleStockMqVO.getMailCode()+"_"+ LocalDate.now().toString().replace("-",""),1);
        String[] sendMails = simpleStockMqVO.getReciveMail().split(",");
        for (int i=0;i<sendMails.length;i++){
            redisUtil.hincr("receive_mail_count",sendMails[i],1);
            redisUtil.hincr("receive_mail_count",sendMails[i]+"_"+LocalDate.now().toString().replace("-",""),1);
        }


    }

    public MimeMessage getMimeMessage(Session session,SimpleStockMqVO simpleStockMqVO) throws Exception{
        //创建一封邮件的实例对象
        MimeMessage msg = new MimeMessage(session);
        //设置发件人地址
        msg.setFrom(new InternetAddress(simpleStockMqVO.getMailCode()));
        /**
         * 设置收件人地址（可以增加多个收件人、抄送、密送），即下面这一行代码书写多行
         * MimeMessage.RecipientType.TO:发送
         * MimeMessage.RecipientType.CC：抄送
         * MimeMessage.RecipientType.BCC：密送
         */
//        msg.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(simpleStockMqVO.getReciveMail()));
        String[] mail = simpleStockMqVO.getReciveMail().split(",");
        Address[] to_mail = new InternetAddress[mail.length];
        for (int i = 0; i < to_mail.length; i++) { // 设置接收邮件人的地址
            to_mail[i] = new InternetAddress(String.valueOf(mail[i]));
        }
        msg.setRecipients(MimeMessage.RecipientType.TO,to_mail);
        //设置邮件主题
        msg.setSubject(simpleStockMqVO.getSubject(),"GBK");
        //设置邮件正文
        msg.setContent(simpleStockMqVO.getContent(), "text/html;charset=GBK");
        //设置邮件的发送时间,默认立即发送
//        msg.setSentDate(new Date());

        return msg;
    }

//    public static void main(String[] s){
//        System.out.println(LocalDate.now().toString());
//    }
}
