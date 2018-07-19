package com.vlad.ioc.definition;

import java.util.Map;

public class BeanDefinition {
    private String id;
    private String beanClassName;
    private Map<String, String> dependencies;
    private Map<String, Object> refDependencies;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public Map<String, String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Map<String, String> dependencies) {
        this.dependencies = dependencies;
    }

    public Map<String, Object> getRefDependencies() {
        return refDependencies;
    }

    public void setRefDependencies(Map<String, Object> refDependencies) {
        this.refDependencies = refDependencies;
    }
}
