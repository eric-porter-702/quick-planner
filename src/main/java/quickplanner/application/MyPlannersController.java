package quickplanner.application;


import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.util.converter.FloatStringConverter;
import quickplanner.workers.*;

// Controller class for "My Planners" scene
public class MyPlannersController implements Initializable {


    // Exit button
    @FXML
    public Button Exit;

    // TableView for planner
    @FXML
    private TableView<Task> tableView;

    // Columns of table
    @FXML
    private TableColumn<Task, Boolean> StatusColumn;
    @FXML
    private TableColumn<Task, Boolean> ExtraCreditColumn;
    @FXML
    private TableColumn<Task, Float> TotalScoreColumn;

    @FXML
    private TableColumn<Task, Float> scoredPointsColumn;

    @FXML
    private TableColumn<Task, String> NameColumn;
    @FXML
    private TableColumn<Task, LocalDateTime> DueDateColumn;
    @FXML
    private TableColumn<Task, String> SubjectColumn;
    @FXML
    private TableColumn<Task, TaskType> TypeColumn;

    @FXML
    private TableColumn<Task, String> descriptionColumn;

    private Planner planner;

    private Courses courses;

    @FXML
    private TextField filter;

    @FXML Label plannerName;

    //set the planner and the tableView to a version of the planner passed from a previous controller
    public void setPlanner(Planner planner, Courses subjects, String name) {
        this.planner = planner;
        courses = subjects;
        tableView.setItems(planner.getTasks());
        //initializing the search filter
        FilteredList<Task> filteredPlanner = new FilteredList<>(planner.getTasks(), b -> true);
        filter.textProperty().addListener((observable, oldValue, newValue) -> filteredPlanner.setPredicate(Task -> {
            //if no search value then display all tasks that were previously there
            if (newValue.isEmpty() || newValue.isBlank()) {
                return true;
            }
            String keyword = newValue.toLowerCase();
            //filter for class and assignment name for now
            if (Task.getName().toLowerCase().contains(keyword)) {
                return true;//return that we have found a match for the search filter
            } else return Task.getSubject().toLowerCase().contains(keyword);//return that we have found a match for the search filter
        }));

        SortedList<Task> sortedPlanner = new SortedList<>(filteredPlanner);

        //Bind the sorted and filtered planner to the tableView
        sortedPlanner.comparatorProperty().bind(tableView.comparatorProperty());

        //set the tableView items to those who are in the filter
        tableView.setItems(sortedPlanner);

        plannerName.setText(name);

        // Save changes made to the checkboxes in extra credit and status
        for (int i = 0; i < planner.getTasks().size(); i++) {
            Task currentTask = planner.getTasks().get(i);
            currentTask.setExtraCredit(currentTask.isExtraCreditCheck().getValue());
            currentTask.setStatus(currentTask.isStatusCheck().getValue());
        }
    }

    // allows user to swap back to home screen
    // method is set to "back to home" button
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
        Scene scene = new Scene(next, Screen.getPrimary().getBounds().getWidth()-50, Screen.getPrimary().getBounds().getHeight()-100);
        //load the HomeScreenController
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    } // end switchToHome_screen()

    // Method is set to "Calendar" button
    @FXML public void switchToCalendar(ActionEvent event) throws IOException {
        //load the Calendar
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Calendar.fxml"));
        Parent next = loader.load();

        //access the controller of Calendar and add the planner instance there
        CalendarController controller = loader.getController();
        controller.setPlanner(planner, courses, plannerName.getText());
        controller.createCalendar();

        //go to the Calendar screen
        Scene scene = new Scene(next, Screen.getPrimary().getBounds().getWidth()-50, Screen.getPrimary().getBounds().getHeight()-100);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();


        stage.setScene(scene);
        stage.show();
    } // switchToMy_Planners()


    // edit task name by double-clicking name column
    public void editName(TableColumn.CellEditEvent<?, ?> editedCell) {
        Task SelectedTask = tableView.getSelectionModel().getSelectedItem();
        SelectedTask.setName(editedCell.getNewValue().toString());
    } // end editName()


    // edit due date by double-clicking date column
    public void editDate(TableColumn.CellEditEvent<?, ?> editedCell) {
        Task SelectedTask = tableView.getSelectionModel().getSelectedItem();
        SelectedTask.setDueDate((LocalDateTime) editedCell.getNewValue());
    } // end editDate()


    // edit subject by double-clicking subject column
    public void editSubject(TableColumn.CellEditEvent<?, ?> editedCell) {
        Task SelectedTask = tableView.getSelectionModel().getSelectedItem();
        SelectedTask.setSubject(editedCell.getNewValue().toString());
    } // end editSubject()


    // edit total score by double-clicking total score column
    public void editTotalScore(TableColumn.CellEditEvent<?, ?> editedCell) {
        Task SelectedTask = tableView.getSelectionModel().getSelectedItem();
        SelectedTask.setTotalPoints((Float) editedCell.getNewValue());
    } // end editTotalScore()


    // edit points scored by double-clicking scored points column
    public void editScoredPoints(TableColumn.CellEditEvent<?, ?> editedCell) {
        Task SelectedTask = tableView.getSelectionModel().getSelectedItem();
        SelectedTask.setScoredPoints((Float) editedCell.getNewValue());
    } // end editTotalScore()


    // edit task type by double-clicking type column
    public void editType(TableColumn.CellEditEvent<?, ?> editedCell) {
        Task SelectedTask = tableView.getSelectionModel().getSelectedItem();
        SelectedTask.setType((TaskType) editedCell.getNewValue());
    } // end editType()


    // edit the description by double-clicking name column
    public void editDescription(TableColumn.CellEditEvent<?, ?> editedCell) {
        Task SelectedTask = tableView.getSelectionModel().getSelectedItem();
        SelectedTask.setDescription(editedCell.getNewValue().toString());
    } // end editName()


    // calls the new task scene that adds a new task
    public void addNewTask(ActionEvent event) throws IOException {
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
    } // end addNewTask()


    // Delete a task by clicking on task from table, then clicking delete button
    public void deleteButton() {

        ObservableList<Task> selectedRows;

        // gives the rows that are selected
        selectedRows = tableView.getSelectionModel().getSelectedItems();

        // loop over the selected rows and removes from the table
        try {
            for (Task taskItem : selectedRows) {
                planner.removeTask(taskItem);
                //taskInfo.remove(taskItem);

            }
        }
        catch(Exception e) {
            System.out.println("Planner is Empty");
        }

        // end for
    } // end deleteButton()


    @FXML public void savePlanner() {
        TextInputDialog fileName = new TextInputDialog();
        fileName.setTitle("Planner Saving");
        fileName.setHeaderText("Please name the planner you wish to save: ");
        fileName.setContentText("File Name: ");

        Optional<String> userInput = fileName.showAndWait();
        String input;
        if (userInput.isPresent()){
            input = userInput.get();
            setPlanner(planner, courses, input);
            Saver saver = new Saver(input, planner);
            saver.save();
            plannerName.setText(input);
        }
        else{
            fileName.showAndWait();
        }

    }

    @FXML public void createNewPlanner() {
        //prompts the save action menu of the previous planner
        savePlanner();

        //renames the planner to New Planner and clears the planner and courses
        plannerName.setText("New Planner");
        planner = new Planner();
        courses = new Courses();
        setPlanner(planner, courses, "New Planner");
        System.out.println("New planner created!");
    }


    // Exits window
    @FXML
    public void Exit() {
        Stage stage = (Stage) Exit.getScene().getWindow();
        stage.close();
    } // end Exit()


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set up the columns in the table with attributes
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        SubjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        DueDateColumn.setCellValueFactory(new PropertyValueFactory<>("DueDate"));
        TotalScoreColumn.setCellValueFactory(new PropertyValueFactory<>("totalPoints"));
        scoredPointsColumn.setCellValueFactory(new PropertyValueFactory<>("scoredPoints"));
        ExtraCreditColumn.setCellValueFactory(new PropertyValueFactory<>("ExtraCredit"));
        StatusColumn.setCellValueFactory(new PropertyValueFactory<>("Status"));
        TypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // update the table to allow for editing
        tableView.setEditable(true);

        // allows for the data in the table columns to be edited
        NameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        DueDateColumn.setCellFactory(column -> new EditDate());

        SubjectColumn.setCellFactory(column -> new ComboBoxTableCell<>(courses.getCourses()));

        TypeColumn.setCellFactory(column -> new ComboBoxTableCell<>(TaskType.values()));

        scoredPointsColumn.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));

        ExtraCreditColumn.setCellValueFactory(cd -> cd.getValue().isExtraCreditCheck());
        ExtraCreditColumn.setCellFactory(CheckBoxTableCell.forTableColumn(ExtraCreditColumn));

        StatusColumn.setCellValueFactory(cd -> cd.getValue().isStatusCheck());
        StatusColumn.setCellFactory(CheckBoxTableCell.forTableColumn(StatusColumn));

        TotalScoreColumn.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));

        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Select multiple rows
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    } // end initialize()
}

