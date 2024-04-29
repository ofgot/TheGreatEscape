package org.game.thegreatescape.levelEditor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * The JsonfileCreate class creates JSON files
 * containing data about images placed on the canvas.
 */
public class JsonfileCreate {
    Logger logger = Logger.getLogger(CanvasData.class.getName());
    private static int counter = 1;
    private final ArrayList<String> names = new ArrayList<String>();
    private final String START_STRING = "{\"tiles\" : {";
    private final StringBuilder sb = new StringBuilder(START_STRING);

    /**
     * Get image names from the given array of image data.
     *
     * @param array The array of imageData objects.
     * @return ArrayList of image names.
     */
    public ArrayList<String> getNamesFromArray(ArrayList<ImageData> array) {
        for (ImageData data : array) {
            if (!names.contains(data.getName().replace(".png", ""))) {
                names.add(data.getName().replace(".png", ""));
            }
        }
        logger.info("Names were successfully written");
        return names;
    }

    /**
     * Generates a JSON string representation of the image data.
     *
     * @param imgList The list of imageData objects.
     * @return JSON string representation of the image data.
     */
    public String toString(ArrayList<ImageData> imgList) {
        jsonStringBuilder(imgList);
        return sb.toString();
    }

    /**
     * Builds the JSON string representation of the image data.
     *
     * @param imgList The list of imageData objects.
     */
    public void jsonStringBuilder(ArrayList<ImageData> imgList) {
        getNamesFromArray(imgList);

        for (String name : names) {
            sb.append("\"" + name + "\": {");
            sb.append("\"fileName\": \"" + name + ".png\",");
            sb.append("\"width\": 64,");
            sb.append("\"height\": 64,");
            sb.append("\"coords\": [");

            // Put coords
            for (ImageData obj : imgList) {
                if (name.equals(obj.getName().replace(".png", ""))) {
                    sb.append("{\"x\":" + obj.getX() + ",\"y\":" + obj.getY() + "},");
                    logger.info("Added coordinates for image " + name + ": x=" + obj.getX() + ", y=" + obj.getY());
                }
            }

            // Delete last comma
            if (sb.charAt(sb.length() - 1) == ',') {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append("]");
            sb.append("},");
        }

        // Delete last comma
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.deleteCharAt(sb.length() - 1);
        }

        sb.append("}");
        sb.append("}");

        logger.info("JSON construction completed.");
    }

    /**
     * Creates a JSON file with the provided string content.
     *
     * @param string The JSON string content.
     */
    public void createJsonFile(String string) {
        File file = new File(counter + "_Json.json");

        try {
            if (file.createNewFile()) {
                logger.info("File created: " + file.getName());
                System.out.println("File created: " + file.getName());
                counter++;
            } else {
                logger.warning("File already exists: " + file.getName());
            }
        } catch (IOException e) {
            logger.severe("An error occurred while creating file: " + file.getName());
            e.printStackTrace();
        }

        try {
            FileWriter fileWrite = new FileWriter(file.getName());
            fileWrite.write(string);
            fileWrite.close();
            logger.info("Successfully wrote to the file: " + file.getName());
        } catch (IOException a) {
            logger.severe("An error occurred while writing to file: " + file.getName());
            a.printStackTrace();
        }
    }
}
