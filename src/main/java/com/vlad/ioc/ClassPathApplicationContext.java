package com.vlad.ioc;

import java.util.List;

public class ClassPathApplicationContext<T> implements ApplicationContext<T> {
    private String[] path;
    private BeanDefinitionReader reader;
    private List<Bean> beans;
    List<BeanDefinition> beanDefinitions;

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

    public void setBeanDefinitionReader(BeanDefinitionReader beanDefinitionReader) {

    }

    private void createBeansFromBeanDefinitions() {

    }

    private void injectDependencies() {

    }

    private void injectRefDependencies() {

    }
}
