package com.dnedev.data.mining.naive_bayes;

public class Main {

    public static void main(String[] args) {
        NaiveBayesClassifier naiveBayesClassifier = new NaiveBayesClassifier("test-data//house-votes-84.data");
        naiveBayesClassifier.testAlgorithm(10);
    }
}
