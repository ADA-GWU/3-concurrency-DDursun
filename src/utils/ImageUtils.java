package utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {

    public static BufferedImage scaleImage(BufferedImage img, double maxWidth, double maxHeight) {

        // Trying to maintian aspect ratio
        double scaleFactor = Math.min(1d, Math.min(maxWidth / img.getWidth(), maxHeight / img.getHeight()));

        // New dimentions
        int scaleWidth = (int) Math.round(img.getWidth() * scaleFactor);
        int scaleHeight = (int) Math.round(img.getHeight() * scaleFactor);

        // New image
        BufferedImage scaledImg = new BufferedImage(scaleWidth, scaleHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = scaledImg.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(img, 0, 0, scaleWidth, scaleHeight, null);
        g2d.dispose();

        return scaledImg;
    }
}
