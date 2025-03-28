package seedu.address.ui;

import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

/**
 * A ui for the status bar that is displayed at the footer of the application.
 */
public class StatusBarFooter extends UiPart<Region> {

    private static final String FXML = "StatusBarFooter.fxml";

    @FXML
    private Label saveLocationStatus;

    /**
     * Creates a {@code StatusBarFooter} with the given {@code Path}.
     */
    public StatusBarFooter(Path saveLocation) {
        super(FXML);

        // Ensure the status bar is visible with clear text
        saveLocationStatus.setText("Data: " + Paths.get(".").resolve(saveLocation).toString());
        saveLocationStatus.getStyleClass().add("label-bright");

        // Ensure root has appropriate styling
        getRoot().getStyleClass().add("status-bar");
    }
}
