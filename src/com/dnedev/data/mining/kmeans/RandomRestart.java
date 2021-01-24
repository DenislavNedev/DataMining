package com.dnedev.data.mining.kmeans;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.awt.*;
import java.util.*;
import java.util.List;

public class RandomRestart {
    private static final int RANDOM_RESTARTS_VALUE = 1000;

    private KMeans kMeans;
    private Set<Centroid> centroids;
    private Map<Point, Centroid> clusters;

    public RandomRestart(KMeans kMeans) {
        this.kMeans = kMeans;
        this.centroids = new HashSet<>();
        this.clusters = new HashMap<>();
        findBestClusters();
    }

    private void findBestClusters() {
        double currentBestMean = Double.MAX_VALUE;

        for (int i = 0; i < RANDOM_RESTARTS_VALUE; i++) {
            kMeans.calculateClusters();
            double currentValue = kMeans.getMeanFromPointToCluster();

            if (currentValue < currentBestMean) {
                System.out.println(i);
                currentBestMean = currentValue;
                this.centroids = kMeans.getCentroids();
                this.clusters = kMeans.getClusters();
            }
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
}
