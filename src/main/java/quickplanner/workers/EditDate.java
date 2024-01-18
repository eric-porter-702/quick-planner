package quickplanner.workers;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class EditDate extends TableCell<Task, LocalDateTime> {
    private DatePicker datePicker;

    public EditDate() {
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        if (isEditing()) {
            if (datePicker != null) {
                datePicker.setValue(LocalDate.from(getDate()));
            }
            setText(null);
            setGraphic(datePicker);
        } else {
                setText(getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
                setGraphic(null);
        }
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createDatePicker();
            setText(null);
            setGraphic(datePicker);
        }
    }
    // create datePicker when user clicks on cell
    private void createDatePicker() {
        datePicker = new DatePicker(LocalDate.from(getDate()));
        datePicker.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        datePicker.setOnAction((event) -> commitEdit(LocalDateTime.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()))));

    }
    @Override
    public void updateItem(LocalDateTime item, boolean empty) {
        super.updateItem(item, empty);

        // keeps the data in the rows below the inserted date empty
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            // Saves the date that was selected and updates it into the planner
            if (isEditing()) {
                if (datePicker != null) {
                    datePicker.setValue(LocalDate.from(getDate()));
                }
                setText(null);
                setGraphic(datePicker);
            } else {
                setText(getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
                setGraphic(null);
            }
        }
    }

    // Gets the date that was chosen from the datePicker
    private LocalDateTime getDate() {
        return getItem() == null ? LocalDateTime.now() : LocalDateTime.from(getItem()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
