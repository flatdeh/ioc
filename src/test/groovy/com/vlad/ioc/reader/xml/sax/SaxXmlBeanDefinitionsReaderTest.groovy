package com.vlad.ioc.reader.xml.sax

import com.vlad.ioc.reader.BeanDefinitionReader
import com.vlad.ioc.reader.BeanDefinitionReaderTest

class SaxXmlBeanDefinitionsReaderTest extends BeanDefinitionReaderTest {
    @Override
    protected BeanDefinitionReader getBeanDefinitionReader(String xmlFilePath) {
        return new SaxXmlBeanDefinitionsReader(xmlFilePath)
    }
}
