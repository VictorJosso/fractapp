package fr.josso.fractales.Fractals;
import fr.josso.fractales.Core.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.UnaryOperator;

public class Julia extends BaseFractal{

    /**
     * a Constructor.
     * @param f the function that will be used to construct the fractal.
     * @param maxIter the maximal number of times that the given function is iterated.
     * @param radius the radius (which define the divergence) of the fractal.
     * @param plane the portion of the plane in which the fractal is build.
     */
    public Julia(UnaryOperator<Complex> f, long maxIter, BigInteger radius, ComplexPlane plane) {
        super(f, maxIter, radius, plane, true);
    }

    /**
     * a Constructor.
     * @param f the function that will be used to construct the fractal.
     * @param maxIter the maximal number of times that the given function is iterated.
     * @param radius the radius (which define the divergence) of the fractal.
     * @param plane the portion of the plane in which the fractal is build.
     * @param isui true for a graphical version else false.
     */
    public Julia(UnaryOperator<Complex> f, long maxIter, BigInteger radius, ComplexPlane plane, boolean isui) {
        super(f, maxIter, radius, plane, isui);
    }


    /**
     * a Constructor.
     * @param params necessary parameters for the creation of a Julia set.
     * @return the corresponding Julia set.
     */
    public static Julia fromParams(FractalParams params) {
        ComplexPlane plane = ComplexPlane.builder()
                .minX(params.minX())
                .maxX(params.maxX())
                .minY(params.minY())
                .maxY(params.maxY())
                .step(params.step())
                .build();
        return new Julia(params.equation(), params.maxIter(), params.radius(), plane);
    }


    /**
     * @param z a Complex number (a point on the plane).
     * @return the divergence index of that number.
     */
    protected int divergenceIndex(Complex z){
        int iterations = 0;
        Complex zn = z;
        while (iterations < this.maxIter - 1 && BigDecimal.valueOf((long) zn.modulus()).compareTo(new BigDecimal(this.radius)) < 0){
            zn = f.apply(zn);
            iterations++;
        }
        return 3*iterations/4;
    }
}
