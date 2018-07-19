package com.vlad.ioc.context;

import com.vlad.ioc.definition.BeanDefinitionReader;

import java.util.List;

public interface ApplicationContext<T> {
    T getBean(Class<T> clazz);

    T getBean(String name, Class<T> clazz);

    T getBean(String name);

    List<String> getBeanNames();

    void setBeanDefinitionReader(BeanDefinitionReader beanDefinitionReader);
}
