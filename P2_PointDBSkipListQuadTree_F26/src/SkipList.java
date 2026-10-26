import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import student.TestableRandom;

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
 * This class implements SkipList data structure and contains an inner
 * SkipNode class which the SkipList will make an array of to store data.
 * The list is kept sorted by key at all times, using compareTo() on the
 * key type K to decide ordering. Duplicate keys are permitted; when a
 * duplicate key is inserted, it is spliced in immediately before any
 * existing nodes that share that key, so among equal keys the most
 * recently inserted one appears first.
 *
 * @author Jai Sundaram, Yaswanth Uppalapati
 *
 * @version 2026-10-25
 * @param <K>
 *            Key
 * @param <V>
 *            Value
 */
public class SkipList<K extends Comparable<K>, V>
    implements Iterable<KVPair<K, V>> {
    private SkipNode head; // First element (Sentinel Node)
    private int size; // number of entries in the Skip List
    private Random rng; // source of randomness used to pick node levels

    /**
     * Initializes the fields head, size and level
     */
    public SkipList() {
        head = new SkipNode(null, 0);
        size = 0;
        this.rng = new TestableRandom();
    }

    /**
     * Picks a random level for a new node using a geometric distribution:
     * starting at 1, the level keeps growing by one for as long as the
     * random number generator keeps producing true. This method is public
     * so that it can be tested directly with preset random values.
     *
     * @return a randomly chosen level, which is always at least 1
     */
    public int randomLevel() {
        int level = 1;
        while (rng.nextBoolean()) {
            level++;
        }
        return level;
    }


    /**
     * Searches for all KVPairs whose key matches the given key. Because
     * duplicate keys are allowed, every matching pair is returned, in the
     * order in which they were inserted.
     *
     * @param key
     *            key to be searched for
     * @return an ArrayList containing every KVPair in the list whose key
     *         equals the given key (empty if there are none)
     */
    public ArrayList<KVPair<K, V>> search(K key) {
        ArrayList<KVPair<K, V>> found = new ArrayList<KVPair<K, V>>();
        // walk down from the top level of the header, moving right while
        // the next key is strictly less than the target key
        SkipNode current = head;
        for (int i = head.level; i >= 0; i--) {
            while (current.forward[i] != null
                && current.forward[i].pair.key().compareTo(key) < 0) {
                current = current.forward[i];
            }
        }
        // current now sits just before the first node whose key is >=
        // the target key (if any); collect every consecutive equal key
        current = current.forward[0];
        while (current != null && current.pair.key().compareTo(key) == 0) {
            found.add(current.pair);
            current = current.forward[0];
        }
        return found;
    }


    /**
     * @return the size of the SkipList
     */
    public int size() {
        return size;
    }


    /**
     * Inserts the KVPair in the SkipList at its appropriate spot as
     * designated by its lexicographical order.
     *
     * @param it
     *            the KVPair to be inserted
     */
    @SuppressWarnings("unchecked")
    public void insert(KVPair<K, V> it) {
        // pick a random level for the new node, growing the header first
        // if this node needs to be taller than anything seen so far. A
        // node's level is the index of its topmost pointer, which is one
        // less than the number of pointers randomLevel() asks for.
        int newLevel = randomLevel() - 1;
        if (newLevel > head.level) {
            adjustHead(newLevel);
        }

        // update[i] will hold the last node at level i that comes before
        // the new node's insertion point
        SkipNode[] update = (SkipNode[])Array.newInstance(SkipNode.class,
            head.level + 1);
        SkipNode current = head;
        for (int i = head.level; i >= 0; i--) {
            // move past keys that are strictly less than the new key,
            // so the new node is spliced in immediately before any
            // existing nodes that share its key
            while (current.forward[i] != null
                && current.forward[i].pair.key().compareTo(
                    it.key()) < 0) {
                current = current.forward[i];
            }
            update[i] = current;
        }

        // splice the new node into the forward chains at every level up
        // to its own level
        SkipNode newNode = new SkipNode(it, newLevel);
        for (int i = 0; i <= newLevel; i++) {
            newNode.forward[i] = update[i].forward[i];
            update[i].forward[i] = newNode;
        }
        size++;
    }


    /**
     * Increases the number of levels in head so that no element has more
     * indices than the head.
     *
     * @param newLevel
     *            the new top level that head must support
     */
    public void adjustHead(int newLevel) {
        // build a taller header node, copying over the existing forward
        // pointers and leaving the new, higher pointers empty
        SkipNode newHead = new SkipNode(null, newLevel);
        for (int i = 0; i <= head.level; i++) {
            newHead.forward[i] = head.forward[i];
        }
        head = newHead;
    }


    /**
     * Removes the first KVPair found with a key matching the parameter and
     * returns it.
     *
     * @param key
     *            the key of the KVPair to be removed
     * @return the removed pair if a matching key was found, or null if not
     */
    @SuppressWarnings("unchecked")
    public KVPair<K, V> remove(K key) {
        // find the predecessor of the target node at every level
        SkipNode[] update = (SkipNode[])Array.newInstance(SkipNode.class,
            head.level + 1);
        SkipNode current = head;
        for (int i = head.level; i >= 0; i--) {
            while (current.forward[i] != null
                && current.forward[i].pair.key().compareTo(key) < 0) {
                current = current.forward[i];
            }
            update[i] = current;
        }
        current = current.forward[0];
        if (current == null || current.pair.key().compareTo(key) != 0) {
            return null;
        }
        // unlink the found node from every level it participates in
        for (int i = 0; i <= current.level; i++) {
            if (update[i].forward[i] == current) {
                update[i].forward[i] = current.forward[i];
            }
        }
        size--;
        return current.pair;
    }

    /**
     * Removes a KVPair with the specified value. Since values are not the
     * sort key of the list, this requires a linear scan across the bottom
     * level while tracking, for each level, the most recent node that has
     * a forward pointer at that level.
     *
     * @param val
     *            the value of the KVPair to be removed
     * @return the removed pair if a match was found, or null if not
     */
    @SuppressWarnings("unchecked")
    public KVPair<K, V> removeByValue(V val) {
        SkipNode[] update = (SkipNode[])Array.newInstance(SkipNode.class,
            head.level + 1);
        for (int i = 0; i <= head.level; i++) {
            update[i] = head;
        }

        SkipNode current = head.forward[0];
        SkipNode target = null;
        while (current != null) {
            if (current.pair.value().equals(val)) {
                target = current;
                break;
            }
            // this node has been passed, so it becomes the newest known
            // predecessor at each level it appears in
            for (int i = 0; i <= current.level; i++) {
                update[i] = current;
            }
            current = current.forward[0];
        }

        if (target == null) {
            return null;
        }
        for (int i = 0; i <= target.level; i++) {
            if (update[i].forward[i] == target) {
                update[i].forward[i] = target.forward[i];
            }
        }
        size--;
        return target.pair;
    }

    /**
     * Prints out the SkipList in a human readable format to the console.
     * Every node, including the header node, is printed from left to
     * right along with the number of forward pointers (its depth) that it
     * holds.
     */
    public void dump() {
        System.out.println("SkipList dump:");
        SkipNode current = head;
        while (current != null) {
            String valueString = (current.pair == null) ? "null"
                : current.pair.toString();
            System.out.println("Node has depth " + (current.level + 1)
                + ", Value (" + valueString + ")");
            current = current.forward[0];
        }
        System.out.println("SkipList size is: " + size);
    }

    /**
     * This class implements a SkipNode for the SkipList data structure.
     */
    private class SkipNode {

        // the KVPair to hold
        private KVPair<K, V> pair;
        // An array of pointers to subsequent nodes
        private SkipNode[] forward;
        // the level of the node (index of its topmost forward pointer)
        private int level;

        /**
         * Initializes the fields with the required KVPair and the number of
         * levels from the random level method in the SkipList.
         *
         * @param tempPair
         *            the KVPair to be inserted
         * @param level
         *            the number of levels that the SkipNode should have
         */
        @SuppressWarnings("unchecked")
        public SkipNode(KVPair<K, V> tempPair, int level) {
            pair = tempPair;
            forward = (SkipNode[])Array.newInstance(SkipNode.class,
                level + 1);
            this.level = level;
        }


        /**
         * Returns the KVPair stored in the SkipList.
         *
         * @return the KVPair
         */
        public KVPair<K, V> element() {
            return pair;
        }

    }

    /**
     * An iterator that walks the SkipList from left to right along the
     * bottom level, returning one KVPair per node.
     */
    private class SkipListIterator implements Iterator<KVPair<K, V>> {
        // the node most recently visited; starts at the header
        private SkipNode current;

        /**
         * Creates an iterator positioned at the header node, so that the
         * first call to next() returns the first element in the list.
         */
        public SkipListIterator() {
            current = head;
        }


        /**
         * Checks whether there is another node after the current one.
         *
         * @return true if another KVPair remains to be visited
         */
        @Override
        public boolean hasNext() {
            return current.forward[0] != null;
        }


        /**
         * Advances to the next node and returns its KVPair.
         * Precondition: hasNext() is true.
         *
         * @return the KVPair stored in the next node
         */
        @Override
        public KVPair<K, V> next() {
            KVPair<K, V> elem = current.forward[0].element();
            current = current.forward[0];
            return elem;
        }

    }

    /**
     * Returns an iterator over the KVPairs in this SkipList, in ascending
     * key order.
     *
     * @return a new iterator positioned before the first element
     */
    @Override
    public Iterator<KVPair<K, V>> iterator() {
        return new SkipListIterator();
    }

}
