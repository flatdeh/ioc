package com.vlad.ioc.injector

import com.vlad.ioc.entity.Bean
import com.vlad.ioc.entity.BeanDefinition
import com.vlad.ioc.service.EmailService
import org.junit.Test

class RefInjectorTest extends InjectorTest {

    @Override
    Injector getInjector(List<BeanDefinition> beanDef, List<Bean> b) {
        return new RefInjector(beanDef, b)
    }

    @Override
    def testInject() {
        injector.inject()

        assert beans.get(1).value.getProperties().get('emailService').getClass() == EmailService.class
        assert beans.get(2).value.getProperties().get('emailService').getClass() == EmailService.class
        assert beans.get(3).value.getProperties().get('emailService').getClass() == EmailService.class
    }

//    @Test
//    void testInjectRefDependency() {
//        injector.inject()
//
//        assert beans.get(1).value.getProperties().get('emailService').getClass() == EmailService.class
//        assert beans.get(2).value.getProperties().get('emailService').getClass() == EmailService.class
//        assert beans.get(3).value.getProperties().get('emailService').getClass() == EmailService.class
//    }

}
