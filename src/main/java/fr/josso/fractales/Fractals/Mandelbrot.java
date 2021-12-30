package fr.josso.fractales.Fractals;

import fr.josso.fractales.Core.Complex;
import fr.josso.fractales.Core.ComplexPlane;
import fr.josso.fractales.Core.FractalParams;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.UnaryOperator;

/**
 * The type Mandelbrot.
 */
public class Mandelbrot extends BaseFractal {

    /**
     * a Constructor.
     *
     * @param f       the function that will be used to construct the fractal.
     * @param maxIter the maximal number of times that the given function is iterated.
     * @param radius  the radius (which define the divergence) of the fractal.
     * @param plane   the portion of the plane in which the fractal is build.
     */
    public Mandelbrot(UnaryOperator<Complex> f, long maxIter, BigInteger radius, ComplexPlane plane) {
        super(f, maxIter, radius, plane, true);

    }

    /**
     * a Constructor.
     *
     * @param f       the function that will be used to construct the fractal.
     * @param maxIter the maximal number of times that the given function is iterated.
     * @param radius  the radius (which define the divergence) of the fractal.
     * @param plane   the portion of the plane in which the fractal is build.
     * @param isui    true for a graphical version else false.
     */
    public Mandelbrot(UnaryOperator<Complex> f, long maxIter, BigInteger radius, ComplexPlane plane, boolean isui) {
        super(f, maxIter, radius, plane, isui);

    }

    /**
     * a Constructor.
     *
     * @param params necessary parameters for the creation of a Mandelbrot set.
     * @return the corresponding Mandelbrot set.
     */
    public static Mandelbrot fromParams(FractalParams params) {
        ComplexPlane plane = ComplexPlane.builder()
                .minX(params.minX())
                .maxX(params.maxX())
                .minY(params.minY())
                .maxY(params.maxY())
                .step(params.step())
                .build();
        return new Mandelbrot(z->z, params.maxIter(), params.radius(), plane);
    }

    /**
     * Calculate the divergence of a number.
     * @param z a Complex number (a point on the plane).
     * @return the divergence index of that number.
     */
    @Override
    public int divergenceIndex(Complex z) {
        UnaryOperator<Complex> fun = complex -> Complex.add(complex.pow(2), z);
        int ite = 0;
        Complex zn = new Complex(0, 0);
        while (ite < this.maxIter - 1) {
            ite++;
            zn = fun.apply(zn);
            if (BigDecimal.valueOf((long) zn.modulus()).compareTo(new BigDecimal(this.radius)) > 0) {
                return ite;
            }
        }
        return ite;
    }
}
