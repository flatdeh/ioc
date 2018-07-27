package com.vlad.ioc.injector;

import com.vlad.ioc.entity.Bean;
import com.vlad.ioc.entity.BeanDefinition;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class ValueInjector extends Injector {
    @Override
    public void injectDependencies(List<BeanDefinition> beanDefinitions, List<Bean> beans) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            for (Bean bean : beans) {
                if (beanDefinition.getId().equals(bean.getId())) {
                    Map<String, String> dependenciesMap = beanDefinition.getDependencies();
                    for (Map.Entry<String, String> entry : dependenciesMap.entrySet()) {
                        String field = entry.getKey();
                        String value = entry.getValue();

                        String setterMethodName = createSetterMethodName(field);

                        try {
                            Field classField = bean.getValue().getClass().getDeclaredField(field);
                            Class<?> classFieldParameter = classField.getType();

                            Method setterMethod = bean.getValue().getClass().getMethod(setterMethodName, classFieldParameter);
                            Object castValue = castValueToParameterType(classFieldParameter, value);
                            setterMethod.invoke(bean.getValue(), castValue);

                        } catch (Exception e) {
                            throw new RuntimeException("\"" + setterMethodName + "\" not found!", e);
                        }
                    }
                }
            }
        }
    }

    private Object castValueToParameterType(Class parameter, String value) {
        if (parameter == int.class) {
            return Integer.parseInt(value);
        } else if (parameter == Integer.class) {
            return Integer.valueOf(value);
        } else if (parameter == String.class) {
            return value;
        } else if (parameter == char.class) {
            return value.charAt(0);
        } else if (parameter == boolean.class) {
            return Boolean.getBoolean(value);
        } else if (parameter == long.class) {
            return Long.parseLong(value);
        } else if (parameter == double.class) {
            return Double.parseDouble(value);
        } else if (parameter == Short.class) {
            return Short.parseShort(value);
        } else if (parameter == float.class) {
            return Float.parseFloat(value);
        } else if (parameter == Byte.class) {
            return Byte.parseByte(value);
        } else {
            throw new RuntimeException("Unknown parameter type: \"" + value + "\"");
        }
    }
}
