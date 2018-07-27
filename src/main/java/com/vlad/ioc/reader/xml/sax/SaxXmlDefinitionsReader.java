package com.vlad.ioc.reader.xml.sax;

import com.vlad.ioc.entity.BeanDefinition;
import com.vlad.ioc.reader.BeanDefinitionReader;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class SaxXmlDefinitionsReader implements BeanDefinitionReader {
    private SAXParserFactory factory = SAXParserFactory.newInstance();
    private SaxXmlParser saxXmlParser = new SaxXmlParser();
    private String[] paths;

    public SaxXmlDefinitionsReader() {
    }

    public SaxXmlDefinitionsReader(String[] paths) {
        this.paths = paths;
    }

    @Override
    public List<BeanDefinition> readBeanDefinitions() {
        return readBeanDefinitions(this.paths);
    }

    @Override
    public List<BeanDefinition> readBeanDefinitions(String[] paths) {
        for (String path : paths) {
            saxXmlParser.setPath(path);
            File xmlFile = new File(path);

            SAXParser parser = null;
            try {
                parser = factory.newSAXParser();
                try {
                    parser.parse(xmlFile, saxXmlParser);
                } catch (IOException e) {
                    throw new RuntimeException("Can't read XML file: " + path);
                }
            } catch (ParserConfigurationException | SAXException e) {
                throw new RuntimeException("Can't parse XML file: " + path);
            }

        }
        return saxXmlParser.getBeanDefinitionList();
    }

}
