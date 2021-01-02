package com.dnedev.data.mining.kmeans;

public class Main {

    public static void main(String[] args) {
        KMeans kMeans = new KMeans(4, "test-data//normal.txt");
        kMeans.calculateClusters();
        kMeans.showClusters();
    }

}