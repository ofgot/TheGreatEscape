package org.game.thegreatescape.levelEditor;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * The LevelEditor class is an application for creating and editing levels
 * using a graphical interface. It allows users to place and delete images
 * on a canvas, and save the created level data as a JSON file.
 */

public class LevelEditor extends Application {
    Logger logger = Logger.getLogger(LevelEditor.class.getName());

    // Constants for sizes
    private final int WIDTH_MAIN = 64 * 12; // 768 = 64 * 12
    private final int HEIGHT_MAIN = 64 * 9; // 576 = 64 * 9
    private final int WIDTH_ASIDE = 75;
    private final int HEIGHT_ASIDE = 640;
    private final int ASIDE_ITEMS_SIZE = 64;
    private final int IMAGE_SIZE = 64;
    private final int LINE_APPEAR = 8;
    // Styles
    private final String stl = "-fx-background-color: grey";

    // Data
    private final ArrayList<String> fileNames = new ArrayList<String>();
    private Image selectedImage;
    private String selectedImageName;

    // Classes
    private final CanvasData canvasData = new CanvasData();
    private final JsonfileCreate jfc = new JsonfileCreate();

    /**
     * Starts the application by initializing and displaying the item stage
     * and canvas stage.
     *
     * @param stage The primary stage of the application.
     */
    @Override
    public void start(Stage stage) {
        logger.info("Starting LevelEditor application...");
        itemStage();
        canvasStage();
    }

    /**
     * Initializes and displays the aside item stage, containing image items for selection.
     */
    private void itemStage() {
        logger.info("Initializing and displaying aside item stage...");
        Stage fisrtStage = new Stage();
        Scene firstScene = new Scene(asideItemPane());
        fisrtStage.setResizable(false);
        fisrtStage.setX(200);
        fisrtStage.setY(75);

        fisrtStage.setScene(firstScene);
        fisrtStage.show();
    }

    /**
     * Initializes and displays the canvas stage, containing the canvas area for level editing.
     */
    private void canvasStage() {
        logger.info("Initializing and displaying canvas stage...");
        Stage secondStage = new Stage();
        Scene secondScene = new Scene(canvasRedactor());
        secondStage.setResizable(false);

        secondStage.setScene(secondScene);
        secondStage.show();
    }

    /**
     * Creates the canvas area for level editing and buttons below for
     * placing, deleting, and saving level data.
     *
     * @return The root pane containing the canvas area and buttons.
     */
    private Pane canvasRedactor() {
        logger.info("Creating canvas area for level editing...");
        // Canvas
        Canvas canvas = new Canvas(WIDTH_MAIN, HEIGHT_MAIN);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        writeLines(gc);
        logger.info("lines were written.");

        // Buttons
        Button putButton = new Button("Put");
        Button deleteButton = new Button("Delete");
        Button saveButton = new Button("Create");

        // Action for placing images from canvas
        putButton.setOnAction(actionEvent -> {
            logger.info("put button was pressed.");
            canvas.setOnMouseClicked(mouseEvent -> {
                if (selectedImage != null) {
                    double x = mouseEvent.getX();
                    double y = mouseEvent.getY();

                    double nearestX = Math.floor(x / 8) * 8;
                    double nearestY = Math.floor(y / 8) * 8;

                    logger.info("Placing image at coordinates: x = " + nearestX + ", y = " + nearestY);

                    canvasData.addImage(new ImageData(selectedImageName, nearestX, nearestY, 64, 64));
                    gc.drawImage(selectedImage, nearestX, nearestY, IMAGE_SIZE, IMAGE_SIZE);
                }
            });
        });

        // Action for deleting images from canvas
        deleteButton.setOnAction(actionEvent -> {
            logger.info("Delete button was pressed.");

            canvas.setOnMouseClicked(mouseEvent -> {
                double x = mouseEvent.getX();
                double y = mouseEvent.getY();

                double nearestX = Math.floor(x / 8) * 8;
                double nearestY = Math.floor(y / 8) * 8;

                if (canvasData.removeImage(nearestX, nearestY)) {
                    logger.info("Deleted image at coordinates: x=" + nearestX + ", y=" + nearestY);

                    gc.clearRect(0, 0, WIDTH_MAIN, HEIGHT_MAIN);
                    writeLines(gc);
                    for (ImageData img : canvasData.getImagesList()) {
                        gc.drawImage(new Image(img.getName()), img.getX(), img.getY(), img.getImgHeight(), img.getImgWidth());
                    }
                }
            });
        });

        // Action for saving level data
        saveButton.setOnAction(actionEvent -> {
            logger.info("Saving level data...");
            jfc.createJsonFile(jfc.toString(canvasData.getImagesList())); //, ArrayList<imageData> imgList
        });


        // Layout
        BorderPane root = new BorderPane();
        root.setTop(canvas);

        HBox buttonsBox = new HBox(15);
        buttonsBox.setMinHeight(50);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.getChildren().addAll(putButton, deleteButton, saveButton);
        root.setBottom(buttonsBox);

        return root;
    }

    /**
     * Creates the aside item pane containing image items for selection.
     *
     * @return The root pane containing the aside item pane.
     */
    private Pane asideItemPane() {
        logger.info("Creating aside item pane...");
        // Vbox
        VBox vbox = new VBox();
        vbox.setStyle(stl);
        vbox.setMaxSize(WIDTH_ASIDE, HEIGHT_ASIDE);
        vbox.setSpacing(20);

        // Read png names from resources directory
        logger.info("Reading PNG files from the resources directory...");
        readFile();
        for (String name : fileNames) {
            Image img = new Image(name);
//            System.out.println(img.getHeight());
            ImageView imageView = new ImageView(img);

            imageView.setFitHeight(ASIDE_ITEMS_SIZE);
            imageView.setFitWidth(ASIDE_ITEMS_SIZE);
            vbox.getChildren().add(imageView);

            imageView.setOnMouseClicked(event -> {
                selectedImage = img;
                selectedImageName = name;
                logger.info("Selected image: " + selectedImageName);
            });
        }


        // Scroll pane for images
        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setMaxSize(WIDTH_ASIDE, HEIGHT_ASIDE);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Stack pane
        StackPane root = new StackPane(scrollPane);
        root.setAlignment(scrollPane, Pos.CENTER_LEFT);
        root.setMaxSize(WIDTH_ASIDE, HEIGHT_ASIDE);

        logger.info("Aside item pane created.");

        return root;
    }

    /**
     * Reads PNG files from the resources directory and adds their names to the fileNames list.
     */
    public void readFile() {
        logger.info("Reading PNG files from the resources directory...");

        File f = new File("src/main/resources/");
        for (String name : f.list()) {
            if (name.contains(".png") && !name.equals("MainGameCover.png")) {
                fileNames.add(name);
                logger.fine("Added file: " + name);
            }
        }

        logger.info("PNG files reading completed.");
    }

    /**
     * Draws grid lines on the canvas.
     *
     * @param gc The graphics context of the canvas.
     */
    public void writeLines(GraphicsContext gc) {

        for (int x = 0; x <= WIDTH_MAIN; x += LINE_APPEAR) {
            gc.strokeLine(x, 0, x, HEIGHT_MAIN);
        }

        for (int y = 0; y <= HEIGHT_MAIN; y += LINE_APPEAR) {
            gc.strokeLine(0, y, WIDTH_MAIN, y);
        }

    }

    /**
     * Main method to launch the application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        launch(args);
    }
}
