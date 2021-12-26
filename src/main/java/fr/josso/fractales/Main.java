package fr.josso.fractales;

import fr.josso.fractales.Core.Complex;
import fr.josso.fractales.Core.ComplexPlane;
import fr.josso.fractales.Core.ResultImg;
import fr.josso.fractales.Fractals.Julia;
import fr.josso.fractales.Fractals.Mandelbrot;
import fr.josso.fractales.Graphics.HelloApplication;
import javafx.application.Application;
import javafx.application.Platform;

import java.math.BigInteger;


public class Main {
    public static void main(String[] args) {
        //Platform.startup(() -> {});
        ComplexPlane test = ComplexPlane.builder()
                .maxY(1)
                .maxX(0.5F)
                .minX(-2.5F)
                .minY(-1)
                .step(0.00005)
                .build();
        //test.trace(z -> Complex.add(z.pow(2), new Complex(-0.7269, 0.1889)));
        //test.trace(z -> z)

        //Julia juliaTest = new Julia(z -> Complex.add(z.pow(2), new Complex(-0.7269, 0.1889)), 1000, 500, test);
        //ResultImg resultImg = juliaTest.compute();
        //resultImg.endTask();

        Mandelbrot mandelbrot = new Mandelbrot(z->z, 1000, BigInteger.valueOf(2), test, false);
        ResultImg resultImg = mandelbrot.compute();
        resultImg.endTask();

        //Application.launch(HelloApplication.class);
    }
}
