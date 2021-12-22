package fr.josso.fractales.Core;

public class Progress {
    private final double totalOperations;
    private int done;

    public Progress(double totalOperations) {
        this.totalOperations = totalOperations;
        this.done = 0;
    }

    public double getProgression(){
        return done/totalOperations;
    }

    public synchronized void operationDone(){
        this.done++;
    }

    public int getDone() {
        return done;
    }

    public double getTotalOperations() {
        return totalOperations;
    }
}
