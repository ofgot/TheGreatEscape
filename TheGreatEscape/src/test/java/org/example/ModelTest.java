package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.game.thegreatescape.model.Model;
import org.game.thegreatescape.view.Game;
import org.junit.jupiter.api.Test;
import org.testfx.util.WaitForAsyncUtils;

import java.util.Arrays;
import java.util.LinkedList;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ModelTest {

    @Test
    public void randomListTest(){


        Model model = new Model();

        String generatedSequence = model.randomList();

        LinkedList<String> linkedList = new LinkedList<String>(Arrays.asList(generatedSequence));
        int expectedSize = 3;

        assertEquals(expectedSize, linkedList.size());
    }

//    @Test
//    public void testModelNewGameConstructor() {
//        Model model = new Model(1);
//
//        assertEquals("firstLevel", model.getLevel());
//
//        int expectedPlayerX = 256;
//        int expectedPlayerY = 256;
//        int expectedAnimationPhase = 0;
//
//        assertEquals(expectedPlayerX, model.getPlayer().getCharacterX());
//        assertEquals(expectedPlayerY, model.getPlayer().getCharacterY());
//        assertEquals(STAY, model.getPlayer().getDirection());
//        assertEquals(expectedAnimationPhase, model.getPlayer().getAnimationPhase());
//    }

    @Test
    public void updateTest(){

    }
}
