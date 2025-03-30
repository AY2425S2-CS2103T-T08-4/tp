package seedu.address.ui;

import java.util.List;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.model.application.Application;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;

/**
 * Panel containing the list of jobs with a sidepane for additional information.
 */
public class JobListPanel extends UiPart<Region> {

    private static final String FXML = "JobListPanel.fxml";
    private static final Logger logger = LogsCenter.getLogger(JobListPanel.class);
    private static final double HIDDEN_DIVIDER_POSITION = 1.0;
    private static final double DEFAULT_DIVIDER_POSITION = 0.6;

    private Logic logic;
    private StatisticsChartPanel statisticsPanel;
    private JobSpecificStatsPanel jobSpecificStatsPanel;
    private PersonDetailPanel personDetailPanel;
    private Job currentlyViewedJob;
    private Person currentlyViewedPerson;

    @FXML
    private SplitPane splitPane;

    @FXML
    private ListView<Job> jobListView;

    private double lastDividerPosition = DEFAULT_DIVIDER_POSITION;

    /**
     * Creates a {@code JobListPanel} with the given {@code ObservableList}.
     */
    public JobListPanel(ObservableList<Job> jobList, Logic logic) {
        super(FXML);
        this.logic = logic;
        logger.info("Creating JobListPanel with logic: " + (logic != null ? "valid" : "null"));

        // Set cell factory
        jobListView.setCellFactory(listView -> new JobListViewCell());

        // Set items
        jobListView.setItems(jobList);

        // Log all FXML components
        logger.info("FXML components initialized: "
                   + "splitPane=" + (splitPane != null)
                   + ", jobListView=" + (jobListView != null));

        // Initialize the sidepane with statistics by default
        initSidepane();

        // Force divider position to make sidebar visible
        if (splitPane != null) {
            splitPane.setDividerPosition(0, DEFAULT_DIVIDER_POSITION);
            logger.info("Set initial divider position to: " + DEFAULT_DIVIDER_POSITION);
        }

        // Add listener to observable list to refresh UI when data changes
        jobList.addListener((javafx.collections.ListChangeListener.Change<? extends Job> c) -> {
            logger.info("Job list changed, refreshing UI components");
            refreshJobView();
            refreshSidepane();
        });

        // Add listener to job selection to update sidepane content if needed in the future
        jobListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            logger.info("Job selection changed: " + (newValue != null ? newValue.getJobTitle().jobTitle() : "null"));
            // In the future, this could update the sidepane content based on the selected job
            // For now, we'll just refresh the statistics
            refreshSidepane();
        });
    }

    /**
     * Initializes the sidepane content.
     */
    private void initSidepane() {
        // Create statistics panel
        statisticsPanel = new StatisticsChartPanel(logic);

        // Add it to the split pane
        if (splitPane != null && splitPane.getItems().size() > 1) {
            logger.info("Setting statistics panel as sidepane content");
            splitPane.getItems().set(1, statisticsPanel.getRoot());
        } else {
            logger.severe("SplitPane doesn't have enough items for sidepane!");
        }
    }

    /**
     * Shows statistics specific to the given job.
     * @param job The job to show statistics for
     */
    public void showJobSpecificStatistics(Job job) {
        if (job == null) {
            showGeneralStatistics(); //If no job selected, show general statistics
            return;
        }

        currentlyViewedJob = job;

        // Create a job-specific stats panel if needed
        if (jobSpecificStatsPanel == null) {
            jobSpecificStatsPanel = new JobSpecificStatsPanel(logic); //Else create a new job-specific stats panel
        }

        // Update the panel with the job data
        jobSpecificStatsPanel.updateForJob(job);

        // Show the job-specific stats panel
        if (splitPane != null && splitPane.getItems().size() > 1) {
            splitPane.getItems().set(1, jobSpecificStatsPanel.getRoot());
        }

        // Ensure the sidepane is visible
        setSidepaneVisible(true);
    }

    /**
     * Shows general statistics for all jobs.
     */
    public void showGeneralStatistics() {
        currentlyViewedJob = null;

        // Show the general statistics panel
        if (splitPane != null && splitPane.getItems().size() > 1 && statisticsPanel != null) {
            // Refresh the statistics panel
            statisticsPanel.refresh();

            // Set the panel
            splitPane.getItems().set(1, statisticsPanel.getRoot());
        }
    }

    /**
     * Displays the details of a person associated with a job.
     *
     * @param job The job the person is applying for
     * @param applicationIndex The index of the application to display
     */
    public void showPersonDetails(Job job, int applicationIndex) {
        if (job == null) {
            showGeneralStatistics(); //If no job selected, show general statistics
            return; //We use job to filter applicants first
        }

        List<Application> applications = logic.getFilteredApplicationsByJob(job);
        if (applications == null || applicationIndex < 0 || applicationIndex >= applications.size()) {
            showGeneralStatistics();
            return;
        }

        Application application = applications.get(applicationIndex);
        Person person = application.getApplicant();
            
        if (person == null) { //Check if person is null
            showGeneralStatistics();
            return;
        }

        currentlyViewedJob = job;
        currentlyViewedPerson = person;

        // Create a person detail panel if needed
        if (personDetailPanel == null) {
            personDetailPanel = new PersonDetailPanel(logic);
        }

        // Update the panel with the person data
        personDetailPanel.updateForPerson(currentlyViewedPerson, currentlyViewedJob, application);

        // Show the person detail panel
        if (splitPane != null && splitPane.getItems().size() > 1) {
            splitPane.getItems().set(1, personDetailPanel.getRoot());
        }

        // Ensure the sidepane is visible
        setSidepaneVisible(true);
    }

    /**
     * Selects the job at the given index.
     *
     * @param index The index of the job to select
     */
    public void selectJob(int index) {
        if (index >= 0 && index < jobListView.getItems().size()) {
            jobListView.getSelectionModel().select(index);
            jobListView.scrollTo(index);
        }
    }

    /**
     * Sets the content type of the sidepane.
     *
     * @param contentType The type of content to show in the sidepane
     */
    public void setSidepaneContent(SidepaneContentType contentType) {
        // Based on the content type, show the appropriate panel
        switch (contentType) {
        case STATISTICS:
            // Initialize statistics panel if needed
            if (statisticsPanel == null) {
                statisticsPanel = new StatisticsChartPanel(logic);
            }
            
            // Set the statistics panel as the second item in the split pane
            if (splitPane != null && splitPane.getItems().size() > 1) {
                splitPane.getItems().set(1, statisticsPanel.getRoot());
            }
            break;
            
        case JOB_DETAILS:
            // Initialize job specific stats panel if needed
            if (jobSpecificStatsPanel == null) {
                jobSpecificStatsPanel = new JobSpecificStatsPanel(logic);
            }
            
            // Set the job specific stats panel as the second item in the split pane
            if (splitPane != null && splitPane.getItems().size() > 1) {
                splitPane.getItems().set(1, jobSpecificStatsPanel.getRoot());
            }
            break;
            
        case PERSON_DETAILS:
            // Initialize person detail panel if needed
            if (personDetailPanel == null) {
                personDetailPanel = new PersonDetailPanel(logic);
            }
            
            // Set the person detail panel as the second item in the split pane
            if (splitPane != null && splitPane.getItems().size() > 1) {
                splitPane.getItems().set(1, personDetailPanel.getRoot());
            }
            break;
            
        default:
            // Default to statistics
            // Initialize statistics panel if needed
            if (statisticsPanel == null) {
                statisticsPanel = new StatisticsChartPanel(logic);
            }
            
            // Set the statistics panel as the second item in the split pane
            if (splitPane != null && splitPane.getItems().size() > 1) {
                splitPane.getItems().set(1, statisticsPanel.getRoot());
            }
        }
    }

    /**
     * Refreshes the sidepane content to reflect updated data.
     */
    private void refreshSidepane() {
        if (currentlyViewedJob != null && jobSpecificStatsPanel != null) {
            // If we're viewing a specific job, update its statistics
            jobSpecificStatsPanel.updateForJob(currentlyViewedJob);
        } else if (statisticsPanel != null) {
            // Otherwise refresh the general statistics
            statisticsPanel.refresh();
        }
    }

    /**
     * Sets the visibility of the sidepane.
     */
    public void setSidepaneVisible(boolean visible) {
        if (visible) {
            splitPane.setDividerPosition(0, lastDividerPosition);
        } else {
            lastDividerPosition = splitPane.getDividerPositions()[0];
            splitPane.setDividerPosition(0, HIDDEN_DIVIDER_POSITION);
        }
    }

    /**
     * Toggles the visibility of the sidepane.
     */
    public void toggleSidepane() {
        boolean isCurrentlyVisible = splitPane.getDividerPositions()[0] < HIDDEN_DIVIDER_POSITION;
        setSidepaneVisible(!isCurrentlyVisible);
    }

    /**
     * Refreshes the job view by forcing an update of visible cells
     */
    public void refreshJobView() {
        int size = jobListView.getItems().size();
        if (size > 0) {
            jobListView.refresh();
        }
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Job} using a {@code JobCard}.
     */
    class JobListViewCell extends ListCell<Job> {
        @Override
        protected void updateItem(Job job, boolean empty) {
            super.updateItem(job, empty);

            if (empty || job == null) {
                setGraphic(null);
                setText(null);
            } else {
                // Use the filtered applications instead of all applications
                List<Application> applications = logic.getFilteredApplicationsByJob(job);
                setGraphic(new JobCard(job, applications, getIndex() + 1).getRoot());
            }
        }
    }

    /**
     * Enum to represent different types of content that can be displayed in the sidepane.
     */
    public enum SidepaneContentType {
        STATISTICS,
        JOB_DETAILS,
        PERSON_DETAILS
    }

    public List<Job> getJobList() {
        return jobListView.getItems();
    }
}
