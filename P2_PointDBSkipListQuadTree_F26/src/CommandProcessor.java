// On my honor:
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or unmodified.
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
// - I have not discussed coding details about this project with
// anyone other than the instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.

/**
 * The purpose of this class is to parse each line of the command file and
 * call the matching Database method. Commands are free-format: any number
 * of spaces may come before, between, or after the command name and its
 * parameters.
 *
 * @author Jai Sundaram, Yaswanth Uppalapati
 *
 * @version 2026-10-25
 */
public class CommandProcessor {

    // the database that the commands are applied to
    private Database data;

    /**
     * Creates a command processor with a new, empty database.
     */
    public CommandProcessor() {
        data = new Database();
    }


    /**
     * Parses one line of the command file and calls the matching Database
     * method. Blank lines are ignored.
     *
     * @param line
     *            a single line from the command file
     */
    public void processor(String line) {
        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            return;
        }
        String[] arr = trimmed.split("\\s+");
        String command = arr[0];
        int numParam = arr.length - 1;

        if (command.equals("insert") && numParam == 3) {
            data.insert(arr[1], Integer.parseInt(arr[2]),
                Integer.parseInt(arr[3]));
        }
        else if (command.equals("remove") && numParam == 1) {
            data.remove(arr[1]);
        }
        else if (command.equals("remove") && numParam == 2) {
            data.remove(Integer.parseInt(arr[1]), Integer.parseInt(arr[2]));
        }
        else if (command.equals("regionsearch") && numParam == 4) {
            data.regionsearch(Integer.parseInt(arr[1]), Integer.parseInt(
                arr[2]), Integer.parseInt(arr[3]), Integer.parseInt(arr[4]));
        }
        else if (command.equals("duplicates") && numParam == 0) {
            data.duplicates();
        }
        else if (command.equals("search") && numParam == 1) {
            data.search(arr[1]);
        }
        else if (command.equals("dump") && numParam == 0) {
            data.dump();
        }
        else {
            System.out.println("Unrecognized command: " + trimmed);
        }
    }
}
