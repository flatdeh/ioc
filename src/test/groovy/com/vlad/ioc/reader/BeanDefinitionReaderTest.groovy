package com.vlad.ioc.reader

import com.vlad.ioc.reader.xml.sax.SaxXmlBeanDefinitionsReader
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

abstract class BeanDefinitionReaderTest {
    protected abstract BeanDefinitionReader getBeanDefinitionReader(String xmlFilePath)

    def xmlServicesPath = "src/test/resources/services.xml"

    @Rule
    public ExpectedException expectedEx = ExpectedException.none()

    @Test
    void testReadBeanDefinitions() {
        def BeanDefinitionsReader = getBeanDefinitionReader(xmlServicesPath)
        def beanDefinitions = BeanDefinitionsReader.readBeanDefinitions()

        def beanDefId = 0;
        assert "emailService" == beanDefinitions.get(beanDefId).id
        assert "com.vlad.ioc.service.EmailService" == beanDefinitions.get(beanDefId).beanClassName
        assert "POP3" == beanDefinitions.get(beanDefId).dependencies.get("protocol")
        assert "3000" == beanDefinitions.get(beanDefId).dependencies.get("port")

        beanDefId = 1;
        assert "paymentWithMaxAmountService" == beanDefinitions.get(beanDefId).id
        assert "com.vlad.ioc.service.PaymentService" == beanDefinitions.get(beanDefId).beanClassName
        assert "5000" == beanDefinitions.get(beanDefId).dependencies.get("maxAmount")
        assert "emailService" == beanDefinitions.get(beanDefId).refDependencies.get("emailService")

        beanDefId = 2;
        assert "paymentService" == beanDefinitions.get(beanDefId).id
        assert "com.vlad.ioc.service.PaymentService" == beanDefinitions.get(beanDefId).beanClassName
        assert "emailService" == beanDefinitions.get(beanDefId).refDependencies.get("emailService")

        beanDefId = 3;
        assert "userService" == beanDefinitions.get(beanDefId).id
        assert "com.vlad.ioc.service.UserService" == beanDefinitions.get(beanDefId).beanClassName
        assert "UserLogin" == beanDefinitions.get(beanDefId).dependencies.get("login")
        assert "emailService" == beanDefinitions.get(beanDefId).refDependencies.get("emailService")
    }

    @Test
    void testReadBeanDefinitionsWithCanNotReadXmlException() {
        expectedEx.expect(RuntimeException.class)
        expectedEx.expectMessage("Can't read XML file: src/test/resources/wrong.xml")

        SaxXmlBeanDefinitionsReader saxXmlBeanDefinitionsReader = new SaxXmlBeanDefinitionsReader();
        saxXmlBeanDefinitionsReader.readBeanDefinitions("src/test/resources/wrong.xml")

    }
}
