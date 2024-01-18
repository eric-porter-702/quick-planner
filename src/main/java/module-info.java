module team7drexel.quickplanner {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.pdfbox;
	requires java.desktop;

	opens quickplanner.application to javafx.fxml;
    opens quickplanner.workers to javafx.fxml;

    exports quickplanner.application;
    exports quickplanner.workers;
}