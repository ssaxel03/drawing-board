package io.codeforall.fanstatics;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {

    private static final String DIRECTORY_NAME = "DrawingBoard";
    private static final String FILE_NAME = "savedCanva.canvas";
    private static String saveFilePath;

    static {
        try {
            // Get user's pictures directory
            String picturesPath = getPicturesDirectoryPath();

            // Create the DrawingBoard directory inside pictures directory
            Path drawingBoardPath = Paths.get(picturesPath, DIRECTORY_NAME);
            if (!Files.exists(drawingBoardPath)) {
                Files.createDirectories(drawingBoardPath);
            }

            // Set the full path to the save file
            saveFilePath = drawingBoardPath.resolve(FILE_NAME).toString();

        } catch (IOException e) {
            System.err.println("Error setting up save directory: " + e.getMessage());
            // Fallback to user home if pictures directory setup fails
            saveFilePath = System.getProperty("user.home") + File.separator +
                    DIRECTORY_NAME + File.separator + FILE_NAME;
        }
    }

    private static String getPicturesDirectoryPath() {
        // Get OS-specific pictures directory
        String os = System.getProperty("os.name").toLowerCase();
        String userHome = System.getProperty("user.home");

        if (os.contains("win")) {
            // Windows
            return userHome + File.separator + "Pictures";
        } else if (os.contains("mac")) {
            // macOS
            return userHome + File.separator + "Pictures";
        } else {
            // Linux and others - try standard XDG directory
            String xdgPictures = System.getenv("XDG_PICTURES_DIR");
            if (xdgPictures != null && !xdgPictures.isEmpty()) {
                return xdgPictures;
            } else {
                return userHome + File.separator + "Pictures";
            }
        }
    }

    public static void saveCanvas(Grid grid) throws IOException {
        System.out.println("IM SAVING");

        // Make sure the directory exists
        File saveDir = new File(saveFilePath).getParentFile();
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }

        System.out.println("Saving to: " + saveFilePath);

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(saveFilePath));
        bufferedWriter.write("");
        bufferedWriter.write(grid.getSaveFile());
        bufferedWriter.close();
    }

    public static String loadCanvas() throws IOException {
        System.out.println("Loading from: " + saveFilePath);

        // Check if file exists before attempting to read
        File saveFile = new File(saveFilePath);
        if (!saveFile.exists()) {
            System.out.println("No saved canvas found");
            return "";
        }

        BufferedReader bufferedReader = new BufferedReader(new FileReader(saveFilePath));
        StringBuilder data = new StringBuilder();
        String read;

        while((read = bufferedReader.readLine()) != null) {
            data.append(read);
        }

        bufferedReader.close();
        return data.toString();
    }

}
