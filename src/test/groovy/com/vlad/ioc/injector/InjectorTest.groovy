package com.vlad.ioc.injector

import com.vlad.ioc.entity.Bean
import com.vlad.ioc.entity.BeanDefinition
import com.vlad.ioc.service.EmailService
import com.vlad.ioc.service.PaymentService
import com.vlad.ioc.service.UserService
import org.junit.Test

import java.beans.Beans

abstract class InjectorTest {
    def beanDefinitions = beanDefinitionsList()
    def beans = beansList()
    Injector injector = getInjector(beanDefinitions, beans)

    abstract Injector getInjector(List<BeanDefinition> beanDef, List<Bean> b)

    @Test
    abstract void testInject()

    static List<BeanDefinition> beanDefinitionsList() {
        def beanDefinitions = new ArrayList<>()

        def emailServiceValueDependencies = [protocol: 'POP3', port: '3000']
        def emailServiceRefDependencies = [:]
        def beanDefinitionEmailService = new BeanDefinition(
                id: 'emailService',
                beanClassName: 'com.vlad.ioc.service.EmailService',
                dependencies: emailServiceValueDependencies,
                refDependencies: emailServiceRefDependencies)

        def paymentWithMaxAmountServiceValueDependencies = [maxAmount: '5000']
        def paymentWithMaxAmountServiceRefDependencies = [emailService: 'emailService']
        def beanDefinitionPaymentWithMaxAmountService = new BeanDefinition(
                id: 'paymentWithMaxAmountService',
                beanClassName: 'com.vlad.ioc.service.PaymentService',
                dependencies: paymentWithMaxAmountServiceValueDependencies,
                refDependencies: paymentWithMaxAmountServiceRefDependencies)

        def paymentServiceValueDependencies = [:]
        def paymentServiceRefDependencies = [emailService: 'emailService']
        def beanDefinitionPaymentService = new BeanDefinition(
                id: 'paymentService',
                beanClassName: 'com.vlad.ioc.service.PaymentService',
                dependencies: paymentServiceValueDependencies,
                refDependencies: paymentServiceRefDependencies)

        def userServiceValueDependencies = [login: 'UserLogin']
        def userServiceRefDependencies = [emailService: 'emailService']
        def beanDefinitionUserService = new BeanDefinition(
                id: 'userService',
                beanClassName: 'com.vlad.ioc.service.UserService',
                dependencies: userServiceValueDependencies,
                refDependencies: userServiceRefDependencies)

        beanDefinitions.add(beanDefinitionEmailService)
        beanDefinitions.add(beanDefinitionPaymentWithMaxAmountService)
        beanDefinitions.add(beanDefinitionPaymentService)
        beanDefinitions.add(beanDefinitionUserService)
        return beanDefinitions
    }

    static List<Bean> beansList() {
        def beans = new ArrayList<>()

        def beanEmailService = new Bean(id: 'emailService', value: new EmailService())
        def beanPaymentWithMaxAmountService = new Bean(id: 'paymentWithMaxAmountService', value: new PaymentService())
        def beanPaymentService = new Bean(id: 'paymentService', value: new PaymentService())
        def beanUserService = new Bean(id: 'userService', value: new UserService())

        beans.add(beanEmailService)
        beans.add(beanPaymentWithMaxAmountService)
        beans.add(beanPaymentService)
        beans.add(beanUserService)

        return beans
    }
}
