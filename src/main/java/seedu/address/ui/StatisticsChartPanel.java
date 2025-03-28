package seedu.address.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import seedu.address.logic.Logic;
import seedu.address.model.application.Application;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;

/**
 * Panel that displays statistics charts for the application.
 */
public class StatisticsChartPanel {

    private final Logic logic;
    private final VBox chartContainer;
    private PieChart jobDistributionChart;
    private BarChart<String, Number> schoolDistributionChart;
    private Label totalApplicationsLabel;
    private Label totalApplicantsLabel;

    /**
     * Creates a {@code StatisticsChartPanel} with the given {@code Logic}.
     */
    public StatisticsChartPanel(Logic logic) {
        this.logic = logic;
        this.chartContainer = createStatisticsContainer();
    }

    /**
     * Returns the root container for this panel.
     */
    public VBox getRoot() {
        return chartContainer;
    }

    /**
     * Refreshes all charts with current data.
     */
    public void refresh() {
        updateSummaryStatistics();
        updateJobDistributionChart();
        updateSchoolDistributionChart();
    }

    /**
     * Creates a container with statistics charts.
     */
    private VBox createStatisticsContainer() {
        VBox container = new VBox();
        container.setStyle("-fx-background-color: #2d2d30; -fx-padding: 20;");
        container.setSpacing(20);
        container.setMinWidth(350);
        container.setPrefWidth(400);

        // Add title
        Label titleLabel = new Label("Statistics");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
        container.getChildren().add(titleLabel);

        // Add summary statistics panel with enhanced styling
        VBox summaryBox = new VBox(10); // Increased spacing
        summaryBox.setStyle("-fx-background-color: linear-gradient(to bottom right, #3a3a3a, #2a2a2a); "
                           + "-fx-padding: 20; "
                           + "-fx-background-radius: 8; "
                           + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10, 0, 0, 5);");

        // Styled total applications label
        totalApplicationsLabel = new Label("Total Applications: 0");
        totalApplicationsLabel.setStyle("-fx-text-fill: linear-gradient(to right, #ff9966, #ff5e62); "
                                       + "-fx-font-size: 18px; "
                                       + "-fx-font-weight: bold; "
                                       + "-fx-padding: 5 0; "
                                       + "-fx-effect: dropshadow(one-pass-box, rgba(255,255,255,0.2), 1, 0, 0, 1); "
                                       + "-fx-background-color: rgba(255,255,255,0.05); "
                                       + "-fx-background-radius: 4; "
                                       + "-fx-padding: 8 12;");

        // Styled unique applicants label
        totalApplicantsLabel = new Label("Unique Applicants: 0");
        totalApplicantsLabel.setStyle("-fx-text-fill: linear-gradient(to right, #4facfe, #00f2fe); "
                                     + "-fx-font-size: 18px; "
                                     + "-fx-font-weight: bold; "
                                     + "-fx-padding: 5 0; "
                                     + "-fx-effect: dropshadow(one-pass-box, rgba(255,255,255,0.2), 1, 0, 0, 1); "
                                     + "-fx-background-color: rgba(255,255,255,0.05); "
                                     + "-fx-background-radius: 4; "
                                     + "-fx-padding: 8 12;");

        summaryBox.getChildren().addAll(totalApplicationsLabel, totalApplicantsLabel);
        container.getChildren().add(summaryBox);

        // Create job distribution chart
        Label jobChartTitle = new Label("Applicants by Job");
        jobChartTitle.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 0 5 0;");
        container.getChildren().add(jobChartTitle);

        jobDistributionChart = new PieChart();
        jobDistributionChart.setMinHeight(250);
        jobDistributionChart.setPrefHeight(250);
        jobDistributionChart.setLegendVisible(true);
        jobDistributionChart.setLegendSide(Side.RIGHT);
        jobDistributionChart.setLabelsVisible(true);
        jobDistributionChart.setStyle("-fx-background-color: #2d2d30; -fx-padding: 10;");

        // Set chart text colors to white for better visibility
        jobDistributionChart.setLabelLineLength(20);
        jobDistributionChart.setLabelsVisible(true);

        // Apply CSS to style the chart legends and labels
        String pieChartCss =
            ".chart-pie-label { -fx-fill: white; }"
            + ".chart-pie-label-line { -fx-stroke: white; }"
            + ".chart-legend { -fx-background-color: transparent; }"
            + ".chart-legend-item { -fx-text-fill: white; }";
        jobDistributionChart.setStyle(jobDistributionChart.getStyle() + pieChartCss);

        container.getChildren().add(jobDistributionChart);

        // Create school distribution chart
        Label schoolChartTitle = new Label("Applicants by School");
        schoolChartTitle.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 20 0 5 0;");
        container.getChildren().add(schoolChartTitle);

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("School");
        yAxis.setLabel("Count");
        xAxis.setTickLabelFill(Color.WHITE);
        yAxis.setTickLabelFill(Color.WHITE);
        xAxis.setTickLabelRotation(30);

        // Configure Y-axis to properly auto-range
        yAxis.setForceZeroInRange(true);
        yAxis.setAutoRanging(true);

        schoolDistributionChart = new BarChart<>(xAxis, yAxis);
        schoolDistributionChart.setMinHeight(250);
        schoolDistributionChart.setPrefHeight(250);
        schoolDistributionChart.setLegendVisible(false);
        schoolDistributionChart.setAnimated(false); // Disable animations for better updates

        // Apply CSS to style the bar chart
        String barChartCss =
            ".chart { -fx-background-color: #2d2d30; -fx-padding: 10; }"
            + ".chart-plot-background { -fx-background-color: transparent; }"
            + ".axis { -fx-tick-label-fill: white; }"
            + ".axis-label { -fx-text-fill: white; }"
            + ".chart-vertical-grid-lines { -fx-stroke: #555555; }"
            + ".chart-horizontal-grid-lines { -fx-stroke: #555555; }";
        schoolDistributionChart.setStyle(barChartCss);

        container.getChildren().add(schoolDistributionChart);

        // Fill charts with initial data
        refresh();

        return container;
    }

    /**
     * Updates the job distribution pie chart with current data.
     */
    private void updateJobDistributionChart() {
        List<Job> jobs = logic.getFilteredJobList();

        // Create data for pie chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Add some default data if no real data is available
        if (jobs.isEmpty()) {
            pieChartData.add(new PieChart.Data("No jobs", 1));
            jobDistributionChart.setData(pieChartData);
            return;
        }

        // Use default JavaFX chart colors for both slices and legend
        // This approach uses JavaFX's built-in default colors which are applied to both
        // the pie slices and legend items automatically

        // Clear any existing data
        jobDistributionChart.getData().clear();

        // We'll use JavaFX's standard coloring which maintains consistency
        // by applying the same colors to pie slices and legend items

        // Prepare the data with consistent order to get predictable colors
        List<Job> sortedJobs = jobs.stream()
            .filter(job -> {
                List<Application> applications = logic.getApplicationsByJob(job);
                return applications != null && !applications.isEmpty();
            })
            .sorted((job1, job2) -> {
                // Sort by number of applications (descending)
                List<Application> apps1 = logic.getApplicationsByJob(job1);
                List<Application> apps2 = logic.getApplicationsByJob(job2);
                return Integer.compare(apps2.size(), apps1.size());
            })
            .toList();

        // Add data in a consistent order
        for (Job job : sortedJobs) {
            List<Application> applications = logic.getApplicationsByJob(job);
            int appCount = applications.size();
            String jobName = job.getJobTitle().jobTitle();
            pieChartData.add(new PieChart.Data(jobName, appCount));
        }

        // If no applications found for any job
        if (pieChartData.isEmpty()) {
            pieChartData.add(new PieChart.Data("No applications yet", 1));
        }

        // Set the data - JavaFX will automatically apply default colors consistently to both slices and legend
        jobDistributionChart.setData(pieChartData);

        // Improve the chart appearance
        jobDistributionChart.setClockwise(true);
        jobDistributionChart.setLabelsVisible(true);
        jobDistributionChart.setStartAngle(90);
    }

    /**
     * Updates the school distribution bar chart with current data.
     */
    private void updateSchoolDistributionChart() {
        // Get all applications from jobs
        List<Job> jobs = logic.getFilteredJobList();

        // Use a set to collect unique persons to avoid duplicates
        java.util.Set<Person> uniquePersons = new java.util.HashSet<>();

        // Collect all unique persons from applications
        for (Job job : jobs) {
            // Use filtered applications to respect status filter
            List<Application> applications = logic.getApplicationsByJob(job);
            if (applications != null) {
                for (Application app : applications) {
                    Person person = app.getApplicant();
                    if (person != null) {
                        uniquePersons.add(person);
                    }
                }
            }
        }

        // Count unique persons by school
        Map<String, Integer> schoolCounts = new HashMap<>();
        for (Person person : uniquePersons) {
            if (person.getSchool() != null) {
                String school = person.getSchool().value;
                // Clean up the school name if needed
                if (school.contains("@")) {
                    school = "Unknown School";
                }
                schoolCounts.put(school, schoolCounts.getOrDefault(school, 0) + 1);
            }
        }

        // Clear existing data first
        schoolDistributionChart.getData().clear();

        // Create new series for bar chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Number of Applicants");

        // Add some default data if no real data is available
        if (schoolCounts.isEmpty()) {
            series.getData().add(new XYChart.Data<>("No data", 0));
            schoolDistributionChart.getData().add(series);
            return;
        }

        // Add actual data
        schoolCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue((a, b) -> b - a)) // Sort by value (descending)
                .limit(5) // Limit to top 5 schools
                .forEach(entry -> series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue())));

        // Add the new series
        schoolDistributionChart.getData().add(series);

        // Force layout update to ensure axis ranges are recalculated
        schoolDistributionChart.layout();

        // Define colors for bars
        final String[] colors = {
            "#3366cc", "#dc3912", "#ff9900", "#109618", "#990099",
            "#0099c6", "#dd4477", "#66aa00", "#b82e2e", "#316395"
        };

        // Use JavaFX Platform.runLater to style nodes after they've been created
        javafx.application.Platform.runLater(() -> {
            int colorIndex = 0;
            for (XYChart.Data<String, Number> data : series.getData()) {
                if (data.getNode() != null) {
                    String color = colors[colorIndex % colors.length];
                    data.getNode().setStyle("-fx-bar-fill: " + color + ";");
                    colorIndex++;
                }
            }
        });
    }

    /**
     * Updates the summary statistics labels with current totals.
     */
    private void updateSummaryStatistics() {
        List<Job> jobs = logic.getFilteredJobList();

        // Calculate total applications (same logic as pie chart)
        int totalApplications = 0;

        // Use a set to collect unique persons (same logic as bar chart)
        java.util.Set<Person> uniquePersons = new java.util.HashSet<>();

        for (Job job : jobs) {
            // Use filtered applications to respect status filter
            List<Application> applications = logic.getApplicationsByJob(job);
            if (applications != null) {
                totalApplications += applications.size();

                // Also collect unique persons
                for (Application app : applications) {
                    Person person = app.getApplicant();
                    if (person != null) {
                        uniquePersons.add(person);
                    }
                }
            }
        }

        // Update the labels with more interesting format
        totalApplicationsLabel.setText("★ Total Applications: " + totalApplications);
        totalApplicantsLabel.setText("☺ Unique Applicants: " + uniquePersons.size());
    }
}
