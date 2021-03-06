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
    private int classNameCount;
    private String path;

    @Override
    public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) {
        if ("bean".equals(qName)) {
            BeanDefinition beanDefinition = new BeanDefinition();
            Map<String, String> valueDependenciesMap = new HashMap<>();
            Map<String, String> refDependenciesMap = new HashMap<>();

            beanDefinition.setDependencies(valueDependenciesMap);
            beanDefinition.setRefDependencies(refDependenciesMap);


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
            beanDefinitions.add(beanDefinition);

        } else if ("property".equals(qName)) {
            String name = attributes.getValue("name");
            String value = attributes.getValue("value");
            String ref = attributes.getValue("ref");

            if (name != null && value != null && ref == null) {
                Map<String, String> valueDependenciesMap = beanDefinitions.get(beanDefinitions.size() - 1).getDependencies();
                valueDependenciesMap.put(name, value);
            } else if (name != null && ref != null && value == null) {
                Map<String, String> refDependenciesMap = beanDefinitions.get(beanDefinitions.size() - 1).getRefDependencies();
                refDependenciesMap.put(name, ref);
            } else {
                throw new BeanParseException("Property attribute not correct, in XML file: " + path);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
    }

    public List<BeanDefinition> getBeanDefinitions() {
        return beanDefinitions;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
