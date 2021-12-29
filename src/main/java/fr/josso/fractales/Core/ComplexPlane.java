package fr.josso.fractales.Core;

public class ComplexPlane {
    private final float maxX;
    private final float maxY;
    private final float minX;
    private final float minY;
    private final double step;
    private final int scale;


    /**
     * @param builder an intern class to build a plane.
     */
    private ComplexPlane(ComplexPlaneBuilder builder) {
        this.maxX = builder.maxX;
        this.maxY = builder.maxY;
        this.minX = builder.minX;
        this.minY = builder.minY;
        this.step = builder.step;
        this.scale = (int) (1/builder.step);
    }

    /**
     * @return a builder necessary for instantiation.
     */
    public static ComplexPlaneBuilder builder() {
        return new ComplexPlaneBuilder();
    }

    /**
     * @return return the step between to point in real and imaginary part.
     */
    public double getStep() {
        return step;
    }

    /**
     * @return minimal real part of any point of the plane.
     */
    public float getMinX() {
        return minX;
    }

    /**
     * @return minimal imaginary part of any point of the plane.
     */
    public float getMinY() {
        return minY;
    }

    /**
     * @return the scale of this plane.
     */
    public int getScale() {
        return scale;
    }

    /**
     * @return the number of points on the X axis.
     */
    public int getNbPointsX(){
        return (int) (this.getScale() * (this.maxX - this.minX));
    }

    /**
     * @return the number of points on the Y axis.
     */
    public int getNbPointsY(){
        return (int) (this.getScale() * (this.maxY - this.minY));
    }

    public static class ComplexPlaneBuilder {
        private float maxX = 1;
        private float maxY = 1;
        private float minX = -1;
        private float minY = -1;
        private double step = 0.01;


        /**
         * @param maxX wanted maximal real part.
         * @return this builder with maxX updated (default at 1).
         */
        public ComplexPlaneBuilder maxX(float maxX) {
            this.maxX = maxX;
            return this;
        }


        /**
         * @param minX wanted minimal real part.
         * @return this builder with minX updated (default at -1).
         */
        public ComplexPlaneBuilder minX(float minX) {
            this.minX = minX;
            return this;
        }

        /**
         * @param maxY wanted maximal imaginary part.
         * @return this builder with maxY updated (default at 1).
         */
        public ComplexPlaneBuilder maxY(float maxY) {
            this.maxY = maxY;
            return this;
        }

        /**
         * @param minY wanted minimal imaginary part.
         * @return this builder with minY updated (default at -1).
         */
        public ComplexPlaneBuilder minY(float minY) {
            this.minY = minY;
            return this;
        }

        /**
         * @param step wanted difference in imaginary or real part between two points.
         * @return this builder with maxY updated (default at 0.01).
         */
        public ComplexPlaneBuilder step(double step) {
            this.step = step;
            return this;
        }

        /**
         * @return a ComplexPlane with the same parameters as this builder.
         */
        public ComplexPlane build() {
            return new ComplexPlane(this);
        }

    }

    /**
     * @return a string representation of this ComplexPlane.
     */
    @Override
    public String toString() {
        return "ComplexPlane{" +
                "maxX=" + maxX +
                ", maxY=" + maxY +
                ", minX=" + minX +
                ", minY=" + minY +
                ", step=" + step +
                ", scale=" + scale +
                '}';
    }
}
