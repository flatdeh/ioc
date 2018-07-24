package com.vlad.ioc.reader;

import com.vlad.ioc.entity.BeanDefinition;

import java.util.List;

public interface BeanDefinitionReader {
    List<BeanDefinition> readBeanDefinitions(String[] path);
}
