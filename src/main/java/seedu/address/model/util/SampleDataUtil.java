package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import seedu.address.model.AddressBook;
import seedu.address.model.ApplicationsManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyApplicationsManager;
import seedu.address.model.application.Application;
import seedu.address.model.application.ApplicationStatus;
import seedu.address.model.job.Job;
import seedu.address.model.job.JobRounds;
import seedu.address.model.job.JobSkills;
import seedu.address.model.job.JobTitle;
import seedu.address.model.job.JobType;
import seedu.address.model.person.Address;
import seedu.address.model.person.Degree;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.School;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
        public static Person[] getSamplePersons() {
                return new Person[] { // Initialisation order: Name, Phone, Email, Address, School, Degree[, Tags]
                                new Person(new Name("Alex Yeoh"), new Phone("87438807"),
                                                new Email("alexyeoh@example.com"),
                                                new Address("Blk 30 Geylang Street 29, #06-40"), new School("NUS"),
                                                new Degree("Computer Science"), getTagSet("DataScience", "Python")),
                                new Person(new Name("Bernice Yu"), new Phone("99272758"),
                                                new Email("berniceyu@example.com"),
                                                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                                                new School("NUS"), new Degree("Information Systems"),
                                                getTagSet("Flutter", "JavaScript")),
                                new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"),
                                                new Email("charlotte@example.com"),
                                                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"), new School("NTU"),
                                                new Degree("Business Analytics"), getTagSet("Python")),
                                new Person(new Name("David Li"), new Phone("91031282"),
                                                new Email("lidavid@example.com"),
                                                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                                                new School("NTU"), new Degree("Mathematics"), getTagSet("Java")),
                                new Person(new Name("Irfan Ibrahim"), new Phone("92492021"),
                                                new Email("irfan@example.com"),
                                                new Address("Blk 47 Tampines Street 20, #17-35"), new School("SMU"),
                                                new Degree("Business"), getTagSet("Clang")),
                                new Person(new Name("Roy Balakrishnan"), new Phone("92624417"),
                                                new Email("royb@example.com"),
                                                new Address("Blk 45 Aljunied Street 85, #11-31"), new School("SMU"),
                                                new Degree("Political Science"), getTagSet("AWSCertified")) };
        }

        public static Job[] getSampleJobs() {
                return new Job[] { // Initialisation order: JobTitle, JobRounds, JobSkills, JobType
                                new Job(new JobTitle("Software Engineer"), new JobRounds(5),
                                                new JobSkills(FXCollections.observableArrayList("Java", "Spring Boot",
                                                                "Microservices")),
                                                JobType.fromDisplayType("Intern")),
                                new Job(new JobTitle("Data Scientist"), new JobRounds(4),
                                                new JobSkills(FXCollections.observableArrayList("Python", "ML",
                                                                "TensorFlow")),
                                                JobType.fromDisplayType("Full Time")),
                                new Job(new JobTitle("Product Manager"), new JobRounds(3),
                                                new JobSkills(FXCollections.observableArrayList("Agile",
                                                                "Roadmap Planning")),
                                                JobType.fromDisplayType("Contract")),
                                new Job(new JobTitle("UX Designer"), new JobRounds(3),
                                                new JobSkills(FXCollections.observableArrayList("Figma",
                                                                "User Research", "Wire Framing")),
                                                JobType.fromDisplayType("Freelance")),
                                new Job(new JobTitle("DevOps Engineer"), new JobRounds(4),
                                                new JobSkills(FXCollections.observableArrayList("AWS", "Kubernetes",
                                                                "CI/CD")),
                                                JobType.fromDisplayType("Part Time")),
                                new Job(new JobTitle("Full Stack Developer"), new JobRounds(4), new JobSkills(
                                                FXCollections.observableArrayList("React", "Node.js", "MongoDB")),
                                                JobType.fromDisplayType("Full Time")) };
        }

        public static ReadOnlyAddressBook getSampleAddressBook() {
                AddressBook sampleAb = new AddressBook();
                for (Person samplePerson : getSamplePersons()) {
                        sampleAb.addPerson(samplePerson);
                }
                for (Job sampleJob : getSampleJobs()) {
                        sampleAb.addJob(sampleJob);
                }
                return sampleAb;
        }

        public static ReadOnlyApplicationsManager getSampleApplicationsManager() {
                ApplicationsManager sampleAm = new ApplicationsManager();
                Person[] samplePersons = getSamplePersons();
                Job[] sampleJobs = getSampleJobs();

                // Create some sample applications
                Application[] sampleApplications = new Application[] {
                                // Alex applied to Google and is in round 2
                                new Application(samplePersons[0], sampleJobs[0], new ApplicationStatus(2)),

                                // Bernice applied to Microsoft and is in round 3
                                new Application(samplePersons[1], sampleJobs[1], new ApplicationStatus(3)),

                                // Charlotte applied to Apple and is in round 1
                                new Application(samplePersons[2], sampleJobs[2], new ApplicationStatus(1)),

                                // David applied to Meta and is in round 2
                                new Application(samplePersons[3], sampleJobs[3], new ApplicationStatus(2)),

                                // Irfan applied to Amazon and is in round 1
                                new Application(samplePersons[4], sampleJobs[4], new ApplicationStatus(1)),

                                // Roy applied to Netflix and is in round 3
                                new Application(samplePersons[5], sampleJobs[5], new ApplicationStatus(3)),

                                // Alex also applied to Microsoft and is in round 1
                                new Application(samplePersons[0], sampleJobs[1], new ApplicationStatus(1)),

                                // Bernice also applied to Google and is in round 4
                                new Application(samplePersons[1], sampleJobs[0], new ApplicationStatus(4)) };

                // Add all sample applications
                for (Application sampleApplication : sampleApplications) {
                        sampleAm.addApplication(sampleApplication);
                }

                return sampleAm;
        }

        /**
         * Returns a tag set containing the list of strings given.
         */
        public static Set<Tag> getTagSet(String... strings) {
                return Arrays.stream(strings).map(Tag::new).collect(Collectors.toSet());
        }
}
