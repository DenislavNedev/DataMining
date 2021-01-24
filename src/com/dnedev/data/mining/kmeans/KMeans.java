package com.dnedev.data.mining.kmeans;

import java.io.File;
import java.util.List;
import java.util.*;

public class KMeans {

    private int numberOfClusters;
    private Set<Centroid> centroids;
    private Random random;
    private List<Point> points;
    private double maxX;
    private double minX;
    private double maxY;
    private double minY;
    private Map<Point, Centroid> clusters;

    public Set<Centroid> getCentroids() {
        return centroids;
    }

    public Map<Point, Centroid> getClusters() {
        return clusters;
    }

    public KMeans(int numberOfClusters, String filePath) {
        this.numberOfClusters = numberOfClusters;
        this.random = new Random();
        this.points = new ArrayList<>();
        this.minX = Double.MAX_VALUE;
        this.maxX = Double.MIN_VALUE;
        this.minY = Double.MAX_VALUE;
        this.maxY = Double.MIN_VALUE;
        loadDataSetFromFile(filePath);
    }

    public double getMeanFromPointToCluster() {
        double sumValue = Double.MIN_VALUE;

        for (Map.Entry<Point, Centroid> entry : clusters.entrySet()) {
            sumValue += entry.getKey().calculateDistanceBetweenPoints(entry.getValue());
        }
        return sumValue;
    }

    public void calculateClusters() {
        this.centroids = new HashSet<>();
        this.clusters = new HashMap<>();

        initRandomCentroids(this.numberOfClusters);
        initDefaultClusters(this.points, this.centroids);
        recalculateCenterOfClusters(this.centroids, this.clusters);

        while (recalculatePointCentroids(this.clusters, this.centroids)) {
            recalculateCenterOfClusters(this.centroids, this.clusters);
        }

    }

    private boolean recalculatePointCentroids(Map<Point, Centroid> clusters, Set<Centroid> centroids) {
        boolean hasChanges = false;
        for (Map.Entry<Point, Centroid> entry : clusters.entrySet()) {

            Point currentPoint = entry.getKey();
            Centroid nearestCentroid = entry.getValue();
            double nearestDistance = currentPoint.calculateDistanceBetweenPoints(nearestCentroid);

            for (Centroid currentCentroid : centroids) {
                double currentDistance = currentPoint.calculateDistanceBetweenPoints(currentCentroid);
                if (currentDistance < nearestDistance && nearestCentroid != currentCentroid) {
                    nearestDistance = currentDistance;
                    nearestCentroid = currentCentroid;

                    if (!hasChanges) hasChanges = true;
                }
            }
            this.clusters.put(currentPoint, nearestCentroid);
        }

        return hasChanges;
    }

    private void initDefaultClusters(List<Point> points, Set<Centroid> centroids) {
        for (Point point : points) {
            double nearestDistance = Double.MAX_VALUE;

            Centroid nearestCentroid = null;
            for (Centroid centroid : centroids) {
                double currentDistance = point.calculateDistanceBetweenPoints(centroid);
                if (currentDistance < nearestDistance) {
                    nearestDistance = currentDistance;
                    nearestCentroid = centroid;
                }
            }
            this.clusters.put(point, nearestCentroid);
        }
    }

    private void recalculateCenterOfClusters(Set<Centroid> centroids, Map<Point, Centroid> clusters) {
        for (Centroid centroid : centroids) {
            List<Point> centroidPoints = new ArrayList<>();
            for (Map.Entry<Point, Centroid> entry : clusters.entrySet()) {
                if (entry.getValue().equals(centroid)) {
                    centroidPoints.add(entry.getKey());
                }
            }
            centroid.recalculateCoordinates(centroidPoints);
        }
    }

    private void initRandomCentroids(int numberOfCentroids) {
        for (int i = 0; i < numberOfCentroids; i++) {
            double randomX = minX + (maxX - minX) * random.nextDouble();
            double randomY = minX + (maxX - minX) * random.nextDouble();
            centroids.add(new Centroid(randomX, randomY));
        }
    }

    private void loadDataSetFromFile(String filePath) {
        try {
            Scanner scanner = new Scanner(new File(filePath));
            while (scanner.hasNextLine()) {
                points.add(createPoint(scanner.nextLine()));
            }
        } catch (Exception e) {
            System.out.println("File not found!");
        }
    }

    private Point createPoint(String data) {
        String[] pointsData = data.split("\\s+");
        double x = Double.parseDouble(pointsData[0]);
        double y = Double.parseDouble(pointsData[1]);
        recalculateMinMaxValues(x, y);
        return new Point(x, y);
    }

    private void recalculateMinMaxValues(double currentX, double currentY) {
        if (currentX > maxX) {
            this.maxX = currentX;
        }

        if (currentY > maxY) {
            this.maxY = currentY;
        }

        if (currentX < minX) {
            this.minX = currentX;
        }

        if (currentY < minY) {
            this.minY = currentY;
        }
    }
}
