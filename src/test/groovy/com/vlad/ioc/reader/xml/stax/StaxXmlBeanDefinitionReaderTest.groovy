package com.vlad.ioc.reader.xml.stax

import com.vlad.ioc.reader.BeanDefinitionReader
import com.vlad.ioc.reader.BeanDefinitionReaderTest

class StaxXmlBeanDefinitionReaderTest extends BeanDefinitionReaderTest {
    @Override
    protected BeanDefinitionReader getBeanDefinitionReader(String xmlFilePath) {
        return new StaxXmlBeanDefinitionReader(xmlFilePath)
    }
}
