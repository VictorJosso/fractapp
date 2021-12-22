package fr.josso.fractales.Core;

import java.util.HashMap;
import java.util.function.UnaryOperator;

public class Parser {

    private String input;

    public Parser(String input) {
        this.input = input.trim();
    }

    private char[] formate(char[] init, int length) {
        char[] res = new char [length];
        System.arraycopy(init, 0, res, 0, length);
        return res;
    }

    private HashMap<Integer, String> parsePow(){
        HashMap<Integer, String> res = new HashMap<>();
        int i = 0;
        int k = 0;
        char[] temp = new char[32];

        while (i < input.length()) {


            if (input.charAt(i) == 'z') {
                char[] exponentTemp = new char[16];
                i += 2;
                int j = 0;
                while(' ' != input.charAt(i)) {
                    exponentTemp[j] = input.charAt(i);
                    i++;
                    j++;
                }

                res.put(Integer.parseInt(new String(formate(exponentTemp, j))), new String(formate(temp, k)));
                k = 0;
                temp = new char[32];
            } else {
                temp[k] = input.charAt(i);
                k++;
            }
            i++;
        }

        res.put(0, new String(formate(temp, k)));
        return res;
    }


    public Complex readCoefficent(String coef){
        int signe = 1;
        String[] parsed;
        if (coef.charAt(0) == '-') {
            signe = -1;
        }

        if (coef.charAt(0) != '+' && coef.charAt(0) != '-') {
            parsed = coef.replaceAll("[()]", "").split(" [+-] ");
        } else {
            parsed = coef.substring(2).replaceAll("[()]", "").split(" [+-] ");
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

    public UnaryOperator<Complex> toFunction(){
        HashMap<Integer, String> steps = this.parsePow();
        return complex -> {
            Complex res = new Complex(0, 0);
            for (Integer key : steps.keySet()) {
                res = Complex.add(res, Complex.multiply(complex.pow(key), readCoefficent(steps.get(key))));
            }
            return res;
        };
    }

}