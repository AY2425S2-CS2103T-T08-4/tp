package seedu.address.model.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalApplications.ALICE_SWE_GOOGLE;
import static seedu.address.testutil.TypicalApplications.BOB_SWE_GOOGLE;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.application.exceptions.ApplicationNotFoundException;
import seedu.address.model.application.exceptions.DuplicateApplicationException;
import seedu.address.testutil.ApplicationBuilder;

public class UniqueApplicationListTest {

    private final UniqueApplicationList uniqueApplicationList = new UniqueApplicationList();

    @Test
    public void contains_nullApplication_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueApplicationList.contains(null));
    }

    @Test
    public void contains_applicationNotInList_returnsFalse() {
        assertFalse(uniqueApplicationList.contains(ALICE_SWE_GOOGLE));
    }

    @Test
    public void contains_applicationInList_returnsTrue() {
        uniqueApplicationList.add(ALICE_SWE_GOOGLE);
        assertTrue(uniqueApplicationList.contains(ALICE_SWE_GOOGLE));
    }

    @Test
    public void add_nullApplication_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueApplicationList.add(null));
    }

    @Test
    public void add_duplicateApplication_throwsDuplicateApplicationException() {
        uniqueApplicationList.add(ALICE_SWE_GOOGLE);
        assertThrows(DuplicateApplicationException.class, () -> uniqueApplicationList.add(ALICE_SWE_GOOGLE));
    }

    @Test
    public void add_validApplication_success() {
        uniqueApplicationList.add(ALICE_SWE_GOOGLE);
        UniqueApplicationList expectedList = new UniqueApplicationList();
        expectedList.add(ALICE_SWE_GOOGLE);
        assertEquals(expectedList, uniqueApplicationList);
    }

    @Test
    public void setApplication_nullTargetApplication_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueApplicationList.setApplication(null, ALICE_SWE_GOOGLE));
    }

    @Test
    public void setApplication_nullEditedApplication_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueApplicationList.setApplication(ALICE_SWE_GOOGLE, null));
    }

    @Test
    public void setApplication_targetApplicationNotInList_throwsApplicationNotFoundException() {
        assertThrows(ApplicationNotFoundException.class,
                () -> uniqueApplicationList.setApplication(ALICE_SWE_GOOGLE, ALICE_SWE_GOOGLE));
    }

    @Test
    public void setApplication_editedApplicationIsSameApplication_success() {
        uniqueApplicationList.add(ALICE_SWE_GOOGLE);
        uniqueApplicationList.setApplication(ALICE_SWE_GOOGLE, ALICE_SWE_GOOGLE);
        UniqueApplicationList expectedList = new UniqueApplicationList();
        expectedList.add(ALICE_SWE_GOOGLE);
        assertEquals(expectedList, uniqueApplicationList);
    }

    @Test
    public void setApplication_editedApplicationHasSameIdentity_success() {
        uniqueApplicationList.add(ALICE_SWE_GOOGLE);
        Application editedAliceSwe = new ApplicationBuilder(ALICE_SWE_GOOGLE)
                .withApplicationStatus(2)
                .build();
        uniqueApplicationList.setApplication(ALICE_SWE_GOOGLE, editedAliceSwe);
        UniqueApplicationList expectedList = new UniqueApplicationList();
        expectedList.add(editedAliceSwe);
        assertEquals(expectedList, uniqueApplicationList);
    }

    @Test
    public void setApplication_editedApplicationHasDifferentIdentity_success() {
        uniqueApplicationList.add(ALICE_SWE_GOOGLE);
        uniqueApplicationList.setApplication(ALICE_SWE_GOOGLE, BOB_SWE_GOOGLE);
        UniqueApplicationList expectedList = new UniqueApplicationList();
        expectedList.add(BOB_SWE_GOOGLE);
        assertEquals(expectedList, uniqueApplicationList);
    }

    @Test
    public void setApplication_editedApplicationHasNonUniqueIdentity_throwsDuplicateApplicationException() {
        uniqueApplicationList.add(ALICE_SWE_GOOGLE);
        uniqueApplicationList.add(BOB_SWE_GOOGLE);
        assertThrows(DuplicateApplicationException.class,
                () -> uniqueApplicationList.setApplication(ALICE_SWE_GOOGLE, BOB_SWE_GOOGLE));
    }

    @Test
    public void delete_nullApplication_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueApplicationList.delete(null));
    }

    @Test
    public void delete_applicationDoesNotExist_throwsApplicationNotFoundException() {
        assertThrows(ApplicationNotFoundException.class, () -> uniqueApplicationList.delete(ALICE_SWE_GOOGLE));
    }

    @Test
    public void delete_existingApplication_removesApplication() {
        uniqueApplicationList.add(ALICE_SWE_GOOGLE);
        uniqueApplicationList.delete(ALICE_SWE_GOOGLE);
        UniqueApplicationList expectedList = new UniqueApplicationList();
        assertEquals(expectedList, uniqueApplicationList);
    }

    @Test
    public void setApplications_nullUniqueApplicationList_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> uniqueApplicationList.setApplications((UniqueApplicationList) null));
    }

    @Test
    public void setApplications_uniqueApplicationList_replacesOwnListWithProvidedUniqueApplicationList() {
        uniqueApplicationList.add(ALICE_SWE_GOOGLE);
        UniqueApplicationList expectedList = new UniqueApplicationList();
        expectedList.add(BOB_SWE_GOOGLE);
        uniqueApplicationList.setApplications(expectedList);
        assertEquals(expectedList, uniqueApplicationList);
    }

    @Test
    public void setApplications_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueApplicationList.setApplications((List<Application>) null));
    }

    @Test
    public void setApplications_list_replacesOwnListWithProvidedList() {
        uniqueApplicationList.add(ALICE_SWE_GOOGLE);
        List<Application> applicationList = Collections.singletonList(BOB_SWE_GOOGLE);
        uniqueApplicationList.setApplications(applicationList);
        UniqueApplicationList expectedList = new UniqueApplicationList();
        expectedList.add(BOB_SWE_GOOGLE);
        assertEquals(expectedList, uniqueApplicationList);
    }

    @Test
    public void setApplications_listWithDuplicateApplications_throwsDuplicateApplicationException() {
        List<Application> listWithDuplicateApplications = Arrays.asList(ALICE_SWE_GOOGLE, ALICE_SWE_GOOGLE);
        assertThrows(DuplicateApplicationException.class,
                () -> uniqueApplicationList.setApplications(listWithDuplicateApplications));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class,
                () -> uniqueApplicationList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void iterator_hasNextAndNext_returnsCorrectElements() {
        uniqueApplicationList.add(ALICE_SWE_GOOGLE);
        uniqueApplicationList.add(BOB_SWE_GOOGLE);

        Iterator<Application> iterator = uniqueApplicationList.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(ALICE_SWE_GOOGLE, iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals(BOB_SWE_GOOGLE, iterator.next());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void equals() {
        // Same object -> returns true
        assertTrue(uniqueApplicationList.equals(uniqueApplicationList));

        // Null -> returns false
        assertFalse(uniqueApplicationList.equals(null));

        // Different type -> returns false
        assertFalse(uniqueApplicationList.equals(5));

        // Different list -> returns false
        UniqueApplicationList otherList = new UniqueApplicationList();
        otherList.add(ALICE_SWE_GOOGLE);
        assertFalse(uniqueApplicationList.equals(otherList));

        // Same contents -> returns true
        uniqueApplicationList.add(ALICE_SWE_GOOGLE);
        assertTrue(uniqueApplicationList.equals(otherList));
    }
}
