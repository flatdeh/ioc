package com.vlad.ioc;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassPathApplicationContext<T> implements ApplicationContext<T> {
    //private String[] path;
    private BeanDefinitionReader reader;
    private List<Bean> beans = new ArrayList<>();
    private List<BeanDefinition> beanDefinitions;

    public T getBean(Class<T> clazz) {
        return null;
    }

    public T getBean(String name, Class<T> clazz) {
        return null;
    }

    public Object getBean(String name) {
        return null;
    }

    public List<String> getBeanNames() {
        return null;
    }

    private void getBeanDefinitionFromBeanDefinitionReader() {
        this.beanDefinitions = reader.readBeanDefinitions();
    }

    public void setBeanDefinitionReader(BeanDefinitionReader beanDefinitionReader) {
        this.reader = beanDefinitionReader;
    }

    public void createBeansFromBeanDefinitions() {
        getBeanDefinitionFromBeanDefinitionReader();
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Bean bean = new Bean();
            bean.setId(beanDefinition.getId());

            String beanClassName = beanDefinition.getBeanClassName();
            try {
                Object objFromBeanClassName = Class.forName(beanClassName).newInstance();
                bean.setValue(objFromBeanClassName);
            } catch (Exception e) {
                throw new RuntimeException("Class " + beanClassName + " not found!", e);
            }
            beans.add(bean);
        }
        injectDependencies();
        injectRefDependencies();
    }

    private void injectDependencies() {
        for (BeanDefinition beanDefinition : beanDefinitions) {

            for (Bean bean : beans) {
                if (beanDefinition.getId().equals(bean.getId())) {
                    Map<String, String> dependenciesMap = beanDefinition.getDependencies();
                    for (Map.Entry<String, String> entry : dependenciesMap.entrySet()) {
                        String field = entry.getKey();
                        String value = entry.getValue();

                        try {
                            String methodName = concatSetMethodName(field);
                            Method method = bean.getValue().getClass().getMethod(methodName, int.class);
                            method.invoke(bean.getValue(), Integer.parseInt(value));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private String concatSetMethodName(String field) {
        return "set" + Character.toUpperCase(field.charAt(0)) + field.substring(1);
    }

    private void injectRefDependencies() {
        for (BeanDefinition beanDefinition : beanDefinitions) {

            for (Bean bean : beans) {
                if (beanDefinition.getId().equals(bean.getId())) {
                    Map<String, Object> refDependenciesMap = beanDefinition.getRefDependencies();
                    for (Map.Entry<String, Object> entry : refDependenciesMap.entrySet()) {
                        String field = entry.getKey();
                        Object ref = entry.getValue();

                        try {
                            String methodName = concatSetMethodName(field);
                            Method method = bean.getValue().getClass().getMethod(methodName, Object.class);
                            method.invoke(bean.getValue(), ref);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
