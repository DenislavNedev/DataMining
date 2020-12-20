package com.dnedev.data.mining.naive_bayes;

public class ArgumentProbability {
    private String argumentValue;
    private Integer argumentIndex;
    private double probability;

    public ArgumentProbability(String argumentValue, Integer argumentIndex, double probability) {
        this.argumentValue = argumentValue;
        this.argumentIndex = argumentIndex;
        this.probability = probability;
    }

    public String getArgumentValue() {
        return argumentValue;
    }

    public Integer getArgumentIndex() {
        return argumentIndex;
    }

    public double getProbability() {
        return probability;
    }
}
