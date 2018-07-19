package com.vlad.ioc;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class XMLBeanDefinitionReaderTest {
    private String[] path= {"src/test/java/resources/test.xml"};

    @Test
    public void testReadBeanDefinitions() {
        XMLBeanDefinitionReader xmlBeanDefinitionReader = new XMLBeanDefinitionReader(path);
        List<BeanDefinition> beanDefinitionList = xmlBeanDefinitionReader.readBeanDefinitions();
        assertEquals(3,beanDefinitionList.size());

        assertEquals("userService", beanDefinitionList.get(0).getId());
        assertEquals("com.vlad.ioc.service.UserService", beanDefinitionList.get(0).getBeanClassName());
        assertEquals(0,beanDefinitionList.get(0).getDependencies().size());
        assertTrue(beanDefinitionList.get(0).getRefDependencies().containsKey("mailService"));
        assertEquals(1,beanDefinitionList.get(0).getRefDependencies().size());
    }
}