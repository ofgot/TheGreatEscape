package org.game.thegreatescape.view;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;
import org.game.thegreatescape.model.*;
import org.game.thegreatescape.model.Character;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The GameSceneFactory class is responsible for creating and managing the game scene,
 * including player interaction, rendering, and game logic.
 * It provides methods to generate the main game scene, handle keyboard input,
 * render game elements, and manage end-of-game scenarios.
 */
public class GameSceneFactory {
    Logger logger = Logger.getLogger(Game.class.getName());

    // Constants
    private final int WIDTH = 64 * 12; // 768 64 * 12
    private final int HEIGHT = 64 * 9; // 576 64 * 9
    private final int WIDTH_CANVAS = 64 * 19; // 768 64 * 12
    private final int HEIGHT_CANVAS = 64 * 12; // 576 64 * 9

    // Initializing classes
    private final Stage stage;
    private final Canvas canvas = new Canvas(WIDTH, HEIGHT);
    private final GraphicsContext gc = canvas.getGraphicsContext2D();
    private Model model;
    StackPane root;
    Translate cameraTranslate;
    private AnimationTimer timer;
    private long startTime;
    private Stage panelStage;
    AnimationTimer animationTimer;

    /**
     * Set the size of the canvas used for rendering the game scene.
     *
     * @param x The new width of the canvas.
     * @param y The new height of the canvas.
     */
    public void setCanvas(int x, int y) {
        canvas.setWidth(x);
        canvas.setHeight(y);
    }

    /**
     * Constructor of a GameSceneFactory with the specified Stage.
     *
     * @param stage The primary stage of the application.
     */
    public GameSceneFactory(Stage stage) {
        this.stage = stage;
        logger.info("GameSceneFactory instance created.");
    }


    /**
     * Get the game scene for the specified level.
     * This method constructs and returns the game scene for the specified level,
     * initializing the model, root node, and canvas, and setting up event handlers
     * for keyboard input to control the game character.
     *
     * @return The generated Scene object for the game.
     */
    public Scene getGameScene(Model gameModel) {
        // Specifies Scene
        model = gameModel;
        root = new StackPane(canvas);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().add("styles.css");
        scene.setFill(Color.BLACK);

        stage.setOnCloseRequest(windowEvent -> {
            windowEvent.consume();
            exitGame();
            logger.info("Stage closed.");
        });

        //********************************************* control character ********************************************//

        animationTimer = new AnimationTimer() {

            // print room and everything
            @Override
            public void handle(long l) {

                model.update();
                render();
                if (model.isClosePanel()) {
                    if (panelStage != null && panelStage.isShowing()) {
                        panelStage.close();
                        model.openChests(model.getLevel());
                        model.openDoors("secondLevel");
                    }
                }
                if (model.isGameIsEnd()) {
                    logger.info("Game ended.");
                    endOfGame();
                    animationTimer.stop();
                }
                Pair<Boolean, Coord[]> result = model.ifNearToTouchableObjectForText();
                if (result.getKey()) {
                    for (Coord crd : result.getValue()) {
                        gc.setStroke(Color.WHITE);
                        gc.setLineWidth(0.8);
                        gc.strokeText("Press F", crd.x, crd.y - 10);
                    }
                }

            }
        };
        animationTimer.start();


        // keyboard pressed
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.D) {
                    model.goRight();
                }
                if (keyEvent.getCode() == KeyCode.W) {
                    model.goUp();
                }
                if (keyEvent.getCode() == KeyCode.S) {
                    model.goDown();
                }
                if (keyEvent.getCode() == KeyCode.A) {
                    model.goLeft();
                }
                if (keyEvent.getCode() == KeyCode.F) {
                    if (model.ifNearToTouchableObject() != null) {
                        switch (model.ifNearToTouchableObject()) {
                            case "panel.png":
                                logger.info("Player pressed a panel.");
                                setScenePanel();
                                break;
                            case "ChestWithKey.png":
                                logger.info("Player pressed a chest with the key.");
                                model.collectKey(model.getLevel());
                                model.putInInventory(model.getLevel());
                                break;
                            case "craftingTable.png":
                                logger.info("Player pressed a crafting table.");
                                if (model.checkIfKeyIsReady()) {
                                    logger.info("Player has two part of the key.");
                                    printForThreeSecond("Door to the fourth room is open now");
                                    model.openDoors("thirdLevel");
                                } else {
                                    logger.info("Player does not have two part of the key.");
                                    printForThreeSecond("You need two half of the key first");
                                }
                                break;
                            case "button.png":
                                logger.info("Player pressed button.");
                                model.openChests("thirdLevel");
                                logger.info("Timer set.");
                                setTimer();
                                printForThreeSecond("Chest was opened in the third room. Get it before time runs out !!!");
                                break;
                        }
                    }
                }
            }
        });

        // keyboard released
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.D) {
                    model.getPlayer().stayRight();
                }
                if (keyEvent.getCode() == KeyCode.W) {
                    model.getPlayer().stayUp();
                }
                if (keyEvent.getCode() == KeyCode.A) {
                    model.getPlayer().stayLeft();
                }
                if (keyEvent.getCode() == KeyCode.S) {
                    model.getPlayer().stayDown();
                }
            }
        });

        return scene;
    }

    /**
     * Renders the game elements onto the canvas.
     * This method clears the canvas, fills it with a background color, adjusts the canvas size
     * based on the current game level, and renders the game elements such as tiles and the player character.
     */
    private void render() {
        Character player = model.getPlayer();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

//        root.getChildren().addAll(blackBackground);

        if (model.getLevel().equals("thirdLevel")) {
            setCanvas(WIDTH_CANVAS, HEIGHT_CANVAS);
            updateCameraPosition();
        } else {
            root.getTransforms().removeAll(cameraTranslate);
            setCanvas(WIDTH, HEIGHT);
        }

        for (String key : model.getGd().tiles.keySet()) {
            printRoom(gc, model.getGd().tiles.get(key));
//            checkCollision(gc, model.getGd().tiles.get(key));
        }

//        gc.fillRect(player.getCharacterX(), player.getCharacterY(), player.getWIDTH(), player.getHEIGHT());
        gc.drawImage(player.getImage(), player.getCharacterX(), player.getCharacterY(), player.getWIDTH(), player.getHEIGHT());
        if (model.getLevel().equals("thirdLevel")) {
            Image img = new Image("dark.png");
            gc.drawImage(img, model.getPlayer().getCharacterX() - 574, model.getPlayer().getCharacterY() - 518, 1200, 1000);
        }

    }

    /**
     * Draw room elements
     *
     * @param gc       graphic context.
     * @param gameItem gameItem from model.
     */
    private void printRoom(GraphicsContext gc, GameItem gameItem) {
        String fileName = gameItem.fileName;
        int width = gameItem.width;
        int height = gameItem.height;
        for (Coord crd : gameItem.coords) {
            gc.drawImage(new Image(fileName), crd.x, crd.y, width, height);
        }
    }

    /**
     * Updates the canvas position to focus on the player character.
     * This method calculates the new position for the canvas based on the current position
     * of the player character.
     */
    private void updateCameraPosition() {
        int playerX = model.getPlayer().getCharacterX();
        int playerY = model.getPlayer().getCharacterY();

        double cameraX = playerX - (WIDTH / 1.3);
        double cameraY = playerY - (HEIGHT / 1.5);

        cameraTranslate = new Translate(-cameraX, -cameraY);

        root.getTransforms().setAll(cameraTranslate);
    }

    /**
     * Sets up the scene panel with buttons.
     * Player have to press them in the right order.
     */
    private void setScenePanel() {
        Button first = new Button("Button 1");
        Button second = new Button("Button 2");
        Button third = new Button("Button 3");

        Text text = new Text("Find secret combination");
        text.setStyle("-fx-fill: white; -fx-font-size: 18px;");

        HBox controllerPanel = new HBox();
        controllerPanel.setSpacing(10);
        controllerPanel.setAlignment(Pos.CENTER);
        controllerPanel.setMinSize(250, 70);
        controllerPanel.getChildren().addAll(first, second, third);

        VBox vBox = new VBox(text, controllerPanel);
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle("-fx-background-color: black; -fx-padding: 10px 0 0 0");

        Scene panelscene = new Scene(vBox);
        panelStage = new Stage();
        panelStage.setScene(panelscene);
        controllerPanel.getStylesheets().add("styles.css");

        first.setOnAction(e -> {
            model.checkButton(1);
        });
        second.setOnAction(e -> {
            model.checkButton(2);
        });
        third.setOnAction(e -> {
            model.checkButton(3);
        });

        panelStage.show();
        logger.log(Level.INFO, "Scene panel set up successfully.");
    }

    /**
     * Prints a message on the canvas for three seconds with given message.
     *
     * @param message The message to be displayed on the canvas.
     */
    public void printForThreeSecond(String message) {
        long startTime1 = System.nanoTime();

        AnimationTimer timer1 = new AnimationTimer() {
            @Override
            public void handle(long l) {
                long elapsedTimeSec = (l - startTime1) / 1_000_000_000;
                long remainingTime = Math.max(3 - elapsedTimeSec, 0);

                if (remainingTime > 0) {
                    gc.setStroke(Color.WHITE);
                    gc.setLineWidth(1);
                    gc.strokeText(message, 32, 32);
                } else {
                    stop();
                }
            }
        };
        timer1.start();
        logger.info("Message displayed for three seconds: " + message);
    }

    /**
     * Sets up and starts a timer for 60 seconds.
     * It manages the opening and closing the chests.
     * If timer is run out, method check if player had time to grab the key in fourth level and closes chest if not.
     */
    public void setTimer() {
        if (timer == null) {
            startTime = System.nanoTime();
            timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    long elapsedTimeSec = (now - startTime) / 1_000_000_000;
                    long remainingTime = Math.max(60 - elapsedTimeSec, 0);

//                    model.openChests("thirdLevel");

                    drawTime(remainingTime);
                    if (remainingTime == 0) {
                        logger.info("Timer is over.");
                        logger.info("Check if chest on the third level is not collected.");
                        if (model.getItemFromGD("thirdLevel", "Chest").fileName.equals("ChestWithKey.png")) {
                            logger.info("Key on the third level was not collected.");
                            model.closeChest("thirdLevel");
                        }

                        logger.info("Check if key on the third level was collected.");
                        if (model.getItemFromGD("thirdLevel", "Chest").fileName.equals("OpenEmptyChest.png")) {
                            logger.info("Key on the third level was collected.");
                            model.unsetTouching("secondLevel", "button.png");
                            logger.info("Button unset.");
                        }

                        timer = null;
                        stop();
                    }
                }
            };
            timer.start();
            logger.info("Timer started successfully.");
        }

    }

    /**
     * Draws the remaining time in seconds on the canvas on the top of the player character.
     * Additional function to setTimer();
     *
     * @param remainingTime The remaining time in seconds to be displayed.
     */
    private void drawTime(long remainingTime) {
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(1);
        double xch = model.getPlayer().getCharacterX();
        double ych = model.getPlayer().getCharacterY();

        gc.strokeText("Time: " + remainingTime, xch, ych - 10);
    }


    /**
     * Displays the end-of-game screen with congratulatory message and options to return to the main menu or exit the game.
     */
    private void endOfGame() {
        VBox endGame = new VBox();
        endGame.setAlignment(Pos.CENTER);
        endGame.setSpacing(20);

        Text congratulationsText = new Text("Congratulation!");
        congratulationsText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 100));
        congratulationsText.setFill(Color.WHITE);
        congratulationsText.setStroke(Color.YELLOW);
        congratulationsText.setStyle("-fx-effect: dropshadow( three-pass-box , #FFFF00 , 10, 0.0 , 0 , 1 );");

        Button returnButton = new Button("Back to main menu");
        returnButton.setPrefSize(200, 50);
        returnButton.setStyle("-fx-font-size: 20px;");

        Button exitButton = new Button("Exit");
        exitButton.setPrefSize(100, 50);
        exitButton.setStyle("-fx-font-size: 20px;");

        returnButton.setOnAction((ActionEvent event) -> {
            MainMenuFactory n = new MainMenuFactory(stage);
            stage.setScene(n.getMainMenu());
            logger.info("Returned to the main menu.");
        });

        exitButton.setOnAction((ActionEvent event) -> {
            stage.close();
            logger.info("Game exited.");
        });

        HBox buttonBox = new HBox(returnButton, exitButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(20);

        endGame.getChildren().addAll(congratulationsText, buttonBox);

        endGame.setStyle("-fx-background-color: #000000");


        Scene endscene = new Scene(endGame, WIDTH, HEIGHT);
        endscene.getStylesheets().add("styles.css");

        stage.setScene(endscene);
        logger.info("EndOfGame screen displayed successfully.");
    }

    private void exitGame() {
        Stage exitStage = new Stage();

        VBox last = new VBox();
        last.setAlignment(Pos.CENTER);
        last.setSpacing(20);

        Text confirmationText = new Text("Are you sure?");
        confirmationText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
        confirmationText.setFill(Color.WHITE);

        // Buttons "Yes" and "No"
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        // VBox for buttons
        HBox buttonsHBox = new HBox(10); // spacing between buttons
        buttonsHBox.setAlignment(Pos.CENTER);
        buttonsHBox.setSpacing(20);
        buttonsHBox.getChildren().addAll(yesButton, noButton);

        last.getChildren().add(confirmationText);
        last.getChildren().add(buttonsHBox);

        // StackPane for centering elements
        StackPane exit = new StackPane();
        exit.setStyle("-fx-background-color: black; -fx-border-color: #64458a");
        exit.setAlignment(Pos.CENTER);
        exit.getChildren().addAll(last);

        yesButton.setOnAction((ActionEvent event) -> {
            try {
                model.saveGame();
                logger.info( "Game saved and returned to main menu.");
            } catch (IOException e) {
                logger.severe( "Error occurred while saving game: " + e);
                throw new RuntimeException(e);
            }

            MainMenuFactory n = new MainMenuFactory(stage);
            stage.setScene(n.getMainMenu());
            exitStage.close();
        });

        noButton.setOnAction((ActionEvent event) -> {
            exitStage.close();
            logger.info( "Exit canceled.");
        });



        // Scene
        Scene scene = new Scene(exit, 300, 200);
        scene.getStylesheets().add("styles.css");

        exitStage.initStyle(StageStyle.UNDECORATED);
        exitStage.setScene(scene);
        exitStage.show();
        logger.info("Exit confirmation dialog displayed.");
    }

//    private void checkCollision(GraphicsContext gc, GameItem gameItem) {
//        String[] collisionFileNames = {"wall1.png", "LeftWall.png", "RightWall.png", "UpperWall.png", "BottomWall.png",
//                "LeftBottomCorner.png", "LeftUpperCorner.png", "RightBottomCorner.png", "RightUpperCorner.png"};
//
//        String name = gameItem.fileName;
//
//        if (Arrays.asList(collisionFileNames).contains(name)) {
//            for (Coord crd : gameItem.coords) {
//                Rectangle square = new Rectangle(crd.x, crd.y, 64, 64);
//                square.setFill(Color.TRANSPARENT.invert());
//                gc.fillRect(crd.x, crd.y, 64, 64);
//            }
//        }
//    }

}
