package com.vlad.ioc.reader;

import com.vlad.ioc.entity.BeanDefinition;
import com.vlad.ioc.reader.xml.XMLBeanDefinitionReader;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XMLBeanDefinitionReaderTest {
    private String[] path= {"src/test/java/resources/test.xml"};

    @Test
    public void testReadBeanDefinitions() {
        XMLBeanDefinitionReader xmlBeanDefinitionReader = new XMLBeanDefinitionReader();
        List<BeanDefinition> beanDefinitionList = xmlBeanDefinitionReader.readBeanDefinitions(path);
        assertEquals(3,beanDefinitionList.size());

        assertEquals("userService", beanDefinitionList.get(0).getId());
        assertEquals("com.vlad.ioc.service.UserService", beanDefinitionList.get(0).getBeanClassName());
        assertEquals(0,beanDefinitionList.get(0).getDependencies().size());
        assertTrue(beanDefinitionList.get(0).getRefDependencies().containsKey("mailService"));
        assertEquals(1,beanDefinitionList.get(0).getRefDependencies().size());

        assertEquals("paymentWithMaxService", beanDefinitionList.get(1).getId());
        assertEquals("com.vlad.ioc.service.PaymentService", beanDefinitionList.get(1).getBeanClassName());
        assertEquals(1,beanDefinitionList.get(1).getDependencies().size());
        assertTrue(beanDefinitionList.get(1).getRefDependencies().containsKey("mailService"));
        assertEquals(1,beanDefinitionList.get(1).getRefDependencies().size());

        assertEquals("paymentService", beanDefinitionList.get(2).getId());
        assertEquals("com.vlad.ioc.service.PaymentService", beanDefinitionList.get(2).getBeanClassName());
        assertEquals(0,beanDefinitionList.get(2).getDependencies().size());
        assertTrue(beanDefinitionList.get(2).getRefDependencies().containsKey("mailService"));
        assertEquals(1,beanDefinitionList.get(2).getRefDependencies().size());
    }
}