package seedu.address.ui;

import java.util.Comparator;
import java.util.List;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.application.Application;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.ui.util.IconUtil;

/**
 * UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved
     * keywords in JavaFX. As a consequence, UI elements' variable names cannot be
     * set to such keywords or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The
     *      issue on AddressBook level 4</a>
     */

    public final Person person;
    public final List<Application> applications; //This should be applications from person

    // Graphic Components
    @FXML
    private HBox cardPane;
    @FXML
    private Label id;

    // Attribute Labels
    @FXML
    private Label name;
    @FXML
    private Label phone;
    @FXML
    private Label email;
    @FXML
    private Label address;
    @FXML
    private Label school;
    @FXML
    private Label degree;
    @FXML
    private FlowPane tags;
    @FXML
    private FlowPane apps;

    // Attribute Containers
    @FXML
    private HBox phoneBox;
    @FXML
    private HBox emailBox;
    @FXML
    private HBox addressBox;
    @FXML
    private HBox schoolBox;
    @FXML
    private HBox degreeBox;
    @FXML
    private HBox skillsBox;
    @FXML
    private HBox applicationsBox;

    @FXML
    private HBox progressBox;
    @FXML
    private ProgressBar applicationProgress;
    @FXML
    private Label progressLabel;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to
     * display.
     */
    public PersonCard(Person person, List<Application> applications, int displayedIndex) {
        super(FXML);
        this.person = person;
        this.applications = applications;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);

        // Phone with white icon
        phoneBox.getChildren().add(0, IconUtil.createIcon(FontAwesomeIcon.PHONE, "white"));
        phone.setText(person.getPhone().value);

        // Email with white icon
        emailBox.getChildren().add(0, IconUtil.createIcon(FontAwesomeIcon.ENVELOPE, "white"));
        email.setText(person.getEmail().value);

        // Address with white icon
        addressBox.getChildren().add(0, IconUtil.createIcon(FontAwesomeIcon.HOME, "white"));
        address.setText(person.getAddress().value);

        // Degree with white icons (Made with AI)
        degreeBox.getChildren().add(0, IconUtil.createIcon(FontAwesomeIcon.GRADUATION_CAP, "white"));
        degree.setText(person.getDegree().value);
        degreeBox.getChildren().add(2, new Label("•")); // Add bullet point
        degreeBox.getChildren().add(3, IconUtil.createIcon(FontAwesomeIcon.UNIVERSITY, "white"));

        // School with white icon
        school.setText(person.getSchool().value);

        // Skills with white icon
        skillsBox.getChildren().add(0, IconUtil.createIcon(FontAwesomeIcon.TAGS, "white"));

        // Add tags
        person.getTags().stream().sorted(Comparator.comparing(Tag::tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName())));

        // Add applications
        applicationsBox.getChildren().add(0, IconUtil.createIcon(FontAwesomeIcon.BRIEFCASE, "white"));
        if (applications.isEmpty()) {
            Label emptyLabel = new Label("No applications yet");
            emptyLabel.getStyleClass().add("empty-apps-label");
            apps.getChildren().add(emptyLabel);
        } else {
            applications.stream().sorted(Comparator.comparing(Application::applicationStatus))
                .forEach(app -> {
                    apps.getChildren().add(createApplicationItem(app));
                });
        }
            
        // Add progress bar showing maximum progress across all applications
        setupProgressBar();

        // In the FXML file or when setting up the progress box
        progressBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        applicationProgress.setMaxWidth(Double.MAX_VALUE); // Let it fill the available space

        // Update FlowPane alignment and wrap length
        apps.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        apps.setColumnHalignment(javafx.geometry.HPos.CENTER); // Center horizontally
        apps.setHgap(15);
        apps.setVgap(15);
        apps.setPrefWrapLength(350); // Adjust based on your UI
    }
    
    /**
     * Sets up the progress bar with the maximum progress ratio across all applications.
     */
    private void setupProgressBar() {
        double maxProgress = 0.0;
        int maxProgressCurrentRound = 0;
        int maxProgressMaxRound = 1; // Default to 1 to avoid division by zero
        
        // Find the maximum progress ratio across all applications
        for (Application app : applications) {
            int currentRound = app.applicationStatus().applicationStatus;
            int maxRound = app.job().getJobRounds().jobRounds;
            
            double progress = (double) currentRound / maxRound;
            if (progress > maxProgress) {
                maxProgress = progress;
                maxProgressCurrentRound = currentRound;
                maxProgressMaxRound = maxRound;
            }
        }
        
        // Add an icon before the progress bar
        HBox progressContent = new HBox(10);
        progressContent.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        progressContent.getChildren().add(IconUtil.createIcon(FontAwesomeIcon.BAR_CHART, "white"));
        
        // Create a more descriptive label with better wording
        String progressDescription;
        if (applications.isEmpty()) {
            progressDescription = "No applications yet";
        } else {
            progressDescription = String.format("Round %d of %d", maxProgressCurrentRound, maxProgressMaxRound);
        }
        
        // Create the progress bar with custom styling based on progress
        applicationProgress.setProgress(maxProgress);
        applicationProgress.setPrefHeight(15); // Slightly taller
        applicationProgress.setPrefWidth(180);
        
        // Apply color based on progress level
        String progressColor;
        if (maxProgress < 0.33) {
            progressColor = "#5bc0de"; // Blue for early stages
        } else if (maxProgress < 0.66) {
            progressColor = "#f0ad4e"; // Orange for middle stages
        } else {
            progressColor = "#5cb85c"; // Green for later stages
        }
        applicationProgress.setStyle("-fx-accent: " + progressColor + ";");
        
        // Create a better title label
        Label progressTitle = new Label("Application Progress");
        progressTitle.getStyleClass().add("progress-title");
        
        // Create the layout
        VBox progressLabels = new VBox(3);
        progressLabels.getChildren().addAll(progressTitle, new Label(progressDescription));
        
        // Add components to the progress content
        progressContent.getChildren().addAll(applicationProgress, progressLabels);
        
        // Clear existing children and add the new content
        progressBox.getChildren().clear();
        progressBox.getChildren().add(progressContent);
    }

    /**
     * Creates a container with both application info and a miniature progress bar
     */
    private VBox createApplicationItem(Application app) {
        String jobTitle = app.job().getJobTitle().toString();
        int currentRound = app.applicationStatus().applicationStatus;
        int maxRound = app.job().getJobRounds().jobRounds;
        String companyTitle = app.job().getJobCompany().jobCompany();
        
        // Create styled job title
        Label jobLabel = new Label(jobTitle);
        jobLabel.getStyleClass().add("app-job-title");
        
        // Create styled company name
        Label companyLabel = new Label(companyTitle);
        companyLabel.getStyleClass().add("app-company");
        
        // Create progress indicator
        double progress = (double) currentRound / maxRound;
        ProgressBar miniProgressBar = new ProgressBar(progress);
        miniProgressBar.setPrefHeight(8);
        miniProgressBar.setMaxWidth(Double.MAX_VALUE);
        miniProgressBar.getStyleClass().add("mini-progress-bar");
        
        // Apply color based on progress
        String progressColor;
        if (progress < 0.33) {
            progressColor = "#5bc0de"; // Blue for early stages
        } else if (progress < 0.66) {
            progressColor = "#f0ad4e"; // Orange for middle stages
        } else {
            progressColor = "#5cb85c"; // Green for later stages
        }
        miniProgressBar.setStyle("-fx-accent: " + progressColor + ";");
        
        // Create round indicator
        Label roundLabel = new Label("Round " + currentRound + " of " + maxRound);
        roundLabel.getStyleClass().add("app-round");
        
        // Layout for job info
        VBox jobInfo = new VBox(3);
        jobInfo.getChildren().addAll(jobLabel, companyLabel);
        
        // Layout for progress info
        VBox progressInfo = new VBox(3);
        progressInfo.getChildren().addAll(miniProgressBar, roundLabel);
        
        // Main container
        VBox appContainer = new VBox(7);
        appContainer.getStyleClass().add("app-card");
        appContainer.getChildren().addAll(jobInfo, progressInfo);
        
        return appContainer;
    }
}
