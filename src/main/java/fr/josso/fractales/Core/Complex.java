package fr.josso.fractales.Core;

import java.util.function.UnaryOperator;

public class Complex {
    private double realPart;
    private double imaginaryPart;


    public Complex(double realPart, double imaginaryPart) {
        this.imaginaryPart = imaginaryPart;
        this.realPart = realPart;
    }

    public double getRealPart() {
        return realPart;
    }

    public double getImaginaryPart() {
        return imaginaryPart;
    }

    public static Complex add(Complex z1, Complex z2) {
        return new Complex(z1.getRealPart() + z2.getRealPart(),
                z1.getImaginaryPart() + z2.getImaginaryPart());
    }

    public static Complex multiply(Complex z1, Complex z2) {
        return new Complex(z1.getRealPart() * z2.getRealPart() - z1.getImaginaryPart() * z2.getImaginaryPart(),
                z1.getRealPart() * z2.getImaginaryPart() + z1.getImaginaryPart() * z2.getRealPart());
    }

    public Complex pow(long exponent) {
        Complex res = new Complex(1,0);
        for (long i = 1; i <= exponent; i++) {
            res = Complex.multiply(this, this);
        }
        return res;
    }

    private double modulus(){
        return Math.sqrt(Math.pow(this.getImaginaryPart(), 2) + Math.pow(this.getRealPart(), 2));
    }

    private Complex applyFunction(UnaryOperator<Complex> f) {
        Complex temp = f.apply(this);
        return new Complex(temp.getRealPart(), temp.getImaginaryPart());
    }


    public Complex applyFunctionXTimes(UnaryOperator<Complex> f, long x) {
        Complex temp = this;
        for (long i = 0; i < x; i++) {
            temp = temp.applyFunction(f);
        }
        return temp;
    }

    public int divergenceIndex(long maxIter, int radius, UnaryOperator<Complex> f) {
        int ite = 0;
        Complex zn = new Complex(this.getRealPart(), this.getImaginaryPart());
        while (ite < maxIter - 1 && zn.modulus() <= radius) {
            ite++;
            zn = zn.applyFunction(f);
        }
        return ite;
    }

    @Override
    public String toString(){
        return (this.getImaginaryPart()>=0)?this.getRealPart() + " + " + this.getImaginaryPart() + "i":
                this.getRealPart() + " - " + (-this.getImaginaryPart() + "i");
    }

}
