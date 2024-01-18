package quickplanner.application;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import quickplanner.workers.Courses;
import quickplanner.workers.Planner;
import quickplanner.workers.Task;

import java.io.IOException;

// Controller class for "My grades" scene
public class MyGradesController {
    private Planner planner;
    private Courses courses;

    @FXML
    private ListView<String> classList;

    @FXML
    private Text gpa;

    @FXML public Button Exit;


    //set this controllers planner equal to a planner from another screen
    public void setPlanner(Planner planner, Courses courses){
        this.planner = planner;
        this.courses = courses;
        classList.setItems(courses.getCourses());
        gpa.setText(String.valueOf(getGPA(planner, courses)));
    }

    @FXML public void Exit() {
        Stage stage = (Stage) Exit.getScene().getWindow();
        stage.close();
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

    @FXML public void classInfo(ActionEvent event) throws IOException{
        String course = classList.getSelectionModel().getSelectedItem();

        //load the home screen fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Course.fxml"));
        Parent next = loader.load();

        //access the controller of HomeScreen and set the planner instance there
        CourseController controller = loader.getController();
        controller.initialize(planner, courses, course);

        //go to the Home screen
        Scene scene = new Scene(next, Screen.getPrimary().getBounds().getWidth()-50, Screen.getPrimary().getBounds().getHeight()-100);
        //load the HomeScreenController
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    public double getGPA(Planner planner, Courses courses){
        double gpa = 0.0;
        ObservableList<String> classes = courses.getCourses();
        ObservableList<Task> tasks = planner.getTasks();


        for (String c : classes){
            double class_points = 0;
            double class_max = 0;
            for (Task t: tasks){
                if (t.getSubject().equals(c)){
                    class_points += t.getScoredPoints();
                    class_max += t.getTotalPoints();
                }
            }
            double percent_grade = class_points/class_max;
            gpa += percentToGPA(percent_grade);
        }
        gpa = gpa/classes.size();
        return gpa;
    }

    public double percentToGPA(double grade){
        if (grade >= 0.93) {
            return 4.0;
        }
        else if (grade >= 0.90){
            return 3.67;
        }
        else if (grade >= 0.87){
            return 3.33;
        }
        else if (grade >= 0.83){
            return 3.0;
        }
        else if (grade >= 0.80){
            return 2.67;
        }
        else if (grade >= 0.77){
            return 2.33;
        }
        else if (grade >= 0.73){
            return 2.0;
        }
        else if (grade >= 0.70) {
            return 1.67;
        }
        else if (grade >= 0.67){
            return 1.33;
        }
        else if (grade >= 0.60){
            return 1.0;
        }
        else{
            return 0.0;
        }
    }
} // end MyGradesController
