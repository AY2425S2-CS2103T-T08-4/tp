package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Degree;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.School;
import seedu.address.model.skill.Skill;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String school;
    private final String degree;
    private final List<JsonAdaptedSkill> skills = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("email") String email, @JsonProperty("address") String address,
            @JsonProperty("school") String school, @JsonProperty("degree") String degree,
            @JsonProperty("skills") List<JsonAdaptedSkill> skills) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.school = school;
        this.degree = degree;
        if (skills != null) {
            this.skills.addAll(skills);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        school = source.getSchool().value;
        degree = source.getDegree().value;
        skills.addAll(source.getSkills().stream().map(JsonAdaptedSkill::new).toList());
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's
     * {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in
     *                               the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Skill> personSkills = new ArrayList<>();
        for (JsonAdaptedSkill skill : skills) {
            personSkills.add(skill.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        if (school == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, School.class.getSimpleName()));
        }
        final School modelSchool = new School(school);

        if (degree == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Degree.class.getSimpleName()));
        }
        final Degree modelDegree = new Degree(degree);

        final Set<Skill> modelSkills = new HashSet<>(personSkills);
        return new Person(modelName, modelPhone, modelEmail, modelAddress, modelSchool, modelDegree, modelSkills);
    }

}
