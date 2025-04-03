---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# TalentMatch User Guide

TalentMatch is a **desktop app for managing applicants, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, TalentMatch can get your applicant management tasks done faster than traditional GUI apps.

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/se-edu/addressbook-level3/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your AddressBook.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar addressbook.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01` : Adds a contact named `John Doe` to the Address Book.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* Job commands should be called only in job view while person commands should be called only in person view. This is to ensure users can see real time updates in response to their query.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</box>

### Viewing help : `help`

Shows a message explaning how to access the help page.

![help message](images/helpMessage.png)

Format: `help`

### Switching views : `switchview`

Switches the view of GUI from a persons list to a jobs list

Format: `switchview
`
### Adding a person/job/application

#### Adding a person: `add`
Adds a person to TalentMatch.

Format: `add n/NAME s/SCHOOL d/DEGREE p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​`

<box type="tip" seamless>

**Tip:** A person can have any number of tags (including 0)
</box>

Examples:
* `add n/John Doe s/NUS d/Computer Science p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
* `add n/Betsy Crowe s/NTU d/Civil Engineering t/friend e/betsycrowe@example.com a/Newgate Prison p/1234567 t/criminal`

---

#### Adding a job: `addjob`
Adds a job to TalentMatch

Format: `addjob jt/JOB_TITLE jr/INTERVIEW_ROUNDS js/JOB_SKILLS ja/JOB_ADDRESS em/JOB_TYPE`

Examples:
* `addjob jt/Software Engineering jr/3 js/Python React ja/1 Fusionopolis Place, Galaxis, Singapore 138522 em/Intern`

---

#### Adding an application: `addapp`
Adds an application to TalentMatch

Format: `addapp ip/PERSON_INDEX ij/JOB_INDEX`

<box type="tip" seamless>

**Tips:**
* All applications start from the 0th round each time.
* The person index can be obtained by switching to person view in TalentMatch.
* The job index can be obtained by switching to job view in TalentMatch.
</box>

<box type="warning" seamless>

**Constraints:**
* Person and job indices must be valid positive integers existing in the respective views.
TalentMatch flags it out with this exception message:
    * ```Invalid command format!
      addapp: Adds an application to the address book.
      Parameters: ip/PERSON INDEX IN PERSON VIEW ij/JOB INDEX IN JOB VIEW
      Example: addapp ip/1 ij/2```
* No existing applications must be present.
TalentMatch flags duplicate applications for the same applicant and job out with this exception message:
    * `This application already exists in the address book Try using delapp instead!`
</box>

Example of a successful command alongside graphical depiction:
* `addapp ip/1 ij/4`
* ![img_1.png](img_1.png)


### Listing all persons/jobs

#### Listing all persons: `list`

Shows a list of all persons in TalentMatch.

Format: `list`

---

#### Listing all jobs: `listjob`

Shows a list of all jobs in TalentMatch.

Format: `listjob`

### Editing a person/job

#### Editing a person: `edit`

Edits an existing person in TalentMatch.

Format: `edit INDEX [n/NAME] [s/SCHOOL] [d/DEGREE] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the person will be removed i.e adding of tags is not cumulative.
* You can remove all the person's tags by typing `t/` without
    specifying any tags after it.

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st person to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2 n/Betsy Crower t/` Edits the name of the 2nd person to be `Betsy Crower` and clears all existing tags.

---

#### Editing a job: `editjob`

Edits an existing job in TalentMatch.

Format: `editjob INDEX [jt/JOB_TITLE] [jr/INTERVIEW_ROUNDS] [js/JOB_SKILLS] [ja/JOB_ADDRESS] [em/JOB_TYPE]`

* Edits the job at the specified `INDEX`. The index refers to the index number shown in the displayed job list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing job skills, the existing skills of the person will be removed i.e adding of skills is not cumulative.
* You can remove all the job's skills by typing `js/` without
  specifying any skills after it.

Examples:
*  `editjob 7 jt/Software Engineering jr/3 [js/Python React ja/1 Fusionopolis Place, Galaxis, Singapore 138522 em/Intern`

### Locating persons/jobs/applications:

#### Locating persons: `find`

Finds persons whose details contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only full words will be matched e.g. `Han` will not match `Hans`
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

---

#### Locating jobs: `findjob`

Finds jobs whose details contain any of the given keywords.

Format: `findjob KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `software` will match `Software`
* The order of the keywords does not matter. e.g. `Engineering Software` will match `Software Engineering`
* Only full words will be matched e.g. `Engi` will not match `Engineer`
* Jobs matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Engineer` will return `Software Engineer`, `AI Engineer`

Examples:
* `findjob Software` returns `Software Engineering`

---

#### Locating application: `findapp`

Finds application by job index and application status in job view.

Format: `findapp ij/JOB INDEX as/APPLICATION STATUS`

<box type="tip" seamless>

**Tip:**
* Both the job index and application status can be retrieved from the job view alone.
* The application status is mandatory but the job index is optional. Specify one for a more general search!
* </box>

<box type="warning" seamless>

**Constraints:**
* Job indices and application status must be valid positive integers existing in the respective views.
TalentMatch flags it out with this exception message:
    * ```Invalid command format!
      findapp: Filters the list to show only applications with the specified status in job view.
      Parameters: ij/ OPTIONAL JOB INDEX IN JOB VIEW as/ROUNDS
      Example: findappij/ 1 as/ 2```
* The desired application must already exist in TalentMatch. TalentMatch cannot find a non-existent application!
</box>
Example of a successful command alongside graphical depiction:
* `findapp as/2`
* ![img_6.png](img_6.png)

### Deleting a person/job/application

#### Deleting a person: `delete`

Deletes the specified person from TalentMatch.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd person in TalentMatch.
* `find Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.

---

#### Deleting a job: `deletejob`

Deletes the specified job from TalentMatch.

Format: `deletejob INDEX`

* Deletes the job at the specified `INDEX`.
* The index refers to the index number shown in the displayed job list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `listjob` followed by `deletejob 2` deletes the 2nd job in TalentMatch.
* `findjob Software Engineering` followed by `delete 1` deletes the 1st job in the results of the `find` command.

---

#### Deleting an application: `delapp`

Deletes the specified application from TalentMatch.

Format: `delapp ij/JOB_INDEX ia/APPLICATION_BY_JOB_INDEX`

* Deletes the application with the specified job index and the application index in the corresponding `JobCard` in job view.

<box type="tip" seamless>
**Tip:**
* Both the job and application-by-job indices can be obtained from the job view alone.
</box>

<box type="warning" seamless>

**Constraints:** 
* HR recruiters must toggle to the job view, if not already in this view, to delete an application.
TalentMatch flags it out with this exception message:
    * `This command is only available in job view. Please switch to job view first using 'switchview' command.`
* Job and application-by-job indices must be valid positive integers existing in job view.
TalentMatch flags it out with this exception message:
    * ```Invalid command format!
      delapp: Deletes an application from the address book.
      Parameters: ij/<JOB_INDEX> ia/<APPLICATION_INDEX>
      Example: delapp ij/1 ia/2```
* A unique existing application must be present. 
TalentMatch flags deleting non-existent out applications with this exception message:
    * `This application does not exist in the address book. Try using addapp to add an application first!`
</box>

Example of a successful command alongside graphical depiction:
* `delapp ip/1 ij/4`
* ![img_4.png](img_4.png)

### Advancing applications: `advapp`

Advances an application to the next round of interview

<box type="tip" seamless>
* All applications are advanced by exactly 1 round each time.
* HR recruiters should exercise discretion in advancing an application as it signifies that the applicant has not only
gone for the round, but also passed it! Therefore, an applicant who has reached the final round of an application will
be deemed to have received a job offer. Congratulations!
* HR recruiters should exercise discretion in advancing an application of any applicant who has reached the final round
of another application, as this would otherwise imply the applicant has more than 1 offers for this company!
</box>

<box type="warning" seamless>

**Constraints:**
* HR recruiters must toggle to the job view, if not already in this view, to advance an application.
TalentMatch flags it out with this exception message:
    * `This command is only available in job view. Please switch to job view first using 'switchview' command.`
* Job and application-by-job indices must be valid positive integers existing in job view.
TalentMatch flags it out with this exception message:
    * ```Invalid command format!
      advapp: Advances an application in the address book.
      Parameters: ij/<JOB_INDEX> ia/<APPLICATION_INDEX>
      Example: advapp ij/1 ia/2```
* A unique existing application must be present.
TalentMatch flags advancing non-existent applications out with this exception message:
    * `This application does not exist in the address book. Try using addapp to add an application first!`
</box>

Format: `advapp p/PHONE_NUMBER jt/JOB_TITLE [as/ROUNDS]`

* Advances the specified application by the specified ROUNDS
* Default rounds would be 1

Example of a successful command alongside graphical depiction:
* `advapp ip/1 ij/4`
* ![img_5.png](img_5.png)

Clears all person entries from TalentMatch.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

TalentMatch data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

TalentMatch data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<box type="warning" seamless>

**Caution:**
If your changes to the data file makes its format invalid, AddressBook will discard all data and start with an empty data file at the next run.  Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the AddressBook to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</box>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous AddressBook home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action     | Format, Examples
-----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------
**Add**    | `add n/NAME s/SCHOOL d/DEGREE p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​` <br> e.g., `add n/James Ho s/NUS d/Physics p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/friend t/colleague`
**AddJob** | `addjob jt/JOB_TITLE jr/INTERVIEW_ROUNDS js/JOB_SKILLS ja/JOB_ADDRESS em/JOB_TYPE` <br> e.g., `addjob jt/Software Engineering jr/3 js/Python React ja/1 Fusionopolis Place, Galaxis, Singapore 138522 em/Intern`
**AddApp** | `addapp ip/PHONE_INDEX ij/JOB_INDEX ` <br> e.g., `addapp ip/1 ij/1`
**AdvApp** | `advapp ij/JOB_INDEX ia/APPLICANT_BY_JOB_INDEX ` <br> e.g., `advapp ij/1 ia/1`
**Clear**  | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**DeleteJob** | `deletejob INDEX` <br> e.g., `deletejob 3`
**DeleteApp** | `delapp ij/JOB_INDEX ia/APPLICANT_BY_JOB_INDEX ` <br> e.g., `delapp ij/1 ia/1`
**Edit**   | `edit INDEX [n/NAME] [s/SCHOOL] [d/DEGREE] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`
**EditJob** | `editjob INDEX [jt/JOB_TITLE] [jr/INTERVIEW_ROUNDS] [js/JOB_SKILLS] [ja/JOB_ADDRESS] [em/JOB_TYPE]` <br> e.g., `editjob 7 jt/Software Engineering jr/3 [js/Python React ja/1 Fusionopolis Place, Galaxis, Singapore 138522 em/Intern`
**Find**   | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**FindJob** | `findjob KEYWORD [MORE_KEYWORDS]`<br> e.g., `findjob Software Engineering`
**FindApp** | `findapp p/PHONE_NUMBER jt/JOB_TITLE` <br> e.g., `findapp p/98765432 jt/Software Engineering`
**List**   | `list`
**ListJobs** | `listjob`
**Help**   | `help`
**SwitchView** | `switchview`
