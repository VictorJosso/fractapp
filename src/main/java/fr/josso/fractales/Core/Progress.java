package fr.josso.fractales.Core;

import me.tongfei.progressbar.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class Progress extends ProgressBar{
    private final double totalOperations;
    private int done;

    /**
     * @param totalOperations how many operations are needed.
     * @param displayProgress true to show the progress bar else otherwise.
     */
    public Progress(double totalOperations, boolean displayProgress) {
        super("Generation de la fractale", (long) totalOperations, 200, (!displayProgress ? null : System.err), ProgressBarStyle.COLORFUL_UNICODE_BLOCK, "", 1, !displayProgress, null, ChronoUnit.SECONDS, 0L, Duration.ZERO);
        this.totalOperations = totalOperations;
        this.done = 0;
    }


    /**
     * @return the actual progression.
     */
    public double getProgression(){
        return done/totalOperations;
    }

    /**
     * Increment the counter.
     */
    public synchronized void operationDone(){
        this.done++;
        this.step();
    }

    /**
     * @return how many operations are done.
     */
    public int getDone() {
        return done;
    }

    /**
     * @return the total number of needed operations.
     */
    public double getTotalOperations() {
        return totalOperations;
    }
}
