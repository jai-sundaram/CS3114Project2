/**
 * A named point in the 1024 by 1024 world. Point objects are shared
 * between the SkipList (which organizes them by name) and the QuadTree
 * (which organizes them by position). Equality is intentionally left as
 * object identity so that a specific point can be located and removed
 * from either structure even when other points share its name or
 * location.
 *
 * @author Jai Sundaram
 *
 * @version 2026-10-11
 */
public class Point {

    /** The width and height of the world box. */
    public static final int WORLD_SIZE = 1024;

    // the name of the point
    private String name;
    // the x-coordinate of the point
    private int x;
    // the y-coordinate of the point
    private int y;

    /**
     * Creates a new point.
     *
     * @param name
     *            the name of the point
     * @param x
     *            the x-coordinate of the point
     * @param y
     *            the y-coordinate of the point
     */
    public Point(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }


    /**
     * Getter for the name.
     *
     * @return the name of the point
     */
    public String getName() {
        return name;
    }


    /**
     * Getter for the x-coordinate.
     *
     * @return the x-coordinate of the point
     */
    public int getX() {
        return x;
    }


    /**
     * Getter for the y-coordinate.
     *
     * @return the y-coordinate of the point
     */
    public int getY() {
        return y;
    }


    /**
     * Checks whether this point has the same location as another point.
     *
     * @param other
     *            the point to compare against
     * @return true if both points have the same (x, y) location
     */
    public boolean sameLocation(Point other) {
        return x == other.x && y == other.y;
    }


    /**
     * Checks whether a location lies inside the world box. Coordinates
     * must be non-negative and strictly less than the world size.
     *
     * @param x
     *            the x-coordinate to check
     * @param y
     *            the y-coordinate to check
     * @return true if (x, y) lies inside the world box
     */
    public static boolean inWorld(int x, int y) {
        return x >= 0 && y >= 0 && x < WORLD_SIZE && y < WORLD_SIZE;
    }


    /**
     * Returns the full description of the point, including its name, in
     * the form "(name, x, y)".
     *
     * @return the name and coordinates of the point in parentheses
     */
    public String fullString() {
        return "(" + name + ", " + toString() + ")";
    }


    /**
     * Returns the coordinates of the point in the form "x, y". The name is
     * left out because the SkipList's KVPair already prints it as the key.
     *
     * @return the coordinates of the point
     */
    @Override
    public String toString() {
        return x + ", " + y;
    }
}
