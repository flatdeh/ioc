package com.vlad.ioc.context;

import com.vlad.ioc.exception.BeanNotFoundExcepton;
import com.vlad.ioc.entity.BeanDefinition;
import com.vlad.ioc.exception.BeanInstantiationException;
import com.vlad.ioc.exception.NotUniqueBeanException;
import com.vlad.ioc.injector.RefInjector;
import com.vlad.ioc.injector.ValueInjector;
import com.vlad.ioc.reader.BeanDefinitionReader;
import com.vlad.ioc.entity.Bean;
import com.vlad.ioc.reader.xml.sax.SaxXmlBeanDefinitionsReader;

import java.util.ArrayList;
import java.util.List;

public class ClassPathApplicationContext implements ApplicationContext {
    private BeanDefinitionReader reader;
    private List<Bean> beans = new ArrayList<>();
    private List<BeanDefinition> beanDefinitions;

    public ClassPathApplicationContext() {
    }

    public ClassPathApplicationContext(String[] paths) {
        setReader(new SaxXmlBeanDefinitionsReader(paths));
        start();
    }

    public void start() {
        if (reader!=null) {
            parseBeanDefinitionsFromBeanDefinitionReader();
            createBeansFromBeanDefinitions();

            new ValueInjector().inject(beanDefinitions, beans);
            new RefInjector().inject(beanDefinitions, beans);
        } else {
            throw new RuntimeException("Set BeanDefinitionReader for ClassPathApplicationContext!");
        }
    }

    public <T> T getBean(Class<T> clazz) {
        T resultBeanValue = null;
        boolean isFound = false;
        for (Bean bean : beans) {
            if (bean.getValue().getClass().equals(clazz)) {
                if (!isFound) {
                    resultBeanValue = clazz.cast(bean.getValue());
                    isFound = true;
                } else {
                    throw new NotUniqueBeanException("Beans with class: " + clazz + ", more than one!");
                }
            }
        }
        if (isFound) {
            return resultBeanValue;
        } else {
            throw new BeanNotFoundExcepton("Bean with class= " + clazz + ", not found!");
        }
    }

    public <T> T getBean(String id, Class<T> clazz) {
        for (Bean bean : beans) {
            if (bean.getValue().getClass().equals(clazz) && bean.getId().equals(id)) {
                return clazz.cast(bean.getValue());
            }
        }
        throw new BeanNotFoundExcepton("Bean with class= " + clazz + " and id= " + id + ", not found!");
    }

    public <T> T getBean(String id) {
        for (Bean bean : beans) {
            if (bean.getId().equals(id)) {
                return (T) bean.getValue();
            }
        }
        throw new BeanNotFoundExcepton("Bean with id= " + id + ", not found!");
    }

    public List<String> getBeanNames() {
        List<String> beansNames = new ArrayList<>();
        for (Bean bean : beans) {
            beansNames.add(bean.getId());
        }
        return beansNames;
    }

    private void parseBeanDefinitionsFromBeanDefinitionReader() {
        this.beanDefinitions = reader.readBeanDefinitions();
    }


    private void createBeansFromBeanDefinitions() {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            String beanId = beanDefinition.getId();

            if (isExist(beanId)) {
                throw new BeanInstantiationException("Error create bean with id= " + beanId + ", bean with this id already exist");
            }

            Bean bean = new Bean();
            bean.setId(beanId);

            String beanClassName = beanDefinition.getBeanClassName();
            try {
                Object objFromBeanClassName = Class.forName(beanClassName).newInstance();
                bean.setValue(objFromBeanClassName);
            } catch (ClassNotFoundException e) {
                throw new BeanInstantiationException("Class " + beanClassName + " not found!", e);
            } catch (IllegalAccessException e) {
                throw new BeanInstantiationException("Class " + beanClassName + " illegal access exception!", e);
            } catch (InstantiationException e) {
                throw new BeanInstantiationException("Class " + beanClassName + " instantiation exception!", e);
            }
            beans.add(bean);
        }
    }

    private boolean isExist(String beanId) {
        for (Bean bean : beans) {
            if (bean.getId().equals(beanId)) {
                return true;
            }
        }
        return false;
    }

    public void setReader(BeanDefinitionReader reader) {
        this.reader = reader;
    }

}
