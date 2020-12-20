package com.dnedev.data.mining.naive_bayes;

import java.util.*;

public class Probabilities {
    private static final String MISSING_ATTRIBUTE = "?";

    private Map<String, Double> classProbabilities;
    private Map<String, List<ArgumentProbability>> attributeProbabilities;
    private Set<String> classes;
    private int numberOfAttributes;

    public Probabilities(Set<String> classes, List<DataInstance> learnSet, int numberOfArguments) {
        this.classes = classes;
        this.numberOfAttributes = numberOfArguments;
        this.classProbabilities = new HashMap<>();
        this.attributeProbabilities = new HashMap<>();
        calculateClassesProbabilities(learnSet, this.classes);
        calculateAttributeProbabilities(learnSet, this.classes);
    }

    private void calculateAttributeProbabilities(List<DataInstance> learnSet, Set<String> classes) {
        for (int i = 0; i < this.numberOfAttributes; i++) {
            List<Attribute> currentAttributes = new ArrayList<>();
            Set<String> attributesValues = new HashSet<>();

            for (DataInstance dataInstance : learnSet) {
                String attribute = dataInstance.getAttributes().get(i);
                String currentClassName = dataInstance.getClassName();

                if (!attribute.equals(MISSING_ATTRIBUTE)) {
                    attributesValues.add(attribute);
                    currentAttributes.add(new Attribute(attribute, currentClassName));
                }
            }

            for (String attributeValue : attributesValues) {
                long countAttributes = currentAttributes
                        .stream()
                        .filter(cAttribute -> cAttribute.getAttributeValue().equals(attributeValue)).count();

                for (String currentClass : classes) {
                    long countAttributesForClass = currentAttributes
                            .stream()
                            .filter(cAttribute -> cAttribute.getAttributeValue().equals(attributeValue)
                                    && cAttribute.getClassName().equals(currentClass)).count();
                    double probability = ((double) countAttributesForClass / countAttributes);

                    if (!attributeProbabilities.containsKey(currentClass)) {
                        attributeProbabilities.put(currentClass, new ArrayList<>());
                    }

                    attributeProbabilities.get(currentClass).add(new ArgumentProbability(attributeValue, i, probability));
//                    attributeProbabilities.get(currentClass).add(new ArgumentProbability(attributeValue, i, Math.log(probability)));
                }
            }
        }
    }

    private void calculateClassesProbabilities(List<DataInstance> learnSet, Set<String> classes) {
        for (String currentClass : classes) {
            long currentClassItems =
                    learnSet.stream().filter(dataInstance -> dataInstance.getClassName().equals(currentClass)).count();
            double classProbability = (double) currentClassItems / learnSet.size();
            classProbabilities.put(currentClass, classProbability);
            //               classProbabilities.put(currentClass, Math.log(classProbability));
        }
    }

    public String guessClass(List<String> testAttributes) {

        double bestProbability = Double.MIN_VALUE;
        String guessedClass = "";
        for (String currentClass : this.classes) {

            double currentProbability = 1;
            for (int i = 0; i < testAttributes.size(); i++) {
                double argumentProbability = getProbabilityForAttribute(i, testAttributes.get(i), currentClass);
                currentProbability *= argumentProbability;
            }
            currentProbability *= classProbabilities.get(currentClass);


            if (currentProbability >= bestProbability) {
                guessedClass = currentClass;
            }
        }
        return guessedClass;
    }

    private double getProbabilityForAttribute(int index, String attributeValue, String className) {
        if (attributeValue.equals(MISSING_ATTRIBUTE)) return 1;

        return attributeProbabilities.get(className)
                .stream()
                .filter(attribute -> attribute.getArgumentIndex().equals(index)
                        && attribute.getArgumentValue().equals(attributeValue)).findFirst().get().getProbability();
    }

    private static class Attribute {
        private String attributeValue;
        private String className;

        public Attribute(String attributeValue, String className) {
            this.attributeValue = attributeValue;
            this.className = className;
        }

        public String getAttributeValue() {
            return attributeValue;
        }


        public String getClassName() {
            return className;
        }
    }
}
