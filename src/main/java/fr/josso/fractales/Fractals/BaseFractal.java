package fr.josso.fractales.Fractals;

import fr.josso.fractales.Core.*;
import javafx.concurrent.Task;

import java.math.BigInteger;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

public abstract class BaseFractal extends Task<ResultImg> {
    protected final UnaryOperator<Complex> f;
    final long maxIter;
    final BigInteger radius;
    private final ComplexPlane plane;
    //private final ArrayList<ArrayList<Float>> results_matrix;
    private float[][] results_matrix;

    private Progress progress;

    public BaseFractal(UnaryOperator<Complex> f, long maxIter, BigInteger radius, ComplexPlane plane) {
        this.f = f;
        this.maxIter = maxIter;
        this.radius = radius;
        this.plane = plane;
        this.results_matrix = new float[plane.getNbPointsX()][plane.getNbPointsY()];
        this.progress = new Progress(plane.getNbPointsX() * plane.getNbPointsY());
    }

    protected abstract int divergenceIndex(Complex z);

    public static BaseFractal fromParams(FractalParams params) {
        return null;
    }

    protected void saveResult(int x, int y, float result){
        this.results_matrix[x][y] = result;
    }

    public ResultImg compute(){
        int scale = (int) Math.pow(10, Double.toString(this.plane.getStep()).length() - 2);
        IntStream.range(0, this.plane.getNbPointsX()).parallel().forEach(
                x -> IntStream.range(0, this.plane.getNbPointsY()).parallel().forEach(
                        y -> {this.saveResult(x, y, (float) this.divergenceIndex(new Complex((double) this.plane.getMinX() + x * this.plane.getStep(), (double) this.plane.getMinY() + y * this.plane.getStep())) / maxIter);
                            this.progress.operationDone();
                            this.updateProgress(this.progress.getDone(), this.progress.getTotalOperations());
                            this.updateMessage(String.valueOf(this.progress.getProgression()));}
                )
        );

        System.out.println(this.progress.getDone() + " operations done over " + this.progress.getTotalOperations());

        return ResultImg.fromMatrix(this.results_matrix);
    }

    @Override
    protected ResultImg call() throws Exception {
        return this.compute();
    }

}
