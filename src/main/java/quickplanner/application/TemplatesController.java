package quickplanner.application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import quickplanner.workers.Courses;
import quickplanner.workers.Planner;

import java.io.IOException;

// Controller class for "Templates" scene
public class TemplatesController {
    private Planner planner;

    private Courses courses;
    //set this controllers planner equal to a planner from another screen
    public void setPlanner(Planner planner, Courses courses){
        this.planner = planner;
        this.courses = courses;
        System.out.println(planner.size());
    }

    // Allows user to swap back to home screen
    // Method is set to "back to home" button
    @FXML public void switchToHome_screen(ActionEvent event) throws IOException {
        //load the MyPlannersController
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Home_screen.fxml"));
        Parent next = loader.load();

        //access the controller of MyGrades and add the planner instance there
        HomeScreenController controller = loader.getController();
        controller.setPlanner(planner, courses);

        //go to the My_Grades screen
        Scene scene = new Scene(next, Screen.getPrimary().getBounds().getWidth()-50, Screen.getPrimary().getBounds().getHeight()-100);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    } // end switchToHome_screen()
} // end TemplatesController
