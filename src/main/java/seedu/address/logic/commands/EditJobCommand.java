package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPLOYMENT_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_COMPANY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_ROUNDS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_SKILLS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_TITLE;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.job.Job;
import seedu.address.model.job.JobAddress;
import seedu.address.model.job.JobCompany;
import seedu.address.model.job.JobRounds;
import seedu.address.model.job.JobSkills;
import seedu.address.model.job.JobTitle;
import seedu.address.model.job.JobType;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditJobCommand extends Command {

    public static final String COMMAND_WORD = "editjob";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the job identified "
            + "by the index number used in the displayed job list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) " + "[" + PREFIX_JOB_TITLE + "JOB TITLE] "
            + "[" + PREFIX_JOB_COMPANY + "COMPANY NAME] " + "[" + PREFIX_JOB_ROUNDS + "NUMBER OF ROUNDS] "
            + "[" + PREFIX_JOB_SKILLS + "SKILLS] " + "[" + PREFIX_JOB_ADDRESS + "COMPANY ADDRESS] "
            + "[" + PREFIX_EMPLOYMENT_TYPE + "WORK TYPE]";

    public static final String MESSAGE_EDIT_JOB_SUCCESS = "Edited Job: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_JOB = "This job already exists in the address book.";

    private final Index index;
    private final EditJobDescriptor editJobDescriptor;

    /**
     * @param index                of the person in the filtered person list to edit
     * @param editJobDescriptor details to edit the person with
     */
    public EditJobCommand(Index index, EditJobDescriptor editJobDescriptor) {
        requireNonNull(index);
        requireNonNull(editJobDescriptor);

        this.index = index;
        this.editJobDescriptor = new EditJobDescriptor(editJobDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Job> lastShownList = model.getFilteredJobList();

        if (this.index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_JOB_DISPLAYED_INDEX);
        }

        Job jobToEdit = lastShownList.get(this.index.getZeroBased());
        Job editedJob = createEditedJob(jobToEdit, editJobDescriptor);

        if (!jobToEdit.isSameJob(editedJob) && model.hasJob(editedJob)) {
            throw new CommandException(MESSAGE_DUPLICATE_JOB);
        }

        model.setJob(jobToEdit, editedJob);
        model.resetFilteredJobList();
        return new CommandResult(String.format(MESSAGE_EDIT_JOB_SUCCESS, Messages.format(editedJob)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code jobToEdit}
     * edited with {@code editJobDescriptor}.
     */
    private static Job createEditedJob(Job jobToEdit, EditJobDescriptor editJobDescriptor) {
        assert jobToEdit != null;

        JobTitle updatedJobTitle = editJobDescriptor.getJobTitle().orElse(jobToEdit.getJobTitle());
        JobCompany updatedJobCompany = editJobDescriptor.getJobCompany().orElse(jobToEdit.getJobCompany());
        JobRounds updatedJobRounds = editJobDescriptor.getJobRounds().orElse(jobToEdit.getJobRounds());
        JobSkills updatedJobSkills = editJobDescriptor.getJobSkills().orElse(jobToEdit.getJobSkills());
        JobAddress updatedJobAddress = editJobDescriptor.getJobAddress().orElse(jobToEdit.getJobAddress());
        JobType updatedJobType = editJobDescriptor.getJobType().orElse(jobToEdit.getJobType());

        return new Job(updatedJobTitle, updatedJobCompany, updatedJobRounds, updatedJobSkills,
                updatedJobAddress, updatedJobType);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof EditJobCommand otherEditJobCommand)) {
            return false;
        }
        return this.index.equals(otherEditJobCommand.index)
                && this.editJobDescriptor.equals(otherEditJobCommand.editJobDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("index", this.index)
                .add("editPersonDescriptor", this.editJobDescriptor).toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will
     * replace the corresponding field value of the person.
     */
    public static class EditJobDescriptor {
        private JobTitle jobTitle;
        private JobCompany jobCompany;
        private JobRounds jobRounds;
        private JobSkills jobSkills;
        private JobAddress jobAddress;
        private JobType jobType;

        public EditJobDescriptor() {
        }

        /**
         * Copy constructor. A defensive copy of {@code tags} is used internally.
         */
        public EditJobDescriptor(EditJobDescriptor toCopy) {
            setJobTitle(toCopy.jobTitle);
            setJobCompany(toCopy.jobCompany);
            setJobRounds(toCopy.jobRounds);
            setJobSkills(toCopy.jobSkills);
            setJobAddress(toCopy.jobAddress);
            setJobType(toCopy.jobType);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.jobTitle, this.jobCompany, this.jobRounds, this.jobSkills,
                    this.jobAddress, this.jobType);
        }

        public void setJobTitle(JobTitle jobTitle) {
            this.jobTitle = jobTitle;
        }

        public Optional<JobTitle> getJobTitle() {
            return Optional.ofNullable(this.jobTitle);
        }

        public void setJobCompany(JobCompany jobCompany) {
            this.jobCompany = jobCompany;
        }

        public Optional<JobCompany> getJobCompany() {
            return Optional.ofNullable(this.jobCompany);
        }

        public void setJobRounds(JobRounds jobRounds) {
            this.jobRounds = jobRounds;
        }

        public Optional<JobRounds> getJobRounds() {
            return Optional.ofNullable(this.jobRounds);
        }

        public void setJobSkills(JobSkills jobSkills) {
            this.jobSkills = jobSkills;
        }

        public Optional<JobSkills> getJobSkills() {
            return Optional.ofNullable(this.jobSkills);
        }

        public void setJobAddress(JobAddress jobAddress) {
            this.jobAddress = jobAddress;
        }

        public Optional<JobAddress> getJobAddress() {
            return Optional.ofNullable(jobAddress);
        }

        public void setJobType(JobType jobType) {
            this.jobType = jobType;
        }

        public Optional<JobType> getJobType() {
            return Optional.ofNullable(jobType);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}. A defensive copy of
         * {@code tags} is used internally.
         */

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof EditJobDescriptor otherEditJobDescriptor)) {
                return false;
            }
            return Objects.equals(this.jobTitle, otherEditJobDescriptor.jobTitle)
                    && Objects.equals(this.jobCompany, otherEditJobDescriptor.jobCompany)
                    && Objects.equals(this.jobRounds, otherEditJobDescriptor.jobRounds)
                    && Objects.equals(this.jobSkills, otherEditJobDescriptor.jobSkills)
                    && Objects.equals(this.jobAddress, otherEditJobDescriptor.jobAddress)
                    && Objects.equals(this.jobType, otherEditJobDescriptor.jobType);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this).add("job title", this.jobTitle)
                    .add("company name", this.jobCompany).add("job rounds", this.jobRounds)
                    .add("job skills", this.jobSkills).add("company address", this.jobAddress)
                    .add("job type", this.jobType).toString();
        }
    }
}
