import java.util.List;

/**
 * The empty leaf node of the PR QuadTree. Because an empty leaf holds no
 * state, a single instance (a flyweight) is shared by every empty position
 * in the tree. Use getInstance() to obtain it.
 *
 * @author Jai Sundaram, Yaswanth Uppalapati
 *
 * @version 2026-10-11
 */
public class EmptyNode implements QuadNode {

    // the single shared empty node
    private static final EmptyNode FLYWEIGHT = new EmptyNode();

    /**
     * Private so that the flyweight is the only instance.
     */
    private EmptyNode() {
        // nothing to initialize
    }


    /**
     * Gets the shared empty node.
     *
     * @return the flyweight empty node
     */
    public static EmptyNode getInstance() {
        return FLYWEIGHT;
    }


    /**
     * Inserting into an empty node produces a leaf holding just the point.
     *
     * @param p
     *            the point to insert
     * @param x
     *            the x-coordinate of this node's upper-left corner
     * @param y
     *            the y-coordinate of this node's upper-left corner
     * @param size
     *            the side length of this node's square
     * @return a new leaf node containing p
     */
    @Override
    public QuadNode insert(Point p, int x, int y, int size) {
        return new LeafNode(p);
    }


    /**
     * There is nothing to remove from an empty node.
     *
     * @param p
     *            the point object to remove
     * @param x
     *            the x-coordinate of this node's upper-left corner
     * @param y
     *            the y-coordinate of this node's upper-left corner
     * @param size
     *            the side length of this node's square
     * @return this node, unchanged
     */
    @Override
    public QuadNode remove(Point p, int x, int y, int size) {
        return this;
    }


    /**
     * An empty node never contains a point.
     *
     * @param px
     *            the x-coordinate to look for
     * @param py
     *            the y-coordinate to look for
     * @param x
     *            the x-coordinate of this node's upper-left corner
     * @param y
     *            the y-coordinate of this node's upper-left corner
     * @param size
     *            the side length of this node's square
     * @return null
     */
    @Override
    public Point find(int px, int py, int x, int y, int size) {
        return null;
    }


    /**
     * Visiting an empty node finds no points but still counts the node.
     *
     * @return one visited node
     */
    @Override
    public int regionSearch(int queryX, int queryY, int queryW, int queryH,
        int x, int y, int size) {
        return 1;
    }


    /**
     * An empty node has no duplicate coordinates.
     */
    @Override
    public void duplicates() {
        // no points here
    }


    /**
     * An empty node has no points to add.
     *
     * @param list
     *            the list to add the points to
     */
    @Override
    public void collectPoints(List<Point> list) {
        // no points here
    }


    /**
     * Prints this node as empty.
     *
     * @param x
     *            the x-coordinate of this node's upper-left corner
     * @param y
     *            the y-coordinate of this node's upper-left corner
     * @param size
     *            the side length of this node's square
     * @param level
     *            the depth of this node in the tree (the root is 0)
     * @return 1, the number of nodes printed
     */
    @Override
    public int dump(int x, int y, int size, int level) {
        System.out.println(QuadTree.indent(level) + "Node at " + x + ", "
            + y + ", " + size + ": Empty");
        return 1;
    }
}
