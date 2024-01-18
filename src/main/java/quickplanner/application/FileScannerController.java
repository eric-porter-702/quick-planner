package quickplanner.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import quickplanner.workers.Courses;
import quickplanner.workers.Planner;
import quickplanner.workers.Task;
import quickplanner.workers.TaskType;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class FileScannerController {

    Planner planner;
    Courses courses;
    File file;

    @FXML
    private TextArea taskList;

    //set this controllers planner equal to a planner from another screen
    public void setPlanner(Planner planner, Courses courses, File file){
        this.planner = planner;
        this.courses = courses;
        this.file = file;
    }

    public ObservableList<Task> findTasks(String scan, String course){
        ObservableList<Task> tasks = FXCollections.observableArrayList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy");

        //split string into an array of lines by splitting along the new line regex
        String [] lines = scan.split("\\R");

        //loop through all lines and figure out which lines have valid dates, add those to an arrayList of lines
        for (String s : lines) {
            if (s.contains("/")) {//check to see if each line has slashes
                int slash = s.indexOf("/");
                char[] chars = s.toCharArray();
                int start, end, possibility;
                if (slash != 0 && slash != lines.length - 1) {
                    if (Character.isDigit(chars[slash - 1]) && Character.isDigit(chars[slash + 1])) {//if its day and month separated by slash

                        if (slash + 1 != chars.length - 1) {
                            //different date month formats
                            if (Character.isDigit(chars[slash - 2]) && Character.isDigit(chars[slash + 2])) {
                                start = slash - 2;
                                end = slash + 2;
                                possibility = 1;
                            } else if (!Character.isDigit(chars[slash - 2]) && Character.isDigit(chars[slash + 2])) {
                                start = slash - 1;
                                end = slash + 2;
                                possibility = 2;
                            } else if (Character.isDigit(chars[slash - 2]) && !Character.isDigit(chars[slash + 2])) {
                                start = slash - 2;
                                end = slash + 1;
                                possibility = 3;
                            } else {
                                start = slash - 1;
                                end = slash + 1;
                                possibility = 4;
                            }
                        } else {
                            start = slash - 1;
                            end = slash + 1;
                            possibility = 4;
                        }
                        //extract both the name and date of each task
                        String date = s.substring(start, end + 1);//the date is the substring from the start to the end of the date
                        String name = s.replace(date, "");//the name is the rest of the string without the date

                        //convert the String date into LocalDate format
                        String fullDate;
                        //add necessary zeroes in order for every month day input to be MM/dd
                        if (possibility == 2) {
                            fullDate = "0" + date;
                        } else if (possibility == 3) {
                            fullDate = date.substring(0, date.length() - 1) + "0" + date.substring(date.length() - 1);
                        } else if (possibility == 4) {
                            fullDate = "0" + date.substring(0, date.length() - 1) + "0" + date.substring(date.length() - 1);
                        } else {
                            fullDate = date;
                        }
                        fullDate += "/2023";//add the year to the fullDate
                        //parse the date into a LocalDate using the formatter declared at the top of the method
                        LocalDate localDate = LocalDate.parse(fullDate, formatter);

                        //add each task to the tasks
                        tasks.add(new Task(name, course, localDate.atTime(LocalTime.now()), 100, false, TaskType.OTHER));
                    }
                }
            }
        }
        return tasks;
    }


    @FXML
    public void reselectFile() {
        System.out.println("Implement reselection later");
    }

    @FXML
    public void selectPages() throws IOException {
        //ask the user to specify on what page of the syllabus the schedule is on

        /*TextInputDialog first = new TextInputDialog();//creates an input dialog that forces the user to enter a number
        first.setTitle("Page Number Input");
        first.setHeaderText("Please enter the page number of the course schedule: ");
        first.setContentText("Page Number: ");

        //set the page number
        Optional<String> pageNumber = first.showAndWait();
        String number = "";
        if (pageNumber.isPresent()){
            number = pageNumber.get();
        }
        else{
            first.showAndWait();
        }
        commented out for purposes of the demo*/
        //load the PDF file
        PDDocument document = PDDocument.load(file);

        //Instantiate PDFTextStripper class
        PDFTextStripper pdfStripper = new PDFTextStripper();
        //Only strip the text from the specified page of the document that contains the Course Schedule
        //pdfStripper.setPageStart(number);
        pdfStripper.setSortByPosition(true);


        //Retrieving text from PDF document
        String data;
        try {
            data = pdfStripper.getText(document);
            System.out.println("Successfully extracted text from PDF");

        } catch (IOException e) {
            System.out.println("An error occurred.");
            throw new RuntimeException(e);
        }
        //Closing the document
        document.close();

        TextInputDialog second = new TextInputDialog();
        second.setTitle("Course Name Input");
        second.setHeaderText("Please enter the course name of the selected syllabus: ");
        second.setContentText("Course Name: ");

        //set the page number
        Optional<String> userInput = second.showAndWait();
        String courseName = "";
        if (userInput.isPresent()){
            courseName = userInput.get();
            courses.addCourse(courseName);//add the course name to the list of courses

        }
        else{
            second.showAndWait();
        }

        ObservableList<Task> tasks = findTasks(data, courseName);
        planner.addTasks(tasks);
        //planner.addTask(new Task("test", courseName, LocalDate.now(), 100, false, TaskType.OTHER));
        taskList.setText("Scrapper found " + planner.size() + " tasks, press load to continue");
    }

    public void loadNewPlanner(ActionEvent event) throws IOException{
        //load the MyPlannersController
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("My_Planners.fxml"));
        Parent next = loader.load();

        //access the controller of MyPlanners and add the planner instance there
        MyPlannersController controller = loader.getController();
        controller.setPlanner(planner, courses, "My Planner");

        //go to the My_Planners screen
        Scene scene = new Scene(next, Screen.getPrimary().getBounds().getWidth()-50, Screen.getPrimary().getBounds().getHeight()-100);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //return to home screen
    @FXML
    public void goHome(ActionEvent event) throws IOException {
        //load the home screen fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Home_screen.fxml"));
        Parent next = loader.load();

        //access the controller of HomeScreen and set the courses instance there
        HomeScreenController controller = loader.getController();
        controller.setPlanner(planner, courses);

        //go to the Home screen
        Scene scene = new Scene(next, Screen.getPrimary().getBounds().getWidth()-50, Screen.getPrimary().getBounds().getHeight()-100);
        //load the HomeScreenController
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}


