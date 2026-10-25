import java.util.ArrayList;

/**
 * This class receives commands from the CommandProcessor and farms them out
 * to the SkipList (which organizes points by name) and the QuadTree (which
 * organizes points by location). Both structures hold pointers to the same
 * shared Point objects, so inserts and removes update both of them to keep
 * them in sync.
 *
 * @author Jai Sundaram, Yaswanth Uppalapati
 *
 * @version 2026-10-11
 */
public class Database {

    // the points organized by name
    private SkipList<String, Point> list;
    // the points organized by location
    private QuadTree tree;

    /**
     * Creates an empty database.
     */
    public Database() {
        list = new SkipList<String, Point>();
        tree = new QuadTree();
    }


    /**
     * Inserts a point into both structures, unless it lies outside the
     * world box, in which case it is rejected.
     *
     * @param name
     *            the name of the point
     * @param x
     *            the x-coordinate of the point
     * @param y
     *            the y-coordinate of the point
     */
    public void insert(String name, int x, int y) {
        Point p = new Point(name, x, y);
        if (!Point.inWorld(x, y)) {
            System.out.println("Point rejected: " + p.fullString());
            return;
        }
        list.insert(new KVPair<String, Point>(name, p));
        tree.insert(p);
        System.out.println("Point inserted: " + p.fullString());
    }


    /**
     * Removes one point with the given name from both structures.
     *
     * @param name
     *            the name of the point to remove
     */
    public void remove(String name) {
        KVPair<String, Point> removed = list.remove(name);
        if (removed == null) {
            System.out.println("Point not removed: " + name);
            return;
        }
        tree.remove(removed.value());
        System.out.println("Point removed: " + removed.value().fullString());
    }


    /**
     * Removes one point at the given location from both structures.
     *
     * @param x
     *            the x-coordinate of the point to remove
     * @param y
     *            the y-coordinate of the point to remove
     */
    public void remove(int x, int y) {
        if (!Point.inWorld(x, y)) {
            System.out.println("Point rejected: (" + x + ", " + y + ")");
            return;
        }
        Point p = tree.find(x, y);
        if (p == null) {
            System.out.println("Point not found: (" + x + ", " + y + ")");
            return;
        }
        tree.remove(p);
        list.removeByValue(p);
        System.out.println("Point removed: " + p.fullString());
    }


    /**
     * Reports the points inside the query rectangle.
     *
     * @param x
     *            the x-coordinate of the region's upper-left corner
     * @param y
     *            the y-coordinate of the region's upper-left corner
     * @param w
     *            the width of the region
     * @param h
     *            the height of the region
     */
    public void regionsearch(int x, int y, int w, int h) {
        String rect = "(" + x + ", " + y + ", " + w + ", " + h + ")";
        if (w <= 0 || h <= 0) {
            System.out.println("Rectangle rejected: " + rect);
            return;
        }
        System.out.println("Points intersecting region " + rect + ":");
        int visited = tree.regionSearch(x, y, w, h);
        System.out.println(visited + " quadtree nodes visited");
    }


    /**
     * Reports every location that holds more than one point.
     */
    public void duplicates() {
        System.out.println("Duplicate points:");
        tree.duplicates();
    }


    /**
     * Reports every point with the given name.
     *
     * @param name
     *            the name to search for
     */
    public void search(String name) {
        ArrayList<KVPair<String, Point>> results = list.search(name);
        if (results.isEmpty()) {
            System.out.println("Point not found: " + name);
            return;
        }
        for (KVPair<String, Point> pair : results) {
            System.out.println("Found " + pair.value().fullString());
        }
    }


    /**
     * Prints a dump of the SkipList followed by a dump of the QuadTree.
     */
    public void dump() {
        list.dump();
        tree.dump();
    }
}
