package com.vlad.ioc.definition;

import com.vlad.ioc.definition.BeanDefinition;

import java.util.List;

public interface BeanDefinitionReader {

    List<BeanDefinition> readBeanDefinitions();
}
