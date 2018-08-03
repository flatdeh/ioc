package com.vlad.ioc.context

import com.vlad.ioc.exception.BeanNotFoundExcepton
import com.vlad.ioc.exception.NotUniqueBeanException
import com.vlad.ioc.service.EmailService
import com.vlad.ioc.service.PaymentService
import com.vlad.ioc.service.UserService
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

import static junit.framework.TestCase.*


class ClassPathApplicationContextTest {
    def xmlServicesPath = "src/test/resources/services.xml"

    @Rule
    public ExpectedException expectedEx = ExpectedException.none()

    @Test
    void testGetBeanByClass() {
        def applicationContext = new ClassPathApplicationContext(xmlServicesPath)
        def emailService = applicationContext.getBean(EmailService.class)
        def userService = applicationContext.getBean(UserService.class)

        assert emailService instanceof EmailService
        assert userService instanceof UserService
    }

    @Test(expected = NotUniqueBeanException.class)
    void testGetBeanByClassWithNotUniqueBeanException() {
        def applicationContext = new ClassPathApplicationContext(xmlServicesPath)
        applicationContext.getBean(PaymentService.class)
    }

    @Test(expected = BeanNotFoundExcepton.class)
    void testGetBeanByClassNotFoundException() {
        def applicationContext = new ClassPathApplicationContext(xmlServicesPath)
        applicationContext.getBean(String.class)
    }

    @Test
    void testGetBeanById() {
        def applicationContext = new ClassPathApplicationContext(xmlServicesPath)
        def emailService = applicationContext.getBean('emailService')
        def userService = applicationContext.getBean('userService')
        def paymentService = applicationContext.getBean('paymentService')

        assertTrue(emailService instanceof EmailService)
        assertTrue(userService instanceof UserService)
        assertTrue(paymentService instanceof PaymentService)
    }

    @Test(expected = BeanNotFoundExcepton.class)
    void testGetBeanByIdNotFoundException() {
        def applicationContext = new ClassPathApplicationContext(xmlServicesPath)
        applicationContext.getBean("wrong id");
    }

    @Test
    void testGetBeanByIdAndClass() {
        def applicationContext = new ClassPathApplicationContext(xmlServicesPath)
        def emailService = applicationContext.getBean('emailService', EmailService.class)
        def userService = applicationContext.getBean('userService', UserService.class)
        def paymentService = applicationContext.getBean('paymentService', PaymentService.class)
        def paymentServiceWithMaxAmount =
                applicationContext.getBean('paymentWithMaxAmountService', PaymentService.class)

        assertTrue(emailService instanceof EmailService)
        assertTrue(userService instanceof UserService)
        assertTrue(paymentService instanceof PaymentService)
        assertTrue(paymentServiceWithMaxAmount instanceof PaymentService)
    }

    @Test(expected = BeanNotFoundExcepton.class)
    void testGetBeanByIdAndClassNotFoundException() {
        def applicationContext = new ClassPathApplicationContext(xmlServicesPath)
        applicationContext.getBean("wrong id", String.class)
    }


    @Test
    void testGetBeanNames() {
        def expectedListBeansNames =
                ['emailService', 'paymentWithMaxAmountService', 'paymentService', 'userService'] as String[]
        def applicationContext = new ClassPathApplicationContext(xmlServicesPath)
        def listBeansNames = applicationContext.getBeanNames()

        assert listBeansNames.size() == 4

        for (int i = 0; i < listBeansNames.size(); i++) {
            assertEquals(expectedListBeansNames[i], listBeansNames[i])
        }
    }


    @Test
    void testCreateBeansFromBeanDefinitionsWithWrongBeanClassBeanInstantiationException() {
        expectedEx.expect(RuntimeException.class)
        expectedEx.expectMessage("Can't create bean of class: \"com.vlad.ioc.service.WrongService\"")

        new ClassPathApplicationContext("src/test/resources/services-with-wrong-bean-class")
    }




}
