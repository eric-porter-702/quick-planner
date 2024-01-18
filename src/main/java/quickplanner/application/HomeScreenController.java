package quickplanner.application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Screen;
import javafx.stage.Stage;

//imports for file upload
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import quickplanner.workers.*;


import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

// Class for Home Screen
public class HomeScreenController extends Application implements Initializable {
    // Exit button
    @FXML public Button Exit;

    private Planner planner;//creates the planner that will store the users plan information

    private Courses courses;//creates the courses observable list that will store all the courses for a planner

    private Saver saver;

    private String plannerName;

    public static Notifier notifier;

    private String[] settings = new String[] {"QuickPlanner", "24", "12", "no", "no"};

    //FileChooser declaration
    FileChooser fileChooser = new FileChooser();
    FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");


    // Opens Home_screen window
    @Override public void start(Stage stage) {
        // Try catch if Home_screen can't open
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HomeScreenController.class.getResource("Home_screen.fxml"));
            Parent root1 = fxmlLoader.load();
            stage.setTitle("Quick Planner");
            stage.setScene(new Scene(root1, Screen.getPrimary().getBounds().getWidth()-50, Screen.getPrimary().getBounds().getHeight()-100));
            stage.show();
        }
        catch (Exception e) {
            System.out.println("Cannot load new window");
        }
    }  // end start()

    //set this controller's planner equal to a planner from another screen
    //set the home controllers courses equal to the new list of courses that will be passed from the AddCourses Controller
    public void setPlanner(Planner planner, Courses courses){
        this.planner = planner;
        this.courses = courses;
        notifier.setPlanner(planner);

        // Save changes made to the checkboxes in extra credit and status
        for (int i = 0; i < planner.getTasks().size(); i++) {
            Task currentTask = planner.getTasks().get(i);
            currentTask.setExtraCredit(currentTask.isExtraCreditCheck().getValue());
            currentTask.setStatus(currentTask.isStatusCheck().getValue());
        }
    }

    public void altSet(String[] settings){
        this.settings = settings;
    }


    // Method is set to "My Planner" button
    @FXML public void switchToMy_Planners(ActionEvent event) throws IOException {
        //load the MyPlannersController
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("My_Planners.fxml"));
        Parent next = loader.load();

        //access the controller of MyPlanners and add the planner instance there
        MyPlannersController controller = loader.getController();
        controller.setPlanner(planner, courses, plannerName);

        //go to the My_Planners screen
        Scene scene = new Scene(next, Screen.getPrimary().getBounds().getWidth()-50, Screen.getPrimary().getBounds().getHeight()-100);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    } // switchToMy_Planners()

    // Method is set to "Templates" button
    @FXML public void switchToTemplates(ActionEvent event) throws IOException {
        //load the MyPlannersController
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Templates.fxml"));
        Parent next = loader.load();

        //access the controller of MyGrades and add the planner instance there
        TemplatesController controller = loader.getController();
        controller.setPlanner(planner, courses);

        //go to the My_Grades screen
        Scene scene = new Scene(next, Screen.getPrimary().getBounds().getWidth()-50, Screen.getPrimary().getBounds().getHeight()-100);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    } // switchToTemplates()

    @FXML public void switchToSettingsMenu(ActionEvent event) throws IOException {
        //load the MyPlannersController
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("SettingsMenu.fxml"));
        Parent next = loader.load();

        //access the controller of MyGrades and add the planner instance there
        SettingsMenuController controller = loader.getController();
        controller.setPlanner(planner, courses, settings);

        //go to the My_Grades screen
        Scene scene = new Scene(next, Screen.getPrimary().getBounds().getWidth()-50, Screen.getPrimary().getBounds().getHeight()-100);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    // Method is set to "Templates" button
    @FXML public void switchToMy_Grades(ActionEvent event) throws IOException {
        //load the MyPlannersController
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("My_Grades.fxml"));
        Parent next = loader.load();

        //access the controller of MyGrades and add the planner instance there
        MyGradesController controller = loader.getController();
        controller.setPlanner(planner, courses);

        //go to the My_Grades screen
        Scene scene = new Scene(next, Screen.getPrimary().getBounds().getWidth()-50, Screen.getPrimary().getBounds().getHeight()-100);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    } // switchToMy_Grades()


    //Method is set to "Add Manually" button
    @FXML
    public void createTask(ActionEvent event) throws IOException {
        //load the NewTaskController
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewTask.fxml"));
        Parent next = loader.load();

        //access the controller of NewTask and add the planner instance there
        NewTaskController controller = loader.getController();
        controller.setPlanner(planner, courses);

        //go to the newTask screen
        Scene scene = new Scene(next, Screen.getPrimary().getBounds().getWidth()-50, Screen.getPrimary().getBounds().getHeight()-100);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }//switch to newTask scene

    @FXML
    public void addCourses(ActionEvent event) throws IOException {
        //load the NewTaskController
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Add_Courses.fxml"));
        Parent next = loader.load();

        //access the controller of addCourses and add the planner instance there
        AddCoursesController controller = loader.getController();
        controller.setPlanner(planner, courses);

        //go to the addCourses screen
        Scene scene = new Scene(next, Screen.getPrimary().getBounds().getWidth()-50, Screen.getPrimary().getBounds().getHeight()-100);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    // Method is set to "Exit" button
    // Home_screen window will close when pressed
    @FXML public void Exit() {
        Stage stage = (Stage) Exit.getScene().getWindow();
        stage.close();
        if (notifier != null) {
            System.out.println("test");
            notifier.pause();
            notifier.shutdown();
        }
        if (saver != null) {
            System.out.println("test2");
            saver.stopAutoSave();
            saver.shutdown();
        }
    } // end Exit()

    //Method for selecting and extracting the text from a PDF File
    //Activated upon clicked the Upload Syllabus button
    @FXML public void selectFile(ActionEvent event) throws  IOException{
        //open File Explorer via FileChooser
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(new Stage());

        //load the FileScannerController
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("File_Scanner.fxml"));
        Parent next = loader.load();

        //access the controller of FIleScanner and add the planner instance there
        FileScannerController controller = loader.getController();
        controller.setPlanner(planner, courses, file);

        //go to the File_Scanner screen
        Scene scene = new Scene(next, Screen.getPrimary().getBounds().getWidth()-50, Screen.getPrimary().getBounds().getHeight()-100);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML public void savePlanner() {
        TextInputDialog fileName = new TextInputDialog();
        fileName.setTitle("Planner Saving");
        fileName.setHeaderText("Please name the planner you wish to save: ");
        fileName.setContentText("File Name: ");

        Optional<String> userInput = fileName.showAndWait();
        String input;
        if (userInput.isPresent()){
            input = userInput.get();
            saver = new Saver(input, planner);
            saver.save();
        }
        else{
            fileName.showAndWait();
        }

    }

    @FXML public void importPlanner() {
        Planner buffer;
        TextInputDialog fileName = new TextInputDialog();
        fileName.setTitle("Planner Loading");
        fileName.setHeaderText("Please name the planner you wish to load in: ");
        fileName.setContentText("File Name: ");

        Optional<String> userInput = fileName.showAndWait();
        String input;
        if (userInput.isPresent()){
            //load in the user input into the loader
            input = userInput.get();
            Loader loader = new Loader(input);
            System.out.println("insane");
            //load the csv into a buffer and then set the planner equal to the loaded planner
            buffer = loader.load();
            planner = buffer;
            notifier.setPlanner(planner);

            //get all the courses the user had from the planner and add them to the courses list
            courses = planner.getAllCourses(planner.getTasks());

            //get the planner name from the import name
            plannerName = input;
        }
        else{
            fileName.showAndWait();
        }
    }


    public static void main(String[] args) {
        launch();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        planner = new Planner();//initialize a new blank planner upon program start
        courses = new Courses();//initialize a null course List upon program start

        //initialize the notifier
        try {
            notifier = new Notifier(planner, 60);
            notifier.setNotificationService();
            notifier.setReNotifierService();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {
        System.exit(0);
    }
}


