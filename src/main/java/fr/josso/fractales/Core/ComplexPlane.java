package fr.josso.fractales.Core;

import java.util.ArrayList;
import java.util.function.UnaryOperator;


public class ComplexPlane {
    private final int maxX;
    private final int maxY;
    private final int minX;
    private final int minY;
    private final double step;
    private final int scale;
    private final ArrayList<ArrayList<Complex>> points;
    ResultImg img;


    private ComplexPlane(ComplexPlaneBuilder builder) {
        this.maxX = builder.maxX;
        this.maxY = builder.maxY;
        this.minX = builder.minX;
        this.minY = builder.minY;
        this.step = builder.step;
        this.points = builder.points;
        this.scale = builder.scale;
    }

    public static ComplexPlaneBuilder builder() {
        return new ComplexPlaneBuilder();
    }

    public ArrayList<ArrayList<Complex>> getPoints() {
        return points;
    }



    public static class ComplexPlaneBuilder {
        private int maxX = 1;
        private int maxY = 1;
        private int minX = -1;
        private int minY = -1;
        private double step = 0.01;
        private int scale;
        private ArrayList<ArrayList<Complex>> points;


        public ComplexPlaneBuilder maxX(int maxX) {
            this.maxX = maxX;
            return this;
        }

        public ComplexPlaneBuilder minX(int minX) {
            this.minX = minX;
            return this;
        }

        public ComplexPlaneBuilder maxY(int maxY) {
            this.maxY = maxY;
            return this;
        }

        public ComplexPlaneBuilder minY(int minY) {
            this.minY = minY;
            return this;
        }

        public ComplexPlaneBuilder step(double step) {
            this.step = step;
            return this;
        }

        private ArrayList<ArrayList<Complex>> genPoints() {
            //Is going up to 25 000 000 points
            this.scale = (int) Math.pow(10, Double.toString(step).length() - 2);
            int cmpt = 0;

            ArrayList<ArrayList<Complex>> res = new ArrayList<>();
            for (int y = minY * scale; y < maxY * this.scale; y += step * this.scale) {
                ArrayList<Complex> line = new ArrayList<>();
                for (int x = minX * scale; x < maxX * this.scale; x += step * this.scale) {
                    line.add(new Complex((double) x / this.scale, (double) y / this.scale));
                    cmpt++;
                }
                res.add(line);
            }
            System.out.println(cmpt);
            return res;
        }

        public ComplexPlane build() {
            this.points = genPoints();
            return new ComplexPlane(this);
        }

    }

    @Override
    public String toString() {
        return "ComplexPlane{" +
                "maxX=" + maxX +
                ", maxY=" + maxY +
                ", step=" + step +
                '}';
    }
}
