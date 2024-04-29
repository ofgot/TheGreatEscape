package org.game.thegreatescape.model;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * The Character class represents a character in the game world.
 * Each character has a position, direction, animation phase, and inventory.
 */
public class Character {
    Logger logger = Logger.getLogger(Character.class.getName());
    private int characterX; // X-coordinate of the character's position
    private int characterY; // Y-coordinate of the character's position
    private Direction direction; // Direction the character is facing
    private int animationPhase; // Current animation phase of the character
    private ArrayList<Integer> inventory; // Inventory of the character

    private final int SPEED = 10; // Speed of the character's movement
    private final int WIDTH = 48; // Width of the character's sprite
    private final int HEIGHT = 53; // Height of the character's sprite

    // Arrays of images for different character directions and animation phases
    final Image[] DOWN = {new Image("godown0.png"), new Image("godown1.png"), new Image("godown2.png")};
    final Image[] LEFT = {new Image("goleft0.png"), new Image("goleft1.png"), new Image("goleft2.png")};
    final Image[] RIGHT = {new Image("goright0.png"), new Image("goright1.png"), new Image("goright2.png")};
    final Image[] UP = {new Image("goup0.png"), new Image("goup1.png"), new Image("goup2.png")};
    final Image[] STAY = {new Image("godown0.png")};

    /**
     * Constructor for the Character class.
     *
     * @param characterX     The initial X-coordinate of the character's position.
     * @param characterY     The initial Y-coordinate of the character's position.
     * @param direction      The initial direction the character is facing.
     * @param animationPhase The initial animation phase of the character.
     */
    public Character(int characterX, int characterY, Direction direction, int animationPhase) {
        this.characterX = characterX;
        this.characterY = characterY;
        this.direction = direction;
        this.animationPhase = animationPhase;
        this.inventory = new ArrayList<>();
        logger.info("New Character created - X: " + characterX + ", Y: " + characterY +
                ", Direction: " + direction + ", Animation Phase: " + animationPhase);
    }

    // Getters and setters for fields
    public void setCharacterX(int characterX) {
        this.characterX = characterX;
    }

    public void setCharacterY(int characterY) {
        this.characterY = characterY;
    }

    public ArrayList<Integer> getInventory() {
        return inventory;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public int getCharacterX() {
        return characterX;
    }

    public int getCharacterY() {
        return characterY;
    }

    public int getSPEED() {
        return SPEED;
    }

    public Direction getDirection() {return direction;}

    public int getAnimationPhase() {return animationPhase;}

    /**
     * Moves the character upwards.
     */
    public void goUp() {
        animationPhase = animationPhase < 2 ? animationPhase + 1 : 1;
        direction = Direction.UP;
        characterY -= SPEED;
    }

    /**
     * Moves the character right.
     */
    public void goRight() {
        animationPhase = animationPhase < 2 ? animationPhase + 1 : 1;
        direction = Direction.RIGHT;
        characterX += SPEED;
    }

    /**
     * Moves the character left.
     */
    public void goLeft() {
        animationPhase = animationPhase < 2 ? animationPhase + 1 : 1;
        direction = Direction.LEFT;
        characterX -= SPEED;
    }

    /**
     * Moves the character downwards.
     */
    public void goDown() {
        animationPhase = animationPhase < 2 ? animationPhase + 1 : 1;
        direction = Direction.DOWN;
        characterY += SPEED;
    }

    /**
     * Sets the animation phase to 0 for staying in right position.
     */
    public void stayRight() {
        animationPhase = 0;
    }

    /**
     * Sets the animation phase to 0 for staying in left position.
     */
    public void stayLeft() {
        animationPhase = 0;
    }

    /**
     * Sets the animation phase to 0 for staying in upwards position.
     */
    public void stayUp() {
        animationPhase = 0;
    }

    /**
     * Sets the animation phase to 0 for staying in downwards position.
     */
    public void stayDown() {
        animationPhase = 0;
    }

    /**
     * Get the image representing the character's current direction and animation phase.
     *
     * @return The image representing the character.
     */
    public Image getImage() {
        return switch (direction) {
            case UP -> UP[animationPhase];
            case RIGHT -> RIGHT[animationPhase];
            case DOWN -> DOWN[animationPhase];
            case LEFT -> LEFT[animationPhase];
            case STAY -> STAY[0];
        };
    }
}
