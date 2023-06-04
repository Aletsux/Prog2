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
import javafx.scene.input.MouseEvent;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.Optional;
import java.util.*;

import javafx.scene.shape.Line;

public class PathFinder extends Application {
    //Class for testing and loading data
    //TestClass testClass = null;
    //ListGraph listGraph = testClass.getListGraph();
    private static ArrayList<City> selectedNodes = new ArrayList(); //temporary public for testing
    //URL graphUrl = PathFinder.class.getResource("europa.gif"); //URL = bakgrundsbild??
    private ListGraph<City> graph = new ListGraph<>();
    private File graphFile = new File("europa.graph");
    private Pane mainField = new Pane();
    private MenuBar menuBar = new MenuBar();
    private BorderPane root = new BorderPane();
    private String imageUrl = "File:europa.gif";//Background image
    private boolean unsavedChanges = false; //Changes at: New Place, New Connection, Change Connection, Open
    private boolean cursorIsCrossHair = false; // tempo public

    //Background
    //Image image = new Image(imageFile.toString());
    private ImageView imageView;
    private Scene scene;
    private FlowPane flow;

    //Buttons
    private Button findPathB;
    private Button showConnectionB;
    private Button newPlaceB;
    private Button newConnectionB;
    private Button changeConnectionB;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //SetId
        menuBar.setId("menu");

        //Declare
        primaryStage.setTitle("PathFinder");

        //mainField = new Pane();
        //background = new Pane();
        flow = new FlowPane();


        //Flow
        findPathB = new Button("Find Path");
        findPathB.setId("btnFindPath");
        findPathB.setOnAction(e -> findPath());

        showConnectionB = new Button("Show Connection");
        showConnectionB.setId("btnShowConnection");
        showConnectionB.setOnAction(event -> {
            if (selectedNodes.size() >= 2) {
                showConnectionHandler(selectedNodes.get(0), selectedNodes.get(1), false);
            } else {
                noSelectedNodesAlert();
            }
        });

        newPlaceB = new Button("New Place");
        //newPlaceB.setId("btnNewPlace");
        newPlaceB.setOnAction(event -> {
            cursorIsCrossHair = true;
            //sets cursor to crosshair, and disables newPlace button
            if (cursorIsCrossHair) {
                scene.setCursor(Cursor.CROSSHAIR);
                newPlaceB.setDisable(true);
                mainField.setOnMousePressed(new CityClickHandler());
            }
        });
        newPlaceB.setId("btnNewPlace");

        newConnectionB = new Button("New Connection");
        newConnectionB.setOnAction(event -> {

            if (selectedNodes.size() == 2) {
                if (graph.getEdgeBetween(selectedNodes.get(0), selectedNodes.get(1)) != null) {
                    showErrorMessage("Connection already exist between the two destinations.");
                    return;
                }
            }

            if (selectedNodes.size() < 2) {
                noSelectedNodesAlert();
            } else {
                openConnectionWindow();
            }
        });
        newConnectionB.setId("btnNewConnection");


        changeConnectionB = new Button("Change Connection");
        changeConnectionB.setId("btnChangeConnection");
        changeConnectionB.setOnAction(event -> {
            if (selectedNodes.size() < 2) {
                noSelectedNodesAlert();
            } else {
                showConnectionHandler(selectedNodes.get(0), selectedNodes.get(1), true);
            }
        });

        flow.getChildren().addAll(findPathB, showConnectionB, newPlaceB, newConnectionB, changeConnectionB);
        flow.setAlignment(Pos.CENTER);
        flow.setHgap(10);


        //background = new Pane();
        //background.getChildren().add(imageView);
        imageView = new ImageView();
        mainField.getChildren().add(imageView);

        VBox menus = new VBox(menuBar);

        Menu archiveMenu = new Menu("File");
        archiveMenu.setId("menuFile");
        menuBar.getMenus().add(archiveMenu);

        //Adding menu items to the 'menu'
        MenuItem mapItem = new MenuItem("New Map");
        mapItem.setOnAction(event -> {
            if (unsavedChanges) {
                Alert alert = createAlertConf("Unsaved Changes");
                if (alert.getResult() == ButtonType.CANCEL) {
                    alert.close();
                    return;
                }
            }
            //loadImage();
            Image image = new Image(imageUrl);
            imageView.setImage(image);
            Stage stage = (Stage) flow.getScene().getWindow();
            stage.sizeToScene();
            stage.centerOnScreen();


            clearNodes();


            unsavedChanges = false;
        });
        mapItem.setId("menuNewMap");
        archiveMenu.getItems().add(mapItem);


        MenuItem openItem = new MenuItem("Open");
        openItem.setId("menuOpenFile");
        archiveMenu.getItems().add(openItem);
        openItem.setOnAction(new OpenHandler());


        MenuItem saveItem = new MenuItem("Save");
        saveItem.setId("menuSaveFile");
        archiveMenu.getItems().add(saveItem);
        saveItem.setOnAction(new SaveHandler());

        MenuItem imageItem = new MenuItem("Save Image");
        imageItem.setId("menuSaveImage");
        archiveMenu.getItems().add(imageItem);
        imageItem.setOnAction(event -> {
            //Get scene
            WritableImage result = new WritableImage((int) scene.getWidth(), (int) scene.getHeight());
            scene.snapshot(result);
            File outputFile = new File("capture.png");
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(result, null), "png", outputFile);
            } catch (IOException e) {
                System.err.println("Error: problem when saving snapshot!");
            }
        });

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setId("menuExit");
        archiveMenu.getItems().add(exitItem);
        exitItem.setOnAction(event -> {
            exitProgram();
        });

        //mainField.getChildren().addAll(background);
        //Set position in BorderPane
        root.setTop(menus);
        root.setCenter(flow);
        //root.setBottom(loadImage(imageFile));
        root.setBottom(mainField);


        BorderPane.setMargin(flow, new Insets(10, 0, 10, 0));

        //Create cursor
        Cursor cursor = Cursor.CROSSHAIR;

        //Show stage
        scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(windowEvent -> exitProgram()); //Call exit program when closing window
        primaryStage.show();
        mainField.setId("outputArea");
    }


    private void clearNodes() { //clear nodes from system, graph.getNodes()
        //Clear all visible nodes
        mainField.getChildren().clear();
        mainField.getChildren().add(imageView); //removes all visual elements
        selectedNodes.clear();

        Set<Edge> edgesToRemove = new HashSet<>();
        for (Object city : graph.getNodes()) {
            edgesToRemove.addAll(graph.getEdges((City) city));
        }
        edgesToRemove.clear(); //removes all edges

        graph.getNodes().clear(); //removes all nodes
        //graph = new ListGraph<>();
    }

    private void createCity(String name, double x, double y) {

        City city = new City(name, x, y + 10); //Position is off by +10
        if (graph.getNodes().contains(city)) {
            return;
        } else {
            graph.add(city);
        }

        Label label = new Label(name);
        label.setLayoutX(x + 2);
        label.setLayoutY(y - 2);


        //city.setOnMouseClicked(new selectCityHandler());
        city.setOnMouseClicked(event -> {
            if (selectedNodes.size() < 2 && city.getFill() == Color.BLUE) {
                selectedNodes.add(city);
                city.setFill(Color.RED);

            } else {
                city.setFill(Color.BLUE);
                selectedNodes.remove(city);
            }
        });
        city.setId(name);
        mainField.getChildren().addAll(city, label);
        unsavedChanges = true;
    }

    class CityClickHandler implements EventHandler<MouseEvent> { //Fixed place
        @Override
        public void handle(MouseEvent event) {
            String name = "";
            if (cursorIsCrossHair) {
                double x = event.getX();
                //minus 62 för att det blev fel med y axeln annars och andra lösningar icke funkna bre
                double y = event.getY(); //-62 from scene
                //double localX = root.sceneToLocal(x, y).getX();
                // double localY = root.sceneToLocal(x, y).getY();
                if (cursorIsCrossHair) {
                    int nrNodes = 1;
                    for (Object obj : graph.getNodes()) {
                        nrNodes++;
                    }
                    Dialog<ButtonType> dialog = new Dialog<>();
                    dialog.setTitle("Name");
                    dialog.setHeaderText("Name of place:");

                    javafx.scene.control.TextField nameField = new javafx.scene.control.TextField("Node" + nrNodes);

                    ButtonType okButton = new ButtonType("OK");
                    ButtonType cancelButton = new ButtonType("CLOSE");
                    dialog.getDialogPane().setContent(new HBox(10, nameField));
                    dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

                    Optional<ButtonType> result = dialog.showAndWait();
                    if (result.isPresent()) {
                        if (result.get().getText().equals("OK")) {
                            name = nameField.getText();
                            Character.toUpperCase(name.charAt(0));
                            createCity(name, x, y);
                        }
                    }
                    dialog.close();


                    disableCrosshair();
                    newPlaceB.setDisable(false);
                }
            }

        }
    }


    //Make this generic, use parameter for path
    private void loadImage() {
        Image image = new Image(imageUrl);
        imageView.setImage(image);
        Stage stage = (Stage) flow.getScene().getWindow();
        stage.sizeToScene();
        stage.centerOnScreen();

    }

    private void saveFile() throws IOException {
        if (graph.getNodes().isEmpty()) {
            System.err.println("Graph is empty!");
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(graphFile))) { //'try with resource' -> autoclose 'writer'
            writer.println(imageUrl);
            writer.println(printNodes()); //writes out node.toString()
            writer.println(printConnections()); //writes out edges, disabled for testing readNodes()
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found error");
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

        buttonBar.getButtons().addAll(alert.getDialogPane());
        buttonBar.setPadding(new Insets(0, 0, 0, 50));

        Optional<ButtonType> result = alert.showAndWait();

        //alert.getDialogPane().setPadding(new Insets(0, 0, 0, 0));

        return alert;
    }


    private void noSelectedNodesAlert() {
        Alert newAlert = new Alert(Alert.AlertType.ERROR);
        newAlert.setTitle("Error!");
        newAlert.setHeaderText(null);
        newAlert.setContentText("Two places must be selected!");
        newAlert.showAndWait();
    }

//    private void noSelectedNodesAlert() {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Error!");
//        alert.setHeaderText("Two places must be selected!");
//
//        ButtonType okButton = new ButtonType("OK");
//        alert.getButtonTypes().setAll(okButton);
//
//        alert.showAndWait();
//    }

    private void openConnectionWindow() {
        //Check whether connection exists
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("New Connection");
        dialog.setHeaderText("Create new connection between " + selectedNodes.get(0).getName().toUpperCase() + " and " + selectedNodes.get(1).getName().toUpperCase());

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

        ButtonType okButton = new ButtonType("OK");
        dialog.getDialogPane().setContent(new VBox(hbName, hbTime));
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);


        dialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType.getText().equals("OK")) {
                String nameInput = nameField.getText();
                String timeInput = timeField.getText();

                if (nameInput.isEmpty() && !timeInput.matches("\\d+")) {
                    showErrorMessage("Input is not valid. Name cannot be empty and Time must contain a numeric value");
                    return;
                } else if (!timeInput.matches("\\d+")) {
                    showErrorMessage("Input is not valid. Time must contain numeric value");
                    return;
                } else if (nameInput.isEmpty()) {
                    showErrorMessage("Input is not valid. Name cannot be empty.");
                    return;
                }

                createLine(null, null);
                //create a connection from first node to second node
                graph.connect(selectedNodes.get(0), selectedNodes.get(1), nameInput, Integer.parseInt(timeInput));
                unsavedChanges = true;
            }
        });
    }

    private void createLine(City nodeFrom, City nodeTo) {

        double radius;
        double node1x;
        double node1y;
        double node2x;
        double node2y;

        if (nodeFrom == null && nodeTo == null) {
            radius = selectedNodes.get(0).getRadius();
            node1x = Double.parseDouble(selectedNodes.get(0).getxPos());
            node1y = Double.parseDouble(selectedNodes.get(0).getyPos()) - radius;
            node2x = Double.parseDouble(selectedNodes.get(1).getxPos());
            node2y = Double.parseDouble(selectedNodes.get(1).getyPos()) - radius;
        } else {
            radius = nodeFrom.getRadius();
            node1x = Double.parseDouble(nodeFrom.getxPos());
            node1y = Double.parseDouble(nodeFrom.getyPos()) - radius;
            node2x = Double.parseDouble(nodeTo.getxPos());
            node2y = Double.parseDouble(nodeTo.getyPos()) - radius;
        }

        Line line = new Line(node1x, node1y, node2x, node2y);
        line.setStrokeWidth(2);
        line.setDisable(true);
        mainField.getChildren().add(line);
    }

    public void showConnectionHandler(City from, City to, boolean edit) { //Bug: pops up twice in change connection
        if (selectedNodes.size() < 2) { //selected less than 2 nodes
            noSelectedNodesAlert();
            // showErrorMessage("Test");
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
        alert.getDialogPane().setContent(new VBox(hbName, hbTime));

        Optional<ButtonType> result = alert.showAndWait();
        if (edit) { //To be tested
            nameField.setEditable(true);
            timeField.setEditable(true);

            if (result.get() == ButtonType.OK) {
                Edge edgeFrom = graph.getEdgeBetween(to, from);
                edge.setWeight(Integer.parseInt(timeField.getText()));
                edge.setName(nameField.getText());
                edgeFrom.setName(nameField.getText());
                edgeFrom.setWeight(Integer.parseInt(timeField.getText()));
                unsavedChanges = true;
            }
        } else {
            nameField.setEditable(false);
            timeField.setEditable(false);
        }
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
                unsavedChanges = false;
                saveFile();
            } catch (IOException e) {

            }
        }
    }


    //Open button handler
    class OpenHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) { //Fix: dialogue layout
            //Confirmation alert
            if (unsavedChanges) {
                Alert alert = createAlertConf("Unsaved Changes");
                if (alert.getResult() == ButtonType.CANCEL) {
                    alert.close();
                    return;
                }
            }

            try {
                clearNodes();
                FileReader fr = new FileReader(graphFile);
                BufferedReader in = new BufferedReader(fr);
                readNodes(in); //Add saved nodes to graph, draw nodes
                readConnections(in);
                loadImage();
                in.close();
                unsavedChanges = true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //Reads each line, splits it and creates new nodes based on parts
    private void readNodes(BufferedReader in) throws IOException { //Fixed!
        String firstLine = in.readLine();
        if (firstLine.contains("file")) {
            imageUrl = firstLine;
        }
        String text = "";
        //Read first line, set url
        int cityCount = 0;
        text = in.readLine();
        if (text != null) {
            String[] parts = text.split(";");
            for (int i = 0; i < parts.length; i += 3) {
                String name = parts[i];
                double x = Double.parseDouble(parts[i + 1]);
                double y = Double.parseDouble(parts[i + 2]);

                createCity(name, x, y);
            }
        }
    }

    private void readConnections(BufferedReader in) throws IOException {
        int connectionCount = 0;
        String text = "";
        for (int i = 0; i < 1; i++) { //Skip first 2 lines
            in.readLine();
        }

        while ((text = in.readLine()) != null) {
            String[] parts = text.split(";");
            for (int i = 0; i < parts.length; i += 4) {
                if (i + 3 < parts.length) {
                    String node1 = parts[i];
                    String node2 = parts[i + 1];
                    String edgeName = parts[i + 2];
                    int weight = Integer.parseInt(parts[i + 3]);

                    createConnection(node1, node2, edgeName, weight);
                    connectionCount++;
                }

            }
        }
    }


    private String printNodes() {
        StringBuilder sb = new StringBuilder();
        for (Object obj : graph.getNodes()) {
            if (obj instanceof City) {
                sb.append(obj).append(";");
            }
        }
        return sb.toString();
    }

    private String printConnections() {
        StringBuilder sb = new StringBuilder();
        for (City obj : graph.getNodes()) {
            Set<Edge<City>> edges = graph.getEdges(obj);
            City current = (City) obj;
            for (Edge e : edges) {
                City destination = (City) e.getDestination();
                sb.append(current.getName()).append(";").append(destination.getName() + ";").append(e.getName() + ";").append(e.getWeight())
                        .append("\n");
            }
        }
        return sb.toString();
    }

    private void createConnection(String from, String to, String name, int weight) {
        Set<City> cities = graph.getNodes();
        City node1 = null;
        City node2 = null;

        for (City c : cities) {
            if (c.getName().equals(from)) {
                node1 = c;
            }
            if (c.getName().equals(to)) {
                node2 = c;
            }
        }
        createLine(node1, node2);

        if (graph.getEdgeBetween(node1, node2) == null) { //Continue if edge exists?
            graph.connect(node1, node2, name, weight);
        }
    }

    private void exitProgram() {
        if (unsavedChanges) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Unsaved Changes");
            alert.setContentText("There are unsaved changes, do you wish to save before exiting?");
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

    private String nameWindow() {
        int nrNodes = 1;
        for (Object obj : graph.getNodes()) {
            nrNodes++;
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Name");
        dialog.setHeaderText("Name of place:");

        javafx.scene.control.TextField nameField = new javafx.scene.control.TextField("Node" + nrNodes);

        ButtonType okButton = new ButtonType("OK");
        ButtonType cancelButton = new ButtonType("close");
        dialog.getDialogPane().setContent(new HBox(10, nameField));
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (result.get().getText().equals("OK")) {
                String name = nameField.getText();
                Character.toUpperCase(name.charAt(0));
                return name;
            }
        }
        dialog.close();
        return null;
    }

    private void findPath() { //Fixed
        if (selectedNodes.size() == 2) {
            List<Edge<City>> path = graph.getPath(selectedNodes.get(0), selectedNodes.get(1)); //sends reverse!
            Collections.reverse(path);
            TextArea result = new TextArea();

            javafx.scene.control.Dialog<ButtonType> dialog = new javafx.scene.control.Dialog<>();
            dialog.setTitle("Message");
            dialog.setHeaderText("The path from " + selectedNodes.get(0).getName() + " to " + selectedNodes.get(1).getName());

            int totalweight = 0;
            StringBuilder message = new StringBuilder();
            for (Edge edge : path) {
                City cityToPrint = (City) edge.getDestination();
                message.append("to " + cityToPrint.getName() + " by " + edge.getName() + " takes " + edge.getWeight()).append("\n");
                totalweight += edge.getWeight();
            }
            message.append("Total " + totalweight);
            result.setText(message.toString());

            ButtonType okButton = new ButtonType("OK");
            dialog.getDialogPane().setContent(result);
            dialog.getDialogPane().getButtonTypes().addAll(okButton);

            Optional<ButtonType> clickResult = dialog.showAndWait();
            if (clickResult.isPresent()) {
                dialog.close();
            }

        } else {
            showErrorMessage("Connection must be selected.");
        }
    }

    public boolean getCrosshairBoolean() {
        return cursorIsCrossHair;
    }

    public void disableCrosshair() {
        this.cursorIsCrossHair = false;
        scene.setCursor(Cursor.DEFAULT);
    }

}







