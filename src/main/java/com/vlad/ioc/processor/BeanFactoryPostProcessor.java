package com.vlad.ioc.processor;

import com.vlad.ioc.entity.BeanDefinition;

import java.util.List;

public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(List<BeanDefinition> definitions);
}
