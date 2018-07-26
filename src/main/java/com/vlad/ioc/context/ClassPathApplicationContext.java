package com.vlad.ioc.context;

import com.vlad.ioc.entity.BeanDefinition;
import com.vlad.ioc.reader.BeanDefinitionReader;
import com.vlad.ioc.entity.Bean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassPathApplicationContext implements ApplicationContext {
    private String[] paths;
    private BeanDefinitionReader reader;
    private List<Bean> beans = new ArrayList<>();
    private List<BeanDefinition> beanDefinitions;

    public ClassPathApplicationContext(String[] paths) {
        this.paths = paths;
        start();
    }

    private void start() {
        parseBeanDefinitionsFromBeanDefinitionReader();
        createBeansFromBeanDefinitions();
        injectValueDependencies();
        injectRefDependencies();
    }

    public <T> T getBean(Class<T> clazz) {
        for (Bean bean : beans) {
            if (bean.getValue().getClass().equals(clazz)) {
                return clazz.cast(bean);
            }
        }
        return null;
    }

    public <T> T getBean(String id, Class<T> clazz) {
        for (Bean bean : beans) {
            if (bean.getValue().getClass().equals(clazz) && bean.getId().equals(id)) {
                return clazz.cast(bean);
            }
        }
        return null;
    }

    public <T> T getBean(String id) {
        for (Bean bean : beans) {
            if (bean.getId().equals(id)) {
                return (T) bean;
            }
        }
        return null;
    }

    public List<String> getBeanNames() {
        List<String> beansNames = new ArrayList<>();
        for (Bean bean : beans) {
            beansNames.add(bean.getId());
        }
        return beansNames;
    }

    private void parseBeanDefinitionsFromBeanDefinitionReader() {
        this.beanDefinitions = reader.readBeanDefinitions(this.paths);
    }


    public void createBeansFromBeanDefinitions() {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Bean bean = new Bean();
            bean.setId(beanDefinition.getId());

            String beanClassName = beanDefinition.getBeanClassName();
            try {
                Object objFromBeanClassName = Class.forName(beanClassName).newInstance();
                bean.setValue(objFromBeanClassName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Class " + beanClassName + " not found!", e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Class " + beanClassName + " illegal access exception!", e);
            } catch (InstantiationException e) {
                throw new RuntimeException("Class " + beanClassName + " instantiation exception!", e);
            }
            beans.add(bean);
        }
    }

    void injectValueDependencies() {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            for (Bean bean : beans) {
                if (beanDefinition.getId().equals(bean.getId())) {
                    Map<String, String> dependenciesMap = beanDefinition.getDependencies();
                    for (Map.Entry<String, String> entry : dependenciesMap.entrySet()) {
                        String field = entry.getKey();
                        String value = entry.getValue();

                        String setterMethodName = createSetterMethodName(field);

                        try {
                            Method[] methods = bean.getValue().getClass().getMethods();
                            for (Method method : methods) {
                                if (method.getName().equals(setterMethodName)) {
                                    Class<?> parameter = method.getParameterTypes()[0];

                                    Method setterMethod = bean.getValue().getClass().getMethod(setterMethodName, parameter);
                                    Object castValue = castValueToParameterType(parameter, value);
                                    setterMethod.invoke(bean.getValue(), castValue);
                                }
                            }
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

    String createSetterMethodName(String field) {
        return "set" + Character.toUpperCase(field.charAt(0)) + field.substring(1);
    }

    void injectRefDependencies() {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            for (Bean bean : beans) {
                if (beanDefinition.getId().equals(bean.getId())) {
                    Map<String, String> refDependenciesMap = beanDefinition.getRefDependencies();
                    for (Map.Entry<String, String> entry : refDependenciesMap.entrySet()) {
                        String field = entry.getKey();
                        String ref = entry.getValue();

                        Bean refBean = getBean(ref);
                        Object refBeanValue = refBean.getValue();
                        if (refBeanValue == null) {
                            throw new RuntimeException("Bean with id= " + ref + " not found!");
                        }

                        String setterMethodName = createSetterMethodName(field);
                        try {
                            //todo: get fields type instead get all methods
                            Method[] methods = bean.getValue().getClass().getMethods();
                            for (Method method : methods) {
                                if (method.getName().equals(setterMethodName)) {
                                    Class<?> parameter = method.getParameterTypes()[0];
                                    if (parameter == refBeanValue.getClass()) {
                                        Method setterMethod = bean.getValue().getClass().getMethod(setterMethodName, parameter);
                                        setterMethod.invoke(bean.getValue(), refBeanValue);
                                    } else {
                                        throw new RuntimeException("Can't invoke method \"" + setterMethodName + "\", different classes");
                                    }
                                }
                            }
                        } catch (Exception e) {
                            throw new RuntimeException("\"" + setterMethodName + "\" not found!");
                        }
                    }
                }
            }
        }
    }

    public void setReader(BeanDefinitionReader reader) {
        this.reader = reader;
    }

    List<Bean> getBeans() {
        return beans;
    }
}
