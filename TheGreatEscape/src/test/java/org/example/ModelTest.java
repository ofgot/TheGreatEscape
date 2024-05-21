package org.example;

import org.game.thegreatescape.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;

import static org.game.thegreatescape.model.Direction.*;
import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {

    @ParameterizedTest
    @CsvSource({"1", "2", "3"})
    public void randomListTest(String a) {
        Model model = new Model();

        String generatedSequence = model.randomList();

        assertNotNull(generatedSequence);
        assertTrue(generatedSequence.matches("\\[\\d, \\d, \\d]"));
        assertTrue(generatedSequence.contains(a));
    }

    @Test
    public void testModelLoadGameConstructor() {
        Model model = new Model();

        assertNotNull(model.getGd());
        assertEquals(model.getSavedNameLevel(), model.getLevel());
        assertEquals(model.getSavedPlayerX(), model.getPlayer().getCharacterX());
        assertEquals(model.getSavedPlayerY(), model.getPlayer().getCharacterY());
        assertEquals(STAY, model.getPlayer().getDirection());
    }

    @Test
    public void testModelNewGameConstructor() {
        Model model = new Model(1);

        int expectedPlayerX = 256;
        int expectedPlayerY = 256;
        int expectedAnimationPhase = 0;

        assertEquals("firstLevel", model.getLevel());
        assertNotNull(model.getGd());
        assertEquals(expectedPlayerX, model.getPlayer().getCharacterX());
        assertEquals(expectedPlayerY, model.getPlayer().getCharacterY());
        assertEquals(STAY, model.getPlayer().getDirection());
        assertEquals(expectedAnimationPhase, model.getPlayer().getAnimationPhase());
    }

    @Test
    public void movingUpTest(){
        Model model = new Model(1);

        model.goUp();
        int expectedY = 246;
        assertEquals(expectedY, model.getPlayer().getCharacterY());
    }

    @Test
    public void PanelClosureOnCorrectButtonSequenceTest(){
        Model model = new Model(1);

        model.setButtonSequence(model.getRandomList());
        model.update();

        boolean expectedClosePanel = true;
        boolean expectedTouching = false;

        assertEquals(expectedTouching, model.getItemFromGD("secondLevel", "panel").getIsTouchable());
        assertEquals(expectedClosePanel, model.getClosePanel());
        assertNull(model.getRandomSequence());
    }


    @Test
    public void ifNearToTouchableObjectTest() {
        Model model = new Model(1);

        model.getPlayer().setCharacterX(5);
        model.getPlayer().setCharacterY(10);

        GameItem touchableItem1 = new GameItem();

        Coord crd1 = new Coord();
        crd1.setX(10);
        crd1.setY(10);

        touchableItem1.setFileName("touchableItem1.png");
        touchableItem1.setCoords(new Coord[]{crd1});
        touchableItem1.setWidth(64);
        touchableItem1.setHeight(64);
        touchableItem1.setTouchable(true);

        GameData gameData = new GameData();
        gameData.getTiles().put("touchableItem1", touchableItem1);

        HashMap<String, GameData> hashMap = new HashMap<>();
        hashMap.put("firstLevel", gameData);

        model.setGd(hashMap);

        assertEquals("touchableItem1.png", model.ifNearToTouchableObject());
    }

    @Test
    public void chengeLevelIfToNextLevelTest(){
        Model model = new Model(1);

        for (int i = 0; i < 10; i++) {
            model.goDown();
        }
        for (int i = 0; i < 47; i++) {
            model.goRight();
        }

        model.changeLevelIfDoorToNextLevel();

        String expectedLevel = "secondLevel";
        int expectedNewX = 32;
        int expectedNewY = 352;

        assertEquals(expectedLevel, model.getLevel());
        assertEquals(expectedNewX, model.getPlayer().getCharacterX());
        assertEquals(expectedNewY, model.getPlayer().getCharacterY());
    }

    @Test
    void checkIfPortalTouchedTest() {
        Model model = new Model(4);
        model.getPlayer().setCharacterX(580);
        model.getPlayer().setCharacterY(300);

        model.checkIfPortalTouched();

        assertEquals("fourthLevel", model.getLevel());
        assertTrue(model.getGameIsEnd());
    }

    @Test
    void CollisionTest(){
        Model model = new Model(1);

        model.getPlayer().setCharacterX(128);
        model.getPlayer().setCharacterY(128);

        model.goDown();
        model.goUp();

        System.out.println(model.getPlayer().getCharacterY());
        System.out.println(model.getPlayer().getCharacterX());

        assertEquals(128, model.getPlayer().getCharacterX());
        assertEquals(128, model.getPlayer().getCharacterY());
    }

}
