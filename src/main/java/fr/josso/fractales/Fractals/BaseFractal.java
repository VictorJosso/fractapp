package fr.josso.fractales.Fractals;

import fr.josso.fractales.Core.*;
import javafx.concurrent.Task;

import java.math.BigInteger;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

/**
 * The fractal type.
 */
public abstract class BaseFractal extends Task<ResultImg> {
    protected final UnaryOperator<Complex> f;
    final long maxIter;
    final BigInteger radius;
    private final ComplexPlane plane;
    private final float[][] results_matrix;

    private final boolean isui;

    private final Progress progress;

    /**
     * a Constructor.
     * @param f the function that will be used to construct the fractal.
     * @param maxIter the maximal number of times that the given function is iterated.
     * @param radius the radius (which define the divergence) of the fractal.
     * @param plane the portion of the plane in which the fractal is build.
     * @param isui true for a graphical version else false.
     */
    public BaseFractal(UnaryOperator<Complex> f, long maxIter, BigInteger radius, ComplexPlane plane, boolean isui) {
        this.f = f;
        this.maxIter = maxIter;
        this.radius = radius;
        this.plane = plane;
        this.results_matrix = new float[plane.getNbPointsX()][plane.getNbPointsY()];
        this.progress = new Progress(plane.getNbPointsX() * plane.getNbPointsY(), !isui);
        this.isui = isui;
    }

    /**
     * Calculate the divergence of a number.
     * @param z a Complex number (a point on the plane).
     * @return the divergence index of that number.
     */
    protected abstract int divergenceIndex(Complex z);

    /**
     * Save the fractal into a matrix.
     * @param x the coordinate on the X-axis.
     * @param y the coordinate on the Y-axis.
     * @param result the float associated to that point of the plane.
     */
    protected void saveResult(int x, int y, float result){
        this.results_matrix[x][y] = result;
    }

    /**
     * Draw the image.
     * @return the image corresponding to the fractal.
     */
    public ResultImg compute(){
        IntStream.range(0, this.plane.getNbPointsX()).parallel().forEach(
                x -> IntStream.range(0, this.plane.getNbPointsY()).parallel().forEach(
                        y -> {this.saveResult(x, y,
                                (float) this.divergenceIndex(
                                        new Complex((double) this.plane.getMinX() + x * this.plane.getStep(),
                                                (double) this.plane.getMinY() + y * this.plane.getStep()))
                                        / maxIter);
                            this.progress.operationDone();
                            if (this.isui){
                            this.updateProgress(this.progress.getDone(), this.progress.getTotalOperations());
                            this.updateMessage(String.valueOf(this.progress.getProgression()));}}
                )
        );
        return ResultImg.fromMatrix(this.results_matrix);
    }

    /**
     * Lunch the Task.
     * @return the result image.
     */
    @Override
    protected ResultImg call() {
        return this.compute();
    }

}
