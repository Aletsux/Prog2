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
import javafx.scene.input.MouseEvent;
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
    private City[] selectedNodes = new City[2];

    BorderPane root = new BorderPane();

    Pane cities = new Pane();

    Scene scene;

    public ListGraph getListGraph() {
        return graph;
    }

    private boolean unsavedChanges = false;
    MenuBar menuBar = new MenuBar();

    @Override
    public void start(Stage primaryStage) throws Exception {
        //for testing
        TestClass testClass = new TestClass();
        graph = testClass.runTests();
        System.out.println(graph.getNodes());

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

        Pane mainField = new Pane();
        Pane background = new Pane();
        FlowPane flow = new FlowPane();
        scene = new Scene(root);





<<<<<<< Updated upstream
=======


>>>>>>> Stashed changes
        // Background
        File imageFile = new File(graphUrl.toString());
        Image image = new Image(imageFile.toString());
        ImageView imageView = new ImageView(image);
        background.getChildren().add(imageView);

<<<<<<< Updated upstream
        mainField.getChildren().addAll(background);
=======
        mainField.getChildren().addAll(background, cities);

// Förvirrad över placering så gör det här
        // detta är för newplace del 2 när stad ska skapas av klick.
        EventHandler<MouseEvent> clickHandler = new cityClickHandler();
        scene.setOnMouseClicked(clickHandler);





>>>>>>> Stashed changes


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
<<<<<<< Updated upstream
        scene = new Scene(root);
=======
>>>>>>> Stashed changes

        //Create cursor
        Cursor cursor = Cursor.CROSSHAIR;

        // change cursor when newPlace has been clicked
        newPlaceB.setOnAction(event -> {
            //Place new node
            scene.setCursor(Cursor.CROSSHAIR);
            newPlaceB.setDisable(true);

            //Name new node + create new node
            nameWindow();

            //Draw new node
        });


        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Handler for citynode buttons

    public void createCity(double x, double y){
        //cities
        City oslo = new City(x, y, Color.RED);
      //  City stockholm = new City(100, 20, 30, Color.RED);
        cities.getChildren().addAll(oslo);
    }

    class cityClickHandler implements EventHandler<MouseEvent>{
        @Override public void handle(MouseEvent event){
            double x = event.getX();
            //minus 62 för att det blev fel med y axeln annars och andra lösningar icke funkna bre
            double y = event.getY() - 62;
// chats förslag för att cirkeln skapas för lågt ner
            //double localX = root.sceneToLocal(x, y).getX();
           // double localY = root.sceneToLocal(x, y).getY();
            createCity(x, y);


        }
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
<<<<<<< Updated upstream
    private Pane loadImage(File imageFile) {
        Image image = new Image(imageFile.toString());
        ImageView imageView = new ImageView(image);
        Pane mapPane = new Pane(imageView);
        return mapPane;
=======
//     Pane loadImage(File imageFile) {
//        Pane mapPane = new Pane();
//        Image image = new Image(imageFile.toString());
//        ImageView imageView = new ImageView(image);
//
//
//        mapPane.getChildren().add(label);
//        return mapPane;
//    }


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
                City node = new City(x, y, Color.BLUE);
                graph.add(node);
            }
        }
        in.close();

        System.out.println("Nodes: " + graph.getNodes());
>>>>>>> Stashed changes
    }

    private void saveFile() throws IOException {
        try (PrintWriter writer = new PrintWriter(graphFile)) { //'try with resource' -> autoclose 'writer'
            //writer.println("HELLO!");
            writer.println("File:" + graphFile);
            if (graph.getNodes().isEmpty()) {
                System.err.println("Graph is empty!");
            }
            System.out.println("Save nodes!");
            writer.println(printNodes()); //writes out node.toString()
            //System.out.println(printNodes());
            //writer.println(printConnections()); //writes out edges, disabled for testing readNodes()
        }
    }

    public void saveChanges() {
        try {
            saveFile();
        } catch (IOException e) {
            System.err.println("Error: problem when saving changes!");
        }
        unsavedChanges = false;
        Platform.exit();
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

    private void openConnectionWindow() {
        //Check whether connection exists
        if (graph.pathExists(selectedNodes[0], selectedNodes[1])) {
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

            //create a connection from first node to second node
            graph.connect(selectedNodes[0], selectedNodes[1], name, Integer.parseInt(time));

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
            if (unsavedChanges) {
                Alert alert = createAlertConf("Unsaved changes");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get().getText().equals("confirm")) {
                    try {
                        saveFile();
                    } catch (IOException e) {
                        System.err.println("Error: saving file");
                    }
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

    //Reads each line, splits it and creates new nodes based on parts
    private void readNodes(BufferedReader in) throws IOException { //Fixed!
        String text = "";
        while ((text = in.readLine()) != null) {
            if (!text.contains(";")) { //Hop over lines which don't contain ";"
                continue;
            }
            String[] parts = text.split(";");
            for (int i = 0; i < parts.length; i += 3) {
                String name = parts[i];
                double x = Double.parseDouble(parts[i + 1]);
                double y = Double.parseDouble(parts[i + 2]);
                City node = new City(name, x, y);
                graph.add(node);
            }
        }
        in.close();
        System.out.println("Nodes: " + graph.getNodes());
    }

    private String printNodes() {
        StringBuilder sb = new StringBuilder();
        for (Object obj : graph.getNodes()) {
            sb.append(obj).append(";");
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

    private Pane nameWindow() {
        javafx.scene.control.Dialog<Boolean> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Name");
        dialog.setHeaderText("Name of place:");

        javafx.scene.control.TextField nameField = new javafx.scene.control.TextField();

        ButtonType okButton = new ButtonType("ok");
        ButtonType cancelButton = new ButtonType("cancel");
        dialog.getDialogPane().setContent(new HBox(10, nameField));
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        dialog.setResultConverter(buttonType -> {
            if (okButton == ButtonType.OK) {
                String name = nameField.getText();
                return true;
            }
            return false;

<<<<<<< Updated upstream
        });
        dialog.showAndWait();
        return dialog.getDialogPane();
    }
}
=======
        }

        private void CreateConnection(String name, int time) {


            }

        }








>>>>>>> Stashed changes







