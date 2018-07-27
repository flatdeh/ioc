package com.vlad.ioc.injector;

import com.vlad.ioc.entity.Bean;
import com.vlad.ioc.entity.BeanDefinition;

import java.util.List;

public abstract class Injector {
    public void inject(List<BeanDefinition> beanDefinitions, List<Bean> beans) {
        injectDependencies(beanDefinitions, beans);
    }

    public abstract void injectDependencies(List<BeanDefinition> beanDefinitions, List<Bean> beans);

    String createSetterMethodName(String field) {
        return "set" + Character.toUpperCase(field.charAt(0)) + field.substring(1);
    }
}
