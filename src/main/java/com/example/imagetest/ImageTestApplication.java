package com.example.imagetest;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ImageTestApplication {

    public static void main(String[] args) {
//        SpringApplication.run(ImageTestApplication.class, args);

        File file = new File("C:\\Users\\TT\\Desktop\\71645695_p0_master1200.jpg");
        List<Color> colorList = parseImg(file);

        BufferedImage img;
        try {
            img = ImageIO.read(file);
            generateImg(img, colorList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static List<Color> parseImg (File file) {
        FileWriter writer;
        List<Color> colorList = new ArrayList<>();

        try {
            writer = new FileWriter("src/main/resources/pixel_values.txt");
            BufferedImage img = ImageIO.read(file);

            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    //Retrieving contents of a pixel
                    int pixel = img.getRGB(x, y);
                    //Creating a Color object from pixel value
                    Color color = new Color(pixel, true);
                    colorList.add(color);
                    //Retrieving the R G B values
                    int red = color.getRed();
                    int green = color.getGreen();
                    int blue = color.getBlue();
                    writer.append(String.valueOf(red)).append(":");
                    writer.append(String.valueOf(green)).append(":");
                    writer.append(String.valueOf(blue)).append(" ");
                    writer.flush();
                }
                writer.append("\n");
            }
            writer.close();
            System.out.println("RGB values at each pixel are stored in the specified file");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return colorList;
    }

    public static void generateImg(BufferedImage img, List<Color> colorList) {
        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage imgOut = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        File f;

        if (colorList.size() != 0) {
            // get pixel on format (219:4:35)
            for (int y = 0; y < height; y++) {
                List<Color> subList = colorList.subList(y * width, y * width + width);
                for (int x = 0; x < subList.size(); x++) {
                    int a, r, g, b, p;
                    if (y == x) {
                        a = 0;
                        r = 0;
                        g = 0;
                        b = 0;
                    } else {
                        a = subList.get(x).getAlpha(); //alpha
                        r = subList.get(x).getRed(); //red
                        g = subList.get(x).getGreen(); //green
                        b = subList.get(x).getBlue(); //blue
                    }
                    p = (a << 24) | (r << 16) | (g << 8) | b;
                    imgOut.setRGB(x, y, p);
                }
            }
        } else {
            // random generation
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int a = (int) (Math.random() * 256); //alpha
                    int r = (int) (Math.random() * 256); //red
                    int g = (int) (Math.random() * 256); //green
                    int b = (int) (Math.random() * 256); //blue
                    int p = (a << 24) | (r << 16) | (g << 8) | b;
                    imgOut.setRGB(x, y, p);
                }
            }
        }

        try {
            f = new File("D:\\Output.jpg");
            ImageIO.write(imgOut, "png", f);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
