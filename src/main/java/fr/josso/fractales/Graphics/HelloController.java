package fr.josso.fractales.Graphics;

import fr.josso.fractales.Core.Complex;
import fr.josso.fractales.Core.ComplexPlane;
import fr.josso.fractales.Core.ResultImg;
import fr.josso.fractales.Fractals.Julia;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.math.BigInteger;

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
    private static final int MIN_PIXELS = 100;

    public HelloController() {
    }

    private void generateJulia(){
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
        Julia julia = new Julia(z -> Complex.add(z.pow(2), new Complex(0.285, 0.01)), maxIter, radius, plane);


        this.progressBar.setDisable(false);
        this.progressBar.progressProperty().bind(julia.progressProperty());
        new Thread(() -> {
            ResultImg resultImg = julia.compute();
            Image image = SwingFXUtils.toFXImage(resultImg.getImage(), null);
            Platform.runLater(() -> {this.displayImage(image);
                this.progressBar.setDisable(true);
                this.progressBar.progressProperty().unbind();});
        }).start();
    }

    private void displayImage(Image image){
        double width = image.getWidth();
        double height = image.getHeight();

        resultImageView.setImage(image);
        resultImageView.setPreserveRatio(true);
        reset(resultImageView, width, height);


        ObjectProperty<Point2D> click = new SimpleObjectProperty<>();

        resultImageView.setOnMouseDragged(e -> {
            Point2D dragPoint = imageViewToImage(resultImageView, new Point2D(e.getX(), e.getY()));
            shift(resultImageView, dragPoint.subtract(click.get()));
            click.set(imageViewToImage(resultImageView, new Point2D(e.getX(), e.getY())));
        });

        resultImageView.setOnMousePressed(e -> {
            Point2D mousePress = imageViewToImage(resultImageView, new Point2D(e.getX(), e.getY()));
            click.set(mousePress);
        });

        resultImageView.setOnScroll(e -> {
            double delta = e.getDeltaY();
            Rectangle2D viewport = resultImageView.getViewport();

            double scale = clamp(Math.pow(1.01, delta),

                    // don't scale so we're zoomed in to fewer than MIN_PIXELS in any direction:
                    Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),

                    // don't scale so that we're bigger than image dimensions:
                    Math.max(width / viewport.getWidth(), height / viewport.getHeight())

            );

            Point2D mouse = imageViewToImage(resultImageView, new Point2D(e.getX(), e.getY()));

            double newWidth = viewport.getWidth() * scale;
            double newHeight = viewport.getHeight() * scale;

            double newMinX = clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale,
                    0, width - newWidth);
            double newMinY = clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale,
                    0, height - newHeight);

            resultImageView.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
        });

        resultImageView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                reset(resultImageView, width, height);
            }
        });

        resultImageView.fitWidthProperty().bind(containerPane.widthProperty());
        resultImageView.fitHeightProperty().bind(containerPane.heightProperty());

    }

    private Point2D imageViewToImage(ImageView imageView, Point2D coordinates) {
        double xProportion = coordinates.getX() / imageView.getBoundsInLocal().getWidth();
        double yProportion = coordinates.getY() / imageView.getBoundsInLocal().getHeight();

        Rectangle2D viewport = imageView.getViewport();
        return new Point2D(
                viewport.getMinX() + xProportion * viewport.getWidth(),
                viewport.getMinY() + yProportion * viewport.getHeight());
    }

    private void shift(ImageView imageView, Point2D delta) {
        Rectangle2D viewport = imageView.getViewport();

        double width = imageView.getImage().getWidth() ;
        double height = imageView.getImage().getHeight() ;

        double maxX = width - viewport.getWidth();
        double maxY = height - viewport.getHeight();

        double minX = clamp(viewport.getMinX() - delta.getX(), 0, maxX);
        double minY = clamp(viewport.getMinY() - delta.getY(), 0, maxY);

        imageView.setViewport(new Rectangle2D(minX, minY, viewport.getWidth(), viewport.getHeight()));
    }

    private double clamp(double value, double min, double max) {

        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }



    private void reset(ImageView imageView, double width, double height) {
        imageView.setViewport(new Rectangle2D(0, 0, width, height));
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