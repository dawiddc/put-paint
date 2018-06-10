package org.dawiddc.paint.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.dawiddc.paint.controller.utils.ImageService;
import org.dawiddc.paint.controller.utils.PropertyService;
import org.dawiddc.paint.model.Canvas;
import org.dawiddc.paint.model.plugin.Plugin;
import org.dawiddc.paint.model.plugin.PluginLoader;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class MainPaneController {

    @FXML
    private ToggleButton rectangleDrawButton;
    @FXML
    private ChoiceBox<Integer> drawSizeChoiceBox;
    @FXML
    private ImageView imageView;
    @FXML
    private MenuItem closeAppMenuItem;
    @FXML
    private MenuItem englishLangMenuItem;
    @FXML
    private Button openButton;
    @FXML
    private MenuItem polishLangMenuItem;
    @FXML
    private ToggleButton lineDrawButton;
    @FXML
    private Button redoButton;
    @FXML
    private Menu languageMenu;
    @FXML
    private HBox pluginsToolBox;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private MenuItem loadImageMenuItem;
    @FXML
    private Menu fileMenu;
    @FXML
    private Button undoButton;

    private Plugin[] plugins;
    private Properties properties;
    private Point lineBeginPoint;
    private Point rectangleBeginPoint;
    private Canvas canvas;


    @FXML
    public void initialize() {
        loadPlugins();
        loadDefaultProperty();

        initMenu();
        initToolbox();
        initImageView();
        setImageViewFromFile("sampleAssets\\empty.png");
    }

    private void loadPlugins() {

        plugins = null;

        try {
            plugins = PluginLoader.initAsPlugin(PluginLoader.loadPlugins("plugins", "config.cfg"));
        } catch (InstantiationException | ClassNotFoundException | IllegalAccessException | IOException e) {
            e.printStackTrace();
        }

        createPluginsButtons();

    }

    private void createPluginsButtons() {
        Arrays.stream(plugins).forEach(plugin -> {
            Button pluginButton = new Button(plugin.getName());
            pluginsToolBox.getChildren().add(pluginButton);
            pluginButton.setOnAction(event -> {
                Mat mat = plugin.run(canvas.getActualImage());
                if (mat != null) {
                    canvas.updateHistory(mat);
                    updateImageView();
                }
            });
        });
    }

    private void updateImageView() {
        ImageService imageService = new ImageService();
        imageView.setImage(imageService.convertToImage(canvas.getActualImage()));
    }

    private void loadDefaultProperty() {
        PropertyService propertyService = new PropertyService();
        properties = propertyService.loadFromFile("properties\\default.properties");
        updateLabels();
    }

    private void updateLabels() {
        fileMenu.setText(properties.getProperty("file-menu", "File"));
        languageMenu.setText(properties.getProperty("language-menu", "Language"));
        closeAppMenuItem.setText(properties.getProperty("close-menu", "Close"));
    }

    private void initMenu() {
        initLoadImageMenu();
        initCloseMenu();
        initEditMenu();
    }

    private void initLoadImageMenu() {
        loadImageMenuItem.setOnAction(event -> setImageViewFromFile());
    }

    private void setImageViewFromFile() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG, PNG", "*.png", "*.jpg"));
        File file = fc.showOpenDialog(new Stage());
        if (file != null && file.exists()) {
            setImageViewFromFile(file.getAbsolutePath());
        }
    }

    private void setImageViewFromFile(String path) {
        ImageService imageService = new ImageService();
        Mat matrix = imageService.loadMatrix(path);
        Image image = imageService.convertToImage(matrix);
        canvas = new Canvas(matrix);
        imageView.setImage(image);
        imageView.setFitWidth(image.getWidth());
        imageView.setFitHeight(image.getHeight());
    }

    private void initCloseMenu() {
        closeAppMenuItem.setOnAction(e -> System.exit(0));
    }

    private void initEditMenu() {
        initEnglishMenuItem();
        initPolishMenuItem();
    }

    private void initEnglishMenuItem() {
        englishLangMenuItem.setOnAction(e -> {
            PropertyService propertyService = new PropertyService();
            properties = propertyService.loadFromFile("properties\\english.properties");
            updateLabels();
        });
    }

    private void initPolishMenuItem() {
        polishLangMenuItem.setOnAction(e -> {
            PropertyService propertyService = new PropertyService();
            properties = propertyService.loadFromFile("properties\\polish.properties");
            updateLabels();
        });
    }

    private void initToolbox() {
        openButton.setOnAction(e -> setImageViewFromFile());
        undoButton.setOnAction(e -> {
            canvas.undo();
            updateImageView();
        });
        redoButton.setOnAction(e -> {
            canvas.redo();
            updateImageView();
        });
        lineDrawButton.setOnAction(e -> rectangleDrawButton.setSelected(false));
        rectangleDrawButton.setOnAction(e -> lineDrawButton.setSelected(false));
        colorPicker.setValue(Color.RED);
        drawSizeChoiceBox.getItems().addAll(1, 2, 3, 5, 8, 12, 18, 26);
        drawSizeChoiceBox.setValue(2);
    }

    private void initImageView() {
        imageView.setOnMousePressed(e -> {
            if (lineDrawButton.isSelected())
                writeLineCoordinates(e);
            if (rectangleDrawButton.isSelected())
                writeRectangleCoordintes(e);
        });

        imageView.setOnMouseReleased(e -> {
            if (lineDrawButton.isSelected())
                drawFigure(e, "line");

            if (rectangleDrawButton.isSelected())
                drawFigure(e, "rectangle");
        });
    }

    private void writeLineCoordinates(MouseEvent e) {
        lineBeginPoint = new Point(e.getX(), e.getY());
    }

    private void writeRectangleCoordintes(MouseEvent e) {
        rectangleBeginPoint = new Point(e.getX(), e.getY());

    }

    private void drawFigure(MouseEvent e, String figureType) {
        Point figureEndPoint = new Point(e.getX(), e.getY());
        Mat figureMat = canvas.getActualImage().clone();
        Color color = colorPicker.getValue();
        Scalar scalar = new Scalar((int) (color.getBlue() * 255), (int) (color.getGreen() * 255), (int) (color.getRed() * 255));
        int size = drawSizeChoiceBox.getValue();
        if (figureType.equals("line")) {
            Imgproc.line(figureMat, lineBeginPoint, figureEndPoint, scalar, size);
        } else {
            Imgproc.rectangle(figureMat, rectangleBeginPoint, figureEndPoint, scalar, size);

        }
        canvas.updateHistory(figureMat);
        updateImageView();
    }
}
