package org.example;

import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import org.game.thegreatescape.model.Character;
import org.game.thegreatescape.model.Direction;
import org.game.thegreatescape.view.GameSceneFactory;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


public class GameSceneFactoryTest {

    @Test
    public void testConstructor(){
        Stage mockStage = mock(Stage.class);
        GameSceneFactory gsf = new GameSceneFactory(mockStage);

        assertNotNull(gsf);
    }

    @Test
    public void printCharImagesTest(){
        Stage mockStage = mock(Stage.class);
        GameSceneFactory gsf = new GameSceneFactory(mockStage);
        Character player = mock(Character.class);
        when(player.returnImagesList()).thenReturn(new String[][]{{"godown0.png","godown1.png","godown2.png"}});

        gsf.printCharImages(player);

        //assertNotNull(gsf.getCharacterImages());
    }

}
