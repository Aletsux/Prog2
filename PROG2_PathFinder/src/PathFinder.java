import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import org.junit.platform.engine.TestEngine;
import org.junit.platform.engine.support.descriptor.FileSystemSource;

import javax.swing.*;
import java.awt.*;
import java.awt.Dialog;
import java.io.*;
import java.net.URL;
import java.nio.Buffer;
import java.util.*;
import java.util.List;
import java.util.logging.Handler;

public class PathFinder extends Application {
    //Class for testing and loading data
    //TestClass testClass = null;
    //ListGraph listGraph = testClass.getListGraph();
    private ListGraph graph = new ListGraph();
    URL graphUrl = PathFinder.class.getResource("europa.gif"); //URL = bakgrundsbild??
    File file = new File(graphUrl.toString()); //Background image
    File graphFile = new File("europa.graph");
    private boolean changed = false;

    public ListGraph getListGraph() {
        return graph;
    }

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
        FlowPane flow = new FlowPane();
        HBox cities = new HBox();


        //cities
        City oslo = new City(100, 50, 30, Color.RED);
        City stockholm = new City(100, 50, 30, Color.RED);


        //Flow
        Button findPathB = new Button("Find Path");
        Button showConnectionB = new Button("Show Connection");
        Button newPlaceB = new Button("New Place");
        Button newConnectionB = new Button("New Connection");
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
        root.setBottom(loadImage(file));


        BorderPane.setMargin(flow, new Insets(10, 0, 10, 0));
        //Show stage
        Scene scene = new Scene(root);

        //Create cursor
        Cursor cursor = Cursor.CROSSHAIR;

        // change cursor when newPlace has been clicked
        newPlaceB.setOnAction(new EventHandler<ActionEvent>()

                {
                    @Override
                    public void handle(ActionEvent changeCursor)
                    {
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
        MenuBar menuBar = new MenuBar();
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

        MenuItem exitItem = new MenuItem("Exit");
        archiveMenu.getItems().add(exitItem);

        return vbox;
    }

    //Make this generic, use parameter for path
    private Label loadImage(File file) {
        Label label = new Label();
        Image image = new Image(file.toString());
        ImageView imageView = new ImageView(image);
        label.setGraphic(imageView);
        return label;
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

            //Load nodes from files
            try {
                FileReader fr = new FileReader(graphFile);
                BufferedReader in = new BufferedReader(fr);
                readNodes(in); //* not yet tested
                loadImage(file);

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
}
