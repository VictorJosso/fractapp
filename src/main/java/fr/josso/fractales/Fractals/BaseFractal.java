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
    private final float[][] results_matrix;

    private final boolean isui;

    private final Progress progress;

    public BaseFractal(UnaryOperator<Complex> f, long maxIter, BigInteger radius, ComplexPlane plane, boolean isui) {
        this.f = f;
        this.maxIter = maxIter;
        this.radius = radius;
        this.plane = plane;
        this.results_matrix = new float[plane.getNbPointsX()][plane.getNbPointsY()];
        this.progress = new Progress(plane.getNbPointsX() * plane.getNbPointsY(), !isui);
        this.isui = isui;
    }

    protected abstract int divergenceIndex(Complex z);

    protected void saveResult(int x, int y, float result){
        this.results_matrix[x][y] = result;
    }

    public ResultImg compute(){
        IntStream.range(0, this.plane.getNbPointsX()).parallel().forEach(
                x -> IntStream.range(0, this.plane.getNbPointsY()).parallel().forEach(
                        y -> {this.saveResult(x, y, (float) this.divergenceIndex(new Complex((double) this.plane.getMinX() + x * this.plane.getStep(), (double) this.plane.getMinY() + y * this.plane.getStep())) / maxIter);
                            this.progress.operationDone();
                            if (this.isui){
                            this.updateProgress(this.progress.getDone(), this.progress.getTotalOperations());
                            this.updateMessage(String.valueOf(this.progress.getProgression()));}}
                )
        );
        return ResultImg.fromMatrix(this.results_matrix);
    }

    @Override
    protected ResultImg call() {
        return this.compute();
    }

}
