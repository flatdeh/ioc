package com.vlad.ioc.context;

import com.vlad.ioc.BeanPostProcessor;
import com.vlad.ioc.exception.BeanNotFoundExcepton;
import com.vlad.ioc.entity.BeanDefinition;
import com.vlad.ioc.exception.NotUniqueBeanException;
import com.vlad.ioc.injector.RefInjector;
import com.vlad.ioc.injector.ValueInjector;
import com.vlad.ioc.reader.BeanDefinitionReader;
import com.vlad.ioc.entity.Bean;
import com.vlad.ioc.reader.xml.sax.SaxXmlBeanDefinitionsReader;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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
        if (reader != null) {
            parseBeanDefinitionsFromBeanDefinitionReader();
            createBeansFromBeanDefinitions();

            new ValueInjector(beanDefinitions, beans).inject();
            new RefInjector(beanDefinitions, beans).inject();

            postProcessBeforeInitialization();

            runInitMethods();
        } else {
            throw new RuntimeException("Set BeanDefinitionReader for ClassPathApplicationContext!");
        }
    }

    private void postProcessBeforeInitialization() {
        for (Bean bean : beans) {
            Class<?>[] beanInterfaces = bean.getValue().getClass().getInterfaces();
            for (Class<?> beanInterface : beanInterfaces) {
                if (BeanPostProcessor.class.equals(beanInterface)) {
                    try {
                        Method postProcessBeforeInitialization =
                                bean.getValue().getClass().getMethod("postProcessBeforeInitialization", Object.class, String.class);
                        Object newBeanValue = postProcessBeforeInitialization.invoke(bean.getValue(), bean.getValue(), bean.getId());
                        bean.setValue(newBeanValue);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private void runInitMethods() {
        for (Bean bean : beans) {
            Method[] methods = bean.getValue().getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(PostConstruct.class)) {
                    try {
                        method.invoke(bean.getValue(), (Object[]) null);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException("Can't invoke method: " + method + ", in class: " + bean.getValue().getClass() + "!", e);
                    }
                }
            }
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

    public Object getBean(String id) {
        for (Bean bean : beans) {
            if (bean.getId().equals(id)) {
                return bean.getValue();
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


            Bean bean = new Bean();
            bean.setId(beanId);

            String beanClassName = beanDefinition.getBeanClassName();
            try {
                Object objFromBeanClassName = Class.forName(beanClassName).newInstance();
                bean.setValue(objFromBeanClassName);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                throw new RuntimeException("Can't create bean of class: \"" + beanClassName + "\"", e);
            }
            beans.add(bean);
        }
    }


    public void setReader(BeanDefinitionReader reader) {
        this.reader = reader;
    }

}
