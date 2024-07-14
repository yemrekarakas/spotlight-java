package net.ekarakas;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static void downloadImage(String imageUrl, String destinationFile) {
        try {
            URL url = new URL(imageUrl);

            Path path = Paths.get(destinationFile);

            Files.createDirectories(path.getParent());

            try (InputStream in = url.openStream()) {
                Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void getImageOrientation(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            BufferedImage image = ImageIO.read(imageFile);

            int width = image.getWidth();
            int height = image.getHeight();

            if (width > height) {
                moveFile(imagePath, "Landscape/");
            } else if (height > width) {
                moveFile(imagePath, "Portrait/");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void moveFile(String sourceFilePath, String targetFolderPath) throws IOException {
        Path sourcePath = Paths.get(sourceFilePath);
        Path targetPath = Paths.get(targetFolderPath + sourcePath.getFileName());

        if (!Files.exists(targetPath)) {
            Files.move(sourcePath, targetPath);
        } else {
            Files.deleteIfExists(sourcePath);
        }
    }

    public static String getId(String source) {
        String id = "";
        String regex = "/([^/]+)\\.jpg$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);

        if (matcher.find()) {
            id = matcher.group(1);
        }
        return id;
    }
}
