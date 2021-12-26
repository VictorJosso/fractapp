package fr.josso.fractales.Core;

import java.util.ArrayList;
import java.util.function.UnaryOperator;


public class ComplexPlane {
    private final float maxX;
    private final float maxY;
    private final float minX;
    private final float minY;
    private final double step;
    private final int scale;
    private final ArrayList<ArrayList<Complex>> points;


    private ComplexPlane(ComplexPlaneBuilder builder) {
        this.maxX = builder.maxX;
        this.maxY = builder.maxY;
        this.minX = builder.minX;
        this.minY = builder.minY;
        this.step = builder.step;
        this.points = builder.points;
        this.scale = (int) (1/builder.step);
    }

    public static ComplexPlaneBuilder builder() {
        return new ComplexPlaneBuilder();
    }

    public ArrayList<ArrayList<Complex>> getPoints() {
        return points;
    }

    public double getStep() {
        return step;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMaxY() {
        return maxY;
    }

    public float getMinX() {
        return minX;
    }

    public float getMinY() {
        return minY;
    }

    public int getScale() {
        return scale;
    }

    public int getNbPointsX(){
        return (int) (this.getScale() * (this.maxX - this.minX));
    }

    public int getNbPointsY(){
        return (int) (this.getScale() * (this.maxY - this.minY));
    }

    public static class ComplexPlaneBuilder {
        private float maxX = 1;
        private float maxY = 1;
        private float minX = -1;
        private float minY = -1;
        private double step = 0.01;
        private int scale;
        private ArrayList<ArrayList<Complex>> points;


        public ComplexPlaneBuilder maxX(float maxX) {
            this.maxX = maxX;
            return this;
        }

        public ComplexPlaneBuilder minX(float minX) {
            this.minX = minX;
            return this;
        }

        public ComplexPlaneBuilder maxY(float maxY) {
            this.maxY = maxY;
            return this;
        }

        public ComplexPlaneBuilder minY(float minY) {
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
            for (float y = minY * scale; y < maxY * this.scale; y += step * this.scale) {
                ArrayList<Complex> line = new ArrayList<>();
                for (float x = minX * scale; x < maxX * this.scale; x += step * this.scale) {
                    line.add(new Complex((double) x / this.scale, (double) y / this.scale));
                    cmpt++;
                }
                res.add(line);
            }
            System.out.println(cmpt);
            return res;
        }

        public ComplexPlane build() {
            //this.points = genPoints();
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
