import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Handler;

public class PathFinder extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Declare
        Label title = new Label("PathFinder");
        title.setAlignment(Pos.TOP_LEFT);

        BorderPane root = new BorderPane(title);
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
        root.setBottom(loadImage());

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

        MenuItem saveItem = new MenuItem("Save");
        archiveMenu.getItems().add(saveItem);
        saveItem.setOnAction(new SaveHandler());

        MenuItem imageItem = new MenuItem("Save Image");
        archiveMenu.getItems().add(imageItem);

        MenuItem exitItem = new MenuItem("Exit");
        archiveMenu.getItems().add(exitItem);

        return vbox;
    }

    private Label loadImage() {
        Label label = new Label();
        Image image = new Image("file:c:/GitHub/Prog2/europa.gif");
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
                BufferedReader br = new BufferedReader(fr);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    class SaveHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {

        }
    }
}
