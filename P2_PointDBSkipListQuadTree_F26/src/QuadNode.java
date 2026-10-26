import java.util.List;

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
 * The interface shared by every node of the PR QuadTree. Nodes do not
 * store their own position or size; instead the upper-left corner and the
 * side length of the node's square are passed in to every recursive call.
 * This keeps the nodes small and lets a single stateless flyweight stand
 * in for every empty leaf. Operations that can change the shape of the
 * tree return the node that should take this node's place in its parent.
 *
 * @author Jai Sundaram, Yaswanth Uppalapati
 *
 * @version 2026-10-25
 */
public interface QuadNode {

    /**
     * Inserts a point into the subtree rooted at this node.
     *
     * @param p
     *            the point to insert
     * @param x
     *            the x-coordinate of this node's upper-left corner
     * @param y
     *            the y-coordinate of this node's upper-left corner
     * @param size
     *            the side length of this node's square
     * @return the node that should replace this node in its parent
     */
    QuadNode insert(Point p, int x, int y, int size);


    /**
     * Removes a specific point object from the subtree rooted at this
     * node, merging nodes as needed to satisfy the decomposition rule.
     *
     * @param p
     *            the point object to remove
     * @param x
     *            the x-coordinate of this node's upper-left corner
     * @param y
     *            the y-coordinate of this node's upper-left corner
     * @param size
     *            the side length of this node's square
     * @return the node that should replace this node in its parent
     */
    QuadNode remove(Point p, int x, int y, int size);


    /**
     * Finds a point at the given location in the subtree rooted at this
     * node.
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
    Point find(int px, int py, int x, int y, int size);


    /**
     * Reports the points in a query rectangle and counts the nodes visited.
     * The query rectangle uses half-open bounds: its right and bottom edges
     * are not part of the region.
     *
     * @param queryX the query rectangle's left edge
     * @param queryY the query rectangle's top edge
     * @param queryW the query rectangle's width
     * @param queryH the query rectangle's height
     * @param x this node's left edge
     * @param y this node's top edge
     * @param size this node's side length
     * @return the number of quadtree nodes visited
     */
    int regionSearch(int queryX, int queryY, int queryW, int queryH,
        int x, int y, int size);


    /**
     * Reports each duplicate coordinate in this subtree exactly once.
     */
    void duplicates();


    /**
     * Adds every point stored in the subtree rooted at this node to the
     * given list, in preorder.
     *
     * @param list
     *            the list to add the points to
     */
    void collectPoints(List<Point> list);


    /**
     * Prints the subtree rooted at this node in preorder, indenting each
     * line by two spaces per level.
     *
     * @param x
     *            the x-coordinate of this node's upper-left corner
     * @param y
     *            the y-coordinate of this node's upper-left corner
     * @param size
     *            the side length of this node's square
     * @param level
     *            the depth of this node in the tree (the root is 0)
     * @return the number of nodes printed
     */
    int dump(int x, int y, int size, int level);
}
