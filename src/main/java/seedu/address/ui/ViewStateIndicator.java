package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.Model;

/**
 * A ui component that displays the current view state.
 */
public class ViewStateIndicator extends UiPart<Region> {

    private static final String FXML = "ViewStateIndicator.fxml";

    @FXML
    private HBox viewStateBox;

    @FXML
    private Label viewStateLabel;

    /**
     * Creates a {@code ViewStateIndicator}.
     */
    public ViewStateIndicator() {
        super(FXML);
        viewStateLabel.getStyleClass().add("view-state-label");
        viewStateBox.getStyleClass().add("person-view-state");
    }

    /**
     * Updates the view state indicator.
     */
    public void updateViewState(Model.ViewState viewState) {
        viewStateLabel.setText(viewState.toString());

        // Apply different styles based on view state
        viewStateBox.getStyleClass().removeAll("job-view-state", "job-detail-view-state",
                                             "person-view-state", "person-detail-view-state");

        switch (viewState) {
        case JOB_VIEW:
            viewStateBox.getStyleClass().add("job-view-state");
            break;
        case JOB_DETAIL_VIEW:
            viewStateBox.getStyleClass().add("job-detail-view-state");
            break;
        case PERSON_VIEW:
            viewStateBox.getStyleClass().add("person-view-state");
            break;
        case PERSON_DETAIL_VIEW:
            viewStateBox.getStyleClass().add("person-detail-view-state");
            break;
        default:
            break;
        }
    }
}
