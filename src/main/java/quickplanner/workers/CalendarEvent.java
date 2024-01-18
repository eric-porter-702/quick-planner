package quickplanner.workers;

import java.time.ZonedDateTime;

// Used to display the task name and task subject from planner onto the calendar month view
public class CalendarEvent {
    private final ZonedDateTime calendarDate;
    private final String taskName;
    private final String taskSubject;

    public CalendarEvent(ZonedDateTime date, String name, String subject) {
        this.calendarDate = date;
        this.taskName = name;
        this.taskSubject = subject;
    }

    public ZonedDateTime getCalendarDate() {
        return calendarDate;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskSubject() {
        return taskSubject;
    }
}
