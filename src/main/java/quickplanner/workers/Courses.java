package quickplanner.workers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Courses {
    private final ObservableList<String> courses;


    public Courses() {
        this.courses = FXCollections.observableArrayList();
    }

    public ObservableList<String> getCourses() {
        return courses;
    }

    //adds a string subject to the courses list
    public void addCourse(String subject){
        if (!subject.equals("")) {
            courses.add(subject);
        }
        else{
            System.out.println("Cannot add blank course");
        }
    }

    //used to validate if the user is trying to change the course name field of Task to a valid course
    public boolean checkCourse(String input){
        return courses.contains(input);
    }
    //removes a specified course which is a string from the courses list
    public void removeCourse(String course) { courses.remove(course); }

}
