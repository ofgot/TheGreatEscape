package org.example;

import org.game.thegreatescape.levelEditor.ImageData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImageDataTest {
    @Test
    public void testConstructor() {
        String name = "test_image";
        double x = 10.1;
        double y = 20.1;
        int imgHeight = 100;
        int imgWidth = 200;

        ImageData imageData = new ImageData(name, x, y, imgHeight, imgWidth);

        assertEquals(name, imageData.getName());
        assertEquals(x, imageData.getX(), 0.0);
        assertEquals(y, imageData.getY(), 0.0);
        assertEquals(imgHeight, imageData.getImgHeight());
        assertEquals(imgWidth, imageData.getImgWidth());
    }
}
