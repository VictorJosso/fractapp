package fr.josso.fractales.Core;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ResultImg {

    int r = 64; int g = 224; int b = 208; //turquoise
    int col = (r << 16) | (g << 8) | b;

    BigBufferedImage img;
    File f = new File("MyFractal.png");


    public ResultImg(int width, int height){
        this.img = (BigBufferedImage) BigBufferedImage.create(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public void setPixel(int x, int y, int color){
        this.img.setRGB(x, y, color);
    }

    public static ResultImg fromMatrix(float[][] matrix){
        ResultImg img = new ResultImg(matrix.length, matrix[0].length);
        for(int x = 0; x < matrix.length; x++){
            for(int y = 0; y < matrix[0].length; y++){
                img.setPixel(x, y, Color.HSBtoRGB(matrix[x][y], 0.7f, 0.7f));
            }
        }
        return img;
    }

    public BigBufferedImage getImage() {
        return img;
    }

    public void endTask() {
        try {
            System.out.println("Done !");
            ImageIO.write(img, "PNG", f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
