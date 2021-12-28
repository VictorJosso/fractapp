package fr.josso.fractales.CmdLine;

import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class CommandParser {

    private static void validate(CommandLine commandLine) throws ParseException {
        Pattern floatPattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        Pattern positiveFloatPattern = Pattern.compile("\\d+(\\.\\d+)?");
        Pattern positiveIntegerPattern = Pattern.compile("\\d+");
        List<String> floatArguments = List.of("minX", "maxX", "minY", "maxY");
        List<String> positiveFloatArguments = List.of("step", "radius");
        List<String> positiveIntegerArguments = List.of("maxIter");

        if (commandLine.hasOption("output")){
            File f = new File(commandLine.getOptionValue("output"));
            try {
                f.createNewFile();
                f.delete();
            } catch (IOException e) {
                throw new ParseException(commandLine.getOptionValue("output") + " is not a valid path");
            }
        }

        if (commandLine.hasOption("julia") && !commandLine.hasOption("fonction")){
            throw new ParseException("Définir la fonction est obligatoire avec l'option -j");
        }

        for (String elem: floatArguments){
            if (!floatPattern.matcher(commandLine.getOptionValue(elem, "1")).matches()){
                throw new ParseException(commandLine.getOptionValue(elem) + ": Invalid value for "+elem);
            }
        }

        for (String elem: positiveFloatArguments){
            if (!positiveFloatPattern.matcher(commandLine.getOptionValue(elem, "1")).matches()){
                throw new ParseException(commandLine.getOptionValue(elem) + ": Invalid value for "+elem);
            }
        }

        for (String elem: positiveIntegerArguments){
            if (!positiveIntegerPattern.matcher(commandLine.getOptionValue(elem, "1")).matches()){
                throw new ParseException(commandLine.getOptionValue(elem) + ": Invalid value for "+elem);
            }
        }
    }

    public static CommandLine parse(String[] argv) {
        Options options = new Options();
        Option juliaOption = Option.builder("j")
                                .argName("julia")
                                .longOpt("julia")
                                .desc("Genère une fractale de Julia")
                                .hasArg(false)
                                .build();
        Option mandelbrotOption = Option.builder("m")
                                .argName("mandelbrot")
                                .longOpt("mandelbrot")
                                .desc("Genère une fractale de Mandelbrot")
                                .hasArg(false)
                                .build();

        Option interactiveOption = Option.builder("i")
                .argName("interactif")
                .longOpt("interactif")
                .desc("Démarre l'interface graphique")
                .hasArg(false)
                .build();

        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        group.addOption(juliaOption);
        group.addOption(mandelbrotOption);
        group.addOption(interactiveOption);

        options.addOptionGroup(group);

        options.addOption(Option.builder("o")
                .longOpt("output")
                .desc("Indique dans quel fichier enregistrer la fractale générée. Default: MyFractal.png")
                .hasArg(true)
                .required(false)
                .build());

        options.addOption(Option.builder()
                .longOpt("minX")
                .desc("Donne la valeur minimale sur l'axe X. Defaut: -1")
                .hasArg(true)
                .required(false)
                .build());

        options.addOption(Option.builder()
                .longOpt("maxX")
                .desc("Donne la valeur maximale sur l'axe X. Defaut: 1")
                .hasArg(true)
                .required(false)
                .build());

        options.addOption(Option.builder()
                .longOpt("minY")
                .desc("Donne la valeur minimale sur l'axe Y. Defaut: -1")
                .hasArg(true)
                .required(false)
                .build());

        options.addOption(Option.builder()
                .longOpt("maxY")
                .desc("Donne la valeur maximale sur l'axe Y. Defaut: 1")
                .hasArg(true)
                .required(false)
                .build());

        options.addOption(Option.builder()
                .longOpt("step")
                .desc("Donne la valeur d'une division du plan complexe. Defaut: 0.001")
                .hasArg(true)
                .required(false)
                .build());

        options.addOption(Option.builder()
                .longOpt("radius")
                .desc("Donne la valeur à partir de laquelle une suite est condidérée comme divergeante. Defaut: 2")
                .hasArg(true)
                .required(false)
                .build());

        options.addOption(Option.builder()
                .longOpt("maxIter")
                .desc("Donne le nombre maximal d'itération avant de considérer que la suite converge. Defaut: 1000")
                .hasArg(true)
                .required(false)
                .build());

        options.addOption(Option.builder("f")
                .longOpt("fonction")
                .desc("Définit la fonction pour la fractale Julia. Requis pour Julia, ignoré pour Mandelbrot")
                .hasArg(true)
                .required(false)
                .build());




        String avant = "Génère des fractales avec les paramètres demandés.";
        String apres = "\nMerci de signaler toute erreur d'execution ici : https://bit.ly/3Ht0tEf";

        HelpFormatter formatter = new HelpFormatter();


        try {
            CommandLine commandLine = (new DefaultParser()).parse(options, argv);
            validate(commandLine);
            return commandLine;
        } catch (ParseException e){
            System.out.println("ERROR : "+e.getMessage());
            formatter.printHelp("fractapp", avant, options, apres, true);
            return null;
        }
    }
}
