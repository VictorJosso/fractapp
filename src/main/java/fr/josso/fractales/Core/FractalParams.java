package fr.josso.fractales.Core;

import java.math.BigInteger;
import java.util.function.UnaryOperator;

public record FractalParams(float minX, float maxX, float minY, float maxY, double step, BigInteger radius,
                            long maxIter, UnaryOperator<Complex> equation) {
    @Override
    public float minX() {
        return minX;
    }

    @Override
    public float maxX() {
        return maxX;
    }

    @Override
    public float minY() {
        return minY;
    }

    @Override
    public float maxY() {
        return maxY;
    }

    @Override
    public double step() {
        return step;
    }

    @Override
    public BigInteger radius() {
        return radius;
    }

    @Override
    public long maxIter() {
        return maxIter;
    }

    @Override
    public UnaryOperator<Complex> equation() {
        return equation;
    }
}
