package com.vlad.ioc.reader.xml.sax;

import com.vlad.ioc.entity.BeanDefinition;
import com.vlad.ioc.exception.BeanParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaxXmlParser extends DefaultHandler {
    private List<BeanDefinition> beanDefinitions = new ArrayList<>();
    private BeanDefinition beanDefinition = null;
    private Map<String, String> valueDependenciesMap = null;
    private Map<String, String> refDependenciesMap = null;
    private int classNameCount;
    private String path;

    @Override
    public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) {
        if ("bean".equals(qName)) {
            beanDefinition = new BeanDefinition();
            valueDependenciesMap = new HashMap<>();
            refDependenciesMap = new HashMap<>();
            String id = attributes.getValue("id");
            String clazz = attributes.getValue("class");

            if (clazz != null) {
                beanDefinition.setBeanClassName(clazz);
            } else {
                throw new BeanParseException("Can't find class, in XML file: " + path + ", for bean id= " + id);
            }

            if (id != null) {
                beanDefinition.setId(id);
            } else {
                String createDefaultId = beanDefinition.getBeanClassName() + "$" + (classNameCount++);
                beanDefinition.setId(createDefaultId);
            }

        } else if ("property".equals(qName)) {
            String name = attributes.getValue("name");
            String value = attributes.getValue("value");
            String ref = attributes.getValue("ref");

            if (valueDependenciesMap != null) {
                if (name != null && value != null && ref == null) {
                    valueDependenciesMap.put(name, value);
                } else if (name != null && ref != null && value == null) {
                    refDependenciesMap.put(name, ref);
                } else {
                    throw new BeanParseException("Property attribute not correct, in XML file: " + path);
                }
            } else {
                throw new BeanParseException("Can't find bean for property in XML file: " + path);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if ("bean".equals(qName)) {
            if (beanDefinition != null) {
                beanDefinition.setDependencies(valueDependenciesMap);
                beanDefinition.setRefDependencies(refDependenciesMap);
                beanDefinitions.add(beanDefinition);
            }
        }
    }

    public List<BeanDefinition> getBeanDefinitions() {
        return beanDefinitions;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
