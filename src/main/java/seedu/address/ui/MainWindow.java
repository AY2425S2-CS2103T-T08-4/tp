package seedu.address.ui;

import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.job.Job;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private PersonListPanel personListPanel;
    private JobListPanel jobListPanel;
    private ResultDisplay resultDisplay;
    private StatusBarFooter statusBarFooter;
    private HelpWindow helpWindow;
    private ViewStateIndicator viewStateIndicator;
    private CommandBox commandBox;
    private boolean isJobView = false;
    private int selectedJobIndex = -1; // -1 means no job selected

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane personListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    @FXML
    private StackPane jobListPanelPlaceholder;

    @FXML
    private StackPane viewStateIndicatorPlaceholder;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        // Setup keyboard shortcuts
        setupKeyboardShortcuts();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        // Initialize status bar first (only once)
        if (statusBarFooter == null) {
            statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
            statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());
        }

        // Initialize view state indicator
        if (viewStateIndicator == null) {
            viewStateIndicator = new ViewStateIndicator();
            viewStateIndicatorPlaceholder.getChildren().add(viewStateIndicator.getRoot());
        }

        // Update the view state indicator
        updateViewStateIndicator();

        // Initialize the appropriate view
        if (isJobView) {
            initJobView();
        } else {
            initPersonView();
        }

        // Initialize result display
        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        // Initialize command box
        commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());

        // Set focus to command text field
        commandBox.getCommandTextField().requestFocus();

        // Inject event listener to CommandBox to handle Arrow Key pressed events.
        commandBox.getCommandTextField().setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.UP) {
                String res = logic.getPrevCommand();
                commandBox.handleArrowKeyPress(res);
            } else if (event.getCode() == javafx.scene.input.KeyCode.DOWN) {
                String res = logic.getNextCommand();
                commandBox.handleArrowKeyPress(res);
            }
        });
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();

        // Set focus to command text field after window is shown
        if (commandBox != null) {
            commandBox.getCommandTextField().requestFocus();
        }
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    /**
     * Toggles the view between person and job.
     */
    public void toggleJobView() {
        this.isJobView = !this.isJobView;

        // Update the UI for the new view
        if (isJobView) {
            initJobView();
        } else {
            initPersonView();
        }

        // Update view state indicator
        updateViewStateIndicator();
    }

    public PersonListPanel getPersonListPanel() {
        return personListPanel;
    }

    public JobListPanel getJobListPanel() {
        return jobListPanel;
    }

    /**
     * Refreshes the application view to reflect changes in application data.
     */
    public void refreshApplicationsView() {
        logger.info("Refreshed application view.");

        // Refresh the appropriate panel based on the current view
        if (isJobView && jobListPanel != null) {
            // If we're in job view, refresh the job panel to show updated applications
            jobListPanel.refreshJobView();
        } else if (personListPanel != null) {
            // If we're in person view, recreate the person list panel
            personListPanelPlaceholder.getChildren().clear();
            personListPanel = new PersonListPanel(logic.getFilteredPersonList(), logic);
            personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());
        }
    }

    /**
     * Clears the detail panel, removing any displayed job or person details.
     */
    public void clearDetailPanel() {
        logger.info("Clearing detail panels.");

        if (isJobView && jobListPanel != null) {
            // Reset job selection
            selectedJobIndex = -1;

            // Return to overview mode
            logic.setViewState(Model.ViewState.JOB_VIEW);

            // Refresh the panel with general statistics
            jobListPanel.showGeneralStatistics();
        }
    }

    /**
     * Refreshes the job panel to reflect changes in job data.
     */
    public void refreshJobPanel() {
        logger.info("Refreshing job panel.");

        if (isJobView && jobListPanel != null) {
            // Refresh the job list view
            jobListPanel.refreshJobView();

            // If a specific job was selected, refresh its view too
            if (selectedJobIndex >= 0 && selectedJobIndex < logic.getFilteredJobList().size()) {
                jobListPanel.selectJob(selectedJobIndex);
            } else {
                // Otherwise show general statistics
                jobListPanel.showGeneralStatistics();
            }
        }
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            // Handle list and listjob commands specifically, as they should clear filters
            String trimmedCommand = commandText.trim();
            if (trimmedCommand.equals("list") || trimmedCommand.equals("listjob")) {
                // These commands should completely reset the view and clear any filters
                if (isJobView && trimmedCommand.equals("listjob")) {
                    // Reset to job view and clear all filters
                    logic.setViewState(Model.ViewState.JOB_VIEW);
                    // Clear application status filter
                    logic.clearStatusFilter();
                    // Clear detail panel
                    clearDetailPanel();
                    // Refresh job panel to show all jobs without filters
                    refreshJobPanel();
                } else if (!isJobView && trimmedCommand.equals("list")) {
                    // Reset to person view and clear all filters
                    logic.setViewState(Model.ViewState.PERSON_VIEW);
                    // Clear application status filter
                    logic.clearStatusFilter();
                    // Recreate person list panel to refresh the view
                    if (personListPanel != null) {
                        personListPanelPlaceholder.getChildren().clear();
                        personListPanel = new PersonListPanel(logic.getFilteredPersonList(), logic);
                        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());
                    }
                }
            }

            // Check if we need to clear the view based on the command result
            if (commandResult.isClearView()) {
                clearDetailPanel();
            }

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.setToggleView()) {
                toggleJobView();
            }

            if (commandResult.isViewJob()) {
                viewJobStatistics(commandResult.getJobIndex());
            }

            if (commandResult.isViewPerson()) {
                viewPersonDetails(commandResult.getJobIndex(), commandResult.getPersonIndex());
            }

            if (commandResult.isRefreshJobView()) {
                refreshJobPanel();
            }

            if (commandResult.isRefreshApplications()) {
                refreshApplicationsView();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            // Update view state indicator after command execution
            updateViewStateIndicator();

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }

    /**
     * Displays the details of a person for a specific job.
     *
     * @param jobIndex The index of the job
     * @param personIndex The index of the person
     */
    private void viewPersonDetails(int jobIndex, int personIndex) {
        if (!isJobView) {
            toggleJobView(); // Ensure we're in job view
        }

        if (jobIndex >= 0 && jobIndex < logic.getFilteredJobList().size()) {
            selectedJobIndex = jobIndex;
            Job selectedJob = logic.getFilteredJobList().get(jobIndex);

            // Set the model to person detail view
            logic.setViewState(Model.ViewState.PERSON_DETAIL_VIEW);

            if (jobListPanel != null) {
                // Tell the job list panel to show the person details
                jobListPanel.showPersonDetails(selectedJob, personIndex);

                // Optionally select the job in the list view
                jobListPanel.selectJob(jobIndex);

                logger.info("Viewing person details for job: " + selectedJob.getJobTitle().jobTitle()
                        + ", person index: " + personIndex);
            }
        } else {
            logger.warning("Invalid job index for person details: " + jobIndex);
            selectedJobIndex = -1;
        }
    }

    /**
     * Initializes the job view components.
     */
    private void initJobView() {
        // Clear person list placeholder
        personListPanelPlaceholder.getChildren().clear();

        // Create job list panel if needed
        jobListPanel = new JobListPanel(logic.getFilteredJobList(), logic);
        jobListPanelPlaceholder.getChildren().clear();
        jobListPanelPlaceholder.getChildren().add(jobListPanel.getRoot());

        // Hide person list completely
        personListPanelPlaceholder.setVisible(false);
        personListPanelPlaceholder.setManaged(false);

        // Show job list
        jobListPanelPlaceholder.setVisible(true);
        jobListPanelPlaceholder.setManaged(true);

        // Reset job selection when switching to job view
        selectedJobIndex = -1;
    }

    /**
     * Initializes the person view components.
     */
    private void initPersonView() {
        // Clear job list placeholder
        jobListPanelPlaceholder.getChildren().clear();

        // Create person list panel if needed
        personListPanel = new PersonListPanel(logic.getFilteredPersonList(), logic);
        personListPanelPlaceholder.getChildren().clear();
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());

        // Hide job list completely
        jobListPanelPlaceholder.setVisible(false);
        jobListPanelPlaceholder.setManaged(false);

        // Show person list
        personListPanelPlaceholder.setVisible(true);
        personListPanelPlaceholder.setManaged(true);

        // Reset job selection when switching to person view
        selectedJobIndex = -1;
    }

    /**
     * Updates the view state indicator with the current view state.
     */
    private void updateViewStateIndicator() {
        if (viewStateIndicator != null) {
            // Use isJobView to determine the state
            viewStateIndicator.updateViewState(isJobView
                ? Model.ViewState.JOB_VIEW
                : Model.ViewState.PERSON_VIEW);
        }
    }

    /**
     * Displays statistics for a specific job.
     *
     * @param jobIndex the index of the job to view
     */
    public void viewJobStatistics(int jobIndex) {
        if (!isJobView) {
            toggleJobView(); // Ensure we're in job view
        }

        if (jobIndex >= 0 && jobIndex < logic.getFilteredJobList().size()) {
            selectedJobIndex = jobIndex;
            Job selectedJob = logic.getFilteredJobList().get(jobIndex);

            // Set the view state using the Logic interface
            logic.setViewState(Model.ViewState.JOB_DETAIL_VIEW);
            updateViewStateIndicator();

            if (jobListPanel != null) {
                // Tell the job list panel to show job-specific statistics
                jobListPanel.showJobSpecificStatistics(selectedJob);

                // Optionally select the job in the list view
                jobListPanel.selectJob(jobIndex);

                logger.info("Viewing statistics for job: " + selectedJob.getJobTitle().jobTitle());
            }
        } else {
            logger.warning("Invalid job index for statistics: " + jobIndex);
            selectedJobIndex = -1;
        }
    }

    /**
     * Handles keyboard shortcuts for the application.
     */
    public void setupKeyboardShortcuts() {
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.isControlDown()) {
                switch (event.getCode()) {
                case H: // Ctrl+H for Help
                    handleHelp();
                    event.consume();
                    break;
                case V: // Ctrl+V for toggle View
                    toggleJobView();
                    event.consume();
                    break;
                case Q: // Ctrl+Q for Quit
                    handleExit();
                    event.consume();
                    break;
                default:
                    break;
                }
            }
        });
    }

    /**
     * Clears the current view and returns to the general overview.
     */
    public void clearView() {
        // Return to overview mode
        if (isJobView) {
            // Ensure model state is synchronized using Logic interface
            logic.setViewState(Model.ViewState.JOB_VIEW);

            // Reset job selection
            selectedJobIndex = -1;

            // Refresh the panel with general statistics
            if (jobListPanel != null) {
                jobListPanel.showGeneralStatistics();
            }

            logger.info("View cleared, returned to job overview");
        }
    }
}
