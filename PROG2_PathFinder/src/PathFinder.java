import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.scene.input.MouseEvent;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.nio.Buffer;
import java.util.*;
import javafx.scene.shape.Line;

public class PathFinder extends Application {
    //Class for testing and loading data
    //TestClass testClass = null;
    //ListGraph listGraph = testClass.getListGraph();
    private ListGraph graph = new ListGraph();
    URL graphUrl = PathFinder.class.getResource("europa.gif"); //URL = bakgrundsbild??
    File imageFile = new File(graphUrl.toString()); //Background image

    File graphFile = new File("europa.graph");
    Scene scene;

    public static City[] selectedNodes = new City[2]; //temporary public for testing

    Pane cities = new Pane();


    public ListGraph getListGraph() {
        return graph;
    }

    private boolean unsavedChanges = false;
    MenuBar menuBar = new MenuBar();
    //Panes
    BorderPane root;
    Pane mainField;
    Pane background;
    FlowPane flow;

    //Buttons
    Button findPathB;
    Button showConnectionB;
    Button newPlaceB;
    Button newConnectionB;
    Button changeConnectionB;

    public boolean cursorIsCrossHair = false;


    @Override
    public void start(Stage primaryStage) throws Exception {
        //for testing
        TestClass testClass = new TestClass();
        graph = testClass.runTests();
        //showConnectionHandler(selectedNodes[0], selectedNodes[1]);

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
        root = new BorderPane();
        mainField = new Pane();
        background = new Pane();
        flow = new FlowPane();

        //Show stage
        scene = new Scene(root);


        // Background
        File imageFile = new File(graphUrl.toString());
        Image image = new Image(imageFile.toString());
        ImageView imageView = new ImageView(image);
        background.getChildren().add(imageView);


        mainField.getChildren().addAll(background, cities);

        // Förvirrad över placering så gör det här
        // detta är för newplace del 2 när stad ska skapas av klick.


        //Flow
        findPathB = new Button("Find Path");
        findPathB.setOnAction(e -> findPath());

        showConnectionB = new Button("Show Connection");
        showConnectionB.setOnAction(event -> {
            showConnectionHandler(selectedNodes[0], selectedNodes[1], false);
        });

        newPlaceB = new Button("New Place");

        newConnectionB = new Button("New Connection");
        newConnectionB.setOnAction(event -> {
            openConnectionWindow();
        });

        changeConnectionB = new Button("Change Connection");
        changeConnectionB.setOnAction(event -> showConnectionHandler(selectedNodes[0], selectedNodes[1], true));

        flow.getChildren().addAll(findPathB, showConnectionB, newPlaceB, newConnectionB, changeConnectionB);
        flow.setAlignment(Pos.CENTER);
        flow.setHgap(10);

        //add nodes to root
//        root.getChildren().add(fileMenu());
//        root.getChildren().add(loadImage());


        //Set position in BorderPane
        root.setTop(fileMenu());
        root.setCenter(flow);
        //root.setBottom(loadImage(imageFile));
        root.setBottom(mainField);


        BorderPane.setMargin(flow, new Insets(10, 0, 10, 0));


        //Create cursor
        Cursor cursor = Cursor.CROSSHAIR;

        // change cursor when newPlace has been clicked
        newPlaceB.setOnAction(event -> {
            cursorIsCrossHair = true;

            //Place new node
            if (cursorIsCrossHair) {
                scene.setCursor(Cursor.CROSSHAIR);
                newPlaceB.setDisable(true);


                EventHandler<MouseEvent> clickHandler = new cityClickHandler();
                scene.setOnMousePressed(clickHandler);


                if (cursorIsCrossHair==false){
                    newPlaceB.setDisable(false);
                }
            }









            //Name new node + create new node

            //Draw new node
        });



        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void clearNodes() { //clear nodes from system, graph.getNodes()
        graph.getNodes().clear();
        if (graph.getNodes().isEmpty()) {
            System.out.println("All nodes cleared!");
        }
    }

    public void createCity(String name, double x, double y) {
        //cities
        City node = new City(x, y, Color.BLUE);

        City city = new City(name, x, y);
        graph.add(city);
        System.out.println("Node created!");
        Label label = new Label (name);
        label.setLayoutX(x + 2);
        label.setLayoutY(y - 2);
        mainField.getChildren().addAll(label);

        //  City stockholm = new City(100, 20, 30, Color.RED);
        if (cities.getChildren().contains(node)) {
            System.out.println("Node already exists");
            return;
        }
        cities.getChildren().addAll(new Pane(node)); //Temporary solution?
    }

    class cityClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {

            System.out.println("Mouse clicked");
            double x = event.getX();
            //minus 62 för att det blev fel med y axeln annars och andra lösningar icke funkna bre
            double y = event.getY() - 62;
// chats förslag för att cirkeln skapas för lågt ner
            //double localX = root.sceneToLocal(x, y).getX();
            // double localY = root.sceneToLocal(x, y).getY();
            nameWindow(x, y);
            //createCity(x, y);
            disableCrosshair();
        }
    }

    class changeCityColorHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {

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
        mapItem.setOnAction(event -> {
            if (unsavedChanges) {
                createAlertConf("Unsaved Changes");
            }
            clearNodes();
            //Clear all visible nodes
            Collection<Node> remove = cities.getChildren();
            cities.getChildren().removeAll(remove); //remove all nodes in cities pane?
        });

        MenuItem openItem = new MenuItem("Open");
        archiveMenu.getItems().add(openItem);
        openItem.setOnAction(new OpenHandler());


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
    private Pane loadImage(File imageFile) {
        Image image = new Image(imageFile.toString());
        ImageView imageView = new ImageView(image);
        Pane mapPane = new Pane(imageView);
        return mapPane;
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
        ButtonType confirmButton = new ButtonType("OK");
        ButtonType cancelButton = new ButtonType("cancel", ButtonBar.ButtonData.CANCEL_CLOSE); //ButtonData = enums for opertations

        buttonBar.getButtons().add(alert.getDialogPane());
        buttonBar.setPadding(new Insets(0, 0, 0, 50));

        if (unsavedChanges) {
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get().getText().equals("OK")) {
                try {
                    saveFile();

                } catch (IOException e) {
                    System.err.println("Error: saving file");
                }
            }
        }

        //alert.getDialogPane().setPadding(new Insets(0, 0, 0, 0));

        return alert;
    }

    private void openConnectionWindow() {
        //Check whether connection exists
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("New Connection");
        dialog.setHeaderText("Create new connection between " + selectedNodes[0].getName().toUpperCase() + " and " + selectedNodes[1].getName().toUpperCase());

        javafx.scene.control.TextField nameField = new javafx.scene.control.TextField();
        javafx.scene.control.TextField timeField = new javafx.scene.control.TextField();
        Label name = new Label("Name: ");
        Label time = new Label("Time:   ");
        HBox hbName = new HBox();
        hbName.getChildren().addAll(name, nameField);
        hbName.setAlignment(Pos.CENTER);
        HBox hbTime = new HBox();
        hbTime.getChildren().addAll(time, timeField);
        hbTime.setAlignment(Pos.CENTER);

        ButtonType okButton = new ButtonType("ok");
        dialog.getDialogPane().setContent(new VBox(hbName, hbTime));
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        String nameInput = nameField.getText();
        String timeInput = timeField.getText();

        double node1x = selectedNodes[0].getLayoutX();
        double node1y = selectedNodes[0].getLayoutY();
        double node2x = selectedNodes[1].getLayoutX();
        double node2y = selectedNodes[1].getLayoutY();

        Line line = new Line (node1x, node1y, node2x, node2y);

        mainField.getChildren().addAll(line);

        dialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType.getText() == "ok") {
                if (graph.pathExists(selectedNodes[0], selectedNodes[1])) {
                    showErrorMessage("Connection already exist between the two destinations.");
                    return;
                }

                if (nameInput.isEmpty() || !timeInput.matches("\\d+")) {
                    showErrorMessage("Input is not valid. Name cannot be empty.");
                    return;
                }
                //create a connection from first node to second node
                graph.connect(selectedNodes[0], selectedNodes[1], nameInput, Integer.parseInt(timeInput));
                //createConnection(name, Integer.parseInt(time));
                System.out.println("Create connections!");
            }
        });
    }

    public void showConnectionHandler(City from, City to, boolean edit) { //Bug: pops up twice in change connection
        if (from == null || to == null) { //selected less than 2 nodes
            showErrorMessage("Please select two nodes");
            return;
        }

        if (graph.getEdgeBetween(from, to) == null) { //No connection between nodes
            showErrorMessage("No connection between selected nodes");
            return;
        }

        //Display alert with information on connection
        Edge edge = graph.getEdgeBetween(from, to);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION); //not null...
        alert.setTitle("Connection");
        alert.setHeaderText("From " + from.getName().toUpperCase() + " to " + to.getName().toUpperCase());

        System.out.println("Visible: " + alert.isShowing());
        HBox hbName = new HBox();
        HBox hbTime = new HBox();
        //VBox vb = new VBox();

        Label name = new Label("Name: ");
        TextField nameField = new TextField(edge.getName());

        Label time = new Label("Time:   ");
        TextField timeField = new TextField("" + edge.getWeight());

        hbName.getChildren().addAll(name, nameField);
        hbName.setAlignment(Pos.CENTER);
        hbTime.getChildren().addAll(time, timeField);
        hbTime.setAlignment(Pos.CENTER);

        if (edit) { //To be tested
            nameField.setEditable(true);
            timeField.setEditable(true);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                edge.setWeight(Integer.parseInt(timeField.getText()));
                edge.setName(nameField.getText());
            }
        } else {
            nameField.setEditable(false);
            timeField.setEditable(false);
        }

        alert.getDialogPane().setContent(new VBox(hbName, hbTime));
        alert.showAndWait();
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
                createAlertConf("Unsaved Changes");
            }


            //readNodes(), drawNodes(), loadImage()
            try {
                FileReader fr = new FileReader(graphFile);
                BufferedReader in = new BufferedReader(fr);
                readNodes(in); //Add saved nodes to graph, draw nodes
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

                createCity(name, x, y);
                System.out.println("Coordinates: " + x + ";" + y);//Draw the node added
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

    private void nameWindow(double x, double y) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Name");
        dialog.setHeaderText("Name of place:");

        javafx.scene.control.TextField nameField = new javafx.scene.control.TextField("Node1");

        ButtonType okButton = new ButtonType("ok");
        ButtonType cancelButton = new ButtonType("close");
        dialog.getDialogPane().setContent(new HBox(10, nameField));
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        dialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType.getText() == "close") {
                dialog.close();
                return;
            }

            System.out.println("Ok button clicked");
            String name = nameField.getText();
            Character.toUpperCase(name.charAt(0));
            Label nameTag = new Label(name); //create node?
            createCity(name, x, y);
        });
    }

    private Pane findPath() {
        List<Edge<City>> path = graph.getPath(selectedNodes[0], selectedNodes[1]);
        TextArea result = new TextArea();

        if (selectedNodes[0] == null || selectedNodes[1] == null) {
            showErrorMessage("Connection must be selected.");
        }

        javafx.scene.control.Dialog<Boolean> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Message");
        dialog.setHeaderText("The path from " + selectedNodes[0].getName() + " to " + selectedNodes[1].getName());

        StringBuilder message = new StringBuilder();
        for (Edge edge : path) {
            message.append(edge.toString() + "\n");
        }
        result.setText(message.toString());

        ButtonType okButton = new ButtonType("ok");
        dialog.getDialogPane().setContent(result);
        dialog.getDialogPane().getButtonTypes().addAll(okButton);

        dialog.setResultConverter(buttonType -> {
            if (okButton == ButtonType.OK) {
                return true;
            }
            return false;
        });
        dialog.showAndWait();
        return dialog.getDialogPane();

    }
    public boolean getCrosshairBoolean(){
        return cursorIsCrossHair;
    }

    public void disableCrosshair(){
        this.cursorIsCrossHair = false;
        scene.setCursor(Cursor.DEFAULT);
    }

}







