package com.vlad.ioc.injector;

import com.vlad.ioc.entity.Bean;
import com.vlad.ioc.entity.BeanDefinition;
import com.vlad.ioc.exception.BeanInjectDependenciesException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class RefInjector extends Injector {
    private List<Bean> beans;

    public RefInjector(List<BeanDefinition> beanDefinitions, List<Bean> beans) {
        super(beanDefinitions, beans);
        this.beans = beans;
    }

    @Override
    public void injectDependencies(Object beanValue, Method setterMethod, String beanRefId) throws InvocationTargetException, IllegalAccessException {
        Object refBeanValue = getBean(beans, beanRefId);
        setterMethod.invoke(beanValue, refBeanValue);
    }

    @Override
    public Map<String, String> getDependenciesMap(BeanDefinition beanDefinition) {
        return beanDefinition.getRefDependencies();
    }

    private Object getBean(List<Bean> beans, String id) {
        for (Bean bean : beans) {
            if (bean.getId().equals(id)) {
                return bean.getValue();
            }
        }
        throw new BeanInjectDependenciesException("Bean with id= " + id + " not found!");
    }
}
