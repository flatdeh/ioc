package com.vlad.ioc.processor;

import com.vlad.ioc.exception.BeanInstantiationException;

public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object bean, String id) throws BeanInstantiationException;

    Object postProcessAfterInitialization(Object bean, String id) throws BeanInstantiationException;
}
