/* Planner.java
 * Arranges a list of tasks in an ObservableList as a planner
 * TODO: Add JavaDoc
 */

package quickplanner.workers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Comparator;

@SuppressWarnings("unused")
public class Planner {
    private final ObservableList<Task> tasks;

    // base constructor
    public Planner() {
        this.tasks = FXCollections.observableArrayList();
    }

    // TODO: add other constructor to grab from an array

    public ObservableList<Task> getTasks() {
        return tasks;
    }

    //used to get access the size of the arraylist in other files
    public int size(){
        return tasks.size();
    }

    public void addTask(Task task){
        if (task.getName() != null) tasks.add(task);
        else System.err.println("TASK IS NULL!");
    }

    public void removeTask(Task task) { tasks.remove(task); }

    public void sortByDate() { tasks.sort(Comparator.comparing(Task::getDueDate)); }

    public void sortBySubject() { tasks.sort(Comparator.comparing(Task::getSubject)); }

    public void sortByName() { tasks.sort(Comparator.comparing(Task::getName)); }

    //returns an Arraylist of tasks that are to be deleted for a certain subject for a task list
    public ObservableList<Task> getTasksForCourse(ObservableList<Task> assignments, String course){
        ObservableList<Task> cloner = FXCollections.observableArrayList();
        for (Task t : assignments){
            if (t.getSubject().equals(course)){//if the task belongs to the input course
                cloner.add(t);
            }
        }
        return cloner;
    }

    public void addTasks(ObservableList<Task> assignments){
        tasks.addAll(assignments);
    }

    //remove all specified items from the list of tasks, used when removed all tasks from a particular course
    public void removeTasks(ObservableList<Task> assignments){
        tasks.removeAll(assignments);
    }


    public Courses getAllCourses(ObservableList<Task> assignments){
        Courses courses = new Courses();

        for (Task t: assignments){
            String current = t.getSubject();
            if (!courses.checkCourse(current)){
                courses.addCourse(current);
            }
        }
        return courses;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        tasks.forEach(task -> sb.append(task.toString()));
        return sb.toString();
    }
}
