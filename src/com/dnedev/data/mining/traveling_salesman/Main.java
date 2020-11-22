package com.dnedev.data.mining.traveling_salesman;

public class Main {

    public static void main(String[] args) {
        SalesmanMap salesmanMap = new SalesmanMap(100, Integer.MAX_VALUE, Integer.MIN_VALUE);
        salesmanMap.getSolution();
    }
}
