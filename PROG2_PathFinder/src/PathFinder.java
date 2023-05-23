import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Cursor;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.nio.Buffer;
import java.util.*;


public class PathFinder extends Application {
    //Class for testing and loading data
    //TestClass testClass = null;
    //ListGraph listGraph = testClass.getListGraph();
    private ListGraph graph = new ListGraph();
    URL graphUrl = PathFinder.class.getResource("europa.gif"); //URL = bakgrundsbild??
    File imageFile = new File(graphUrl.toString()); //Background image

    File graphFile = new File("europa.graph");
    Scene scene;
    private boolean changed = false;

    private boolean fromDestinationChosen;
    private boolean toDestinationChosen;
    private City[] selectedNodes = new City[2];

    public ListGraph getListGraph() {
        return graph;
    }

    private boolean unsavedChanges = false;

    MenuBar menuBar = new MenuBar();

    @Override
    public void start(Stage primaryStage) throws Exception {
        //for testing
        TestClass testClass = new TestClass();
        //graph = testClass.runTests();

        if (graph.getNodes().isEmpty()) {
            System.err.println("Data not loaded!");
        }

        if (graphUrl == null) {
            System.out.println("URL IS NULL!");
        } else {
            System.out.println("URL EXISTS!");
        }
        //Declare
        primaryStage.setTitle("PathFinder");
        BorderPane root = new BorderPane();
        Pane mainField = new Pane();
        Pane background = new Pane();
        FlowPane flow = new FlowPane();



        //cities
        City oslo = new City(500, 500, 10, Color.RED);
        City stockholm = new City(100, 20, 30, Color.RED);
        Pane cities = new Pane();
        cities.getChildren().addAll(oslo, stockholm);

        // Background
        File imageFile = new File(graphUrl.toString());
        Image image = new Image(imageFile.toString());
        ImageView imageView = new ImageView(image);
        background.getChildren().add(imageView);

        mainField.getChildren().addAll(background, cities);




        //Flow
        Button findPathB = new Button("Find Path");

        Button showConnectionB = new Button("Show Connection");

        Button newPlaceB = new Button("New Place");

        Button newConnectionB = new Button("New Connection");
        newConnectionB.setOnAction(event -> {
            openConnectionWindow();
        });

        Button changeConnectionB = new Button("Change Connection");

        flow.getChildren().addAll(findPathB, showConnectionB, newPlaceB, newConnectionB, changeConnectionB);
        flow.setAlignment(Pos.CENTER);
        flow.setHgap(10);

        //add nodes to root
//        root.getChildren().add(fileMenu());
//        root.getChildren().add(loadImage());


        //Set position in BorderPane
        root.setTop(fileMenu());
        root.setCenter(flow);
        root.setBottom(loadImage(imageFile));
        root.setBottom(mainField);


        BorderPane.setMargin(flow, new Insets(10, 0, 10, 0));
        //Show stage
        scene = new Scene(root);

        //Create cursor
        Cursor cursor = Cursor.CROSSHAIR;

        // change cursor when newPlace has been clicked
        newPlaceB.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent changeCursor) {
                // change the cursor
                scene.setCursor(Cursor.CROSSHAIR);
                newPlaceB.setDisable(true);
            }
        });



        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox fileMenu() {
        //A second one is created?
        //Create a menuBar and add it to the VBox to implement menuItems
        VBox vbox = new VBox();
        vbox.getChildren().add(menuBar);

        //Create menu for menu functionality
        Menu archiveMenu = new Menu("File");
        menuBar.getMenus().add(archiveMenu);

        //Adding menu items to the 'menu'
        MenuItem mapItem = new MenuItem("New Map");
        archiveMenu.getItems().add(mapItem);

        MenuItem openItem = new MenuItem("Open");
        archiveMenu.getItems().add(openItem);
        openItem.setOnAction(new OpenHandler());

        //WIP
        MenuItem saveItem = new MenuItem("Save");
        archiveMenu.getItems().add(saveItem);
        /*saveItem.setOnAction(event -> { //Saves all existing nodes to file -> europa.graph + (Screenshot)
            if (!graphFile.exists()) {
                graphFile = new File("europa.graph");
            }
            try {
                saveFile();
            } catch (IOException e) {
                System.err.println("Error: saving file");
            }
        }); //end of lambda expression*/
        saveItem.setOnAction(new SaveHandler());

        MenuItem imageItem = new MenuItem("Save Image");
        archiveMenu.getItems().add(imageItem);
        imageItem.setOnAction(event -> {
            //Get scene
            WritableImage result = new WritableImage((int) scene.getWidth(), (int) scene.getHeight());
            if (scene == null) {
                System.out.println("Error: scene is null!");
                return;
            }
            scene.snapshot(result);
            File outputFile = new File("capture.png");
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(result, null), "png", outputFile);
                System.out.println("Snapshot image saved: " + outputFile.getAbsolutePath());

            } catch (IOException e) {
                System.err.println("Error: problem when saving snapshot!");
            }
        });

        MenuItem exitItem = new MenuItem("Exit");
        archiveMenu.getItems().add(exitItem);
        exitItem.setOnAction(event -> {
            exitProgram();
        });

        return vbox;
    }

    //Make this generic, use parameter for path
    private Pane loadImage(File imageFile) {
        Image image = new Image(imageFile.toString());
        ImageView imageView = new ImageView(image);
        Pane mapPane = new Pane(imageView);
        return mapPane;
    }

    //Reads each line, splits it and creates new nodes based on parts
    private void readNodes(BufferedReader in) throws IOException { //Fixed!
        String text = in.readLine();
        while ((text = in.readLine()) != null) {
            if (!text.contains(";")) { //Hop over lines which don't contain ";"
                continue;
            }
            String[] parts = text.split(";");
            for (int i = 0; i < parts.length; i += 3) {
                String name = parts[i];
                float x = Float.parseFloat(parts[i + 1]);
                float y = Float.parseFloat(parts[i + 2]);
                City node = new City(x, y, 30, Color.BLUE);
                graph.add(node);
            }
        }
        in.close();

        System.out.println("Nodes: " + graph.getNodes());
    }

    private void saveFile() throws IOException {
        try (PrintWriter writer = new PrintWriter(graphFile)) { //'try with resource' -> autoclose 'writer'
            //writer.println("HELLO!");
            writer.println("File:" + graphFile);
            if (graph.getNodes().isEmpty()) {
                System.err.println("Graph is empty!");
            }
            System.out.println("Print nodes!");
            writer.println(printNodes()); //writes out node.toString()
            //writer.println(printConnections()); //writes out edges, disabled for testing readNodes()
        }
    }

    private Alert createAlertConf(String title) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText("There are unsaved changes in the project");
        alert.setContentText("Save changes?");

        ButtonBar buttonBar = new ButtonBar();
        ButtonType confirmButton = new ButtonType("confirm");
        ButtonType cancelButton = new ButtonType("cancel", ButtonBar.ButtonData.CANCEL_CLOSE); //ButtonData = enums for opertations

        buttonBar.getButtons().add(alert.getDialogPane());
        buttonBar.setPadding(new Insets(0, 0, 0, 50));
        //alert.getDialogPane().setPadding(new Insets(0, 0, 0, 0));

        return alert;
    }

    private void setNodes() {

    }

    class SaveHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            try {
                saveFile();

            } catch (IOException e) {
                System.err.println("Error: saving file");
            }
        }
    }

    //Open button handler
    class OpenHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) { //Fix: dialogue layout
            if (!graphFile.exists()) {
                System.out.println("Error: File doesnt exist!");
            }
            //Confirmation alert
            Alert alert = createAlertConf("Unsaved changes");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get().getText().equals("confirm")) {
                try {
                    saveFile();
                } catch (IOException e) {
                    System.err.println("Error: saving file");
                }
            }


            //readNodes(), drawNodes(), loadImage()
            try {
                FileReader fr = new FileReader(graphFile);
                BufferedReader in = new BufferedReader(fr);
                readNodes(in); //fixed
                loadImage(imageFile);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String printNodes() {
        StringBuilder sb = new StringBuilder();
        for (Object city : graph.getNodes()) {
            sb.append(city).append(";");
        }
        return sb.toString();
    }

    private String printConnections() {
        return graph.printConnections();
    }

    private void exitProgram() {
        if (unsavedChanges) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Unsaved Changes");
            alert.setTitle("There are unsaved changes, do you wish to save before exiting?");
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.YES) {
                saveChanges();
            } else if (result.get() == ButtonType.NO) {
                unsavedChanges = false;
            } else {
                return;
            }
        }
        Platform.exit();
    }

    public void saveChanges() {
        unsavedChanges = false;
        Platform.exit();
    }

    private void openConnectionWindow() {
            /*if (getListGraph().pathExists() {
                showErrorMessage("Select two destinations, please.");
                    return;
                }*/

        if (connectionExist(selectedNodes[0], selectedNodes[1])) {
            showErrorMessage("Connection already exist between the two destinations.");
            return;
        }

        javafx.scene.control.Dialog<Boolean> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("New Connection");
        dialog.setHeaderText("Create new connection between " + selectedNodes[0].getName() + " and " + selectedNodes[1].getName());

        javafx.scene.control.TextField nameField = new javafx.scene.control.TextField();
        javafx.scene.control.TextField timeField = new javafx.scene.control.TextField();

        ButtonType okButton = new ButtonType("ok");
        dialog.getDialogPane().setContent(new HBox(10, nameField, timeField));
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        dialog.setResultConverter(ButtonType -> {
            String name = nameField.getText();
            String time = timeField.getText();
            CreateConnection(name, time);
            if (okButton == ButtonType.OK) {
                if (name.isEmpty() || !time.matches("\\d+")) {
                    showErrorMessage("Input is not valid. Name cannot be empty.");
                    return false;
                }

                //createConnection(name, Integer.parseInt(time));
                return true;
            }

            return false;
        });

        dialog.showAndWait();
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //Checks if connection exists
    private boolean connectionExist(City fromDestination, City toDestination) {
        if (getListGraph().getEdgeBetween(fromDestination, toDestination) != null) {
            return true;
        }
        return false;
    }

    private void CreateConnection(String name, String time) {
        graph.connect(selectedNodes[0], selectedNodes[1], name, Integer.parseInt(time));
        //draw connection


    }

}






