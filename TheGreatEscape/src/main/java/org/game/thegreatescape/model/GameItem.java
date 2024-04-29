package org.game.thegreatescape.model;

/**
 * The GameItem class represents an item or element in the game world.
 */
public class GameItem {

    public String fileName; // The file name of the item's sprite
    public Coord[] coords; // Array of coordinates defining the item's position
    public int width; // Width of the item
    public int height; // Height of the item
    public boolean isDoorToTheNextLevel; // Indicates if the item is a door to the next level
    public String nextLevel; // The name of the next level to transition to
    public int nextX; // X-coordinate of the player's position in the next level
    public int nextY; // Y-coordinate of the player's position in the next level
    public boolean isCollision; // Indicates if the item causes collision
    public boolean isTouchable; // Indicates if the item can be interacted with
    public boolean isTheEnd; // Indicates if the item represents the end of the game

    /**
     * Default constructor for the GameItem class.
     */
    public GameItem() {
    }

}
