package com.dt.utils.service;

public interface MailSendService {

    void sendSimpleMail(String to, String subject, String content);
}
