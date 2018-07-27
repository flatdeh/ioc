import com.vlad.ioc.context.ClassPathApplicationContext;
import com.vlad.ioc.reader.BeanDefinitionReader;
import com.vlad.ioc.reader.xml.stax.StaxXmlBeanDefinitionReader;
import org.junit.Test;
import static groovy.test.GroovyAssert.shouldFail;
import groovy.util.GroovyTestSuite;

import static org.junit.Assert.assertEquals;

public class ClassPath {

    @Test
    void indexOutOfBoundsAccess() {
        def numbers = [1,2,3,4]
        shouldFail {
            numbers.get(4)
        }
    }


}
