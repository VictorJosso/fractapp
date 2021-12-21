package fr.josso.fractales.Graphics;

import fr.josso.fractales.Core.Complex;
import fr.josso.fractales.Core.ComplexPlane;
import fr.josso.fractales.Core.ResultImg;
import fr.josso.fractales.Fractals.Julia;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;

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



    private FRACTAL currentFractal = FRACTAL.JULIA;


    public HelloController() {
    }

    private void generateJulia(){
        float minX = Float.parseFloat(minXTextField.getText());
        float maxX = Float.parseFloat(maxXTextField.getText());
        float minY = Float.parseFloat(minYTextField.getText());
        float maxY = Float.parseFloat(maxYTextField.getText());
        double step = Double.parseDouble(stepTextField.getText());

        int radius = Integer.parseInt(radiusTextField.getText());
        int maxIter = Integer.parseInt(maxIterationsTextField.getText());

        ComplexPlane plane = ComplexPlane.builder()
                .minX(minX)
                .maxX(maxX)
                .minY(minY)
                .maxY(maxY)
                .step(step)
                .build();
        Julia julia = new Julia(z -> Complex.add(z.pow(2), new Complex(-0.7269, 0.1889)), maxIter, radius, plane);

        new Thread(() -> {
            ResultImg resultImg = julia.compute();

            Image image = SwingFXUtils.toFXImage(resultImg.getImage(), null);
            resultImg.endTask();
            Platform.runLater(() -> resultImageView.setImage(image));
        }).start();



    }

    @FXML
    private void onButtonGeneratePressed(){
        if (this.currentFractal == FRACTAL.JULIA){
            this.generateJulia();
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