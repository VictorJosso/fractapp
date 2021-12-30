package fr.josso.fractales.Core;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * The type Image helper.
 */
public record ImageHelper(ImageView resultImageView, Pane containerPane) {
    private static final int MIN_PIXELS = 100;

    /**
     * Display image.
     *
     * @param image         the image
     * @param imageView     the image view
     * @param containerPane the container pane
     */
    public static void displayImage(Image image, ImageView imageView, Pane containerPane) {
        ImageHelper helper = new ImageHelper(imageView, containerPane);
        helper.process(image);
    }

    /**
     * Handle the zoom.
     * @param image the actual image
     */
    private void process(Image image) {
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

    /**
     * Make an image from a view.
     * @param imageView initial view.
     * @param coordinates origin of the image.
     * @return origin of an image.
     */
    private Point2D imageViewToImage(ImageView imageView, Point2D coordinates) {
        double xProportion = coordinates.getX() / imageView.getBoundsInLocal().getWidth();
        double yProportion = coordinates.getY() / imageView.getBoundsInLocal().getHeight();

        Rectangle2D viewport = imageView.getViewport();
        return new Point2D(
                viewport.getMinX() + xProportion * viewport.getWidth(),
                viewport.getMinY() + yProportion * viewport.getHeight());
    }

    /**
     * Shift the view.
     * @param imageView initial view.
     * @param delta shift size.
     */
    private void shift(ImageView imageView, Point2D delta) {
        Rectangle2D viewport = imageView.getViewport();

        double width = imageView.getImage().getWidth();
        double height = imageView.getImage().getHeight();

        double maxX = width - viewport.getWidth();
        double maxY = height - viewport.getHeight();

        double minX = clamp(viewport.getMinX() - delta.getX(), 0, maxX);
        double minY = clamp(viewport.getMinY() - delta.getY(), 0, maxY);

        imageView.setViewport(new Rectangle2D(minX, minY, viewport.getWidth(), viewport.getHeight()));
    }

    /**
     * Limit a value in a min-max range.
     * @param value the value to limit.
     * @param min minimal value
     * @param max maximal value
     * @return value limited by min and max
     */
    private double clamp(double value, double min, double max) {
        return Math.max(Math.min(value, max), min);
    }


    /**
     * Reset the view.
     * @param imageView initial view.
     * @param width width of the original view.
     * @param height height of the original view.
     */
    private void reset(ImageView imageView, double width, double height) {
        imageView.setViewport(new Rectangle2D(0, 0, width, height));
    }

}
