package seedu.address.ui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.HelpCommand;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {
    public static final String USERGUIDE_URL = "https://ay2425s2-cs2103t-t08-4.github.io/tp/UserGuide.html#quick-start";
    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Label helpMessage;

    @FXML
    private Hyperlink userGuideLink;

    /**
     * Instantiates a new {@code HelpWindow} by singleton design.
     * @param root Stage to use as the root of the HelpWindow.
     */
    private HelpWindow(Stage root, String message) {
        super(FXML, root);
        this.userGuideLink.setText(USERGUIDE_URL);
        this.helpMessage.setText(message);
        this.userGuideLink.setOnAction(event -> copyUrl());
    }

    /**
     * Returns an instance of {@code HelpWindow}, instantiating it if necessary once.
     */
    public HelpWindow() {
        this(new Stage(), HelpCommand.getGeneralHelpMessage());
    }

    /**
     * Shows the help window.
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Copies the URL to the user guide to the clipboard.
     */
    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent url = new ClipboardContent();
        url.putString(USERGUIDE_URL);
        clipboard.setContent(url);
    }

    @FXML
    private void openUserGuide() {
        try {
            Desktop.getDesktop().browse(new URI(USERGUIDE_URL));
        } catch (URISyntaxException se) {
            logger.warning("Failed to open invalid user guide URL: " + USERGUIDE_URL);
        } catch (IOException ioe) {
            logger.warning("Failed to open URL owing to some failed or interrupted I/O: " + ioe.getMessage());
        }
    }
}
