package com.vlad.ioc.injector;

import com.vlad.ioc.entity.Bean;
import com.vlad.ioc.entity.BeanDefinition;
import com.vlad.ioc.exception.BeanInjectDependenciesException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public abstract class Injector {
    private List<BeanDefinition> beanDefinitions;
    private List<Bean> beans;

    public Injector(List<BeanDefinition> beanDefinitions, List<Bean> beans) {
        this.beanDefinitions = beanDefinitions;
        this.beans = beans;
    }

    public void inject() {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            for (Bean bean : beans) {
                if (beanDefinition.getId().equals(bean.getId())) {
                    Map<String, String> dependenciesMap = getDependenciesMap(beanDefinition);
                    for (Map.Entry<String, String> entry : dependenciesMap.entrySet()) {
                        String field = entry.getKey();
                        String value = entry.getValue();

                        String setterMethodName = createSetterMethodName(field);
                        try {
                            Field classField = bean.getValue().getClass().getDeclaredField(field);
                            Class<?> parameter = classField.getType();

                            Method setterMethod = bean.getValue().getClass().getMethod(setterMethodName, parameter);
                            Object beanValue = bean.getValue();

                            injectDependencies(beanValue, setterMethod, value);
                        } catch (NoSuchMethodException e) {
                            throw new BeanInjectDependenciesException("Method \"" + setterMethodName + "\" in class: " + value + ", not found!", e);
                        } catch (InvocationTargetException | IllegalAccessException e) {
                            throw new BeanInjectDependenciesException("Can't invoke method \"" + setterMethodName + "\" in class: " + value, e);
                        } catch (NoSuchFieldException e) {
                            throw new BeanInjectDependenciesException("Field \"" + field + "\" in class: " + value + ", not found!", e);
                        }
                    }
                }
            }
        }
    }

    public abstract void injectDependencies(Object beanValue, Method setterMethod, String value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    public abstract Map<String, String> getDependenciesMap(BeanDefinition beanDefinition);

    private String createSetterMethodName(String field) {
        return "set" + Character.toUpperCase(field.charAt(0)) + field.substring(1);
    }
}
