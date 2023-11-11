import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out
                    .println("Input format should be: java ImagePixelizer <filename> <square size> <processing mode>");
            System.exit(1);
        }

        try {
            String fileName = args[0];
            int squareSize = Integer.parseInt(args[1]);
            char processingMode = args[2].charAt(0);
            ImagePixelizer pixelizer = new ImagePixelizer(fileName, squareSize, processingMode);
            pixelizer.processImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
