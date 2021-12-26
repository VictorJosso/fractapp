package fr.josso.fractales.Fractals;
import fr.josso.fractales.Core.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.UnaryOperator;

public class Julia extends BaseFractal{

    public Julia(UnaryOperator<Complex> f, long maxIter, BigInteger radius, ComplexPlane plane) {
        super(f, maxIter, radius, plane, true);
    }

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


    protected int divergenceIndex(Complex z){
        int iterations = 0;
        Complex zn = z;
        while (iterations < this.maxIter - 1 && BigDecimal.valueOf((long) zn.modulus()).compareTo(new BigDecimal(this.radius)) < 0){
            zn = f.apply(zn);
            iterations++;
        }
        return iterations;
    }
}
