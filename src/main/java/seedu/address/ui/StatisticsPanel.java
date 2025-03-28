package seedu.address.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.logic.Logic;
import seedu.address.model.application.Application;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;

/**
 * Panel containing statistics visualizations (charts).
 */
public class StatisticsPanel extends UiPart<Region> {

    private static final String FXML = "StatisticsPanel.fxml";

    @FXML
    private VBox statisticsContainer;

    @FXML
    private PieChart jobDistributionChart;

    @FXML
    private BarChart<String, Number> schoolDistributionChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private Label noDataLabel;

    private Logic logic;

    /**
     * Creates a {@code StatisticsPanel} with the given {@code Logic}.
     */
    public StatisticsPanel(Logic logic) {
        super(FXML);
        this.logic = logic;

        // Initialize charts
        initializeCharts();
    }

    /**
     * Initializes the charts with data.
     */
    private void initializeCharts() {
        updateJobDistributionChart();
        updateSchoolDistributionChart();
    }

    /**
     * Updates the job distribution pie chart.
     */
    private void updateJobDistributionChart() {
        List<Job> jobs = logic.getFilteredJobList();

        // If no jobs, show no data message
        if (jobs.isEmpty()) {
            jobDistributionChart.setVisible(false);
            noDataLabel.setVisible(true);
            return;
        }

        // Create data for pie chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Job job : jobs) {
            List<Application> applications = logic.getApplicationsByJob(job);
            pieChartData.add(new PieChart.Data(job.getJobTitle().toString(), applications.size()));
        }

        jobDistributionChart.setData(pieChartData);
        jobDistributionChart.setTitle("Applicants by Job");
        jobDistributionChart.setLegendVisible(true);
        jobDistributionChart.setLabelsVisible(true);

        jobDistributionChart.setVisible(true);
        noDataLabel.setVisible(false);
    }

    /**
     * Updates the school distribution bar chart.
     */
    private void updateSchoolDistributionChart() {
        List<Person> persons = logic.getFilteredPersonList();

        // If no persons, show no data message
        if (persons.isEmpty()) {
            schoolDistributionChart.setVisible(false);
            return;
        }

        // Count persons by school
        Map<String, Integer> schoolCounts = new HashMap<>();

        for (Person person : persons) {
            String school = person.getSchool().value;
            schoolCounts.put(school, schoolCounts.getOrDefault(school, 0) + 1);
        }

        // Create series for bar chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Number of Applicants");

        schoolCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue((a, b) -> b - a)) // Sort by value (descending)
                .limit(10) // Limit to top 10 schools
                .forEach(entry -> series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue())));

        schoolDistributionChart.getData().clear();
        schoolDistributionChart.getData().add(series);

        // Set axes labels
        xAxis.setLabel("School");
        yAxis.setLabel("Number of Applicants");

        schoolDistributionChart.setTitle("Applicants by School");
        schoolDistributionChart.setVisible(true);
    }

    /**
     * Refreshes the charts with updated data.
     */
    public void refresh() {
        updateJobDistributionChart();
        updateSchoolDistributionChart();
    }
}
