package fr.josso.fractales.Core;

import java.math.BigInteger;
import java.util.function.UnaryOperator;

/**
 * The needed parameters to build a fractal.
 */
public record FractalParams(float minX, float maxX, float minY, float maxY, double step, BigInteger radius,
                            long maxIter, UnaryOperator<Complex> equation) {
    /**
     * Getter for minX.
     * @return the minimal real part of the fractal.
     */
    @Override
    public float minX() {
        return minX;
    }

    /**
     * Getter for maxX.
     * @return the maximal real part of the fractal.
     */
    @Override
    public float maxX() {
        return maxX;
    }

    /**
     * Getter for minY.
     * @return the minimal imaginary part of the fractal.
     */
    @Override
    public float minY() {
        return minY;
    }

    /**
     * Getter for maxY.
     * @return the maximal imaginary part of the fractal.
     */
    @Override
    public float maxY() {
        return maxY;
    }

    /**
     * Getter for step.
     * @return the step between two points of the fractal.
     */
    @Override
    public double step() {
        return step;
    }

    /**
     * Getter for radius.
     * @return the radius (which define the divergence) of the fractal.
     */
    @Override
    public BigInteger radius() {
        return radius;
    }

    /**
     * Getter for maxIter.
     * @return the maximal number of times that the given function is iterated.
     */
    @Override
    public long maxIter() {
        return maxIter;
    }

    /**
     * Getter for equation/function.
     * @return the function that define the fractal.
     */
    @Override
    public UnaryOperator<Complex> equation() {
        return equation;
    }
}
