import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import utils.ImageUtils;

public class ImagePixelizer {

    private BufferedImage originalImage;
    private BufferedImage scaledImage;
    private JFrame frame;
    private JLabel imageLabel;
    private int squareSize;
    private ExecutorService service;

    public ImagePixelizer(String fileName, int squareSize, char processingMode) throws IOException {
        this.squareSize = squareSize;
        this.originalImage = ImageIO.read(new File(fileName));
        this.scaledImage = scaleImage(originalImage);

        service = processingMode == 'S' ? Executors.newSingleThreadExecutor()
                : Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        // GUI Setup
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Image Processing Progress");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            imageLabel = new JLabel(new ImageIcon(scaledImage));
            frame.getContentPane().add(imageLabel, BorderLayout.CENTER);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private BufferedImage scaleImage(BufferedImage img) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double maxWidth = screenSize.getWidth() - 50;
        double maxHeight = screenSize.getHeight() - 50;
        return ImageUtils.scaleImage(img, maxWidth, maxHeight);
    }

    public void processImage() {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        int updateFrequency = 3;

        // Multi-threaded mode - division to strips
        if (service instanceof java.util.concurrent.ThreadPoolExecutor) {
            int cores = Runtime.getRuntime().availableProcessors();
            int heightPerCore = height / cores;

            for (int i = 0; i < cores; i++) {
                final int startY = i * heightPerCore;
                final int endY = (i == cores - 1) ? height : (startY + heightPerCore);

                service.submit(() -> {
                    for (int y = startY; y < endY; y += squareSize) {
                        for (int x = 0; x < width; x += squareSize) {
                            synchronized (originalImage) {
                                setColor(x, y);
                            }
                            // System.out.println()
                            if ((x / squareSize) % updateFrequency == 0) {
                                BufferedImage stripCopy;
                                synchronized (originalImage) {
                                    stripCopy = new BufferedImage(scaledImage.getWidth(), scaledImage.getHeight(),
                                            BufferedImage.TYPE_INT_RGB);
                                    Graphics2D graphics = stripCopy.createGraphics();
                                    graphics.drawImage(originalImage, 0, 0, scaledImage.getWidth(),
                                            scaledImage.getHeight(), null);
                                    graphics.dispose();
                                }
                                final BufferedImage toDisplay = stripCopy;
                                SwingUtilities.invokeLater(() -> imageLabel.setIcon(new ImageIcon(toDisplay)));
                            }
                        }
                    }
                });
            }

            // !Single-threaded mode
        } else {
            for (int y = 0; y < height; y += squareSize) {
                for (int x = 0; x < width; x += squareSize) {
                    final int fx = x;
                    final int fy = y;
                    service.submit(() -> {
                        setColor(fx, fy);
                        SwingUtilities.invokeLater(() -> imageLabel.setIcon(new ImageIcon(scaledImage)));
                    });
                }
            }
        }

        service.shutdown();
        try {
            if (service.awaitTermination(1, TimeUnit.HOURS)) {
                ImageIO.write(scaledImage, "jpg", new File("result.jpg"));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setColor(int x, int y) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int maxX = Math.min(x + squareSize, width);
        int maxY = Math.min(y + squareSize, height);
        long sumRed = 0, sumGreen = 0, sumBlue = 0;
        int count = 0;

        for (int ix = x; ix < maxX; ix++) {
            for (int iy = y; iy < maxY; iy++) {
                Color pixel = new Color(originalImage.getRGB(ix, iy));
                sumRed += pixel.getRed();
                sumGreen += pixel.getGreen();
                sumBlue += pixel.getBlue();
                count++;
            }
        }

        int averageRed = (int) (sumRed / count);
        int averageGreen = (int) (sumGreen / count);
        int averageBlue = (int) (sumBlue / count);
        int averageColor = new Color(averageRed, averageGreen, averageBlue).getRGB();

        for (int ix = x; ix < maxX; ix++) {
            for (int iy = y; iy < maxY; iy++) {
                originalImage.setRGB(ix, iy, averageColor);
            }
        }

        // Updatin scaled image
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, scaledImage.getWidth(), scaledImage.getHeight(), null);
        g2d.dispose();
    }
}