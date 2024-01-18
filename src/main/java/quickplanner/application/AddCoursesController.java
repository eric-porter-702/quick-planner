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
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import quickplanner.workers.Courses;
import quickplanner.workers.Planner;
import quickplanner.workers.Task;

import java.io.IOException;

public class AddCoursesController {
    @FXML private TextField input;
    @FXML private ListView<String> courseView;
    private Planner planner;
    private Courses courses;
    @FXML public Button Exit;

    // set this controllers planner equal to a planner from another screen
    public void setPlanner(Planner planner, Courses courses){
        this.planner = planner;
        this.courses = courses;
        courseView.setItems(courses.getCourses());
    }

    @FXML
    public void addCourse() {
        courses.addCourse(input.getText());
        System.out.println("Course added: " + input.getText());
    }

    @FXML
    public void deleteCourse() {
        // get the subject the user wants to delete
        String subject = courseView.getSelectionModel().getSelectedItem();
        // get all the classes for the deleted subject
        ObservableList<Task> removedTasks = planner.getTasksForCourse(planner.getTasks(), subject);
        // automatically remove all the classes for the selected subject
        planner.removeTasks(removedTasks);
        // remove the selected subject from the list of courses
        courses.removeCourse(subject);
    }

    // return to home screen
    @FXML
    public void goHome(ActionEvent event) throws IOException {
        // load the home screen fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Home_screen.fxml"));
        Parent next = loader.load();

        // access the controller of HomeScreen and set the courses instance there
        HomeScreenController controller = loader.getController();
        controller.setPlanner(planner, courses);

        // go to the Home screen
        Scene scene = new Scene(
                next,
                Screen.getPrimary().getBounds().getWidth() - 50,
                Screen.getPrimary().getBounds().getHeight() - 100
        );
        // load the HomeScreenController
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML public void Exit() {
        Stage stage = (Stage) Exit.getScene().getWindow();
        stage.close();
    }
}
