package org.example;

import org.game.thegreatescape.model.Character;
import org.game.thegreatescape.model.Direction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CharacterTest {

    @Test
    public void CharacterConstructorTest(){
        int expectedX = 0;
        int expectedY = 0;
        Direction expectedDirection = Direction.STAY;
        int expectedAnimationPhase = 0;

        Character character = new Character(expectedX,expectedY, expectedDirection,expectedAnimationPhase);

        assertEquals(expectedX, character.getCharacterX());
        assertEquals(expectedY, character.getCharacterY());
        assertEquals(expectedDirection, character.getDirection());
        assertEquals(expectedAnimationPhase, character.getAnimationPhase());
    }

    @Test
    public void CharacterMoveUpTest(){
        Character player = new Character(0,0, Direction.DOWN, 0);

        player.goUp();

        int expectedX = 0;
        int expectedY = -10;
        Direction expectedDir = Direction.UP;
        int expectedAnimationPhase  = 1;

        assertEquals(expectedX, player.getCharacterX());
        assertEquals(expectedY, player.getCharacterY());
        assertEquals(expectedDir, player.getDirection());
        assertEquals(expectedAnimationPhase, player.getAnimationPhase());
    }

    @Test
    public void CharacterMoveDownTest(){
        Character player = new Character(0,0, Direction.DOWN, 0);

        player.goDown();

        int expectedX = 0;
        int expectedY = 10;
        Direction expectedDir = Direction.DOWN;
        int expectedAnimationPhase  = 1;

        assertEquals(expectedX, player.getCharacterX());
        assertEquals(expectedY, player.getCharacterY());
        assertEquals(expectedDir, player.getDirection());
        assertEquals(expectedAnimationPhase, player.getAnimationPhase());
    }

    @Test
    public void CharacterMoveRightTest(){
        Character player = new Character(0,0, Direction.DOWN, 0);

        player.goRight();

        int expectedX = 10;
        int expectedY = 0;
        Direction expectedDir = Direction.RIGHT;
        int expectedAnimationPhase  = 1;

        assertEquals(expectedX, player.getCharacterX());
        assertEquals(expectedY, player.getCharacterY());
        assertEquals(expectedDir, player.getDirection());
        assertEquals(expectedAnimationPhase, player.getAnimationPhase());
    }

    @Test
    public void CharacterMoveLeftTest(){
        Character player = new Character(0,0, Direction.DOWN, 0);

        player.goLeft();

        int expectedX = -10;
        int expectedY = 0;
        Direction expectedDir = Direction.LEFT;
        int expectedAnimationPhase  = 1;

        assertEquals(expectedX, player.getCharacterX());
        assertEquals(expectedY, player.getCharacterY());
        assertEquals(expectedDir, player.getDirection());
        assertEquals(expectedAnimationPhase, player.getAnimationPhase());
    }

    @ParameterizedTest
    @CsvSource({"godown0.png, DOWN", "goright0.png, RIGHT", "goleft0.png, LEFT", "goup0.png, UP"})
    public void testGetImageName(String name, String direction) {
        Character player = new Character(0,0, Direction.valueOf(direction), 0);

        assertEquals(name, player.getImageName());
    }
}
