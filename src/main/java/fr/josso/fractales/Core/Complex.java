package fr.josso.fractales.Core;

public record Complex(double realPart, double imaginaryPart) {

    /**
     * @return The imaginary part of that number.
     */
    public double getRealPart() {
        return realPart;
    }

    /**
     * @return The imaginary part of that number.
     */
    public double getImaginaryPart() {
        return imaginaryPart;
    }


    /**
     * @param z1 First term of the addition
     * @param z2 Second term of the addition
     * @return The addition of z1 and z2
     */
    public static Complex add(Complex z1, Complex z2) {
        return new Complex(z1.getRealPart() + z2.getRealPart(),
                z1.getImaginaryPart() + z2.getImaginaryPart());
    }


    /**
     * @param z1 First factor of the multiplication
     * @param z2 Second factor of the multiplication
     * @return The multiplication of z1 and z2
     */
    public static Complex multiply(Complex z1, Complex z2) {
        return new Complex(z1.getRealPart() * z2.getRealPart() - z1.getImaginaryPart() * z2.getImaginaryPart(),
                z1.getRealPart() * z2.getImaginaryPart() + z1.getImaginaryPart() * z2.getRealPart());
    }

    /**
     * @param exponent the wanted power
     * @return this number to the power exponent
     */
    public Complex pow(long exponent) {
        Complex res = new Complex(1, 0);
        for (long i = 1; i <= exponent; i++) {
            res = Complex.multiply(res, this);
        }
        return res;
    }


    /**
     * @return this number's modulus
     */
    public double modulus() {
        return Math.sqrt(Math.pow(this.getImaginaryPart(), 2) + Math.pow(this.getRealPart(), 2));
    }


    /**
     * @return a string representation of this number
     */
    @Override
    public String toString() {
        return (this.getImaginaryPart() >= 0) ? this.getRealPart() + " + " + this.getImaginaryPart() + "i" :
                this.getRealPart() + " - " + (-this.getImaginaryPart() + "i");
    }

}
