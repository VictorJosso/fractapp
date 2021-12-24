package fr.josso.fractales.Fractals;

import fr.josso.fractales.Core.Complex;
import fr.josso.fractales.Core.ComplexPlane;
import javafx.scene.SubScene;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.UnaryOperator;

public class Mandelbrot extends Julia {

    public Mandelbrot(UnaryOperator<Complex> f, long maxIter, BigInteger radius, ComplexPlane plane) {
        super(f, maxIter, radius, plane);

    }


    @Override
    public int divergenceIndex(Complex z) {
        UnaryOperator<Complex> fun = new UnaryOperator<Complex>() {
            @Override
            public Complex apply(Complex complex) {
                return Complex.add(complex.pow(2), z);
            }
        };
        int ite = 0;
        Complex zn = new Complex(0, 0);
        while (ite < this.maxIter - 1) {
            ite++;
            zn = fun.apply(zn);
            if (BigDecimal.valueOf((long) zn.modulus()).compareTo(new BigDecimal(this.radius)) > 0) {
                return 0;
            }
        }
        return 100;
    }
}
