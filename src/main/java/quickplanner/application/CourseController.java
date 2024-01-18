package quickplanner.application;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.converter.FloatStringConverter;
import quickplanner.workers.*;


import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

// Controller class for "My grades" scene
public class CourseController implements Initializable {
    private Planner planner;
    private Courses courses;

    @FXML
    private Text courseName;

    @FXML
    private Text percentGrade;

    @FXML
    private TableColumn<Task, Float> ActualScoreColumn;

    @FXML
    private TableColumn<Task, String> NameColumn;

    @FXML
    private TableColumn<Task, Float> TotalScoreColumn;

    @FXML
    private TableColumn<Task, TaskType> TypeColumn;

    @FXML
    private TableView<Task> tableView;

    //set this controllers planner equal to a planner from another screen
    public void initialize(Planner planner, Courses courses, String course){
        this.planner = planner;
        this.courses = courses;
        courseName.setText(course);
        percentGrade.setText(String.valueOf(getGrade(planner, course)));

        ObservableList <Task> courseTasks = planner.getTasksForCourse(planner.getTasks(), course);
        tableView.setItems(courseTasks);
    }

    // Allows user to swap back to home screen
    // Method is set to "back to home" button
    @FXML public void switchToHome_screen(ActionEvent event) throws IOException {
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

    public double getGrade(Planner planner, String name){
        ObservableList<Task> tasks = planner.getTasks();

        double class_points = 0;
        double class_max = 0;
        for (Task t: tasks){
            if (t.getSubject().equals(name)){
                class_points += t.getScoredPoints();
                class_max += t.getTotalPoints();
            }
        }


        double val = 100.00 * (class_points/class_max);
        val = 100.0 * val;
        val = Math.round(val);
        val = val /100.0;
        return val;
    }

    @FXML public void goBack(ActionEvent event) throws IOException {
        //load the home screen fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("My_Grades.fxml"));
        Parent next = loader.load();

        //access the controller of HomeScreen and set the planner instance there
        MyGradesController controller = loader.getController();
        controller.setPlanner(planner, courses);

        //go to the Home screen
        Scene scene = new Scene(next, Screen.getPrimary().getBounds().getWidth()-50, Screen.getPrimary().getBounds().getHeight()-100);
        //load the HomeScreenController
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void editScoredPoints(TableColumn.CellEditEvent<?, ?> editedCell) {
        Task SelectedTask = tableView.getSelectionModel().getSelectedItem();
        SelectedTask.setScoredPoints((Float) editedCell.getNewValue());
        //recalculate grade changes dynamically
        percentGrade.setText(String.valueOf(getGrade(planner, courseName.getText())));

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        ActualScoreColumn.setCellValueFactory(new PropertyValueFactory<>("scoredPoints"));
        TotalScoreColumn.setCellValueFactory(new PropertyValueFactory<>("totalPoints"));


        // update the table to allow for editing
        tableView.setEditable(true);
        ActualScoreColumn.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        //ActualScoreColumn.setCellFactory(col -> new editScoredPoints());
    }
} // end MyGradesController
