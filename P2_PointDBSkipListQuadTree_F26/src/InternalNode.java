import java.util.ArrayList;
import java.util.List;

/**
 * An internal node of the PR QuadTree. It stores no points itself, only
 * its four children, in the order NW, NE, SW, SE. Each child covers one
 * equal-sized quadrant of this node's square.
 *
 * @author Jai Sundaram, Yaswanth Uppalapati
 *
 * @version 2026-10-11
 */
public class InternalNode implements QuadNode {

    /** Index of the northwest child. */
    public static final int NW = 0;
    /** Index of the northeast child. */
    public static final int NE = 1;
    /** Index of the southwest child. */
    public static final int SW = 2;
    /** Index of the southeast child. */
    public static final int SE = 3;

    // the four children, indexed by NW, NE, SW and SE
    private QuadNode[] children;

    /**
     * Creates an internal node whose four children are all empty.
     */
    public InternalNode() {
        children = new QuadNode[4];
        for (int i = 0; i < children.length; i++) {
            children[i] = EmptyNode.getInstance();
        }
    }


    /**
     * Gets one of the children of this node.
     *
     * @param quadrant
     *            NW, NE, SW or SE
     * @return the child in that quadrant
     */
    public QuadNode getChild(int quadrant) {
        return children[quadrant];
    }


    /**
     * Works out which quadrant of this node a location falls in.
     *
     * @param px
     *            the x-coordinate of the location
     * @param py
     *            the y-coordinate of the location
     * @param x
     *            the x-coordinate of this node's upper-left corner
     * @param y
     *            the y-coordinate of this node's upper-left corner
     * @param size
     *            the side length of this node's square
     * @return NW, NE, SW or SE
     */
    private int quadrant(int px, int py, int x, int y, int size) {
        int half = size / 2;
        int quad = (px < x + half) ? NW : NE;
        if (py >= y + half) {
            quad += 2; // move from the north row to the south row
        }
        return quad;
    }


    /**
     * Gets the x-coordinate of a child's upper-left corner.
     *
     * @param quad
     *            the child's quadrant
     * @param x
     *            the x-coordinate of this node's upper-left corner
     * @param size
     *            the side length of this node's square
     * @return the child's x-coordinate
     */
    private int childX(int quad, int x, int size) {
        return (quad == NE || quad == SE) ? x + size / 2 : x;
    }


    /**
     * Gets the y-coordinate of a child's upper-left corner.
     *
     * @param quad
     *            the child's quadrant
     * @param y
     *            the y-coordinate of this node's upper-left corner
     * @param size
     *            the side length of this node's square
     * @return the child's y-coordinate
     */
    private int childY(int quad, int y, int size) {
        return (quad == SW || quad == SE) ? y + size / 2 : y;
    }


    /**
     * Inserts the point into the child whose quadrant contains it.
     *
     * @param p
     *            the point to insert
     * @param x
     *            the x-coordinate of this node's upper-left corner
     * @param y
     *            the y-coordinate of this node's upper-left corner
     * @param size
     *            the side length of this node's square
     * @return this node
     */
    @Override
    public QuadNode insert(Point p, int x, int y, int size) {
        int quad = quadrant(p.getX(), p.getY(), x, y, size);
        children[quad] = children[quad].insert(p, childX(quad, x, size),
            childY(quad, y, size), size / 2);
        return this;
    }


    /**
     * Removes the point from the child whose quadrant contains it, then
     * merges this node into a single leaf if the decomposition rule says
     * it should no longer be internal.
     *
     * @param p
     *            the point object to remove
     * @param x
     *            the x-coordinate of this node's upper-left corner
     * @param y
     *            the y-coordinate of this node's upper-left corner
     * @param size
     *            the side length of this node's square
     * @return this node, or the leaf that replaces it after a merge
     */
    @Override
    public QuadNode remove(Point p, int x, int y, int size) {
        int quad = quadrant(p.getX(), p.getY(), x, y, size);
        children[quad] = children[quad].remove(p, childX(quad, x, size),
            childY(quad, y, size), size / 2);
        return merge();
    }


    /**
     * Collapses this node into a leaf if all of its children are leaves
     * and their points together hold no more than the leaf capacity, or
     * all share the same location.
     *
     * @return this node, or the leaf (or empty node) that replaces it
     */
    private QuadNode merge() {
        for (QuadNode child : children) {
            if (child instanceof InternalNode) {
                return this;
            }
        }
        List<Point> all = new ArrayList<Point>();
        collectPoints(all);
        if (all.isEmpty()) {
            return EmptyNode.getInstance();
        }
        LeafNode leaf = new LeafNode(all.get(0));
        for (int i = 1; i < all.size(); i++) {
            leaf.add(all.get(i));
        }
        if (all.size() <= LeafNode.CAPACITY || leaf.allSameLocation()) {
            return leaf;
        }
        return this;
    }


    /**
     * Looks for a point at the location in the child whose quadrant
     * contains it.
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
        int quad = quadrant(px, py, x, y, size);
        return children[quad].find(px, py, childX(quad, x, size),
            childY(quad, y, size), size / 2);
    }


    /**
     * Recursively searches only child squares that intersect the query.
     *
     * @return the number of nodes visited, including this node
     */
    @Override
    public int regionSearch(int queryX, int queryY, int queryW, int queryH,
        int x, int y, int size) {
        int count = 1;
        int childSize = size / 2;
        for (int quad = NW; quad <= SE; quad++) {
            int cx = childX(quad, x, size);
            int cy = childY(quad, y, size);
            if (intersects(queryX, queryY, queryW, queryH, cx, cy,
                childSize)) {
                count += children[quad].regionSearch(queryX, queryY, queryW,
                    queryH, cx, cy, childSize);
            }
        }
        return count;
    }


    /**
     * Tests two half-open rectangles for overlap. Long arithmetic prevents
     * overflow when a valid query rectangle extends far outside the world.
     */
    private boolean intersects(int queryX, int queryY, int queryW,
        int queryH, int nodeX, int nodeY, int nodeSize) {
        long queryRight = (long)queryX + queryW;
        long queryBottom = (long)queryY + queryH;
        long nodeRight = (long)nodeX + nodeSize;
        long nodeBottom = (long)nodeY + nodeSize;
        return queryX < nodeRight && queryRight > nodeX
            && queryY < nodeBottom && queryBottom > nodeY;
    }


    /**
     * Reports duplicates in quadrant order.
     */
    @Override
    public void duplicates() {
        for (QuadNode child : children) {
            child.duplicates();
        }
    }


    /**
     * Adds the points of all four children to the list, NW to SE.
     *
     * @param list
     *            the list to add the points to
     */
    @Override
    public void collectPoints(List<Point> list) {
        for (QuadNode child : children) {
            child.collectPoints(list);
        }
    }


    /**
     * Prints this node, then its children in NW, NE, SW, SE order.
     *
     * @param x
     *            the x-coordinate of this node's upper-left corner
     * @param y
     *            the y-coordinate of this node's upper-left corner
     * @param size
     *            the side length of this node's square
     * @param level
     *            the depth of this node in the tree (the root is 0)
     * @return the number of nodes printed in this subtree
     */
    @Override
    public int dump(int x, int y, int size, int level) {
        System.out.println(QuadTree.indent(level) + "Node at " + x + ", "
            + y + ", " + size + ": Internal");
        int count = 1;
        for (int quad = NW; quad <= SE; quad++) {
            count += children[quad].dump(childX(quad, x, size),
                childY(quad, y, size), size / 2, level + 1);
        }
        return count;
    }
}
