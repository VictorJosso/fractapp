package fr.josso.fractales.Core;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ResultImg {

    int yShift;
    int xShift;

    int r = 64; int g = 224; int b = 208; //turquoise
    int col = (r << 16) | (g << 8) | b;

    BufferedImage img;
    File f = new File("MyFractal.png");

    public ResultImg (int maxX, int minX, int maxY, int minY, double step, int scale, Complex origin) {
        int width = (int) ((maxX - minX) / step);
        int height = (int) ((maxY - minY) /step);

        this.yShift = (int) - (origin.getImaginaryPart() * scale) ;
        this.xShift = (int) - (origin.getRealPart() * scale);

        System.out.println(yShift);
        System.out.println(xShift);

        this.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public void writeInImg(double x, double y, int scale, int ind, long max_iter) {
        int px = (int) (x * scale) + this.xShift;
        int py = (int) (y * scale) + this.yShift;
        //System.out.println(" x, y : " + x + ", " + y + " et px, py : " + px + ", " + py);

        img.setRGB(px, py, (int) ((col * ind) / max_iter));
    }

    public void endTask() {
        try {
            ImageIO.write(img, "PNG", f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
