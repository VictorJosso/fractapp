package fr.josso.fractales.Graphics;

import fr.josso.fractales.Core.*;
import fr.josso.fractales.Fractals.Julia;
import fr.josso.fractales.Fractals.Mandelbrot;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.math.BigInteger;
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

    private FRACTAL currentFractal = FRACTAL.JULIA;

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



    private void generateJulia(){

        Parser parser = new Parser(functionTextField.getText());
        UnaryOperator<Complex> equation = parser.toFunction();

        float minX = Float.parseFloat(minXTextField.getText());
        float maxX = Float.parseFloat(maxXTextField.getText());
        float minY = Float.parseFloat(minYTextField.getText());
        float maxY = Float.parseFloat(maxYTextField.getText());
        double step = Double.parseDouble(stepTextField.getText());

        BigInteger radius = new BigInteger(radiusTextField.getText());
        long maxIter = Long.parseLong(maxIterationsTextField.getText());

        ComplexPlane plane = ComplexPlane.builder()
                .minX(minX)
                .maxX(maxX)
                .minY(minY)
                .maxY(maxY)
                .step(step)
                .build();
        Julia julia = new Julia(equation, maxIter, radius, plane);


        this.progressBar.setDisable(false);
        this.progressBar.progressProperty().bind(julia.progressProperty());
        new Thread(() -> {
            ResultImg resultImg = julia.compute();
            Image image = SwingFXUtils.toFXImage(resultImg.getImage(), null);
            Platform.runLater(() -> {
                ImageHelper.displayImage(image, resultImageView, containerPane);
                this.progressBar.setDisable(true);
                this.progressBar.progressProperty().unbind();});
        }).start();
    }


    private void generateMandelbrot() {

        float minX = Float.parseFloat(minXTextField.getText());
        float maxX = Float.parseFloat(maxXTextField.getText());
        float minY = Float.parseFloat(minYTextField.getText());
        float maxY = Float.parseFloat(maxYTextField.getText());
        double step = Double.parseDouble(stepTextField.getText());

        BigInteger radius = new BigInteger(radiusTextField.getText());
        long maxIter = Long.parseLong(maxIterationsTextField.getText());

        ComplexPlane plane = ComplexPlane.builder()
                .minX(minX)
                .maxX(maxX)
                .minY(minY)
                .maxY(maxY)
                .step(step)
                .build();
        Mandelbrot mandelbrot = new Mandelbrot(z->z, maxIter, radius, plane);

        this.progressBar.setDisable(false);
        this.progressBar.progressProperty().bind(mandelbrot.progressProperty());
        new Thread(() -> {
            ResultImg resultImg = mandelbrot.compute();
            Image image = SwingFXUtils.toFXImage(resultImg.getImage(), null);
            Platform.runLater(() -> {
                ImageHelper.displayImage(image, resultImageView, containerPane);
                this.progressBar.setDisable(true);
                this.progressBar.progressProperty().unbind();});
        }).start();

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
    }

    @FXML
    private void setMandelBrotFractal(){
        this.currentFractal = FRACTAL.MANDELBROT;
        this.titleLabel.setText("Mandelbrot");
    }
}