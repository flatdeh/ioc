package com.vlad.ioc.injector;

import com.vlad.ioc.entity.Bean;
import com.vlad.ioc.entity.BeanDefinition;
import com.vlad.ioc.exception.BeanInjectDependenciesException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public abstract class Injector {
    public void inject(List<BeanDefinition> beanDefinitions, List<Bean> beans) {
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

                            injectDependencies(beans, bean, parameter, setterMethodName, value);
                        } catch (Exception e) {
                            throw new BeanInjectDependenciesException("\"" + setterMethodName + "\" not found!", e);
                        }
                    }
                }
            }
        }
    }

    public abstract void injectDependencies(List<Bean> beans, Bean bean, Class<?> parameter, String setterMethodName, String value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    public abstract Map<String, String> getDependenciesMap(BeanDefinition beanDefinition);

    private String createSetterMethodName(String field) {
        return "set" + Character.toUpperCase(field.charAt(0)) + field.substring(1);
    }
}
