package fr.josso.fractales.Core;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * A visual representation of a fractal.
 */
public class ResultImg {

    BufferedImage img;
    File f = new File("MyFractal.png");


    /**
     * a Constructor.
     * @param width width of the new ResultImg.
     * @param height height of the new ResultImg.
     */
    public ResultImg(int width, int height){
        this.img = BigBufferedImage.create(width, height, BufferedImage.TYPE_INT_RGB);
    }

    /**
     * Set the color of a pixel.
     * @param x position on the X-axis of the wanted pixel.
     * @param y position on the Y-axis of the wanted pixel.
     * @param color color wanted at the pixel given by x and y.
     */
    public void setPixel(int x, int y, int color){
        this.img.setRGB(x, y, color);
    }

    /**
     * Build a ResultImg from a matrix.
     * @param matrix a matrix containing the wanted color for each pixel of the ResultImg.
     * @return a ResultImg with the wanted color (given by matrix) at each pixel.
     */
    public static ResultImg fromMatrix(float[][] matrix){
        ResultImg img = new ResultImg(matrix.length, matrix[0].length);
        for(int x = 0; x < matrix.length; x++){
            for(int y = 0; y < matrix[0].length; y++){
                img.setPixel(x, y, Color.HSBtoRGB((float) Math.abs(Math.sin(matrix[x][y]*2*Math.PI)), (float) Math.abs(Math.sin((1-matrix[x][y])*2*Math.PI)), matrix[x][y]));
            }
        }
        return img;
    }

    /**
     * get this image.
     * @return this image.
     */
    public BufferedImage getImage() {
        return img;
    }


    /**
     * Save the png in f file.
     * @param f the saving file.
     */
    public void endTask(File f){
        this.f = f;
        this.endTask();
    }

    /**
     * Save the image.
     */
    public void endTask() {
        try {
            System.out.println("Saving file...");
            ImageIO.write(img, "PNG", f);
            System.out.println("Done !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Invert the color of each pixel of the image.
     */
    public void invertColors() {
        for (int x = 0; x < img.getWidth(); x++){
            for (int y = 0; y < img.getHeight(); y++){
                Color oldColor = new Color(img.getRGB(x, y));
                Color newColor = new Color(255 - oldColor.getRed(), 255 - oldColor.getGreen(), 255 - oldColor.getBlue());
                setPixel(x, y, newColor.getRGB());
            }
        }
    }
}
