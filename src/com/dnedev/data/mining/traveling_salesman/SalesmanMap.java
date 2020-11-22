package com.dnedev.data.mining.traveling_salesman;

import java.util.*;

public class SalesmanMap {
    private static final int TOTAL_POPULATION = 2000;
    private static final int GENERATION_SIZE = 1000;

    private int rangeMin;
    private int rangeMax;
    private List<Point> points;
    private List<Route> currentPopulation;
    private Random randomGenerator;

    public SalesmanMap(int numberOfPoints, int rangeMax, int rangeMin) {
        this.rangeMax = rangeMax;
        this.rangeMin = rangeMin;
        this.points = new ArrayList<>();
        this.randomGenerator = new Random();
        generatePoints(numberOfPoints);
        generateInitialPopulation(numberOfPoints);
    }

    private void generateInitialPopulation(int numberOfPoints) {
        this.currentPopulation = new ArrayList<>();
        for (int i = 0; i < TOTAL_POPULATION; i++) {
            this.currentPopulation.add(new Route(numberOfPoints, this.points));
        }
    }

    private void generatePoints(int numberOfPoints) {

        Set<Point> uniquePoints = new HashSet<>();
        while (uniquePoints.size() != numberOfPoints) {

            double xValue = rangeMin + Math.random() * (rangeMax - rangeMin);
            double yValue = rangeMin + Math.random() * (rangeMax - rangeMin);

            uniquePoints.add(new Point(Double.parseDouble(String.format("%.4f", xValue)), Double.parseDouble(String.format("%.4f", yValue))));
        }

        this.points.addAll(uniquePoints);
    }

    public void printPoints() {
        for (Point point : points) {
            System.out.println(point.toString());
        }
    }

    public void getSolution() {
        double minimumDistance = Double.MAX_VALUE;
        Route bestPopulation;

        for (int j = 0; j < GENERATION_SIZE; j++) {
            System.out.println("Generation " + (j + 1) + ": " + minimumDistance);
            if (j == 9 || j == 39 || j == 59 || j == 79) {
            //    System.out.println("Generation " + (j + 1) + ": " + minimumDistance);
            }

            for (Route route : this.currentPopulation) {
                double currentDistance = route.calculateDistance();

                if (currentDistance < minimumDistance) {
                    minimumDistance = currentDistance;
                    bestPopulation = route;
                }
            }

            List<Route> newPopulation = new ArrayList<>();

            for (int k = 0; k < currentPopulation.size(); k++) {
                Route population1 = currentPopulation.get(randomGenerator.nextInt(currentPopulation.size()));
                Route population2 = currentPopulation.get(randomGenerator.nextInt(currentPopulation.size()));

                while (population1 == population2) {
                    population2 = currentPopulation.get(randomGenerator.nextInt(currentPopulation.size()));
                }

                List<Integer> order = population1.crossover(population2);
                newPopulation.add(k, new Route(order, this.points));
            }

            currentPopulation = newPopulation;
        }

        System.out.println("Generation " + GENERATION_SIZE + ": " + minimumDistance);
    }
}
