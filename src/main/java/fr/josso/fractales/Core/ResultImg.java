package fr.josso.fractales.Core;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ResultImg {

    int yShift;
    int xShift;

    int r = 64; int g = 224; int b = 208; //turquoise
    int col = (r << 16) | (g << 8) | b;

    BufferedImage img;
    File f = new File("MyFractal.png");


    public ResultImg(int width, int height){
        this.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public void setPixel(int x, int y, int color){
        this.img.setRGB(x, y, color);
    }

    public static ResultImg fromMatrix(ArrayList<ArrayList<Float>> matrix){
        ResultImg img = new ResultImg(matrix.size(), matrix.get(0).size());
        for(int x = 0; x < matrix.size(); x++){
            for(int y = 0; y < matrix.get(x).size(); y++){
                img.setPixel(x, y, Color.HSBtoRGB(matrix.get(x).get(y), 0.7f, 0.7f));
            }
        }
        return img;
    }

    public BufferedImage getImage() {
        return img;
    }

    public void endTask() {
        try {
            ImageIO.write(img, "PNG", f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
