package quickplanner.application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.ResourceBundle;

import quickplanner.workers.Courses;
import quickplanner.workers.Planner;
import quickplanner.workers.Task;
import quickplanner.workers.TaskType;


public class NewTaskController implements Initializable {
    @FXML
    private DatePicker dueDate;

    @FXML
    private TextField name;

    @FXML
    private CheckBox extraCredit;

    @FXML
    private TextField totalPoints;

    @FXML
    private TextField scoredPoints;

    @FXML
    private ComboBox<String> subject;

    @FXML
    private TextArea taskDescription;

    @FXML
    private ComboBox<String> type;

    private Planner planner;

    private Courses courses;
    //set this controller's planner equal to a planner from another screen
    public void setPlanner(Planner planner, Courses subjects){
        this.planner = planner;
        courses = subjects;
        subject.setItems(courses.getCourses());//sets the choice-box to be the list of courses the user is taking
    }

    @FXML
    void addTask(ActionEvent event) throws IOException {
        //Get user input from input fields
        String description = name.getText();
        float totPoints = Float.parseFloat(totalPoints.getText());
        float scorePoints = Float.parseFloat(scoredPoints.getText());
        boolean eCredit = extraCredit.isSelected();
        LocalDateTime date = dueDate.getValue().atTime(23, 59);
        String course = subject.getSelectionModel().getSelectedItem();
        TaskType flag = TaskType.valueOf(type.getSelectionModel().getSelectedItem());
        String txtDescription = taskDescription.getText();


        if (eCredit) {
            totPoints = 0;
        }

        //Set Task fields based on inputs
        Task task = new Task(description, course, date, totPoints, scorePoints, eCredit, false, flag, txtDescription);

        //Attempt to add task to the planner
        planner.addTask(task);

        //load the MyPlannersController
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("My_Planners.fxml"));
        Parent next = loader.load();

        //access the controller of MyPlanners to pass the planner to the next screen
        MyPlannersController controller = loader.getController();
        controller.setPlanner(planner,courses, "Blah");

        //go to the planner screen
        Scene scene = new Scene(next, Screen.getPrimary().getBounds().getWidth()-50, Screen.getPrimary().getBounds().getHeight()-100);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    // Allows user to swap back to home screen
    // Method is set to "back to home" button
    public void switchToHome_screen(ActionEvent event) throws IOException {
        //load the home screen fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Home_screen.fxml"));
        Parent next = loader.load();

        //access the controller of HomeScreen and set the planner instance there
        HomeScreenController controller = loader.getController();
        controller.setPlanner(planner, courses);

        //go to the Home screen
        Scene scene = new Scene(next, Screen.getPrimary().getBounds().getWidth()-50, Screen.getPrimary().getBounds().getHeight()-100);
        //load the HomeScreenController
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    } // end switchToHome_screen()

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Converts the List of enums to an Array of String for ChoiceBox purposes
        type.getItems().addAll(Arrays.stream(TaskType.values()).map(Enum::toString).toArray(String[]::new));

    }
}
