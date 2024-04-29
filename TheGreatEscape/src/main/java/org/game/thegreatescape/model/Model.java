package org.game.thegreatescape.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

import org.game.thegreatescape.view.Game;
import org.json.JSONObject;

/**
 * The Model class represents the backend logic of the game.
 * It manages game data, player movement, level transitions, and game events.
 */
public class Model {
    Logger logger = Logger.getLogger(Game.class.getName());

    private final String[] levels = {"firstLevel", "secondLevel", "thirdLevel", "fourthLevel"};
    private HashMap<String, GameData> gd = new HashMap<String, GameData>();
    private final Character player;
    private String level;
    private boolean closePanel = false;
    private final LinkedList<Integer> buttonSequence = new LinkedList<Integer>();
    LinkedList<Integer> randomList = new LinkedList<Integer>();
    String randomSequence = randomList();
    Random random = new Random();

    private boolean gameIsEnd = false;

    static String savedNameLevel;
    static int savedPlayerX;
    static int savedPlayerY;

    /**
     * Constructor of Model object with the specified level number (Used by new game).
     *
     * @param levelNumber The level number to start the game.
     */
    public Model(int levelNumber) {
        this.level = levels[levelNumber - 1];
        for (String gameLevel : levels) {
            loadGameData(gameLevel);
        }
        GameData currentGd = gd.get(level);
        player = new Character(currentGd.START_POSITION_PLAYERX, currentGd.START_POSITION_PLAYERY, Direction.STAY, currentGd.START_ANIMATION_PHASE);
        logger.info("Model for new game was successful");
    }

    /**
     * Constructor of Model object with the specified level number (Used by load game).
     */
    public Model() {
        loadSavedGameData();
        this.level = savedNameLevel;
        GameData currentGd = gd.get(level);
        player = new Character(savedPlayerX, savedPlayerY, Direction.STAY, 0);
        logger.info("Model for load game was successful");
    }

    public Character getPlayer() {
        return player;
    }
    public boolean isGameIsEnd() {
        return gameIsEnd;
    }
    public boolean isClosePanel() {
        return closePanel;
    }
    public String getLevel() {
        return level;
    }
    public GameData getGd() {
        return gd.get(level);
    }


    /**
     * Loads game data from a JSON file for the specified level.
     *
     * @param levelName The name of the level for which to load the game data.
     */
    private void loadGameData(String levelName) {
        ObjectMapper objectMapper = new ObjectMapper(); //objectMapper transform data to json and back
        try {
            gd.put(levelName, objectMapper.readValue(new File(levelName + ".json"), GameData.class));
            logger.info("Successfully loaded game data for level: " + levelName);
        } catch (IOException e) {
            logger.severe("Error loading game data for level: " + levelName  + " " + e);
        }
    }

    private void loadSavedGameData() {
        try {
            logger.info("Loading Saved game data.");

            ObjectMapper mapper = new ObjectMapper();
            File fileObj = new File("saveGame.json");
            File saveGamePlayer = new File("saveGamePlayer.json");
            TypeReference<HashMap<String, GameData>> typeRef   = new TypeReference<HashMap<String, GameData>>() {};
            gd = mapper.readValue(fileObj, typeRef);

            String jsonString = new String(Files.readAllBytes(Paths.get("saveGamePlayer.json")));
            JSONObject jsonObject = new JSONObject(jsonString);

            savedNameLevel = jsonObject.getString("savedLevel");
            savedPlayerX = jsonObject.getInt("savedPlayerPositionX");
            savedPlayerY = jsonObject.getInt("savedPlayerPositionY");

            logger.info("Saved level: " + savedNameLevel + ", Saved player position X: " + savedPlayerX + ", Saved player position Y: " + savedPlayerY);
            logger.info("Saved game data loaded successfully.");
        } catch (IOException e) {
            logger.severe("Error occurred while loading saved game data: " + e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates a random sequence of three unique integers between 1 and 3.
     *
     * @return A string representation of the random sequence.
     */
    public String randomList() {
        randomList = new LinkedList<Integer>();
        random = new Random();
        logger.info("Generating random list...");
        while (randomList.size() != 3) {
            int randomNumber = random.nextInt(3) + 1;
            if (!randomList.contains(randomNumber)) {
                randomList.add(randomNumber);
                logger.info("Added random number to list: " + randomNumber);
            }
        }
        logger.info("Random list generated: " + randomList.toString());
        return randomList.toString();
    }

    /**
     * Updates the game state, including checking for level transitions and game events.
     * If the player touches a door, it changes the level accordingly.
     * If the button sequence matches the random sequence, it closes the panel.
     * If the current level is the fourth level, it checks if the player has touched the portal to end the game.
     */
    public void update() {
        changeLevelIfDoorToNextLevel();
        if (buttonSequence.toString().equals(randomSequence)) {
            logger.info("Button sequence matches random sequence. Closing panel...");
            closePanel = true;
            unsetTouching("secondLevel", "panel.png");
            randomSequence = null;
        }

        if (getLevel().equals("fourthLevel")) {
            checkIfPortalTouched();
        }
    }

    /**
     * Moves the player character upwards if there is no collision with game objects.
     * If there is no collision, the player character's vertical position is decremented by its speed.
     */
    public void goUp() {
        if (!isCollision(new Rectangle(player.getCharacterX(), player.getCharacterY() - player.getSPEED(), player.getHEIGHT(), player.getWIDTH()))) {
            player.goUp();
        }
    }

    /**
     * Moves the player character to the right if there is no collision with game objects.
     * If there is no collision, the player character's horizontal position is incremented by its speed.
     */
    public void goRight() {
        if (!isCollision(new Rectangle(player.getCharacterX() + player.getSPEED(), player.getCharacterY(), player.getHEIGHT(), player.getWIDTH()))) {
            player.goRight();
        }
    }

    /**
     * Moves the player character to the left if there is no collision with game objects.
     * If there is no collision, the player character's horizontal position is decremented by its speed.
     */
    public void goLeft() {
        if (!isCollision(new Rectangle(player.getCharacterX() - player.getSPEED(), player.getCharacterY(), player.getHEIGHT(), player.getWIDTH()))) {
            player.goLeft();
        }
    }

    /**
     * Moves the player character downwards if there is no collision with game objects.
     * If there is no collision, the player character's vertical position is incremented by its speed.
     */
    public void goDown() {
        if (!isCollision(new Rectangle(player.getCharacterX(), player.getCharacterY() + player.getSPEED(), player.getHEIGHT(), player.getWIDTH()))) {
            player.goDown();
        }
    }

    /**
     * Checks for collision between the player character and game objects.
     *
     * @param playerRectangle The rectangle representing the player character's bounding box.
     * @return true if there is a collision between the player character and any game object, false otherwise.
     */
    private boolean isCollision(Rectangle playerRectangle) {
        GameData currentGd = gd.get(level);
        for (GameItem item : currentGd.tiles.values()) {
            if (item.isCollision) {
                for (Coord crd : item.coords) {
                    Rectangle itemRectangle = new Rectangle(crd.x, crd.y, item.height, item.width);
                    if (playerRectangle.getBoundsInParent().intersects(itemRectangle.getBoundsInParent())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if the player character is touching a door to transition to the next level.
     * If the player touches a door, it changes the current level to the next level and updates the player's position.
     *
     * @return true if the player transitions to the next level, false otherwise.
     */
    private boolean changeLevelIfDoorToNextLevel() {
        Rectangle playerRectangle = new Rectangle(player.getCharacterX(), player.getCharacterY(), player.getHEIGHT(), player.getWIDTH());
        GameData currentGd = gd.get(level);
        for (GameItem item : currentGd.tiles.values()) {
            if (item.isDoorToTheNextLevel) {
                for (Coord crd : item.coords) {
                    Rectangle itemRectangle = new Rectangle(crd.x, crd.y, item.height, item.width);
                    if (playerRectangle.getBoundsInParent().intersects(itemRectangle.getBoundsInParent())) {
                        logger.info("Player collided with door to the next level.");
                        level = item.nextLevel;
                        logger.info("Next level is " + level);
                        player.setCharacterX(item.nextX);
                        player.setCharacterY(item.nextY);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if the player character is near a touchable object and returns its filename if found.
     * A touchable object is an interactive game item that the player can interact with.
     *
     * @return The filename of the touchable object if the player is near one, null otherwise.
     */
    public String ifNearToTouchableObject() {
        Rectangle playerRectangle = new Rectangle(player.getCharacterX(), player.getCharacterY(), player.getHEIGHT(), player.getWIDTH());
        GameData currentGd = gd.get(level);
        for (GameItem item : currentGd.tiles.values()) {
            if (item.isTouchable) {
                for (Coord crd : item.coords) {
                    Rectangle itemRectangle = new Rectangle(crd.x + 10, crd.y + 20, item.height, item.width);
                    if (playerRectangle.getBoundsInParent().intersects(itemRectangle.getBoundsInParent())) {
                        return item.fileName;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Checks if the player character is near a touchable object and returns a pair indicating its proximity and coordinates.
     *
     * @return A pair where the first element indicates whether the player is near a touchable object (true) or not (false),
     * and the second element contains the coordinates of the touchable object if the player is near one, or null otherwise.
     */
    public Pair<Boolean, Coord[]> ifNearToTouchableObjectForText() {
        Rectangle playerRectangle = new Rectangle(player.getCharacterX(), player.getCharacterY(), player.getHEIGHT(), player.getWIDTH());
        GameData currentGd = gd.get(level);
        for (GameItem item : currentGd.tiles.values()) {
            if (item.isTouchable) {
                for (Coord crd : item.coords) {
                    Rectangle itemRectangle = new Rectangle(crd.x + 10, crd.y + 20, item.height, item.width);
                    if (playerRectangle.getBoundsInParent().intersects(itemRectangle.getBoundsInParent())) {
                        return new Pair<>(true, item.coords);
                    }
                }
            }
        }
        return new Pair<>(false, null);
    }

    /**
     * Simulates the action of collecting a key from a chest in the specified level.
     * If a chest containing a key is found in the level, it updates the chest's image and touchability status.
     *
     * @param levelName The name of the level where the key is collected.
     */
    public void collectKey(String levelName) {
        logger.info("Collecting key in level: " + levelName);
        GameData currentGd = gd.get(levelName);
        for (GameItem item : currentGd.tiles.values()) {
            if (item.fileName.equals("ChestWithKey.png")) {
                logger.info("Key found! Collecting...");
                item.fileName = "OpenEmptyChest.png";
                item.isTouchable = false;
                logger.info("Key collected successfully.");
            }
        }
    }

    /**
     * Simulates the action of opening closed chests in the specified level.
     * If closed chests are found in the level, it updates their images to indicate they contain keys
     * and sets their touchability status to true to allow interaction.
     *
     * @param levelName The name of the level where closed chests are opened.
     */
    public void openChests(String levelName) {
        logger.info("Opening chests in level: " + levelName);
        GameData currentGd = gd.get(levelName);
        for (GameItem item : currentGd.tiles.values()) {
            if (item.fileName.equals("ClosedChest.png")) {
                logger.info("Chest found! Opening...");
                item.fileName = "ChestWithKey.png";
                item.isTouchable = true;
                logger.info("Chest opened successfully.");

            }
        }
    }

    /**
     * Simulates the action of closing previously opened chests in the specified level.
     * If opened chests or chests containing keys are found in the level, it updates their images
     * to indicate they are closed and sets their touchability status to false to prevent further interaction.
     *
     * @param levelName The name of the level where opened chests are closed.
     */
    public void closeChest(String levelName) {
        logger.info("Closing chests in level: " + levelName);
        GameData currentGd = gd.get(levelName);
        for (GameItem item : currentGd.tiles.values()) {
            if (item.fileName.equals("OpenEmptyChest.png") || item.fileName.equals("ChestWithKey.png")) {
                logger.info("Chest found! Closing...");
                item.isTouchable = false;
                item.fileName = "ClosedChest.png";
                logger.info("Chest closed successfully.");
            }
        }
    }

    /**
     * Retrieves a specific game item from the game data of the specified level.
     * Searches for the item by its name within the game data of the specified level.
     *
     * @param levelName The name of the level from which to retrieve the game item.
     * @param itemName  The name of the game item to retrieve.
     * @return The game item with the specified name from the game data of the specified level,
     * or null if the level or the item does not exist.
     */
    public GameItem getItemFromGD(String levelName, String itemName) {
        logger.info("Retrieving item '" + itemName + "' from level: " + levelName);
        GameData currentGd = gd.get(levelName);
        if (currentGd != null && currentGd.tiles.containsKey(itemName)) {
            logger.info("Item '" + itemName + "' retrieved successfully.");
            return currentGd.tiles.get(itemName);
        } else {
            logger.warning("Item '" + itemName + "' not found in level: " + levelName);
        }
        return null;
    }

    /**
     * Simulates the action of opening closed doors in the specified level.
     * If closed doors are found in the level, it updates their images to indicate they are opened
     * and sets their collision status to false to allow passage through them.
     *
     * @param levelName The name of the level where closed doors are opened.
     */
    public void openDoors(String levelName) {
        logger.info("Opening doors in level: " + levelName);
        GameData currentGd = gd.get(levelName);
        for (GameItem item : currentGd.tiles.values()) {
            if (item.fileName.equals("ClosedDoor.png")) {
                logger.info("Door found! Opening...");
                item.fileName = "OpenedDoor.png";
                item.isCollision = false;
                logger.info("Door opened successfully.");
            }
        }
    }

    /**
     * Simulates unsetting the touching interaction with a specific game item in the specified level.
     * If the specified game item is found in the level, it sets its touchability status to false,
     * preventing further interaction with the item.
     *
     * @param levelName The name of the level where the interaction is unset.
     * @param itemName  The name of the game item with which the touching interaction is unset.
     */
    public void unsetTouching(String levelName, String itemName) {
        logger.info("Unsetting touching for item '" + itemName + "' in level: " + levelName);
        GameData currentGd = gd.get(levelName);
        for (GameItem item : currentGd.tiles.values()) {
            if (item.fileName.equals(itemName)) {
                logger.info("Item '" + itemName + "' found! Unsetting touching...");
                item.isTouchable = false;
                logger.info("Touchability unset for item '" + itemName + "'.");
            }
        }
    }

    /**
     * Checks if the player character has touched the portal leading to the end of the game.
     * It iterates over all game items in the current level and searches for the portal item.
     * If the portal is found and the player character intersects with it, the game is marked as ended.
     * The method stops searching after finding the portal to improve performance.
     */
    public void checkIfPortalTouched() {
        Rectangle playerRectangle = new Rectangle(player.getCharacterX(), player.getCharacterY(), player.getHEIGHT(), player.getWIDTH());
        GameData currentGd = gd.get(level);
        for (GameItem item : currentGd.tiles.values()) {
            if (item.isTheEnd) {
                for (Coord crd : item.coords) {
                    Rectangle itemRectangle = new Rectangle(crd.x, crd.y, item.height, item.width);
                    if (playerRectangle.getBoundsInParent().intersects(itemRectangle.getBoundsInParent())) {
                        logger.info("Portal touched!");
                        gameIsEnd = true;
                        break;
                    }
                }
            }
        }
    }

    /**
     * Checks the input from a button press and updates the sequence of button presses accordingly.
     * Adds the ID of the pressed button to the button sequence. If the button sequence exceeds a length of 4,
     * the oldest button press is removed to maintain a sequence length of 4.
     *
     * @param button_id The ID of the button pressed.
     */
    public void checkButton(int button_id) {
        buttonSequence.add(button_id);
        if (buttonSequence.size() == 4) {
            buttonSequence.removeFirst();
        }
    }

    /**
     * Checks if the player character has collected all the necessary keys to proceed.
     * Retrieves the player's inventory and checks if it contains both key IDs (1 and 2).
     *
     * @return true if the player has collected both key IDs, false otherwise.
     */
    public boolean checkIfKeyIsReady() {
        ArrayList<Integer> key = player.getInventory();
        return key.contains(1) && key.contains(2);
    }

    /**
     * Adds a key to the player's inventory based on the current level.
     * If the specified level is the second level, a key with ID 1 is added to the player's inventory.
     * If the specified level is the third level, a key with ID 2 is added to the player's inventory.
     *
     * @param levelName The name of the level where the key is obtained.
     */
    public void putInInventory(String levelName) {
        logger.info("Putting item into inventory from level: " + levelName);
        if (levelName.equals("secondLevel")) {
            logger.info("Adding item 1 to inventory.");
            player.getInventory().add(1);
            logger.info("Item 1 was added to inventory successfully.");
        }
        if (levelName.equals("thirdLevel")) {
            logger.info("Adding item 2 to inventory.");
            player.getInventory().add(2);
            logger.info("Item 2 was added to inventory successfully.");
        }
    }

    public void saveGame() throws IOException {
        logger.info("Saving game...");
        SavedGameDataPlayer savePlayer = new SavedGameDataPlayer();
        savePlayer.setSavedLevel(level);
        savePlayer.setSavedPlayerPositionX(player.getCharacterX());
        savePlayer.setSavedPlayerPositionY(player.getCharacterY());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File("saveGamePlayer.json"), savePlayer);
        logger.info("Player data saved successfully.");

        ObjectMapper objectMapper1 = new ObjectMapper();
        objectMapper1.writeValue(new File("saveGame.json"), gd);
        logger.info("Game data saved successfully.");
    }
}
