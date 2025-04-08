package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.ApplicationsManager;
import seedu.address.model.application.Application;

/**
 * A utility class containing a list of {@code Application} objects to be used
 * in tests.
 */
public class TypicalApplications {

    // Alice applied to Software Engineer at Google, currently at status 1 (passed
    // first round)
    public static final Application ALICE_SWE_GOOGLE = new ApplicationBuilder()
            .withApplicant(TypicalPersons.ALICE)
            .withJob(TypicalPersons.SOFTWARE_ENGINEER_GOOGLE)
            .withApplicationStatus(1)
            .build();

    // Bob applied to Software Engineer at Google, currently at status 2 (passed
    // second round)
    public static final Application BOB_SWE_GOOGLE = new ApplicationBuilder()
            .withApplicant(TypicalPersons.BOB)
            .withJob(TypicalPersons.SOFTWARE_ENGINEER_GOOGLE)
            .withApplicationStatus(2)
            .build();

    // Alice applied to Data Scientist at Microsoft, currently at status 0 (just
    // applied)
    public static final Application ALICE_DS_MICROSOFT = new ApplicationBuilder()
            .withApplicant(TypicalPersons.ALICE)
            .withJob(TypicalPersons.DATA_SCIENTIST_MICROSOFT)
            .withApplicationStatus(0)
            .build();

    // Carl applied to Product Manager at Apple, currently at status 3 (passed third
    // round)
    public static final Application CARL_PM_APPLE = new ApplicationBuilder()
            .withApplicant(TypicalPersons.CARL)
            .withJob(TypicalPersons.PRODUCT_MANAGER_APPLE)
            .withApplicationStatus(3)
            .build();

    // Daniel applied to UX Designer at Meta, currently at status 4 (passed fourth
    // round)
    public static final Application DANIEL_UX_META = new ApplicationBuilder()
            .withApplicant(TypicalPersons.DANIEL)
            .withJob(TypicalPersons.UX_DESIGNER_META)
            .withApplicationStatus(2)
            .build();

    private TypicalApplications() {
    } // prevents instantiation

    /**
     * Returns an {@code ApplicationsManager} with all the typical applications.
     */
    public static ApplicationsManager getTypicalApplicationsManager() {
        ApplicationsManager am = new ApplicationsManager();
        for (Application application : getTypicalApplications()) {
            am.addApplication(application);
        }
        return am;
    }

    public static List<Application> getTypicalApplications() {
        return new ArrayList<>(Arrays.asList(ALICE_SWE_GOOGLE, BOB_SWE_GOOGLE,
                ALICE_DS_MICROSOFT, CARL_PM_APPLE, DANIEL_UX_META));
    }
}
