package fr.josso.fractales.cmdLine;

import fr.josso.fractales.Core.ComplexPlane;
import fr.josso.fractales.Core.Parser;
import fr.josso.fractales.Fractals.Julia;
import fr.josso.fractales.Fractals.Mandelbrot;

import java.math.BigInteger;

public class cmdLineController {

    private String[] options;

    public cmdLineController(String[] options) {
        this.options = options;
    }

    public void exec(){

        ComplexPlane plane = ComplexPlane.builder()
                .minX(Float.parseFloat(this.options[2]))
                .maxX(Float.parseFloat(this.options[3]))
                .minY(Float.parseFloat(this.options[4]))
                .maxY(Float.parseFloat(this.options[5]))
                .step(Double.parseDouble(this.options[6]))
                .build();


        long maxIter = Long.parseLong(this.options[7]);
        BigInteger radius = new BigInteger(this.options[8]);


        if (this.options[1].equals("-m")) {
            new Mandelbrot(z->z, maxIter, radius, plane, false).compute().endTask();
        }
        if (this.options[1].equals("-j")) {
            new Julia(new Parser(readPoly()).toFunction(), maxIter, radius, plane, false).compute().endTask();
        }
    }

    private String readPoly(){
        String res = new String();
        for (int i = 9; i < this.options.length; i++) {
            res += this.options[i] + " ";
        }
        return res;
    }
}
