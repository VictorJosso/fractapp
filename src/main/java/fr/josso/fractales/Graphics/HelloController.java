package fr.josso.fractales.Graphics;

import fr.josso.fractales.Core.*;
import fr.josso.fractales.Fractals.BaseFractal;
import fr.josso.fractales.Fractals.Julia;
import fr.josso.fractales.Fractals.Mandelbrot;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

/**
 * The type Hello controller.
 */
public class HelloController {

    /**
     * The enum Fractal.
     */
    enum FRACTAL{
        /**
         * Julia fractal.
         */
        JULIA,
        /**
         * Mandelbrot fractal.
         */
        MANDELBROT
    }
    @FXML
    private MenuButton choixMenuButton;

    @FXML
    private MenuItem menuItemJulia;

    @FXML
    private MenuItem menuItemMandelbrot;

    @FXML
    private Button buttonReset;

    @FXML
    private Label progressionLabel;

    @FXML
    private Button buttonGenerate;

    @FXML
    private TextField functionTextField;

    @FXML
    private TextField minXTextField;

    @FXML
    private TextField maxXTextField;

    @FXML
    private TextField minYTextField;

    @FXML
    private TextField maxYTextField;

    @FXML
    private TextField radiusTextField;

    @FXML
    private TextField maxIterationsTextField;

    @FXML
    private TextField stepTextField;

    @FXML
    private Label titleLabel;

    @FXML
    private ImageView resultImageView;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Pane containerPane;

    @FXML
    private Button saveButton;

    @FXML
    private Button helpFunctionButton;

    @FXML
    private Button invertColorsButton;

    private Stage mainStage;

    private FRACTAL currentFractal = FRACTAL.JULIA;
    private ResultImg resultImg;
    private File destinationFile;

    /**
     * Instantiates a new Hello controller.
     */
    public HelloController() {
    }

    /**
     * Initialisation of the fields.
     */
    @FXML
    private void initialize(){
        minXTextField.textProperty().addListener(new RegexValidator(Pattern.compile("(-*\\d*\\.*)+"), Pattern.compile("-?\\d+(\\.\\d+)?"), minXTextField));
        maxXTextField.textProperty().addListener(new RegexValidator(Pattern.compile("(-*\\d*\\.*)+"), Pattern.compile("-?\\d+(\\.\\d+)?"), maxXTextField));
        minYTextField.textProperty().addListener(new RegexValidator(Pattern.compile("(-*\\d*\\.*)+"), Pattern.compile("-?\\d+(\\.\\d+)?"), minYTextField));
        maxYTextField.textProperty().addListener(new RegexValidator(Pattern.compile("(-*\\d*\\.*)+"), Pattern.compile("-?\\d+(\\.\\d+)?"), maxYTextField));

        stepTextField.textProperty().addListener(new RegexValidator(Pattern.compile("(\\d*\\.*)+"), Pattern.compile("\\d+(\\.\\d+)?"), stepTextField));

        radiusTextField.textProperty().addListener(new RegexValidator(Pattern.compile("(\\d*\\.*)+"), Pattern.compile("\\d+(\\.\\d+)?"), radiusTextField));
        maxIterationsTextField.textProperty().addListener(new RegexValidator(Pattern.compile("(\\d*)+"), Pattern.compile("\\d+"), maxIterationsTextField));
    }

    /**
     * Sets main stage.
     *
     * @param mainStage the main stage
     */
    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    /**
     * Read the fields and interprets them.
     * @return a FractalParams which attributes are supplied by the fields.
     */
    private FractalParams getParams() {
        Parser parser = new Parser(functionTextField.getText());
        UnaryOperator<Complex> equation = parser.toFunction();

        float minX = Float.parseFloat(minXTextField.getText());
        float maxX = Float.parseFloat(maxXTextField.getText());
        float minY = Float.parseFloat(minYTextField.getText());
        float maxY = Float.parseFloat(maxYTextField.getText());
        double step = Double.parseDouble(stepTextField.getText());

        BigInteger radius = new BigInteger(radiusTextField.getText());
        long maxIter = Long.parseLong(maxIterationsTextField.getText());

        return new FractalParams(
                minX,
                maxX,
                minY,
                maxY,
                step,
                radius,
                maxIter,
                equation
        );
    }

    /**
     * Calculate the fractal and can display it.
     * @param baseFractal the wanted fractal.
     * @param shouldDisplayImage true to display image, else false.
     */
    private void startFractal(BaseFractal baseFractal, boolean shouldDisplayImage){
        this.progressBar.setDisable(false);
        this.saveButton.setDisable(true);
        this.buttonGenerate.setDisable(true);
        this.invertColorsButton.setDisable(true);
        this.progressBar.progressProperty().bind(baseFractal.progressProperty());
        new Thread(() -> {
            this.resultImg = baseFractal.compute();
            if (shouldDisplayImage) {
                Image image = SwingFXUtils.toFXImage(resultImg.getImage(), null);
                Platform.runLater(() -> {
                    ImageHelper.displayImage(image, resultImageView, containerPane);
                    this.progressBar.setDisable(true);
                    this.saveButton.setDisable(false);
                    this.invertColorsButton.setDisable(false);
                    this.buttonGenerate.setDisable(false);
                    this.progressBar.progressProperty().unbind();
                });
            } else {
                resultImg.endTask(this.destinationFile);
                Platform.runLater(() -> {
                    this.progressBar.setDisable(true);
                    this.buttonGenerate.setDisable(false);
                    this.progressBar.progressProperty().unbind();
                });
            }
        }).start();

    }

    /**
     * Allow to choose the saving slot for the ResultImg.
     * @return a FileChooser to save the fractal.
     */
    private FileChooser getFileChooser(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer la fractale");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setInitialFileName("MyFractal.png");

        return fileChooser;
    }

    /**
     * Save the displayed image.
     */
    @FXML
    private void saveFractal(){
        FileChooser fileChooser = this.getFileChooser();
        File file = fileChooser.showSaveDialog(this.mainStage);
        if (file != null){
            this.resultImg.endTask(file);
        }
    }

    /**
     * Display help for the equation input.
     */
    @FXML
    private void displayHelpEquation(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Format d'équations");
        alert.setContentText("""
                Entrez votre fonction comme suit :
                +-(cn)z^n +- ... +- (c1)z^1 +- (c) où les cn sont écrits sous la forme : a +- bi (les espaces après et avant le symbole d'opération sont importants)
                Attention à mettre exactement un espace après chaque exposant et pas avant.
                Exemple :  - (-1)z^2 - (42 - .25i)z^1 - (3.5 + 8i)z^7 - (3.14 + 1.2i)\s""");
        alert.showAndWait();
    }

    /**
     * Warn the user if the wanted fractal could be to big.
     * @param fractal the wanted fractal.
     */
    private void warnIfTooBig(BaseFractal fractal){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Attention, image trop grande");
        alert.setHeaderText("Votre fractale peut faire planter l'application");
        alert.setContentText("Les paramètres d'affichage donnent une image de plus de 100 millions de pixels. " +
                "Une image aussi grande risque de provoquer une erreur lors de son affichage car JavaFX doit charger chaque " +
                "pixel en mémoire. Il est recommandé d'enregistrer cette fractale sans l'afficher à l'écran.");

        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Enregistrer l'image");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Afficher l'image");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isEmpty()){
            this.warnIfTooBig(fractal);
        } else {
            this.destinationFile = this.getFileChooser().showSaveDialog(this.mainStage);
            this.startFractal(fractal, result.get() == ButtonType.CANCEL);
        }
    }

    /**
     * Generate a Julia set.
     */
    private void generateJulia(){
        FractalParams params = getParams();
        Julia julia = Julia.fromParams(params);

        if ((params.maxX() - params.minX()) * (1/params.step()) * (params.maxY() - params.minY()) * (1/params.step()) > 100000000){
            this.warnIfTooBig(julia);
        } else {
            this.startFractal(julia, true);
        }
    }

    /**
     * Allow the user to invert color on the image.
     */
    @FXML
    private void invertColors(){
        if (this.resultImg != null){
            new Thread(() -> {
                Platform.runLater(() -> {
                    this.invertColorsButton.setDisable(true);
                    this.saveButton.setDisable(true);
                    this.buttonGenerate.setDisable(true);
                    this.progressBar.setDisable(false);
                    this.progressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
                });
                resultImg.invertColors();
                Platform.runLater(() -> {
                        ImageHelper.displayImage(SwingFXUtils.toFXImage(this.resultImg.getImage(), null), resultImageView, containerPane);
                        this.invertColorsButton.setDisable(false);
                        this.saveButton.setDisable(false);
                        this.buttonGenerate.setDisable(false);
                        this.progressBar.setDisable(true);
                        this.progressBar.setProgress(100);
                });
            }).start();
        }
    }


    /**
     * Generate the Mandelbrot set.
     */
    private void generateMandelbrot() {
        FractalParams params = getParams();
        Mandelbrot mandelbrot = Mandelbrot.fromParams(params);

        if ((params.maxX() - params.minX()) * (1/params.step()) * (params.maxY() - params.minY()) * (1/params.step()) > 100000000){
            this.warnIfTooBig(mandelbrot);
        } else {
            this.startFractal(mandelbrot, true);
        }
    }

    /**
     * Lunch the calculation of the fractal.
     */
    @FXML
    private void onButtonGeneratePressed(){
        if (this.currentFractal == FRACTAL.JULIA){
            this.generateJulia();
        } else {
            if (this.currentFractal == FRACTAL.MANDELBROT){
                this.generateMandelbrot();
            }
        }
    }

    /**
     * Make the menu usable.
     */
    @FXML
    private void setJuliaFractal(){
        this.currentFractal = FRACTAL.JULIA;
        this.titleLabel.setText("Julia");
        this.functionTextField.setDisable(false);
        this.helpFunctionButton.setDisable(false);
    }

    /**
     * Make the menu usable.
     */
    @FXML
    private void setMandelbrotFractal(){
        this.currentFractal = FRACTAL.MANDELBROT;
        this.titleLabel.setText("Mandelbrot");
        this.functionTextField.setDisable(true);
        this.helpFunctionButton.setDisable(true);
    }
}