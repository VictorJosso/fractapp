package fr.josso.fractales.Core;

import java.util.HashMap;
import java.util.Set;
import java.util.function.UnaryOperator;

public class Parser {

    private final String input;
    private HashMap<Integer, Complex> coefficients;

    /**
     * @param input the function to parse.
     */
    public Parser(String input) {
        this.input = input.trim();
        this.setHashMap();
    }

    /**
     * @param init initial char[].
     * @param length number of char to copy.
     * @return a copy of the first length char of init.
     */
    private char[] formate(char[] init, int length) {
        char[] res = new char [length];
        System.arraycopy(init, 0, res, 0, length);
        return res;
    }

    /**
     * @return an HashMap in which the keys are the power and the values are the coefficients of the polynomial.
     */
    private HashMap<Integer, String> parsePow(){
        HashMap<Integer, String> res = new HashMap<>();
        int i = 0;
        int k = 0;
        char[] temp = new char[64];

        while (i < input.length()) {
            if (input.charAt(i) == 'z') {
                char[] exponentTemp = new char[16];
                i += 2;
                int j = 0;
                while(i < input.length() && isDigit(input.charAt(i))) {
                    exponentTemp[j] = input.charAt(i);
                    i++;
                    j++;
                }
                res.put(Integer.parseInt(new String(formate(exponentTemp, j))), new String(formate(temp, k)));
                k = 0;
                temp = new char[64];
            } else {
                temp[k] = input.charAt(i);
                k++;
            }
            i++;
        }

        res.put(0, new String(formate(temp, k)));
        return res;
    }


    /**
     * @param coef a coefficient of the polynomial to parse.
     * @return a Complex representation of coef.
     */
    public Complex readCoefficent(String coef){


        if (coef.isEmpty()) return new Complex(1,0);

        int signe = 1;
        String[] parsed;
        if (coef.charAt(0) == '-') {
            signe = -1;
        }

        if (coef.charAt(0) != '+' && coef.charAt(0) != '-') {
            parsed = coef
                    .replaceAll("[-]", "-")
                    .replaceAll("[()]", "")
                    .split(" [+-] ");
        } else {
            parsed = coef
                    .substring(2)
                    .replaceAll("[-]", "-")
                    .replaceAll("[()]", "")
                    .split(" [+-] ");
        }

        if(parsed.length < 2) {
            if (parsed[0].isEmpty()) {
                return new Complex(signe, 0);
            }
            Complex res;
            if (parsed[0].contains("i")) {
                String imag = parsed[0].replaceAll("i","");
                if (imag.isEmpty()) {
                    res = new Complex(0, 1);
                } else {
                    res = new Complex(0, Double.parseDouble(parsed[0].replaceAll("i", "")));
                }
            } else {
                res = new Complex(Double.parseDouble(parsed[0]),0);
            }
            return Complex.multiply(res, new Complex(signe, 0));
        }
        Complex res = new Complex(Double.parseDouble(parsed[0]),Double.parseDouble(parsed[1].replaceAll("i", "")));
        return Complex.multiply(res, new Complex(signe, 0));
    }

    /**
     * Parse the polynomial and save the result in the coefficients attribute.
     */
    private void setHashMap() {
        HashMap<Integer, Complex> res = new HashMap<>();
        HashMap<Integer, String> steps = this.parsePow();
        Set<Integer> keys = steps.keySet();
        for (Integer key : keys) {
            res.put(key, readCoefficent(steps.get(key)));
        }
        this.coefficients = res;
    }

    /**
     * @param n power of the Complex.
     * @param func the function already build by recursive usage.
     * @return a function that adds func with z to the power n with a coefficient.
     */
    private UnaryOperator<Complex> buildFunctionForExposant(int n, UnaryOperator<Complex> func){
        Complex coeff = coefficients.get(n);
        return z -> Complex.add(func.apply(z), Complex.multiply(z.pow(n), coeff));
    }

    /**
     * @return build the UnaryOperator that match the given polynomial to parse.
     */
    public UnaryOperator<Complex> toFunction(){

        UnaryOperator<Complex> result = z -> new Complex(0, 0);
        for(Integer key : this.coefficients.keySet()){
            result = this.buildFunctionForExposant(key, result);
        }
        return result;
    }

    /**
     * @param c a char.
     * @return true if c is a digit else false.
     */
    private boolean isDigit(char c) {
        return c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c =='9';
    }
}