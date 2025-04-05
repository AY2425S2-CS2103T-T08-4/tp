package seedu.address.model.job;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_JOB_ROUNDS_3;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SKILL_JAVA;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.DATA_SCIENTIST_MICROSOFT;
import static seedu.address.testutil.TypicalPersons.SOFTWARE_ENGINEER_GOOGLE;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.job.exceptions.DuplicateJobException;
import seedu.address.model.job.exceptions.JobNotFoundException;
import seedu.address.testutil.JobBuilder;

public class UniqueJobListTest {
    private final UniqueJobList uniqueJobList = new UniqueJobList();

    @Test
    public void contains_nullJob_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueJobList.contains(null));
    }

    @Test
    public void contains_jobNotInList_returnsFalse() {
        assertFalse(uniqueJobList.contains(SOFTWARE_ENGINEER_GOOGLE));
    }

    @Test
    public void contains_jobInList_returnsTrue() {
        uniqueJobList.add(SOFTWARE_ENGINEER_GOOGLE);
        assertTrue(uniqueJobList.contains(SOFTWARE_ENGINEER_GOOGLE));
    }

    @Test
    public void contains_jobWithSameIdentityFieldsInList_returnsTrue() {
        uniqueJobList.add(SOFTWARE_ENGINEER_GOOGLE);
        Job editJob = new JobBuilder(SOFTWARE_ENGINEER_GOOGLE).withJobRounds(VALID_JOB_ROUNDS_3)
                .withSkills(VALID_SKILL_JAVA).build();
        assertTrue(uniqueJobList.contains(editJob));
    }

    @Test
    public void add_nullJob_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueJobList.add(null));
    }

    @Test
    public void add_duplicateJob_throwsDuplicateJobException() {
        uniqueJobList.add(SOFTWARE_ENGINEER_GOOGLE);
        assertThrows(DuplicateJobException.class, () -> uniqueJobList.add(SOFTWARE_ENGINEER_GOOGLE));
    }

    @Test
    public void setJob_nullTargetJob_throwsNullPointerException() {
        uniqueJobList.add(SOFTWARE_ENGINEER_GOOGLE);
        assertThrows(NullPointerException.class, () -> uniqueJobList.setJob(SOFTWARE_ENGINEER_GOOGLE, null));
    }

    @Test
    public void setJob_targetJobNotInList_throwsJobNotFoundException() {
        assertThrows(JobNotFoundException.class, () -> uniqueJobList
                .setJob(SOFTWARE_ENGINEER_GOOGLE, SOFTWARE_ENGINEER_GOOGLE));
    }

    @Test
    public void setJob_editedJobIsSameJob_success() {
        uniqueJobList.add(SOFTWARE_ENGINEER_GOOGLE);
        uniqueJobList.setJob(SOFTWARE_ENGINEER_GOOGLE, SOFTWARE_ENGINEER_GOOGLE);
        UniqueJobList expectedUniqueJobList = new UniqueJobList();
        expectedUniqueJobList.add(SOFTWARE_ENGINEER_GOOGLE);
        assertEquals(expectedUniqueJobList, uniqueJobList);
    }

    @Test
    public void setJob_editedJobSameTitle_success() {
        uniqueJobList.add(SOFTWARE_ENGINEER_GOOGLE);
        uniqueJobList.setJob(SOFTWARE_ENGINEER_GOOGLE, DATA_SCIENTIST_MICROSOFT);
        UniqueJobList expectedUniqueJobList = new UniqueJobList();
        expectedUniqueJobList.add(DATA_SCIENTIST_MICROSOFT);
        assertEquals(expectedUniqueJobList, uniqueJobList);
    }

    @Test
    public void setJob_editedJobHasNonUniqueTitle_throwsDuplicateJobException() {
        uniqueJobList.add(SOFTWARE_ENGINEER_GOOGLE);
        uniqueJobList.add(DATA_SCIENTIST_MICROSOFT);
        assertThrows(DuplicateJobException.class, () -> uniqueJobList
                .setJob(SOFTWARE_ENGINEER_GOOGLE, DATA_SCIENTIST_MICROSOFT));
    }

    @Test
    public void remove_nullJob_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueJobList.remove(null));
    }

    @Test
    public void remove_jobDoesNotExist_throwsJobNotFoundException() {
        assertThrows(JobNotFoundException.class, () -> uniqueJobList.remove(SOFTWARE_ENGINEER_GOOGLE));
    }

    @Test
    public void remove_existingJob_removesJob() {
        uniqueJobList.add(SOFTWARE_ENGINEER_GOOGLE);
        uniqueJobList.remove(SOFTWARE_ENGINEER_GOOGLE);
        UniqueJobList expectedUniqueJobList = new UniqueJobList();
        assertEquals(expectedUniqueJobList, uniqueJobList);
    }

    @Test
    public void setJobs_nullUniqueJobList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueJobList.setJobs((UniqueJobList) null));
    }

    @Test
    public void setJobs_uniqueJobList_replacesOwnListWithProvidedUniqueJobList() {
        uniqueJobList.add(DATA_SCIENTIST_MICROSOFT);
        UniqueJobList expectedUniqueJobList = new UniqueJobList();
        expectedUniqueJobList.add(DATA_SCIENTIST_MICROSOFT);
        uniqueJobList.setJobs(expectedUniqueJobList);
        assertEquals(expectedUniqueJobList, uniqueJobList);
    }

    @Test
    public void setJobs_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueJobList.setJobs((List<Job>) null));
    }

    @Test
    public void setJobs_list_replacesOwnListWithProvidedList() {
        uniqueJobList.add(SOFTWARE_ENGINEER_GOOGLE);
        List<Job> jobList = Collections.singletonList(DATA_SCIENTIST_MICROSOFT);
        uniqueJobList.setJobs(jobList);
        UniqueJobList expectedUniqueJobList = new UniqueJobList();
        expectedUniqueJobList.add(DATA_SCIENTIST_MICROSOFT);
        assertEquals(expectedUniqueJobList, uniqueJobList);
    }

    @Test
    public void setJobs_listWithDuplicateJobs_throwsDuplicatePersonException() {
        List<Job> listWithDuplicateJobs = Arrays.asList(SOFTWARE_ENGINEER_GOOGLE, SOFTWARE_ENGINEER_GOOGLE);
        assertThrows(DuplicateJobException.class, () -> uniqueJobList.setJobs(listWithDuplicateJobs));
    }

    @Test
    public void asUnmodifiableObservaleList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> uniqueJobList.asUnmodifiableObservableList()
                .remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueJobList.asUnmodifiableObservableList().toString(), uniqueJobList.toString());
    }
}
