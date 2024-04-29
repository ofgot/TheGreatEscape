package org.game.thegreatescape.levelEditor;

import java.util.ArrayList;

/**
 * The CanvasData class represents data and functions for adding and deleting them from canvas.
 */

public class CanvasData {

    private ArrayList<ImageData> imgList = new ArrayList<ImageData>();

    /**
     * Adds an imageData object to the list.
     *
     * @param img The imageData object to add.
     */
    public void addImage(ImageData img) {
        imgList.add(img);
    }

    /**
     * Removes an image data object from the list based on its coordinates.
     *
     * @param x The x-coordinate of the image.
     * @param y The y-coordinate of the image.
     * @return true if the image was successfully removed, false otherwise.
     */
    public boolean removeImage(double x, double y) {
        for (ImageData img : imgList) {
            if (img.getX() == x && img.getY() == y) {
                imgList.remove(img);
                return true;
            }
        }
        return false;
    }

    /**
     * Get the list of imageData objects and return it.
     *
     * @return The list of imageData objects.
     */
    public ArrayList<ImageData> getImagesList() {
        return imgList;
    }


}
