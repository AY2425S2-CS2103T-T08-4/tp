package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final String feedbackToUser;

    /** Help information should be shown to the user. */
    private final boolean showHelp;

    /** The application should exit. */
    private final boolean exit;

    /** Whether the job view should be shown. */
    private final boolean toggleView;

    /** Whether the job view should be shown. */
    private final boolean viewJob;

    /** Whether the person view should be shown. */
    private final boolean viewPerson;

    /** Whether to clear the current view. */
    private final boolean clearView;

    /** Whether to refresh the job view. */
    private final boolean refreshJobView;

    /** Job index. */
    private final int jobIndex;

    /** Person index. */
    private final int personIndex;

    /** Whether applications have been updated and need to be refreshed. */
    private final boolean refreshApplications;

    /**
     * Private constructor used by static factory methods.
     */
    private CommandResult(String feedbackToUser, boolean showHelp, boolean exit, boolean toggleView,
                         boolean viewJob, boolean viewPerson, boolean clearView, boolean refreshJobView,
                         int jobIndex, int personIndex, boolean refreshApplications) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.toggleView = toggleView;
        this.viewJob = viewJob;
        this.viewPerson = viewPerson;
        this.clearView = clearView;
        this.refreshJobView = refreshJobView;
        this.jobIndex = jobIndex;
        this.personIndex = personIndex;
        this.refreshApplications = refreshApplications;
    }

    /**
     * Creates a command result with default parameters.
     */
    public static CommandResult withFeedback(String feedbackToUser) {
        return new CommandResult(
            feedbackToUser, false, false, false,
            false, false, false, false,
            -1, -1, false
        );
    }

    /**
     * Creates a command result with help display.
     */
    public static CommandResult withHelp(String feedbackToUser) {
        return new CommandResult(
            feedbackToUser, true, false, false,
            false, false, false, false,
            -1, -1, false
        );
    }

    /**
     * Creates a command result that signals exit.
     */
    public static CommandResult withExit(String feedbackToUser) {
        return new CommandResult(
            feedbackToUser, false, true, false,
            false, false, false, false,
            -1, -1, false
        );
    }

    /**
     * Creates a command result that toggles view.
     */
    public static CommandResult withToggleView(String feedbackToUser) {
        return new CommandResult(
            feedbackToUser, false, false, true,
            false, false, false, false,
            -1, -1, false
        );
    }

    /**
     * Creates a command result for viewing job.
     */
    public static CommandResult withJobView(String feedbackToUser, int jobIndex) {
        return new CommandResult(
            feedbackToUser, false, false, false,
            true, false, false, false,
            jobIndex, -1, false
        );
    }

    /**
     * Creates a command result for viewing person.
     */
    public static CommandResult withPersonView(String feedbackToUser, int jobIndex, int personIndex) {
        return new CommandResult(
            feedbackToUser, false, false, false,
            true, true, false, false,
            jobIndex, personIndex, false
        );
    }

    /**
     * Creates a command result with clear view.
     */
    public static CommandResult withClearView(String feedbackToUser) {
        return new CommandResult(
            feedbackToUser, false, false, false,
            false, false, true, false,
            -1, -1, false
        );
    }

    /**
     * Creates a command result with refresh job view.
     */
    public static CommandResult withRefreshJobView(String feedbackToUser) {
        return new CommandResult(
            feedbackToUser, false, false, false,
            false, false, true, true,
            -1, -1, false
        );
    }

    /**
     * Creates a command result with refresh job view without clearing the view.
     * This is useful for commands that need to refresh the UI but not clear filters.
     */
    public static CommandResult withRefreshJobViewOnly(String feedbackToUser) {
        return new CommandResult(
            feedbackToUser, false, false, false,
            false, false, false, true,
            -1, -1, false
        );
    }

    /**
     * Creates a command result with refreshed applications.
     */
    public static CommandResult withRefreshApplications(String feedbackToUser) {
        return new CommandResult(
            feedbackToUser, false, false, false,
            false, false, false, false,
            -1, -1, true
        );
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isExit() {
        return exit;
    }

    public boolean setToggleView() {
        return toggleView;
    }

    public boolean isViewJob() {
        return viewJob;
    }

    public boolean isViewPerson() {
        return viewPerson;
    }

    public boolean isClearView() {
        return clearView;
    }

    public boolean isRefreshJobView() {
        return refreshJobView;
    }

    public boolean isRefreshApplications() {
        return refreshApplications;
    }

    public int getJobIndex() {
        return jobIndex;
    }

    public int getPersonIndex() {
        return personIndex;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && showHelp == otherCommandResult.showHelp
                && exit == otherCommandResult.exit
                && toggleView == otherCommandResult.toggleView
                && viewJob == otherCommandResult.viewJob
                && viewPerson == otherCommandResult.viewPerson
                && clearView == otherCommandResult.clearView
                && refreshJobView == otherCommandResult.refreshJobView
                && jobIndex == otherCommandResult.jobIndex
                && personIndex == otherCommandResult.personIndex
                && refreshApplications == otherCommandResult.refreshApplications;
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit, toggleView, viewJob, viewPerson, clearView,
                refreshJobView, jobIndex, personIndex, refreshApplications);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("showHelp", showHelp)
                .add("exit", exit)
                .add("toggleView", toggleView)
                .add("viewJob", viewJob)
                .add("viewPerson", viewPerson)
                .add("clearView", clearView)
                .add("refreshJobView", refreshJobView)
                .add("jobIndex", jobIndex)
                .add("personIndex", personIndex)
                .add("refreshApplications", refreshApplications)
                .toString();
    }
}
