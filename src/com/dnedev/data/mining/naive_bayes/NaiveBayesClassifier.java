package com.dnedev.data.mining.naive_bayes;

import java.io.File;
import java.util.*;

public class NaiveBayesClassifier {
    private static final String SPLIT_ATTRIBUTE_REGEX = ",";
    private List<DataInstance> data;
    private int numberOfAttributes;
    private Set<String> classes;

    public NaiveBayesClassifier(String dataSetPath) {
        this.data = new ArrayList<>();
        this.classes = new HashSet<>();
        loadDataSetFromFile(dataSetPath);
    }

    public void testAlgorithm(int nCrossValidation) {
        //Randomize every time the elements
        Collections.shuffle(this.data);

        List<List<DataInstance>> subSets = new ArrayList<>();

        for (int i = 0; i < nCrossValidation; i++) {
            subSets.add(new ArrayList<>());
        }

        for (int i = 0; i < data.size(); i++) {
            subSets.get(i % nCrossValidation).add(data.get(i));
        }

        double sum = 0;
        for (int i = 0; i < nCrossValidation; i++) {
            List<DataInstance> testSet = subSets.get(i);

            List<DataInstance> learnSet = new ArrayList<>();

            for (int j = 0; j < nCrossValidation; j++) {
                if (i != j) {
                    learnSet.addAll(subSets.get(j));
                }
            }

            double currentAccuracy = calculateAccuracy(testSet, learnSet);
            sum += currentAccuracy;

            System.out.println((i + 1) + " Validation - Accuracy:   " + currentAccuracy);
        }

        System.out.println("Accuracy:   " + (sum / nCrossValidation));
    }

    private double calculateAccuracy(List<DataInstance> testSet, List<DataInstance> learnSet) {
        int totalGuessed = 0;

        Probabilities probabilities = new Probabilities(this.classes, testSet, this.numberOfAttributes);

        for (DataInstance dataInstance : testSet) {
            if (dataInstance.getClassName().equals(probabilities.guessClass(dataInstance.getAttributes()))) {
                totalGuessed++;
            }
        }

        return (double) totalGuessed / testSet.size();
    }

    private void loadDataSetFromFile(String filePath) {
        try {
            Scanner scanner = new Scanner(new File(filePath));
            while (scanner.hasNextLine()) {
                data.add(createDataInstance(scanner.nextLine()));
            }
        } catch (Exception e) {
            System.out.println("File not found!");
        }

        this.numberOfAttributes = data.get(0).getAttributes().size();
    }

    private DataInstance createDataInstance(String data) {
        List<String> attributes = new ArrayList<>(Arrays.asList(data.split(SPLIT_ATTRIBUTE_REGEX)));
        String className = attributes.remove(0);
        this.classes.add(className);
        return new DataInstance(className, attributes);
    }
}
