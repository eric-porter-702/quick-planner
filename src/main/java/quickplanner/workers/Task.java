/* Task.java
 * Last edited 16 February 2023 - Eric Porter
 * Used to classify tasks in the application layer
 * TODO: add JavaDocs
 */
package quickplanner.workers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.time.LocalDateTime;

public class Task {
    // object member attributes
    private String name, subject, description;
    private LocalDateTime dueDate;
    private float totalPoints, scoredPoints;
    private boolean extraCredit;
    private TaskType type;
    private boolean status;
    private final BooleanProperty statusCheck = new SimpleBooleanProperty();
    private final BooleanProperty extraCreditCheck = new SimpleBooleanProperty();
    private final int id;

    // static member of next id
    public static int nextId = 0;

    // base constructor
    public Task(
            String name,
            String subject,
            LocalDateTime dueDate,
            float totalPoints,
            boolean extraCredit,
            TaskType type) {
        this.name = name;
        this.subject = subject;
        this.dueDate = dueDate;
        this.totalPoints = totalPoints;
        this.scoredPoints = 0.0f;
        this.extraCredit = extraCredit;
        this.type = type;
        this.id = Task.nextId;
        Task.nextId++;
    }

    // constructor for optional description
    public Task(
            String name,
            String subject,
            LocalDateTime dueDate,
            float totalPoints,
            boolean extraCredit,
            TaskType type,
            String description) {
        this.name = name;
        this.subject = subject;
        this.description = description;
        this.dueDate = dueDate;
        this.totalPoints = totalPoints;
        this.scoredPoints = 0.0f;
        this.extraCredit = extraCredit;
        this.type = type;
        this.id = Task.nextId;
        Task.nextId++;
    }

    // constructor for pulling from csv
    public Task(
            String name,
            String subject,
            LocalDateTime dueDate,
            float totalPoints,
            float scoredPoints,
            boolean extraCredit,
            boolean status,
            TaskType type,
            String description) {
        this.name = name;
        this.subject = subject;
        this.description = description;
        this.dueDate = dueDate;
        this.totalPoints = totalPoints;
        this.scoredPoints = scoredPoints;
        extraCreditCheck.setValue(extraCredit);
        this.extraCredit = extraCreditCheck.getValue();
        this.type = type;
        statusCheck.setValue(status);
        this.status = statusCheck.getValue();
        this.id = Task.nextId;
        Task.nextId++;
    }
    // getters
    public String getName() { return name; }
    public String getSubject() { return subject; }
    public String getDescription() { return description == null ? "" : description; }
    public LocalDateTime getDueDate() { return dueDate; }
    public float getTotalPoints() { return totalPoints; }
    public float getScoredPoints() { return scoredPoints; }
    public BooleanProperty isExtraCreditCheck() {return extraCreditCheck; }
    public boolean isExtraCredit() { return extraCredit; }
    public TaskType getType() { return type; }
    public BooleanProperty isStatusCheck() {return statusCheck; }
    public boolean getStatus() {return status; }
    public int getId() { return id; }

    // setters
    public void setName(String name) { this.name = name; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setDescription(String description) { this.description = description; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public void setTotalPoints(float totalPoints) { this.totalPoints = totalPoints; }
    public void setScoredPoints(float scoredPoints) { this.scoredPoints = scoredPoints; }
    public void setType(TaskType type) { this.type = type; }
    public void setExtraCredit(boolean extraCredit) { this.extraCredit = extraCredit; }
    public void setExtraCreditCheck(boolean isChecked) { this.extraCreditCheck.setValue(isChecked);}
    public void setStatus(boolean status) {this.status = status;}
    @Override
    public String toString() {
        return "%s,%s,%s,%s,%s,%s,%s,%s,%s,%d\n".formatted(
                name,
                subject,
                description,
                dueDate,
                totalPoints,
                scoredPoints,
                extraCredit,
                status,
                type,
                id
        );
    }
}