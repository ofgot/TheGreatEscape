package org.game.thegreatescape.levelEditor;

/**
 * The imageData class represents data associated with an image, including its name, position, and sizes.
 */

public class ImageData {
    private final String name;
    private final double x;
    private final double y;
    private final int imgHeight;
    private final int imgWidth;

    /**
     * Constructor of an ImageData object.
     *
     * @param name      The name of the image.
     * @param x         The x-coordinate of the image's position.
     * @param y         The y-coordinate of the image's position.
     * @param imgHeight The height of the image.
     * @param imgWidth  The width of the image.
     */
    public ImageData(String name, double x, double y, int imgHeight, int imgWidth) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.imgHeight = imgHeight;
        this.imgWidth = imgWidth;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public String getName() {
        return name;
    }
    public int getImgHeight() {
        return imgHeight;
    }

    public int getImgWidth() {
        return imgWidth;
    }
}
