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
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.util.Optional;
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
    private Scene scene;

    private static ArrayList<City> selectedNodes = new ArrayList(); //temporary public for testing

    private Pane cities = new Pane(); //Important positioning


    public ListGraph getListGraph() {
        return graph;
    }

    private boolean unsavedChanges = false; //WIP
    private MenuBar menuBar = new MenuBar();
    //Panes
    private BorderPane root;
    private Pane mainField; //not necessary?
    private Pane background;
    private FlowPane flow;

    //Buttons
    private Button findPathB;
    private Button showConnectionB;
    private Button newPlaceB;
    private Button newConnectionB;
    private Button changeConnectionB;
    public boolean cursorIsCrossHair = false; // tempo public

    @Override
    public void start(Stage primaryStage) throws Exception {
        //for testing
        //TestClass testClass = new TestClass();
        //graph = testClass.runTests();

        System.out.println(graph.getNodes());
        System.out.println(Arrays.asList(selectedNodes).toString());

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


        //Background
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
            if (selectedNodes.size() >= 2) {
                showConnectionHandler(selectedNodes.get(0), selectedNodes.get(1), false);
            } else {
                noSelectedNodesAlert();
            }
        });

        newPlaceB = new Button("New Place");

        newConnectionB = new Button("New Connection");
        newConnectionB.setOnAction(event -> {
            if (selectedNodes.size() != 2){
                noSelectedNodesAlert();
            } else {
                openConnectionWindow();
            }
        });


        changeConnectionB = new Button("Change Connection");
        changeConnectionB.setOnAction(event -> showConnectionHandler(selectedNodes.get(0), selectedNodes.get(1), true));

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

        // handles newPlace Button
        newPlaceB.setOnAction(event -> {
            cursorIsCrossHair = true;
            //sets cursor to crosshair, and disables newPlace button
            if (cursorIsCrossHair) {
                scene.setCursor(Cursor.CROSSHAIR);
                newPlaceB.setDisable(true);
                scene.setOnMousePressed(new cityClickHandler());
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(windowEvent -> exitProgram()); //Call exit program when closing window
        primaryStage.show();
    }


    private void clearNodes() { //clear nodes from system, graph.getNodes()
        Set<Edge> edgesToRemove = new HashSet<>();
        for (Object city : graph.getNodes()) {
            edgesToRemove.addAll(graph.getEdges((City) city));
        }
        edgesToRemove.clear();

        graph.getNodes().clear();

        if (graph.getNodes().isEmpty()) {
            System.out.println("All nodes cleared!");
        }
        if (edgesToRemove.isEmpty()) {
            System.out.println("All connections cleared!");
        }
    }


    private City createCity(String name, double x, double y) {
        //cities
        //City node = new City(x, y);

        City city = new City(name, x, y);
        if (graph.getNodes().contains(city)) {
            System.out.println("Node already exists");
            return city;
        } else {
            graph.add(city);
            System.out.println("Node created!");
        }

        Label label = new Label(name);
        label.setLayoutX(x + 2);
        label.setLayoutY(y - 2);
        cities.getChildren().addAll(label);


        cities.getChildren().addAll(city);//Temporary solution?
        //EventHandler<MouseEvent> selectCityHandler = new selectCityHandler();
        //city.setOnMouseClicked(new selectCityHandler());
        city.setOnMouseClicked(event -> {
            System.out.println("Set color!");
            if (selectedNodes.size() < 2 && city.getFill() == Color.BLUE) {
                selectedNodes.add(city);
                city.setFill(Color.RED);

            } else {
                city.setFill(Color.BLUE);
                selectedNodes.remove(city);
            }
            System.out.println("SelectedNodes: " + selectedNodes.toString());
        });
        return city;
    }

    class cityClickHandler implements EventHandler<MouseEvent> { //Fixed place
        @Override
        public void handle(MouseEvent event) {
            //System.out.println("CLICKED!");
            //System.out.println("ListGraph: " + graph.getNodes().toString());

            if (event.getSource() instanceof Pane) {
                System.out.println("City found!");
            }

            String name = "";
            if (cursorIsCrossHair) {
                System.out.println("Mouse clicked");
                double x = event.getX();
                //minus 62 för att det blev fel med y axeln annars och andra lösningar icke funkna bre
                double y = event.getY() - 62;
// chats förslag för att cirkeln skapas för lågt ner
                //double localX = root.sceneToLocal(x, y).getX();
                // double localY = root.sceneToLocal(x, y).getY();
                if (cursorIsCrossHair) {
                    name = nameWindow();
                    if (name != null) {
                        createCity(name, x, y);
                    }
                    disableCrosshair();
                    newPlaceB.setDisable(false);
                }
            }

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
        if (graph.getNodes().isEmpty()) {
            System.err.println("Graph is empty!");
        }
        //System.out.println(printNodes());
        if (!imageFile.exists()) {
            System.out.println("File missing?");
        }

        try (PrintWriter writer = new PrintWriter(graphFile)) { //'try with resource' -> autoclose 'writer'
            writer.println("File:" + graphFile);
            System.out.println("Save nodes!");
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

    private void noSelectedNodesAlert (){
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

        ButtonType okButton = new ButtonType("ok");
        dialog.getDialogPane().setContent(new VBox(hbName, hbTime));
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);


        dialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType.getText() == "ok") {
                String nameInput = nameField.getText();
                String timeInput = timeField.getText();


                if (graph.pathExists(selectedNodes.get(0), selectedNodes.get(1))) {
                    showErrorMessage("Connection already exist between the two destinations.");
                    return;
                }

                if (nameInput.isEmpty() && !timeInput.matches("\\d+")) {
                    showErrorMessage("Input is not valid. Name cannot be empty and Time must contain a numeric value");
                    return;
                } else if(!timeInput.matches("\\d+")) {
                    showErrorMessage("Input is not valid. Time must contain numeric value");
                    return;
                } else if (nameInput.isEmpty()) {
                    showErrorMessage("Input is not valid. Name cannot be empty.");
                    return;
                }

                createLine();


                //create a connection from first node to second node
                graph.connect(selectedNodes.get(0), selectedNodes.get(1), nameInput, Integer.parseInt(timeInput));
                System.out.println("Create connections!");

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
            node1x = Double.parseDouble(nodeFrom.getxPos());
            node1y = Double.parseDouble(nodeFrom.getyPos());
            node2x = Double.parseDouble(nodeTo.getxPos());
            node2y = Double.parseDouble(nodeTo.getyPos());
        }

        Line line = new Line(node1x, node1y, node2x, node2y);
        line.setStrokeWidth(2);
        cities.getChildren().add(line);
    }

    public void showConnectionHandler(City from, City to, boolean edit) { //Bug: pops up twice in change connection
        if (selectedNodes.size() != 2) { //selected less than 2 nodes
            System.out.println(selectedNodes);
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
        System.out.println("Selected edge: " + edge.toString());

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
                System.out.println("Edge: " + edge.getName() + edge.getWeight());
            }
        } else {
            nameField.setEditable(false);
            timeField.setEditable(false);
        }


        //alert.showAndWait();
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
                System.out.println("Attempt to save");
                saveFile();
            } catch (IOException e) {

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

            try {
                FileReader fr = new FileReader(graphFile);
                BufferedReader in = new BufferedReader(fr);
                readNodes(in); //Add saved nodes to graph, draw nodes
                readConnections(in);
                loadImage(imageFile);
                in.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //Reads each line, splits it and creates new nodes based on parts
    private void readNodes(BufferedReader in) throws IOException { //Fixed!
        String text = "";
        in.readLine(); //skip first line
        int cityCount = 0;

        text = in.readLine();
        if (text != null) {
            String[] parts = text.split(";");
            for (int i = 0; i < parts.length; i += 3) {
                String name = parts[i];
                double x = Double.parseDouble(parts[i + 1]);
                double y = Double.parseDouble(parts[i + 2]);

                createCity(name, x, y);
                System.out.println("Coordinates: " + x + ";" + y);//Draw the node added
            }
        }
        System.out.println("Nodes: " + graph.getNodes());
    }

    private void readConnections(BufferedReader in) throws IOException {
        System.out.println("Reading connections");
        int connectionCount = 0;
        String text = "";
        for (int i = 0; i < 2; i++) { //Skip first 2 lines
            in.readLine();
        }

        while ((text = in.readLine()) != null) {
            String[] parts = text.split(";");
            for (int i = 0; i < parts.length; i += 4) { //Every other line
                String node1 = parts[i];
                String node2 = parts[i + 1];
                String edgeName = parts[i + 2];
                int weight = Integer.parseInt(parts[i + 3]);

                //Create connection
                //System.out.println("Connections: " + connectionCount + "\n" + printConnections());
                createConnection(node1, node2, edgeName, weight); //creates a 2 sided connection

                connectionCount++;

            }
        }
        //System.out.println("Connections: " + graph.printConnections());
        System.out.println("Amount: " + connectionCount);
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
        for (Object obj : graph.getNodes()) {
            if (obj instanceof City) {
                Set<Edge> edges = graph.getEdges(obj);
                City current = (City) obj;
                for (Edge e : edges) {
                    City destination = (City) e.getDestination();
                    sb.append(current.getName()).append(";").append(destination.getName() + ";").append(e.getName() + ";").append(e.getWeight())
                            .append("\n");
                }

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

    private String nameWindow() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Name");
        dialog.setHeaderText("Name of place:");

        javafx.scene.control.TextField nameField = new javafx.scene.control.TextField("Node1");

        ButtonType okButton = new ButtonType("ok");
        ButtonType cancelButton = new ButtonType("close");
        dialog.getDialogPane().setContent(new HBox(10, nameField));
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (result.get().getText() == "ok") {
                System.out.println("OK button clicked");
                String name = nameField.getText();
                Character.toUpperCase(name.charAt(0));
                return name;
            }
        }
        System.out.println("Cancel button clicked");
        dialog.close();
        return null;
    }

    private void findPath() { //Fixed
        if (selectedNodes.size() == 2) {
            List<Edge<City>> path = graph.getPath(selectedNodes.get(0), selectedNodes.get(1)); //sends reverse!
            Collections.reverse(path);
            TextArea result = new TextArea();

            javafx.scene.control.Dialog<Boolean> dialog = new javafx.scene.control.Dialog<>();
            dialog.setTitle("Message");
            dialog.setHeaderText("The path from " + selectedNodes.get(0).getName() + " to " + selectedNodes.get(1).getName());

            int totalweight = 0;
            StringBuilder message = new StringBuilder();
            for (Edge edge : path) {
                City cityToPrint = (City) edge.getDestination();
                message.append("to " + cityToPrint.getName() + " by " + edge.getName() + " takes " + edge.getWeight()).append("\n");
                totalweight += edge.getWeight();
            }
            result.setText(message.toString() + "\n" + "total " + totalweight);

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







