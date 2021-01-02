package com.dnedev.data.mining.kmeans;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.awt.*;
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

    public KMeans(int numberOfClusters, String filePath) {
        this.numberOfClusters = numberOfClusters;
        this.centroids = new HashSet<>();
        this.clusters = new HashMap<>();
        this.random = new Random();
        this.points = new ArrayList<>();
        this.minX = Double.MAX_VALUE;
        this.maxX = Double.MIN_VALUE;
        this.minY = Double.MAX_VALUE;
        this.maxY = Double.MIN_VALUE;
        loadDataSetFromFile(filePath);
    }

    public void calculateClusters() {
        initRandomCentroids(this.numberOfClusters);
        initDefaultClusters(this.points, this.centroids);
        recalculateCenterOfClusters(this.centroids, this.clusters);


        while (recalculatePointCentroids(this.clusters, this.centroids)) {
            recalculateCenterOfClusters(this.centroids, this.clusters);
        }


    }

    public void showClusters() {
        XYChart chart = new XYChartBuilder().width(900).height(700).title("KMean").xAxisTitle("X").yAxisTitle("Y").build();

        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setChartTitleVisible(true);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        chart.getStyler().setMarkerSize(20);

        int i = 0;
        List<Double> centroidDataX = new ArrayList<>();
        List<Double> centroidDataY = new ArrayList<>();
        for (Centroid currentCentroid : centroids) {

            centroidDataX.add(currentCentroid.getX());
            centroidDataY.add(currentCentroid.getY());

            List<Double> dataX = new ArrayList<>();
            List<Double> dataY = new ArrayList<>();

            for (Map.Entry<Point, Centroid> entry : clusters.entrySet()) {
                if (entry.getValue().equals(currentCentroid)) {
                    dataX.add(entry.getKey().getX());
                    dataY.add(entry.getKey().getY());
                }
            }
            if (!dataX.isEmpty() && !dataY.isEmpty()) {
                chart.addSeries(Integer.toString(i), dataX, dataY);
                i++;
            }
        }

        XYSeries series = chart.addSeries("Centroids", centroidDataX, centroidDataY);
        series.setMarker(SeriesMarkers.DIAMOND);
        series.setMarkerColor(new Color(255, 0, 0));
        new SwingWrapper(chart).displayChart();
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
