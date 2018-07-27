package com.vlad.ioc.injector;

import com.vlad.ioc.entity.Bean;
import com.vlad.ioc.entity.BeanDefinition;
import com.vlad.ioc.exception.BeanInjectDependenciesException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class RefInjector extends Injector {
    @Override
    public void injectDependencies(List<BeanDefinition> beanDefinitions, List<Bean> beans) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            for (Bean bean : beans) {
                if (beanDefinition.getId().equals(bean.getId())) {
                    Map<String, String> refDependenciesMap = beanDefinition.getRefDependencies();
                    for (Map.Entry<String, String> entry : refDependenciesMap.entrySet()) {
                        String field = entry.getKey();
                        String ref = entry.getValue();

                        String setterMethodName = createSetterMethodName(field);
                        try {
                            Field classField = bean.getValue().getClass().getDeclaredField(field);

                            Class<?> parameter = classField.getType();

                            Bean refBean = getBean(beans, ref);
                            Object refBeanValue = refBean.getValue();
                            if (refBeanValue == null) {
                                throw new BeanInjectDependenciesException("Bean with id= " + ref + " not found!");
                            }

                            if (parameter == refBeanValue.getClass()) {
                                Method setterMethod = bean.getValue().getClass().getMethod(setterMethodName, parameter);
                                setterMethod.invoke(bean.getValue(), refBeanValue);
                            } else {
                                throw new BeanInjectDependenciesException("Can't invoke method \"" + setterMethodName + "\", different classes");
                            }

                        } catch (Exception e) {
                            throw new BeanInjectDependenciesException("\"" + setterMethodName + "\" not found!");
                        }
                    }
                }
            }
        }
    }

    private  <T> T getBean(List<Bean> beans, String id) {
        for (Bean bean : beans) {
            if (bean.getId().equals(id)) {
                return (T) bean;
            }
        }
        return null;
    }

}
