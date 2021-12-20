package fr.josso.fractales;

import fr.josso.fractales.Core.Complex;
import fr.josso.fractales.Core.ComplexPlane;
import fr.josso.fractales.Core.ResultImg;
import fr.josso.fractales.Fractals.Julia;


public class Main {
    public static void main(String[] args) {
        ComplexPlane test = ComplexPlane.builder()
                .maxY(1)
                .maxX(2)
                .minX(-2)
                .minY(-1)
                .step(0.001)
                .build();
        //test.trace(z -> Complex.add(z.pow(2), new Complex(-0.7269, 0.1889)));
        //test.trace(z -> z)

        Julia juliaTest = new Julia(z -> Complex.add(z.pow(2), new Complex(-0.7269, 0.1889)), 1000, 500, test);
        ResultImg resultImg = juliaTest.compute();
        resultImg.endTask();
    }
}
