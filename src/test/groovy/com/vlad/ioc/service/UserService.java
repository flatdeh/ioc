package com.vlad.ioc.service;

import com.vlad.ioc.processor.BeanFactoryPostProcessor;
import com.vlad.ioc.entity.BeanDefinition;

import javax.annotation.PostConstruct;
import java.util.List;

public class UserService implements BeanFactoryPostProcessor {
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

    @Override
    public void postProcessBeanFactory(List<BeanDefinition> definitions) {

    }
}
