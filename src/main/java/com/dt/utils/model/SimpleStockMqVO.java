package com.dt.utils.model;

/**
 * @Author jiangyao
 * @Date 2020/4/18 0:44
 **/
public class SimpleStockMqVO {
    String reciveMail;
    String subject;
    String content;

    String mailCode;
    String password;
    String mailHost;
    String smtpPort;

    public SimpleStockMqVO() {
    }

    public SimpleStockMqVO(String reciveMail, String subject, String content, String mailCode, String password, String mailHost,String smtpPort) {
        this.reciveMail = reciveMail;
        this.subject = subject;
        this.content = content;
        this.mailCode = mailCode;
        this.password = password;
        this.mailHost = mailHost;
        this.smtpPort = smtpPort;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMailCode() {
        return mailCode;
    }

    public void setMailCode(String mailCode) {
        this.mailCode = mailCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMailHost() {
        return mailHost;
    }

    public void setMailHost(String mailHost) {
        this.mailHost = mailHost;
    }

    public String getReciveMail() {
        return reciveMail;
    }

    public void setReciveMail(String reciveMail) {
        this.reciveMail = reciveMail;
    }

    @Override
    public String toString() {
        return "SimpleStockMqVO{" +
                "reciveMail='" + reciveMail + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", mailCode='" + mailCode + '\'' +
                ", password='" + password + '\'' +
                ", mailHost='" + mailHost + '\'' +
                ", smtpPort='" + smtpPort + '\'' +
                '}';
    }
}
