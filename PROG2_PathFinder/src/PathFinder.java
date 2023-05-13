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

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.logging.Handler;

public class PathFinder extends Application {
    ListGraph listGraph = new ListGraph();
    File file = new File("europa.graph");

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Url för bild fil
        URL url = new URL("file:c:/GitHub/Prog2/europa.gif");

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
        root.setBottom(loadImage(url));

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
                if (file.exists()) {
                    System.out.println("Error: File already exists!");
                    return;
                }
                try (PrintWriter writer = new PrintWriter(file)) { //'try with resource' -> autoclose 'writer'
                    for (Object node : listGraph.getNodes()) {
                        writer.print(node.toString() + ";");
                    }
                } //end of try clause,

            } catch (FileNotFoundException e) { //Note: Might be omitted due to try-with-resource
                System.err.println("Erro: File not found?");
            } //try / catch clause
        }); //end of lambda expression

        MenuItem imageItem = new MenuItem("Save Image");
        archiveMenu.getItems().add(imageItem);

        MenuItem exitItem = new MenuItem("Exit");
        archiveMenu.getItems().add(exitItem);

        return vbox;
    }

    //Make this generic, use parameter for path
    private Label loadImage(URL url) {
        Label label = new Label();
        Image image = new Image(url.toString());
        ImageView imageView = new ImageView(image);
        label.setGraphic(imageView);
        return label;
    }

    //Open button handler
    class OpenHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            try {
                FileReader fr = new FileReader("europa.graph");
                Image image = new Image(fr.toString());

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
