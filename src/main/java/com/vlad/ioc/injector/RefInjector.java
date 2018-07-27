package com.vlad.ioc.injector;

import com.vlad.ioc.entity.Bean;
import com.vlad.ioc.entity.BeanDefinition;
import com.vlad.ioc.exception.BeanInjectDependenciesException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class RefInjector extends Injector {
    @Override
    public void injectDependencies(List<Bean> beans, Bean bean, Class<?> parameter, String setterMethodName, String value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Bean refBean = getBean(beans, value);
        Object refBeanValue = refBean.getValue();
        if (refBeanValue == null) {
            throw new BeanInjectDependenciesException("Bean with id= " + value + " not found!");
        }

        if (parameter == refBeanValue.getClass()) {
            Method setterMethod = bean.getValue().getClass().getMethod(setterMethodName, parameter);
            setterMethod.invoke(bean.getValue(), refBeanValue);
        } else {
            throw new BeanInjectDependenciesException("Can't invoke method \"" + setterMethodName + "\", different classes");
        }

    }

    @Override
    public Map<String, String> getDependenciesMap(BeanDefinition beanDefinition) {
        return beanDefinition.getRefDependencies();
    }

    private <T> T getBean(List<Bean> beans, String id) {
        for (Bean bean : beans) {
            if (bean.getId().equals(id)) {
                return (T) bean.getValue();
            }
        }
        return null;
    }

}
