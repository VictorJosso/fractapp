package fr.josso.fractales.Core;

import java.math.BigInteger;
import java.util.function.UnaryOperator;

public record FractalParams(float minX, float maxX, float minY, float maxY, double step, BigInteger radius,
                            long maxIter, UnaryOperator<Complex> equation) {
    /**
     * @return the minimal real part of the fractal.
     */
    @Override
    public float minX() {
        return minX;
    }

    /**
     * @return the maximal real part of the fractal.
     */
    @Override
    public float maxX() {
        return maxX;
    }

    /**
     * @return the minimal imaginary part of the fractal.
     */
    @Override
    public float minY() {
        return minY;
    }

    /**
     * @return the maximal imaginary part of the fractal.
     */
    @Override
    public float maxY() {
        return maxY;
    }

    /**
     * @return the step between two points of the fractal.
     */
    @Override
    public double step() {
        return step;
    }

    /**
     * @return the radius (which define the divergence) of the fractal.
     */
    @Override
    public BigInteger radius() {
        return radius;
    }

    /**
     * @return the maximal number of times that the given function is iterated.
     */
    @Override
    public long maxIter() {
        return maxIter;
    }

    /**
     * @return the function that define the fractal.
     */
    @Override
    public UnaryOperator<Complex> equation() {
        return equation;
    }
}
