package com.dnedev.data.mining.naive_bayes;

import java.util.List;

public class DataInstance {
    private String className;
    private List<String> attributes;

    public DataInstance(String className, List<String> attributes) {
        this.className = className;
        this.attributes = attributes;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "DataInstance{" +
                "className='" + className + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}
