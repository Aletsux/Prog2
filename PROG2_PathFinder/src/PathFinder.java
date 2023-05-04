import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class PathFinder extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        FlowPane flow = new FlowPane();

        Button findPathB = new Button("Find Path");
        Button showConnectionB = new Button("Show Connection");
        Button newPlaceB = new Button("New Place");
        Button newConnectionB = new Button("New Connection");
        Button changeConnectionB = new Button("Change Connection");

        flow.getChildren().addAll(findPathB, showConnectionB, newPlaceB, newConnectionB, changeConnectionB);

        flow.setAlignment(Pos.CENTER);
        flow.setHgap(10);

        root.setTop(flow);
        root.getChildren().add(createMenu());
        //createMenu().setAlignment(Pos.TOP_RIGHT);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createMenu() {
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
        MenuItem saveItem = new MenuItem("Save");
        archiveMenu.getItems().add(saveItem);
        MenuItem imageItem = new MenuItem("Save Image");
        archiveMenu.getItems().add(imageItem);
        MenuItem exitItem = new MenuItem("Exit");
        archiveMenu.getItems().add(exitItem);

        return vbox;
    }
}
