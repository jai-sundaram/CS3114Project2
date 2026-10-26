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
 * KVPair class declaration
 *
 * @param <K>
 *            Key
 * @param <E>
 *            Element
 * @author CS Staff
 * @version September 2026
 */
public class KVPair<K extends Comparable<K>, E> 
    implements Comparable<KVPair<K, E>> {
    
    private K theKey; // The key
    private E theVal; // The value

    /**
     * Create a new KVPair object.
     *
     * @param key
     *            The key
     * @param value
     *            The value
     */
    KVPair(K key, E value) {
        theKey = key;
        theVal = value;
    }


    /**
     * Compare KVPairs
     *
     * @param it
     *            What to compare against
     * @return Standard values for compareTo
     */
    public int compareTo(KVPair<K, E> it) {
        return theKey.compareTo(it.key());
    }


    /**
     * Compare against a key.
     *
     * @param it
     *            The key to compare to
     * @return Standard values for compareTo
     */
    public int compareTo(K it) {
        return theKey.compareTo(it);
    }


    /**
     * Getter for key.
     *
     * @return The key
     */
    public K key() {
        return theKey;
    }


    /**
     * Getter for value.
     *
     * @return The value
     */
    public E value() {
        return theVal;
    }


    /**
     * toString override
     *
     * @return key, value as a string
     */

    public String toString() {
        return theKey.toString() + ", " + theVal.toString();
    }
}
