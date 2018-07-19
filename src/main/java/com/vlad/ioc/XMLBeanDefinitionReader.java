package com.vlad.ioc;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLBeanDefinitionReader implements BeanDefinitionReader {
    private String[] path;

    public XMLBeanDefinitionReader(String[] path) {
        this.path = path;
    }

    public List<BeanDefinition> readBeanDefinitions() {
        List<BeanDefinition> beanDefinitionList = new ArrayList<>();
        BeanDefinition beanDefinition = null;
        Map<String, String> dependenciesMap = null;
        Map<String, Object> refDependenciesMap = null;
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try {
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(this.path[0]));
            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();
                if (xmlEvent.isStartElement()) {

                    StartElement startElement = xmlEvent.asStartElement();
                    if (startElement.getName().getLocalPart().equals("bean")) {
                        beanDefinition = new BeanDefinition();
                        dependenciesMap = new HashMap<>();
                        refDependenciesMap = new HashMap<>();

                        Attribute idAttr = startElement.getAttributeByName(new QName("id"));
                        if (idAttr != null) {
                            beanDefinition.setId(idAttr.getValue());
                        }
                        idAttr = startElement.getAttributeByName(new QName("class"));
                        if (idAttr != null) {
                            beanDefinition.setBeanClassName(idAttr.getValue());
                        }
                    } else if (startElement.getName().getLocalPart().equals("property")) {

                        Attribute nameAttr = startElement.getAttributeByName(new QName("name"));
                        Attribute valueAttr = startElement.getAttributeByName(new QName("value"));
                        Attribute refAttr = startElement.getAttributeByName(new QName("ref"));
                        if (nameAttr != null && valueAttr != null && dependenciesMap != null) {
                            dependenciesMap.put(nameAttr.getValue(), valueAttr.getValue());
                        }
                        if (nameAttr != null && refAttr != null && refDependenciesMap != null) {
                            refDependenciesMap.put(nameAttr.getValue(), refAttr.getValue());
                        }
                    }
                }

                if (xmlEvent.isEndElement()) {
                    EndElement endElement = xmlEvent.asEndElement();
                    if (endElement.getName().getLocalPart().equals("bean")) {
                        if (beanDefinition != null) {
                            beanDefinition.setDependencies(dependenciesMap);
                            beanDefinition.setRefDependencies(refDependenciesMap);
                        }
                        beanDefinitionList.add(beanDefinition);
                    }
                }
            }

        } catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
        }
        return beanDefinitionList;
    }

}
