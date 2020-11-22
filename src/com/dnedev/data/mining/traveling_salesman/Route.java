package com.dnedev.data.mining.traveling_salesman;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Route {
    private static final int RANDOM_SHUFFLE = 300;

    private List<Integer> order = new ArrayList<>();
    private Random randomGenerator = new Random();
    private List<Point> points;

    public Route(int totalPoint, List<Point> points) {
        this.points = points;

        for (int i = 0; i < totalPoint; i++) {
            this.order.add(i, i);
        }

        for (int n = 0; n < RANDOM_SHUFFLE; n++) {
            this.shuffle();
        }
    }

    public Route(List<Integer> order, List<Point> points) {
        this.points = points;
        this.order = order;
        if (randomGenerator.nextBoolean()) {
            this.shuffle();
        }
    }

    private void shuffle() {
        int i = randomGenerator.nextInt(this.order.size());
        int j = randomGenerator.nextInt(this.order.size());
        while (i == j) {
            j = randomGenerator.nextInt(this.order.size());
        }
        swap(i, j);
    }

    private void swap(int i, int j) {
        int temp = this.order.get(i);
        this.order.set(i, this.order.get(j));
        this.order.set(j, temp);
    }

    public double calculateDistance() {
        double distance = 0;
        for (int i = 0; i < this.order.size() - 1; i++) {
            int pointAIndex = this.order.get(i);
            Point pointA = points.get(pointAIndex);

            int pointBIndex = this.order.get(i + 1);
            Point pointB = points.get(pointBIndex);

            distance += calculateDistanceBetweenPoints(pointA, pointB);
        }

        return distance;
    }


    public double calculateDistanceBetweenPoints(Point point1, Point point2) {
        double distance = Math.hypot(Math.abs(point2.getY() - point1.getY()), Math.abs(point2.getX() - point1.getX()));
        return Double.parseDouble(String.format("%.4f", distance));
    }


    public List<Integer> crossover(Route otherRoute) {
        List<Integer> order1 = this.order;
        List<Integer> order2 = otherRoute.order;

        int start = randomGenerator.nextInt(order1.size() - 1);
        int end = randomGenerator.nextInt(order1.size() - start) + start + 1;

        while (start == end) {
            end = randomGenerator.nextInt(order1.size() - start) + start;
        }
        List<Integer> newOrder = new ArrayList<>(order1.subList(start, end));

        int leftOver = order1.size() - newOrder.size();

        int count = 0;
        int i = 0;

        while (count < leftOver) {

            int point = order2.get(i);

            if (!newOrder.contains(point)) {
                newOrder.add(point);
                count++;
            }

            i++;
        }
        return newOrder;
    }
}