package quickplanner.application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import quickplanner.workers.CalendarEvent;
import quickplanner.workers.Courses;
import quickplanner.workers.Planner;
import quickplanner.workers.Task;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;

public class CalendarController implements Initializable {
    @FXML private Label year;
    @FXML private Label month;
    @FXML private FlowPane calendar;
    private Planner planner;
    private Courses courses;
    private ZonedDateTime dateFocus, today;
    private int day = 0;
    private String plannerName;

    // set this controller's planner equal to a planner from another screen
    public void setPlanner(Planner planner, Courses courses, String plannerName) {
        this.planner = planner;
        this.courses = courses;
        this.plannerName = plannerName;

        // Save changes made to the checkboxes in extra credit and status
        for (int i = 0; i < planner.getTasks().size(); i++) {
            Task currentTask = planner.getTasks().get(i);
            currentTask.setExtraCredit(currentTask.isExtraCreditCheck().getValue());
            currentTask.setStatus(currentTask.isStatusCheck().getValue());
        }
    }

    @FXML
    public void switchToHome_screen(ActionEvent event) throws IOException {
        //load the home screen fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Home_screen.fxml"));
        Parent next = loader.load();

        //access the controller of HomeScreen and set the planner instance there
        HomeScreenController controller = loader.getController();
        controller.setPlanner(planner, courses);

        //go to the Home screen
        Scene scene = new Scene(
                next,
                Screen.getPrimary().getBounds().getWidth() - 50,
                Screen.getPrimary().getBounds().getHeight() - 100
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    } // end switchToHome_screen()

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
    }

    @FXML
    public void switchToCalendarDay(MouseEvent event) throws IOException {
        // load the CalendarDayController
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("CalendarDay.fxml"));
        Parent next = loader.load();

        CalendarDayController calendarDay = loader.getController();
        calendarDay.setYear(dateFocus.getYear());
        calendarDay.setMonth(String.valueOf(dateFocus.getMonth()));
        calendarDay.setDay(day);

        // access the controller of CalendarDay and add the planner instance there
        CalendarDayController controller = loader.getController();
        controller.setPlanner(planner, courses, plannerName);
        controller.checkDate();

        // go to the CalendarDay screen
        Scene scene = new Scene(
                next,
                Screen.getPrimary().getBounds().getWidth() - 50,
                Screen.getPrimary().getBounds().getHeight() - 100
        );

        //load the HomeScreenController
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }


    @FXML
    public void nextMonth() {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        createCalendar();
    }

    @FXML
    public void previousMonth() {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        createCalendar();
    }

    @FXML
    public void createCalendar() {
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        double calendarWidth = 1460;
        double calendarHeight = 625;
        double outlineWidth = 1;
        double spacingHorizontal = calendar.getHgap();
        double spacingVertical = calendar.getVgap();

        //List of activities for a given month
        Map<Integer, List<CalendarEvent>> calendarActivityMap = getCalendarActivitiesMonth(dateFocus);

        int monthMaxDate = dateFocus.getMonth().maxLength();
        // Checks for leap year
        if(dateFocus.getYear() % 4 != 0 && monthMaxDate == 29)
            monthMaxDate = 28;

        int dateOffset = ZonedDateTime.of(
                dateFocus.getYear(),
                dateFocus.getMonthValue(),
                1,
                0,
                0,
                0,
                0,
                dateFocus.getZone()
        ).getDayOfWeek().getValue();

        // loop to create blocks for each date of calendar
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {

                // create rectangle
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(outlineWidth);
                double rectangleWidth = (calendarWidth/7) - outlineWidth - spacingHorizontal;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight/6) - outlineWidth - spacingVertical;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                // creates the dates in the rectangle boxes
                int calculatedDate = (j+1)+(7*i);
                if(calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;

                    if(currentDate <= monthMaxDate) {
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = - (rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);

                        List<CalendarEvent> calendarActivities = calendarActivityMap.get(currentDate);
                        if(calendarActivities != null){
                            createCalendarActivity(calendarActivities, rectangleHeight, rectangleWidth, stackPane);
                        }
                    }

                    int finalMonthMaxDate = monthMaxDate;

                    if (currentDate > 0 && currentDate <= finalMonthMaxDate) {

                        // Turns the current day block to blue
                        if (today.getYear() == dateFocus.getYear()
                                        && today.getMonth() == dateFocus.getMonth()
                                        && today.getDayOfMonth() == currentDate
                        )
                            rectangle.setStroke(Color.BLUE);
                        else {
                            // Sets the borders to red when mouse hovers over box
                            rectangle.setOnMouseEntered(event -> rectangle.setStroke(Color.RED));

                            // Reverts border color when mouse leaves box
                            rectangle.setOnMouseExited(event -> rectangle.setStroke(Color.BLACK));
                        }
                    }

                    // Looks at tasks for the day when double-clicking box
                    rectangle.setOnMouseClicked(event ->
                    {
                        if (currentDate > 0 && currentDate <= finalMonthMaxDate) {

                            if (event.getButton().equals(MouseButton.PRIMARY)) {
                                if(event.getClickCount() == 2) {

                                    this.day = currentDate;
                                    try {
                                        switchToCalendarDay(event);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
                }
                // display calendar
                calendar.getChildren().add(stackPane);
            }
        }
    }

    private void createCalendarActivity(
            List<CalendarEvent> calendarActivities,
            double rectangleHeight,
            double rectangleWidth,
            StackPane stackPane
    ) {
        VBox calendarActivityBox = new VBox();
        for (int k = 0; k < calendarActivities.size(); k++) {
            if(k >= 2) {
                Text moreActivities = new Text("...");
                calendarActivityBox.getChildren().add(moreActivities);
                break;
            }

            // Displays information onto calendar page
            Text text = new Text(" " + calendarActivities.get(k).getTaskSubject() + ":  " + calendarActivities.get(k).getTaskName());
            calendarActivityBox.getChildren().add(text);
        }

        // Vbox for displaying text information
        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
        calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
        calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
        calendarActivityBox.setStyle("-fx-background-color:#CCCCCC");
        stackPane.getChildren().add(calendarActivityBox);
    }

    private Map<Integer, List<CalendarEvent>> createCalendarMap(List<CalendarEvent> calendarActivities) {
        Map<Integer, List<CalendarEvent>> calendarActivityMap = new HashMap<>();

        for (CalendarEvent activity: calendarActivities) {
            int activityDate = activity.getCalendarDate().getDayOfMonth();
            if(!calendarActivityMap.containsKey(activityDate)) {
                calendarActivityMap.put(activityDate, List.of(activity));
            }
            else {
                List<CalendarEvent> OldListByDate = calendarActivityMap.get(activityDate);

                List<CalendarEvent> newList = new ArrayList<>(OldListByDate);
                newList.add(activity);
                calendarActivityMap.put(activityDate, newList);
            }
        }
        return  calendarActivityMap;
    }

    private Map<Integer, List<CalendarEvent>> getCalendarActivitiesMonth(ZonedDateTime dateFocus) {
        List<CalendarEvent> calendarActivities = new ArrayList<>();
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

            // Checks to make sure that task name and subject appear on the month view at the correct date.
            if (Integer.parseInt(dueDateMonth) == dateFocus.getMonthValue() && Integer.parseInt(dueDateYear) == dateFocus.getYear() && !currentTask.getStatus()) {
                ZonedDateTime time = ZonedDateTime.of(Integer.parseInt(dueDateYear), Integer.parseInt(dueDateMonth), Integer.parseInt(dueDateDay), 16, 0, 0, 0, dateFocus.getZone());
                calendarActivities.add(new CalendarEvent(time, currentTask.getName(), currentTask.getSubject()));
            }
        }
        return createCalendarMap(calendarActivities);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
    }
}
