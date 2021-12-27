package fr.josso.fractales.Core;

public class ComplexPlane {
    private final float maxX;
    private final float maxY;
    private final float minX;
    private final float minY;
    private final double step;
    private final int scale;


    private ComplexPlane(ComplexPlaneBuilder builder) {
        this.maxX = builder.maxX;
        this.maxY = builder.maxY;
        this.minX = builder.minX;
        this.minY = builder.minY;
        this.step = builder.step;
        this.scale = (int) (1/builder.step);
    }

    public static ComplexPlaneBuilder builder() {
        return new ComplexPlaneBuilder();
    }

    public double getStep() {
        return step;
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

        public ComplexPlane build() {
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
