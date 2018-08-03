package com.vlad.ioc.service;

import javax.annotation.PostConstruct;

public class UserService {
    private String login;
    private EmailService emailService;

    @PostConstruct
    public void init(){
        System.out.println("Init!");
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public EmailService getEmailService() {
        return emailService;
    }

    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }
}
