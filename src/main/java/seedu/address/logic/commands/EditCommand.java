package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEGREE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHOOL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SKILL;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Degree;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.School;
import seedu.address.model.skill.Skill;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {
    public static final String COMMAND_WORD = "edit";
    public static final String BRIEF_MESSAGE_USAGE = "INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] " + "[" + PREFIX_SCHOOL + "SCHOOL] " + "[" + PREFIX_DEGREE + "DEGREE] "
            + "[" + PREFIX_PHONE + "PHONE] " + "[" + PREFIX_EMAIL + "EMAIL] " + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_SKILL + "SKILL]...";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: " + BRIEF_MESSAGE_USAGE
            + "\nExample: " + COMMAND_WORD + " 1 " + PREFIX_PHONE + "91234567 " + PREFIX_EMAIL + "johndoe@example.com";
    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";
    public static final String MESSAGE_WRONG_VIEW = "This command is only available in person view. "
            + "Please switch to person view first using " + SwitchViewCommand.COMMAND_WORD + " command.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index                of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Check that we're in person view
        if (model.isInJobView()) {
            throw new CommandException(Messages.MESSAGE_NOT_IN_PERSON_VIEW);
        }

        List<Person> lastShownList = model.getFilteredPersonList();

        if (this.index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(this.index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);
        return CommandResult.withFeedback(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        School updatedSchool = editPersonDescriptor.getSchool().orElse(personToEdit.getSchool());
        Degree updatedDegree = editPersonDescriptor.getDegree().orElse(personToEdit.getDegree());
        Set<Skill> updatedSkills = editPersonDescriptor.getSkills().orElse(personToEdit.getSkills());

        return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedSchool, updatedDegree,
                updatedSkills);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof EditCommand otherEditCommand)) {
            return false;
        }
        return this.index.equals(otherEditCommand.index)
                && this.editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("index", this.index).add("editPersonDescriptor", this.editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will
     * replace the corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Degree degree;
        private Phone phone;
        private Email email;
        private Address address;
        private School school;
        private Set<Skill> skills;

        public EditPersonDescriptor() {
        }

        /**
         * Copy constructor. A defensive copy of {@code EditPersonDescriptor} is used
         * internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setDegree(toCopy.degree);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setSchool(toCopy.school);
            setSkills(toCopy.skills);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.name, this.phone, this.email, this.address,
                    this.skills, this.school, this.degree);
        }

        public void setName(Name newName) {
            this.name = newName;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(this.name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(this.phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(this.email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(this.address);
        }

        public void setSchool(School school) {
            this.school = school;
        }

        public Optional<School> getSchool() {
            return Optional.ofNullable(school);
        }

        public void setDegree(Degree degree) {
            this.degree = degree;
        }

        public Optional<Degree> getDegree() {
            return Optional.ofNullable(degree);
        }

        /**
         * Sets {@code skills} to this object's {@code skills}. A defensive copy of
         * {@code skills} is used internally.
         */
        public void setSkills(Set<Skill> skills) {
            this.skills = (skills != null) ? new HashSet<>(skills) : null;
        }

        /**
         * Returns an unmodifiable skill set, which throws
         * {@code UnsupportedOperationException} if modification is attempted. Returns
         * {@code Optional#empty()} if {@code skills} is null.
         */
        public Optional<Set<Skill>> getSkills() {
            return (this.skills != null) ? Optional.of(Collections.unmodifiableSet(skills)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            if (!(other instanceof EditPersonDescriptor otherEditPersonDescriptor)) {
                return false;
            }
            return Objects.equals(this.name, otherEditPersonDescriptor.name)
                    && Objects.equals(this.phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(this.email, otherEditPersonDescriptor.email)
                    && Objects.equals(this.address, otherEditPersonDescriptor.address)
                    && Objects.equals(this.school, otherEditPersonDescriptor.school)
                    && Objects.equals(this.degree, otherEditPersonDescriptor.degree)
                    && Objects.equals(this.skills, otherEditPersonDescriptor.skills);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this).add("name", this.name).add("school", this.school).add("email", this.email)
                    .add("address", this.address).add("skills", this.skills).add("degree", this.degree)
                    .add("phone", this.phone).toString();
        }
    }
}
