package fr.josso.fractales.Fractals;

import fr.josso.fractales.Core.Complex;
import fr.josso.fractales.Core.ComplexPlane;
import fr.josso.fractales.Core.ResultImg;

import java.util.ArrayList;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

public class Julia {
    private final UnaryOperator<Complex> f;
    private final int maxIter;
    private final int radius;
    private final ComplexPlane plane;
    private final ArrayList<ArrayList<Float>> results_matrix;


    public Julia(UnaryOperator<Complex> f, int maxIter, int radius, ComplexPlane plane) {
        this.f = f;
        this.maxIter = maxIter;
        this.radius = radius;
        this.plane = plane;
        this.results_matrix = new ArrayList<>();
        for (int x = 0; x < this.plane.getNbPointsX(); x++){
            this.results_matrix.add(new ArrayList<>());
            for (int y = 0; y < this.plane.getNbPointsY(); y++){
                this.results_matrix.get(x).add(0.f);
            }
        }
    }

    private int divergenceIndex(Complex z){
        int iterations = 0;
        Complex zn = z;
        while (iterations < this.maxIter-1 && zn.modulus() < this.radius){
            zn = f.apply(zn);
            iterations++;
        }
        return iterations;
    }

    private void saveResult(int x, int y, float result){
        this.results_matrix.get(x).set(y, result);
    }

    public ResultImg compute(){
        int scale = (int) (1/this.plane.getStep());
        IntStream.range(0, this.plane.getNbPointsX()).parallel().forEach(
                x -> IntStream.range(0, this.plane.getNbPointsY()).parallel().forEach(
                        y -> this.saveResult(x, y, (float) this.divergenceIndex(new Complex((double) this.plane.getMinX() + x * this.plane.getStep(), (double) this.plane.getMinY() + y * this.plane.getStep())) / maxIter)
                )
        );

        return ResultImg.fromMatrix(this.results_matrix);
        //this.plane.getPoints().parallelStream().forEach(
        //        line -> line.parallelStream().forEach(
        //                z ->
        //        )
        //);
    }
}
