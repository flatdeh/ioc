package com.vlad.ioc.service;

import com.vlad.ioc.processor.BeanPostProcessor;
import com.vlad.ioc.exception.BeanInstantiationException;

public class PaymentService implements BeanPostProcessor {
    private EmailService emailService;
    private int maxAmount;

    public EmailService getEmailService() {
        return emailService;
    }

    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String id) throws BeanInstantiationException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String id) throws BeanInstantiationException {
        return bean;
    }
}
