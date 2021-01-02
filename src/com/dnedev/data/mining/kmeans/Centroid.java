package com.dnedev.data.mining.kmeans;

import java.util.List;
import java.util.OptionalDouble;

public class Centroid extends Point {

    public Centroid(double x, double y) {
        super(x, y);
    }

    public void recalculateCoordinates(List<Point> points) {
        OptionalDouble xMean = points.stream().mapToDouble(Point::getX).average();
        OptionalDouble yMean = points.stream().mapToDouble(Point::getY).average();
        super.setX(xMean.isPresent() ? xMean.getAsDouble() : this.getX());
        super.setY(yMean.isPresent() ? yMean.getAsDouble() : this.getY());
    }

}
