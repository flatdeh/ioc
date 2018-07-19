package com.vlad.ioc;

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
    }

    private void injectDependencies() {
        for (BeanDefinition beanDefinition : beanDefinitions) {

            for (Bean bean : beans) {
                if (beanDefinition.getId().equals(bean.getId())){
                    Map<String, String> dependenciesMap = beanDefinition.getDependencies();
                    for(Map.Entry<String, String> entry : dependenciesMap.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();

                        try {
                            bean.getValue().getClass().getMethod("setId", int.class);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void injectRefDependencies() {

    }
}
