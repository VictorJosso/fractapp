package fr.josso.fractales;

import fr.josso.fractales.Core.ComplexPlane;
import fr.josso.fractales.Core.Parser;
import fr.josso.fractales.Core.ResultImg;
import fr.josso.fractales.Fractals.BaseFractal;
import fr.josso.fractales.Fractals.Julia;
import fr.josso.fractales.Fractals.Mandelbrot;
import fr.josso.fractales.Graphics.HelloApplication;
import fr.josso.fractales.CmdLine.CommandParser;
import javafx.application.Application;
import org.apache.commons.cli.CommandLine;

import java.io.File;
import java.math.BigInteger;

/**
 * The type Main.
 */
public class Main {
    /**
     * the Main method.
     *
     * @param args commandLine arguments.
     */
    public static void main(String[] args) {
        CommandLine commandLine = CommandParser.parse(args);

        if (commandLine == null) return;
        ComplexPlane plane = ComplexPlane.builder()
                .minX(Float.parseFloat(commandLine.getOptionValue("minX", "-1")))
                .maxX(Float.parseFloat(commandLine.getOptionValue("maxX", "1")))
                .minY(Float.parseFloat(commandLine.getOptionValue("minY", "-1")))
                .maxY(Float.parseFloat(commandLine.getOptionValue("maxY", "1")))
                .step(Double.parseDouble(commandLine.getOptionValue("step", "0.001")))
                .build();

        long maxIter = Long.parseLong(commandLine.getOptionValue("maxIter", "1000"));
        BigInteger radius = new BigInteger(commandLine.getOptionValue("radius", "2"));


        if (commandLine.hasOption("interactif")){
            Application.launch(HelloApplication.class);
        } else {
            BaseFractal fractale;
            if (commandLine.hasOption("julia")) {
                fractale = new Julia((new Parser(commandLine.getOptionValue("fonction"))).toFunction(), maxIter, radius, plane, false);
            } else {
                fractale = new Mandelbrot(z -> z, maxIter, radius, plane, false);
            }

            ResultImg image = fractale.compute();
            if (commandLine.hasOption("output")){
                image.endTask(new File(commandLine.getOptionValue("output")));
            } else {
                image.endTask();
            }
        }
    }
}
