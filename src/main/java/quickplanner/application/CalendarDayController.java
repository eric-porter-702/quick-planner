package quickplanner.application;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;
import javafx.stage.Stage;
import quickplanner.workers.*;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;


public class CalendarDayController implements Initializable{

    private Planner planner;

    private Courses courses;

    private int yearCheck;

    private String monthCheck;

    private int dayCheck;

    // TableView for planner
    @FXML
    private TableView<Task> tableView;

    @FXML
    private Label year;

    @FXML
    private Label month;

    @FXML
    private Label day;

    @FXML
    private TableColumn<Task, String> subjectColumn;

    @FXML
    private TableColumn<Task, TaskType> typeColumn;

    @FXML
    private TableColumn<Task, String> nameColumn;

    @FXML
    private TableColumn<Task, String> descriptionColumn;

    private String plannerName;

    public void setPlanner(Planner planner, Courses courses, String plannerName) {
        this.planner = planner;
        this.courses = courses;
        this.plannerName = plannerName;
    }

    public void setYear(int year) {
        this.year.setText(String.valueOf(year));
        this.yearCheck = year;
    }

    public void setMonth(String month) {
        this.month.setText(month);
        this.monthCheck = month;
    }

    public void setDay(int day) {
        this.day.setText(String.valueOf(day));
        this.dayCheck = day;
    }

    public void checkDate() {
        for (int i = 0; i < planner.getTasks().size(); i++) {
            Task currentTask = planner.getTasks().get(i);

            LocalDateTime plannerDate = currentTask.getDueDate();
            LocalDate dateToSplit = plannerDate.toLocalDate();

            String[] splitDate = String.valueOf(dateToSplit).split("-", 3);
            String dueDateYear = splitDate[0];
            String dueDateMonth = splitDate[1];
            String dueDateDay = splitDate[2];

            char zeroDay = dueDateDay.charAt(0);
            char zeroMonth = dueDateMonth.charAt(0);

            if (zeroDay == '0') {
                dueDateDay = dueDateDay.substring(1);
            }
            if (zeroMonth == '0') {
                dueDateMonth = dueDateMonth.substring(1);
            }

            // converts numbered month of due date to named month
            String dueDateMonthName = getMonth(Integer.parseInt(dueDateMonth)).toUpperCase();


            // Checks if the date entered by the user on the planner is equal to the date the user clicks on in the calendar
            // if so, then display tasks that are due on the day the user entered
            if (Integer.parseInt(dueDateDay) == dayCheck && dueDateMonthName.equals(monthCheck) && Integer.parseInt(dueDateYear) == yearCheck && !currentTask.getStatus()) {
                // add data to table
                ObservableList<Task> dayTask = tableView.getItems();
                dayTask.add(currentTask);
                tableView.setItems(dayTask);
            }
        }
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }

    // Method is set to "Calendar" button
    @FXML public void switchToCalendar(ActionEvent event) throws IOException {
        //load the Calendar
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Calendar.fxml"));
        Parent next = loader.load();

        //access the controller of Calendar and add the planner instance there
        CalendarController controller = loader.getController();
        controller.setPlanner(planner, courses, plannerName);
        controller.createCalendar();

        //go to the Calendar screen
        Scene scene = new Scene(next, Screen.getPrimary().getBounds().getWidth()-50, Screen.getPrimary().getBounds().getHeight()-100);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    } // switchToMy_Planners()

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set up the columns in the table with attributes
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    }
}
