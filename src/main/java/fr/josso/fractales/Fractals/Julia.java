package fr.josso.fractales.Fractals;

import fr.josso.fractales.Core.Complex;
import fr.josso.fractales.Core.ComplexPlane;
import fr.josso.fractales.Core.Progress;
import fr.josso.fractales.Core.ResultImg;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

public class Julia extends Task<ResultImg>{
    private final UnaryOperator<Complex> f;
    private final int maxIter;
    private final int radius;
    private final ComplexPlane plane;
    private final ArrayList<ArrayList<Float>> results_matrix;

    private Progress progress;


    public Julia(UnaryOperator<Complex> f, int maxIter, int radius, ComplexPlane plane) {
        this.f = f;
        this.maxIter = maxIter;
        this.radius = radius;
        this.plane = plane;
        this.results_matrix = new ArrayList<>();
        for (int x = 0; x < this.plane.getNbPointsX(); x++){
            this.results_matrix.add(new ArrayList<>());
            for (int y = 0; y < this.plane.getNbPointsY(); y++){
                this.results_matrix.get(x).add(-1.f);
            }
        }
        this.progress = new Progress(plane.getNbPointsX() * plane.getNbPointsY());
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
        IntStream.range(0, this.plane.getNbPointsX()).parallel().forEach(
                x -> IntStream.range(0, this.plane.getNbPointsY()).parallel().forEach(
                        y -> {this.saveResult(x, y, (float) this.divergenceIndex(new Complex((double) this.plane.getMinX() + x * this.plane.getStep(), (double) this.plane.getMinY() + y * this.plane.getStep())) / maxIter);
                            this.progress.operationDone();
                            this.updateProgress(this.progress.getDone(), this.progress.getTotalOperations());
                            this.updateMessage(String.valueOf(this.progress.getProgression()));}
                )
        );

        System.out.println(this.progress.getDone() + " operations done over " + this.progress.getTotalOperations());
        for(int x = 0; x < this.plane.getNbPointsX(); x++){
            for (int y = 0; y < this.plane.getNbPointsY(); y++){
                if (this.results_matrix.get(x).get(y) == -1.f){
                    System.out.println("Point non traite : ("+x+", "+y+")");
                }
            }
        }

        return ResultImg.fromMatrix(this.results_matrix);
    }

    @Override
    protected ResultImg call() throws Exception {
        return this.compute();
    }
}
