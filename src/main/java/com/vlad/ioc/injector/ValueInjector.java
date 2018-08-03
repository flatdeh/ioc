package com.vlad.ioc.injector;

import com.vlad.ioc.entity.Bean;
import com.vlad.ioc.entity.BeanDefinition;
import com.vlad.ioc.exception.BeanInjectDependenciesException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class ValueInjector extends Injector {
    public ValueInjector(List<BeanDefinition> beanDefinitions, List<Bean> beans) {
        super(beanDefinitions, beans);
    }

    @Override
    public void injectDependencies(Object beanValue, Method setterMethod, String value) throws InvocationTargetException, IllegalAccessException {
        Object castValue = castValueToParameterType(setterMethod.getParameterTypes()[0], value);
        setterMethod.invoke(beanValue, castValue);
    }

    @Override
    public Map<String, String> getDependenciesMap(BeanDefinition beanDefinition) {
        return beanDefinition.getDependencies();
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
            throw new BeanInjectDependenciesException("Unknown parameter type: \"" + value + "\"");
        }
    }
}
