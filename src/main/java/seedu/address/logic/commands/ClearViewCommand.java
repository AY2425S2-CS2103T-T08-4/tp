package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.Model;

/**
 * Clears the current detailed view and returns to the general overview.
 */
public class ClearViewCommand extends Command {

    public static final String COMMAND_WORD = "clearview";
    public static final String MESSAGE_SUCCESS = "View cleared";
    public static final String MESSAGE_NOT_IN_DETAIL_VIEW = "Not in a detailed view, nothing to clear";

    private static final Logger logger = LogsCenter.getLogger(ClearViewCommand.class);

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        // Only clear the view if we're in job detail view or person detail view
        if (model.getCurrentViewState() == Model.ViewState.JOB_DETAIL_VIEW
                || model.getCurrentViewState() == Model.ViewState.PERSON_DETAIL_VIEW) {
            // Reset state to job view
            model.setViewState(Model.ViewState.JOB_VIEW);
            return CommandResult.withClearView(MESSAGE_SUCCESS);
        } else {
            // Not in a detail view, nothing to clear
            return CommandResult.withFeedback(MESSAGE_NOT_IN_DETAIL_VIEW);
        }
    }
}
