import javafx.application.Application;
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
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.junit.platform.engine.support.descriptor.FileSystemSource;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Handler;

public class PathFinder extends Application {
    //Class for testing and loading data
    TestClass testClass = new TestClass();
    ListGraph listGraph = testClass.getListGraph();
    URL graphUrl = PathFinder.class.getResource("europa.gif"); //URL = bakgrundsbild??
    File file = new File(graphUrl.toString()); //Background image

    File graphFile = new File("europa.graph");

    @Override
    public void start(Stage primaryStage) throws Exception {
        testClass.runTests();

        if (listGraph.getNodes().isEmpty()) {
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
        //root.getChildren().add(fileMenu());
        //root.getChildren().add(loadImage());

        //Set position in BorderPane
        root.setTop(fileMenu());
        root.setCenter(flow);
        root.setBottom(loadImage(file));

        BorderPane.setMargin(flow, new Insets(10, 0, 10, 0));
        //Show stage
        Scene scene = new Scene(root);
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
        saveItem.setOnAction(event -> { //Saves all existing nodes to file -> europa.graph
            try {
                if (!graphFile.exists()) {
                    graphFile = new File("europa.graph");
                }
                try (PrintWriter writer = new PrintWriter(graphFile)) { //'try with resource' -> autoclose 'writer'
                    //writer.println("HELLO!");
                    writer.println("File:" + graphFile);
                    if (listGraph.getNodes().isEmpty()) {
                        System.err.println("Graph is empty!");
                    }
                    System.out.println("Print nodes!");
                    writer.println(printNodes());
                    writer.println(testClass.listGraphClass.toString());

                } //end of try clause

            } catch (FileNotFoundException e) { //Note: Might be omitted due to try-with-resource
                System.err.println("Error: File not found?");
            } //try / catch clause
        }); //end of lambda expression

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

    //Open button handler
    class OpenHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            try {
                FileReader fr = new FileReader(graphUrl.toString());
                BufferedReader in = new BufferedReader(fr);
                Image image = new Image(in.toString());

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String printNodes() {
        StringBuilder sb = new StringBuilder();
        for (Object city : testClass.listGraphClass.getNodes()) {
            sb.append(city).append("; ");
        }
        return sb.toString();
    }

    private String printConnections() {
        StringBuilder sb = new StringBuilder();


        sb.append(testClass.listGraphClass.toString());
        return sb.toString();
    }
}
