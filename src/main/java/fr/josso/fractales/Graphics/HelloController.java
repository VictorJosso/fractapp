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

public class HelloController {
    enum FRACTAL{
        JULIA,
        MANDELBROT;
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
    private Label progressionLabel;

    @FXML
    private Pane containerPane;

    @FXML
    private Button saveButton;

    private Stage mainStage;

    private FRACTAL currentFractal = FRACTAL.JULIA;
    private ResultImg resultImg;
    private File destinationFile;

    public HelloController() {
    }

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

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

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

    private void startFractal(BaseFractal baseFractal, boolean shouldDisplayImage){
        this.progressBar.setDisable(false);
        this.saveButton.setDisable(true);
        this.progressBar.progressProperty().bind(baseFractal.progressProperty());
        new Thread(() -> {
            this.resultImg = baseFractal.compute();
            if (shouldDisplayImage) {
                Image image = SwingFXUtils.toFXImage(resultImg.getImage(), null);
                Platform.runLater(() -> {
                    ImageHelper.displayImage(image, resultImageView, containerPane);
                    this.progressBar.setDisable(true);
                    this.saveButton.setDisable(false);
                    this.progressBar.progressProperty().unbind();
                });
            } else {
                resultImg.endTask(this.destinationFile);
                Platform.runLater(() -> {
                    this.progressBar.setDisable(true);
                    this.progressBar.progressProperty().unbind();
                });
            }
        }).start();

    }

    private FileChooser getFileChooser(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer la fractale");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setInitialFileName("MyFractal.png");

        return fileChooser;
    }

    @FXML
    private void saveFractal(){
        FileChooser fileChooser = this.getFileChooser();
        File file = fileChooser.showSaveDialog(this.mainStage);
        if (file != null){
            this.resultImg.endTask(file);
        }
    }

    private void warnIfTooBig(BaseFractal fractale){
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
            this.warnIfTooBig(fractale);
        } else {
            this.destinationFile = this.getFileChooser().showSaveDialog(this.mainStage);
            this.startFractal(fractale, result.get() == ButtonType.CANCEL);
        }
    }

    private void generateJulia(){

        FractalParams params = getParams();
        Julia julia = Julia.fromParams(params);

        if ((params.maxX() - params.minX()) * (1/params.step()) * (params.maxY() - params.minY()) * (1/params.step()) > 100000000){
            this.warnIfTooBig(julia);
        } else {
            this.startFractal(julia, true);
        }
    }


    private void generateMandelbrot() {
        FractalParams params = getParams();
        Mandelbrot mandelbrot = Mandelbrot.fromParams(params);

        if ((params.maxX() - params.minX()) * (1/params.step()) * (params.maxY() - params.minY()) * (1/params.step()) > 100000000){
            this.warnIfTooBig(mandelbrot);
        } else {
            this.startFractal(mandelbrot, true);
        }
    }

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

    @FXML
    private void setJuliaFractal(){
        this.currentFractal = FRACTAL.JULIA;
        this.titleLabel.setText("Julia");
        this.functionTextField.setDisable(false);
    }

    @FXML
    private void setMandelBrotFractal(){
        this.currentFractal = FRACTAL.MANDELBROT;
        this.titleLabel.setText("Mandelbrot");
        this.functionTextField.setDisable(true);
    }
}