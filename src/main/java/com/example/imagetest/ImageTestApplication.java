package com.example.imagetest;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class ImageTestApplication {

    public static void main(String[] args) throws IOException {
//        SpringApplication.run(ImageTestApplication.class, args);

        FileWriter writer = new FileWriter("src/main/resources/pixel_values.txt");
        //Reading the image
        File file= new File("C:\\Users\\TT\\Desktop\\71645695_p0_master1200.jpg");
        BufferedImage img = ImageIO.read(file);

        List<Color> colorList = new ArrayList<>();

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                //Retrieving contents of a pixel
                int pixel = img.getRGB(x,y);
                //Creating a Color object from pixel value
                Color color = new Color(pixel, true);
                colorList.add(color);
                //Retrieving the R G B values
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                writer.append(red+":");
                writer.append(green+":");
                writer.append(blue+" ");
                writer.flush();
            }
            writer.append("\n");
        }
        writer.close();
        System.out.println("RGB values at each pixel are stored in the specified file");


        /*int width, height;
        File file= new File("C:\\Users\\TT\\Desktop\\71645695_p0_master1200.jpg");
        BufferedImage source = ImageIO.read(file);
        width = source.getWidth();
        height = source.getHeight();
        getImageFromArray(convertToPixels(source), width, height);*/


        int width = img.getWidth();
        int height = img.getHeight();
//create buffered image object img
        BufferedImage imgOut = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//file object
        File f = null;

        for(int y = 0; y < height; y++){
            List<Color> subList = colorList.subList(y * width, y * width + width);
            for(int x = 0; x < subList.size(); x++){
                int a = subList.get(x).getAlpha(); //alpha
                int r = subList.get(x).getRed(); //red
                int g = subList.get(x).getGreen(); //green
                int b = subList.get(x).getBlue(); //blue
                int p = (a<<24) | (r<<16) | (g<<8) | b;
                img.setRGB(x, y, p);
            }
        }

        try{
            f = new File("D:\\Output.jpg");
            ImageIO.write(imgOut, "png", f);
        }catch(IOException e){
            System.out.println(e);
        }

    }

    public static int[] convertToPixels(Image img) {
        int width = img.getWidth(null);

        int height = img.getHeight(null);
        int[] pixel = new int[width * height];

        PixelGrabber pg = new PixelGrabber(img, 0, 0, width, height, pixel, 0, width);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            throw new IllegalStateException("Error: Interrupted Waiting for Pixels");
        }
        if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
            throw new IllegalStateException("Error: Image Fetch Aborted");
        }
        return pixel;
    }

    public static Image getImageFromArray(int[] pixels, int width, int height) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        WritableRaster raster = (WritableRaster) image.getData();
        raster.setPixels(0, 0, width, height, pixels);
        File output = new File("C:\\out.png");
        ImageIO.write(image, "png", output);
        System.out.print("written");
        return image;
    }

    public static int generateIntNumber(int max) {
        return new Random().nextInt(max);
    }
}
