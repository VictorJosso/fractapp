package fr.josso.fractales.Core;

public class Main {

    public static void main(String[] args) {

        ComplexPlane test = ComplexPlane.builder()
                .maxY(2)
                .maxX(2)
                .minX(-2)
                .minY(-2)
                .step(0.001)
                .build();
        test.trace(z -> Complex.add(z.pow(2), new Complex(-0.7269, 0.1889)));
        //test.trace(z -> z)
    }
}