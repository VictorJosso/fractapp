package fr.josso.fractales.Core;

public record Complex(double realPart, double imaginaryPart) {

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
        Complex res = new Complex(1, 0);
        for (long i = 1; i <= exponent; i++) {
            res = Complex.multiply(res, this);
        }
        return res;
    }

    public double modulus() {
        return Math.sqrt(Math.pow(this.getImaginaryPart(), 2) + Math.pow(this.getRealPart(), 2));
    }


    @Override
    public String toString() {
        return (this.getImaginaryPart() >= 0) ? this.getRealPart() + " + " + this.getImaginaryPart() + "i" :
                this.getRealPart() + " - " + (-this.getImaginaryPart() + "i");
    }

}
