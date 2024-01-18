package quickplanner.application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import quickplanner.workers.Courses;
import quickplanner.workers.Planner;

import java.io.IOException;

public class SettingsMenuController {

    @FXML
    private TextField autoSave;

    @FXML
    private Text autosaveText = new Text();

    @FXML
    private TextField directoryName;

    @FXML
    private Text directoryText = new Text();

    @FXML
    private TextField lastSaved;

    @FXML
    private Text lastsavedText = new Text();

    @FXML
    private TextField notificationRepeat;

    @FXML
    private TextField notificationTimer;

    @FXML
    private Text notirepeatText = new Text();

    @FXML
    private Text notistartText = new Text();

    @FXML public Button Exit;

    private Planner planner;
    private Courses courses;
    private String[] settings;
    //set this controllers planner equal to a planner from another screen
    public void setPlanner(Planner planner, Courses courses, String[] settings){
        this.planner = planner;
        this.courses = courses;
        this.settings = settings;

        directoryText.setText(settings[0]);
        notistartText.setText(settings[1]);
        notirepeatText.setText(settings[2]);
        autosaveText.setText(settings[3]);
        lastsavedText.setText(settings[4]);
    }

    // Allows user to swap back to home screen
    // Method is set to "back to home" button
    @FXML
    public void switchToHome_screen(ActionEvent event) throws IOException {
        //load the MyPlannersController
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Home_screen.fxml"));
        Parent next = loader.load();

        //access the controller of MyGrades and add the planner instance there
        HomeScreenController controller = loader.getController();
        controller.setPlanner(planner, courses);
        controller.altSet(settings);

        //go to the My_Grades screen
        Scene scene = new Scene(next, Screen.getPrimary().getBounds().getWidth()-50, Screen.getPrimary().getBounds().getHeight()-100);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    } // end switchToHome_screen()

    @FXML
    public void saveSettings(ActionEvent event) throws IOException{
        if (!(directoryName.getText().isBlank())){
            directoryText.setText(directoryName.getText());
            settings[0] = directoryName.getText();
        }
        if (!(notificationTimer.getText().isBlank())){
            notistartText.setText(notificationTimer.getText());
            settings[1] = notificationTimer.getText();
        }
        if (!(notificationRepeat.getText().isBlank())){
            notirepeatText.setText(notificationRepeat.getText());
            settings[2] = notificationRepeat.getText();
        }
        if (!(autoSave.getText().isBlank())){
            System.out.println(autoSave.getText());
            autosaveText.setText(autoSave.getText());
            settings[3] = autoSave.getText();
        }
        if (!(lastSaved.getText().isBlank())){
            lastsavedText.setText(lastSaved.getText());
            settings[4] = lastSaved.getText();
        }

    }

    @FXML public void Exit() {
        Stage stage = (Stage) Exit.getScene().getWindow();
        stage.close();
    }
}
