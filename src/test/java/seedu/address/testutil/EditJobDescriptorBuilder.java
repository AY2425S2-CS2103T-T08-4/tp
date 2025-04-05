package seedu.address.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditJobCommand.EditJobDescriptor;
import seedu.address.model.job.Job;
import seedu.address.model.job.JobRounds;
import seedu.address.model.job.JobTitle;
import seedu.address.model.skill.Skill;

/**
 * A utility class to help with building EditJobDescriptor objects.
 */
public class EditJobDescriptorBuilder {

    private final EditJobDescriptor descriptor;

    public EditJobDescriptorBuilder() {
        descriptor = new EditJobDescriptor();
    }

    public EditJobDescriptorBuilder(EditJobDescriptor descriptor) {
        this.descriptor = new EditJobDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPersonDescriptor} with fields containing
     * {@code job}'s details
     */
    public EditJobDescriptorBuilder(Job job) {
        descriptor = new EditJobDescriptor();
        descriptor.setJobTitle(job.getJobTitle());
        descriptor.setJobRounds(job.getJobRounds());
        descriptor.setSkills(job.getSkills());
    }

    /**
     * Sets the {@code JobTitle} of the {@code EditJobDescriptor} that we are
     * building.
     */
    public EditJobDescriptorBuilder withJobTitle(String jobTitle) {
        descriptor.setJobTitle(new JobTitle(jobTitle));
        return this;
    }

    /**
     * Sets the {@code JobRounds} of the {@code EditJobDescriptor} that we are
     * building.
     */
    public EditJobDescriptorBuilder withJobRounds(int jobRounds) {
        descriptor.setJobRounds(new JobRounds(jobRounds));
        return this;
    }

    /**
     * Parses the {@code skills} into a {@code Set<Skill>} and set it to the
     * {@code EditPersonDescriptor} that we are building.
     */
    public EditJobDescriptorBuilder withSkills(String... skills) {
        Set<Skill> skillSet = Stream.of(skills).map(Skill::new).collect(Collectors.toSet());
        descriptor.setSkills(skillSet);
        return this;
    }

    public EditJobDescriptor build() {
        return descriptor;
    }
}
