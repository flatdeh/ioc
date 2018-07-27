package com.vlad.ioc.context;

import com.vlad.ioc.entity.Bean;
import com.vlad.ioc.reader.BeanDefinitionReader;
import com.vlad.ioc.reader.xml.stax.StaxXmlBeanDefinitionReader;
import org.junit.Test;
import resources.service.PaymentService;
import resources.service.UserService;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ClassPathApplicationContextTest {

    @Test
    public void testGetBeanByClass() {
        String[] paths = {"src/test/resources/test.xml"};
        BeanDefinitionReader reader = new StaxXmlBeanDefinitionReader();
        ClassPathApplicationContext applicationContext = new ClassPathApplicationContext(paths);
        applicationContext.setReader(reader);

        assertEquals(UserService.class, applicationContext.getBean(UserService.class));
        assertEquals(PaymentService.class.getName(), applicationContext.getBean(PaymentService.class).getClass().getName());
    }

    @Test
    public void testGetBeanById() {
        String[] paths = {"src/test/resources/test.xml"};
        BeanDefinitionReader reader = new StaxXmlBeanDefinitionReader();
        ClassPathApplicationContext applicationContext = new ClassPathApplicationContext(paths);
        applicationContext.setReader(reader);


        assertEquals(UserService.class.getName(),applicationContext.getBean("userService").getClass().getName());
        assertEquals(PaymentService.class.getName(),applicationContext.getBean("paymentService").getClass().getName());
    }

    @Test
    public void testGetBeanNames() {
        String[] paths = {"src/test/resources/test.xml"};
        BeanDefinitionReader reader = new StaxXmlBeanDefinitionReader();
        ClassPathApplicationContext applicationContext = new ClassPathApplicationContext(paths);
        applicationContext.setReader(reader);

        List<String> beansNames = applicationContext.getBeanNames();

        assertEquals("userService", beansNames.get(0));
        assertEquals("paymentWithMaxService", beansNames.get(1));
        assertEquals("paymentService", beansNames.get(2));
    }

    @Test
    public void testCreateBeansFromBeanDefinitions() {
        String[] paths = {"src/test/resources/test.xml"};
        BeanDefinitionReader reader = new StaxXmlBeanDefinitionReader();
        ClassPathApplicationContext applicationContext = new ClassPathApplicationContext(paths);
        applicationContext.setReader(reader);

        List<Bean> beans = applicationContext.getBeans();

        assertEquals(3, beans.size());
        assertEquals("userService", beans.get(0).getId());
        assertEquals("paymentWithMaxService", beans.get(1).getId());
        assertEquals("paymentService", beans.get(2).getId());
        assertEquals(UserService.class.getName(), beans.get(0).getValue().getClass().getName());
        assertEquals(PaymentService.class.getName(), beans.get(1).getValue().getClass().getName());
        assertEquals(PaymentService.class.getName(), beans.get(2).getValue().getClass().getName());
    }


    @Test
    public void injectDependencies() {
        String[] paths = {"src/test/resources/test.xml"};
        BeanDefinitionReader reader = new StaxXmlBeanDefinitionReader();
        ClassPathApplicationContext applicationContext = new ClassPathApplicationContext(paths);
        applicationContext.setReader(reader);

        List<Bean> beans = applicationContext.getBeans();
        //assertEquals("", beans.get(0).getValue().getClass());
    }
}