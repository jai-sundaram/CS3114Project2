/**
 * A PR QuadTree variant that organizes points by location over the
 * 1024 by 1024 world. A leaf may hold up to three points, or any number
 * of points that all share one location; any other node with more than
 * three points is split into four quadrants. The tree only keeps a
 * pointer to the root; the position and size of each node are worked out
 * as the recursive calls descend.
 *
 * @author Jai Sundaram, Yaswanth Uppalapati
 *
 * @version 2026-10-11
 */
public class QuadTree {

    // the root of the tree
    private QuadNode root;
    // the number of points in the tree
    private int size;

    /**
     * Creates an empty QuadTree.
     */
    public QuadTree() {
        root = EmptyNode.getInstance();
        size = 0;
    }


    /**
     * Gets the root node.
     *
     * @return the root of the tree
     */
    public QuadNode getRoot() {
        return root;
    }


    /**
     * Gets the number of points in the tree.
     *
     * @return the number of points stored
     */
    public int size() {
        return size;
    }


    /**
     * Inserts a point. The caller is responsible for making sure the point
     * lies inside the world box.
     *
     * @param p
     *            the point to insert
     */
    public void insert(Point p) {
        root = root.insert(p, 0, 0, Point.WORLD_SIZE);
        size++;
    }


    /**
     * Removes a specific point object from the tree. The caller is
     * responsible for making sure the point is in the tree (the Database
     * only removes points it has found in the SkipList or in this tree).
     *
     * @param p
     *            the point object to remove
     */
    public void remove(Point p) {
        root = root.remove(p, 0, 0, Point.WORLD_SIZE);
        size--;
    }


    /**
     * Finds a point at the given location.
     *
     * @param x
     *            the x-coordinate to look for
     * @param y
     *            the y-coordinate to look for
     * @return a point at (x, y), or null if there is none
     */
    public Point find(int x, int y) {
        if (!Point.inWorld(x, y)) {
            return null;
        }
        return root.find(x, y, 0, 0, Point.WORLD_SIZE);
    }


    /**
     * Reports points in a query rectangle and returns the node visit count.
     *
     * @param x
     *            the x-coordinate of the rectangle's upper-left corner
     * @param y
     *            the y-coordinate of the rectangle's upper-left corner
     * @param width
     *            the positive width of the query rectangle
     * @param height
     *            the positive height of the query rectangle
     * @return the number of quadtree nodes visited during the search
     */
    public int regionSearch(int x, int y, int width, int height) {
        return root.regionSearch(x, y, width, height, 0, 0,
            Point.WORLD_SIZE);
    }


    /**
     * Reports all coordinates occupied by more than one point.
     */
    public void duplicates() {
        root.duplicates();
    }


    /**
     * Prints every node of the tree in preorder, followed by the number of
     * nodes printed.
     */
    public void dump() {
        System.out.println("QuadTree dump:");
        int count = root.dump(0, 0, Point.WORLD_SIZE, 0);
        System.out.println(count + " quadtree nodes printed");
    }


    /**
     * Builds the indentation for a node at the given level: two spaces
     * per level.
     *
     * @param level
     *            the depth of the node (the root is 0)
     * @return a string of 2 * level spaces
     */
    public static String indent(int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("  ");
        }
        return sb.toString();
    }
}
