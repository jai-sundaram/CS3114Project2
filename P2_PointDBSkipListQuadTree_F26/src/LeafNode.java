import java.util.ArrayList;
import java.util.List;

/**
 * A non-empty leaf node of the PR QuadTree. A leaf holds 1 to 3 points, or
 * any number of points as long as they all share the same location. New
 * points are added to the front of the leaf's list.
 *
 * @author Jai Sundaram, Yaswanth Uppalapati
 *
 * @version 2026-10-11
 */
public class LeafNode implements QuadNode {

    /** The most points a leaf may hold when they are not all together. */
    public static final int CAPACITY = 3;

    // the points stored in this leaf
    private List<Point> points;

    /**
     * Creates a leaf holding a single point.
     *
     * @param p
     *            the first point of the leaf
     */
    public LeafNode(Point p) {
        points = new ArrayList<Point>();
        points.add(p);
    }


    /**
     * Gets the points stored in this leaf, front of the list first.
     *
     * @return the list of points in this leaf
     */
    public List<Point> getPoints() {
        return points;
    }


    /**
     * Adds a point to the front of this leaf's list without checking the
     * decomposition rule.
     *
     * @param p
     *            the point to add
     */
    public void add(Point p) {
        points.add(0, p);
    }


    /**
     * Checks whether every point in this leaf has the same location.
     *
     * @return true if all points share one (x, y) location
     */
    public boolean allSameLocation() {
        Point first = points.get(0);
        for (Point p : points) {
            if (!p.sameLocation(first)) {
                return false;
            }
        }
        return true;
    }


    /**
     * Adds the point to this leaf. If the leaf then breaks the
     * decomposition rule, it is split into an internal node and all of
     * its points are inserted again into the new internal node.
     *
     * @param p
     *            the point to insert
     * @param x
     *            the x-coordinate of this node's upper-left corner
     * @param y
     *            the y-coordinate of this node's upper-left corner
     * @param size
     *            the side length of this node's square
     * @return this leaf, or the internal node that replaces it
     */
    @Override
    public QuadNode insert(Point p, int x, int y, int size) {
        add(p);
        if (points.size() <= CAPACITY || allSameLocation()) {
            return this;
        }
        QuadNode node = new InternalNode();
        for (Point q : points) {
            node = node.insert(q, x, y, size);
        }
        return node;
    }


    /**
     * Removes the given point object from this leaf, if it is here.
     *
     * @param p
     *            the point object to remove
     * @param x
     *            the x-coordinate of this node's upper-left corner
     * @param y
     *            the y-coordinate of this node's upper-left corner
     * @param size
     *            the side length of this node's square
     * @return this leaf, or the empty flyweight if the leaf became empty
     */
    @Override
    public QuadNode remove(Point p, int x, int y, int size) {
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i) == p) {
                points.remove(i);
                break;
            }
        }
        if (points.isEmpty()) {
            return EmptyNode.getInstance();
        }
        return this;
    }


    /**
     * Finds the first point in this leaf at the given location.
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
     * @return a point at (px, py), or null if there is none
     */
    @Override
    public Point find(int px, int py, int x, int y, int size) {
        for (Point p : points) {
            if (p.getX() == px && p.getY() == py) {
                return p;
            }
        }
        return null;
    }


    /**
     * Adds this leaf's points to the list.
     *
     * @param list
     *            the list to add the points to
     */
    @Override
    public void collectPoints(List<Point> list) {
        list.addAll(points);
    }


    /**
     * Prints this leaf's header line followed by one line per point.
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
        String indent = QuadTree.indent(level);
        System.out.println(indent + "Node at " + x + ", " + y + ", " + size
            + ":");
        for (Point p : points) {
            System.out.println(indent + p.fullString());
        }
        return 1;
    }
}
