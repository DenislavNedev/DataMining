package com.dnedev.data.mining.kmeans;

public class Main {

    public static void main(String[] args) {
        KMeans kMeans = new KMeans(8, "test-data//unbalance.txt");
        RandomRestart randomRestart = new RandomRestart(kMeans);
        randomRestart.showClusters();
    }

}