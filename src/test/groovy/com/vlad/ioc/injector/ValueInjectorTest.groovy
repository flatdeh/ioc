package com.vlad.ioc.injector

import com.vlad.ioc.entity.Bean
import com.vlad.ioc.entity.BeanDefinition
import org.junit.Test

class ValueInjectorTest extends InjectorTest {

    @Override
    Injector getInjector(List<BeanDefinition> beanDef, List<Bean> b) {
        return new ValueInjector(beanDef, b)
    }

    @Override
    def testInject() {
        injector.inject()

        assert beans.get(0).value.getProperties().get('protocol') == "POP3"
        assert beans.get(0).value.getProperties().get('port') == 3000

        assert beans.get(1).value.getProperties().get('maxAmount') == 5000

        assert beans.get(3).value.getProperties().get('login') == "UserLogin"
    }

//    @Test
//    void testInjectValueDependency() {
//        injector.inject()
//
//        assert beans.get(0).value.getProperties().get('protocol') == "POP3"
//        assert beans.get(0).value.getProperties().get('port') == 3000
//
//        assert beans.get(1).value.getProperties().get('maxAmount') == 5000
//
//        assert beans.get(3).value.getProperties().get('login') == "UserLogin"
//
//    }
}
