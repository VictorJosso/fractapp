package fr.josso.fractales.Core;

import me.tongfei.progressbar.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class Progress extends ProgressBar{
    private final double totalOperations;
    private int done;

    public Progress(double totalOperations, boolean displayProgress) {
        super("Generation de la fractale", (long) totalOperations, 200, (!displayProgress ? null : System.err), ProgressBarStyle.COLORFUL_UNICODE_BLOCK, "", 1, !displayProgress, null, ChronoUnit.SECONDS, 0L, Duration.ZERO);
        this.totalOperations = totalOperations;
        this.done = 0;
    }


    public double getProgression(){
        return done/totalOperations;
    }

    public synchronized void operationDone(){
        this.done++;
        this.step();
    }

    public int getDone() {
        return done;
    }

    public double getTotalOperations() {
        return totalOperations;
    }
}
