package org.obiz.sdtd;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Tools {

    public static void log(String message) {
        System.out.println(message);
    }

    public static void showImage(BufferedImage image) throws IOException {
        String filename = "result.png";
        File jpegFile = new File(filename);
        ImageIO.write(image, "PNG", jpegFile);
        Desktop.getDesktop().open(jpegFile);
    }

    public static void saveImage(BufferedImage image, String name) throws IOException {
        String filename = "images\\" + name + ".png";
        File imagesDir = new File("images\\");
        if(!imagesDir.exists()) {
//            Files.createDirectory(Path.of(imagesDir));
            imagesDir.mkdir();
        } else if (!imagesDir.isDirectory()) {
            throw new IOException("Can't create images dir!");
        }
        File jpegFile = new File(filename);
        ImageIO.write(image, "PNG", jpegFile);
//        Desktop.getDesktop().open(jpegFile);
    }

    public static BufferedImage rotateClockwise90(BufferedImage src, int count) {
        if(count==0) {
            return src;
        } else if (count < 0 || count > 3) {
            System.out.println("Error on rotate count! Wrong value: " + count);
            return src;
        }
        int width = src.getWidth();
        int height = src.getHeight();

        BufferedImage dest = new BufferedImage(height, width, src.getType());

        Graphics2D graphics2D = dest.createGraphics();
        graphics2D.translate((height - width) / 2, (height - width) / 2);
        graphics2D.rotate((Math.PI / 2 ) * count, height / 2, width / 2);
        graphics2D.drawRenderedImage(src, null);

        return dest;
    }
}
