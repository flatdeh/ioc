package com.vlad.ioc.reader.xml.stax;

import com.vlad.ioc.entity.BeanDefinition;
import com.vlad.ioc.exception.BeanParseException;
import com.vlad.ioc.reader.BeanDefinitionReader;

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

public class StaxXmlBeanDefinitionReader implements BeanDefinitionReader {
    private XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
    private int classNameCount;

    public List<BeanDefinition> readBeanDefinitions(String[] paths) {
        List<BeanDefinition> beanDefinitionList = new ArrayList<>();
        BeanDefinition beanDefinition = null;
        Map<String, String> dependenciesMap = null;
        Map<String, String> refDependenciesMap = null;

        for (String path : paths) {
            try (BufferedReader xmlFileBufferedReader = new BufferedReader(new FileReader(path))) {
                XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(xmlFileBufferedReader);
                while (xmlEventReader.hasNext()) {
                    XMLEvent xmlEvent = xmlEventReader.nextEvent();
                    if (xmlEvent.isStartElement()) {

                        StartElement startElement = xmlEvent.asStartElement();
                        String xmlTagName = startElement.getName().getLocalPart();

                        if ("bean".equals(xmlTagName)) {
                            dependenciesMap = new HashMap<>();
                            refDependenciesMap = new HashMap<>();
                            beanDefinition = new BeanDefinition();


                            Attribute attributeByName = startElement.getAttributeByName(new QName("class"));
                            if (attributeByName != null) {
                                beanDefinition.setBeanClassName(attributeByName.getValue());
                            } else {
                                attributeByName = startElement.getAttributeByName(new QName("id"));
                                throw new BeanParseException("Can't find bean class, in XML file" + path + ", for bean id= " + attributeByName);
                            }

                            attributeByName = startElement.getAttributeByName(new QName("id"));
                            if (attributeByName != null) {
                                beanDefinition.setId(attributeByName.getValue());
                            } else {
                                String createDefaultId = beanDefinition.getBeanClassName() + "$" + (classNameCount++);
                                beanDefinition.setId(createDefaultId);
                            }

                        } else if ("property".equals(xmlTagName)) {
                            Attribute nameAttr = startElement.getAttributeByName(new QName("name"));
                            Attribute valueAttr = startElement.getAttributeByName(new QName("value"));
                            Attribute refAttr = startElement.getAttributeByName(new QName("ref"));

                            if (dependenciesMap != null && refDependenciesMap != null) {
                                if (nameAttr != null && valueAttr != null) {
                                    dependenciesMap.put(nameAttr.getValue(), valueAttr.getValue());
                                } else if (nameAttr != null && refAttr != null) {
                                    refDependenciesMap.put(nameAttr.getValue(), refAttr.getValue());
                                } else {
                                    throw new BeanParseException("Property attribute not correct, in XML file" + path);
                                }
                            } else {
                                throw new BeanParseException("Can't find bean for property " +
                                        xmlTagName + " in XML file: " + path);
                            }
                        }
                    } else if (xmlEvent.isEndElement()) {
                        EndElement endElement = xmlEvent.asEndElement();
                        String xmlTagName = endElement.getName().getLocalPart();

                        if ("bean".equals(xmlTagName)) {
                            if (beanDefinition != null) {
                                beanDefinition.setDependencies(dependenciesMap);
                                beanDefinition.setRefDependencies(refDependenciesMap);
                            }
                            beanDefinitionList.add(beanDefinition);
                        }
                    }
                }

            } catch (XMLStreamException e) {
                throw new RuntimeException("Unexpected error, can't parse XML file: " + path, e);
            } catch (IOException e) {
                throw new RuntimeException("Can't find XML file: " + path, e);
            }
        }
        return beanDefinitionList;
    }

}
