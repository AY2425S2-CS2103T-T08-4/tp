package seedu.address.ui;

import java.util.Comparator;
import java.util.List;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to
     * display.
     */
    public PersonCard(Person person, List<Application> applications, int displayedIndex) {
        super(FXML);
        this.person = person;
        this.applications = applications;
        id.setText(displayedIndex
                + ". ");
        name.setText(person.getName().fullName);

        // Phone with white icon and modern styling
        phoneBox.getChildren().add(0, IconUtil.createIcon(FontAwesomeIcon.PHONE, "white"));
        phone.setText(person.getPhone().value);
        phoneBox.setStyle("-fx-background-color: rgba(62, 123, 145, 0.15);"
                + " -fx-background-radius: 4;"
                + " -fx-padding: 5 10 5 10;");

        // Email with white icon and modern styling
        emailBox.getChildren().add(0, IconUtil.createIcon(FontAwesomeIcon.ENVELOPE, "white"));
        email.setText(person.getEmail().value);
        emailBox.setStyle("-fx-background-color: rgba(62, 123, 145, 0.15);"
                + " -fx-background-radius: 4;"
                + " -fx-padding: 5 10 5 10;");

        // Address with white icon and modern styling
        addressBox.getChildren().add(0, IconUtil.createIcon(FontAwesomeIcon.HOME, "white"));
        address.setText(person.getAddress().value);
        addressBox.setStyle("-fx-background-color: rgba(62, 123, 145, 0.15);"
                + " -fx-background-radius: 4;"
                + " -fx-padding: 5 10 5 10;");

        // Degree with white icons and modern styling
        degreeBox.getChildren().add(0, IconUtil.createIcon(FontAwesomeIcon.GRADUATION_CAP, "white"));
        degree.setText(person.getDegree().value);
        degreeBox.getChildren().add(2, new Label("•")); // Add bullet point
        degreeBox.getChildren().add(3, IconUtil.createIcon(FontAwesomeIcon.UNIVERSITY, "white"));
        degreeBox.setStyle("-fx-background-color: rgba(249, 105, 14, 0.15);"
                + " -fx-background-radius: 4;"
                + " -fx-padding: 5 10 5 10;");

        // School with white icon
        school.setText(person.getSchool().value);

        // Skills with white icon and modern styling
        skillsBox.getChildren().add(0, IconUtil.createIcon(FontAwesomeIcon.TAGS, "white"));
        skillsBox.setStyle("-fx-background-color: rgba(43, 43, 43, 0.7);"
                + " -fx-background-radius: 4 4 0 0;"
                + " -fx-padding: 5 10 5 10;");

        // Add tags with modern styling
        person.getTags().stream().sorted(Comparator.comparing(Tag::tagName))
                .forEach(tag -> {
                    Label tagLabel = new Label(tag.tagName());
                    tagLabel.setStyle(
                        "-fx-background-color: #3e7b91; "
                        + "-fx-text-fill: white; "
                        + "-fx-padding: 3 8 3 8; "
                        + "-fx-background-radius: 3; "
                        + "-fx-font-size: 11px; "
                        + "-fx-font-weight: bold;"
                    );
                    tags.getChildren().add(tagLabel);
                });
        tags.setStyle("-fx-spacing: 5;"
                + " -fx-alignment: center-left;");

        // Add applications with Balenciaga-inspired styling - remove company name
        applicationsBox.getChildren().add(0, IconUtil.createIcon(FontAwesomeIcon.BRIEFCASE, "white"));
        applicationsBox.setStyle("-fx-background-color: rgba(43, 43, 43, 0.7);"
                + " -fx-background-radius: 4 4 0 0;"
                + " -fx-padding: 5 10 5 10;");
        applications.stream().sorted(Comparator.comparing(Application::getApplicationStatus))
            .forEach(app -> {
                String jobTitle = app.getJob().getJobTitle().toString();
                int currentRound = app.getApplicationStatus().applicationStatus;
                int maxRound = app.getJob().getJobRounds().jobRounds;

                // Create a stylish application label - removed company name
                Label appLabel = new Label(jobTitle);
                appLabel.setStyle(
                    "-fx-background-color: #2d2d30; "
                    + "-fx-text-fill: white; "
                    + "-fx-padding: 5 10 5 10; "
                    + "-fx-background-radius: 4; "
                    + "-fx-font-weight: bold; "
                    + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 2, 0, 0, 1);"
                );

                // Create a progress indicator
                Label progressLabel = new Label("Round: "
                        + currentRound
                        + "/"
                        + maxRound);
                progressLabel.setStyle(
                    "-fx-text-fill: #f39c12; "
                    + "-fx-font-size: 11px; "
                    + "-fx-padding: 2 8 2 8; "
                    + "-fx-background-color: rgba(243, 156, 18, 0.15); "
                    + "-fx-background-radius: 10;"
                );

                // Create a VBox to hold both labels
                VBox appBox = new VBox(3);
                appBox.getChildren().addAll(appLabel, progressLabel);
                appBox.setStyle("-fx-padding: 2;");

                apps.getChildren().add(appBox);
            });
        apps.setStyle("-fx-spacing: 5;"
                + " -fx-alignment: center-left;");

        // Style the entire card
        cardPane.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #303030, #252525); "
            + "-fx-background-radius: 8; "
            + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10, 0, 0, 5);"
        );
    }
}
