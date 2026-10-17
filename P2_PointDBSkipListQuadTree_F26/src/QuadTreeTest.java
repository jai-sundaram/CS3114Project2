import java.util.ArrayList;
import java.util.List;
import student.TestCase;

/**
 * Tests the QuadTree and its node classes.
 *
 * @author Jai Sundaram, Yaswanth Uppalapati
 *
 * @version 2026-10-11
 */
public class QuadTreeTest extends TestCase {

    private QuadTree tree;

    /**
     * Creates an empty tree before each test.
     */
    public void setUp() {
        tree = new QuadTree();
        systemOut().clearHistory();
    }


    /**
     * Tests the dump of an empty tree.
     */
    public void testEmpty() {
        assertSame(EmptyNode.getInstance(), tree.getRoot());
        assertEquals(0, tree.size());
        assertNull(tree.find(1, 1));
        tree.dump();
        assertFuzzyEquals("QuadTree dump:\nNode at 0, 0, 1024: Empty\n"
            + "1 quadtree nodes printed\n", systemOut().getHistory());
    }


    /**
     * Tests the empty flyweight directly.
     */
    public void testEmptyNode() {
        EmptyNode e = EmptyNode.getInstance();
        Point p = new Point("a", 1, 1);
        assertSame(e, e.remove(p, 0, 0, 1024));
        assertNull(e.find(1, 1, 0, 0, 1024));
        List<Point> list = new ArrayList<Point>();
        e.collectPoints(list);
        assertEquals(0, list.size());
        assertTrue(e.insert(p, 0, 0, 1024) instanceof LeafNode);
    }


    /**
     * Tests that a leaf holds up to three points and splits on the fourth,
     * using the same inserts as SyntaxTest2.
     */
    public void testSplitCascade() {
        tree.insert(new Point("p_p", 1, 20));
        tree.insert(new Point("poi", 10, 30));
        tree.insert(new Point("p_42", 1, 20));
        assertTrue(tree.getRoot() instanceof LeafNode);
        tree.insert(new Point("far", 200, 200));
        assertTrue(tree.getRoot() instanceof InternalNode);
        assertEquals(4, tree.size());
        tree.dump();
        assertFuzzyEquals("QuadTree dump:\n"
            + "Node at 0, 0, 1024: Internal\n"
            + "  Node at 0, 0, 512: Internal\n"
            + "    Node at 0, 0, 256: Internal\n"
            + "      Node at 0, 0, 128:\n"
            + "      (p_p, 1, 20)\n"
            + "      (poi, 10, 30)\n"
            + "      (p_42, 1, 20)\n"
            + "      Node at 128, 0, 128: Empty\n"
            + "      Node at 0, 128, 128: Empty\n"
            + "      Node at 128, 128, 128:\n"
            + "      (far, 200, 200)\n"
            + "    Node at 256, 0, 256: Empty\n"
            + "    Node at 0, 256, 256: Empty\n"
            + "    Node at 256, 256, 256: Empty\n"
            + "  Node at 512, 0, 512: Empty\n"
            + "  Node at 0, 512, 512: Empty\n"
            + "  Node at 512, 512, 512: Empty\n"
            + "13 quadtree nodes printed\n", systemOut().getHistory());
    }


    /**
     * Tests that removals merge a cascade of internal nodes back into a
     * single leaf.
     */
    public void testMergeCascade() {
        Point pp = new Point("p_p", 1, 20);
        tree.insert(pp);
        tree.insert(new Point("poi", 10, 30));
        tree.insert(new Point("p_42", 1, 20));
        tree.insert(new Point("far", 200, 200));
        tree.remove(pp);
        tree.remove(tree.find(10, 30));
        assertEquals(2, tree.size());
        assertTrue(tree.getRoot() instanceof LeafNode);
        tree.dump();
        assertFuzzyEquals("QuadTree dump:\nNode at 0, 0, 1024:\n"
            + "(far, 200, 200)\n(p_42, 1, 20)\n1 quadtree nodes printed\n",
            systemOut().getHistory());
    }


    /**
     * Tests that points in each of the four quadrants are placed and
     * found correctly.
     */
    public void testQuadrants() {
        Point nw = new Point("nw", 0, 0);
        Point ne = new Point("ne", 1023, 0);
        Point sw = new Point("sw", 0, 1023);
        Point se = new Point("se", 1023, 1023);
        tree.insert(nw);
        tree.insert(ne);
        tree.insert(sw);
        tree.insert(se);
        InternalNode root = (InternalNode)tree.getRoot();
        assertTrue(root.getChild(InternalNode.NW) instanceof LeafNode);
        assertTrue(root.getChild(InternalNode.NE) instanceof LeafNode);
        assertTrue(root.getChild(InternalNode.SW) instanceof LeafNode);
        assertTrue(root.getChild(InternalNode.SE) instanceof LeafNode);
        assertSame(nw, tree.find(0, 0));
        assertSame(ne, tree.find(1023, 0));
        assertSame(sw, tree.find(0, 1023));
        assertSame(se, tree.find(1023, 1023));
        assertNull(tree.find(512, 512));
        assertNull(tree.find(-1, 5));
        assertNull(tree.find(1024, 5));

        tree.remove(se);
        assertTrue(tree.getRoot() instanceof LeafNode);
        List<Point> list = new ArrayList<Point>();
        tree.getRoot().collectPoints(list);
        assertEquals(3, list.size());
    }


    /**
     * Tests that many points at one location stay in a single leaf, and
     * that a different point forces a split.
     */
    public void testSameLocation() {
        for (int i = 0; i < 6; i++) {
            tree.insert(new Point("d" + i, 700, 700));
        }
        assertTrue(tree.getRoot() instanceof LeafNode);
        assertTrue(((LeafNode)tree.getRoot()).allSameLocation());
        Point other = new Point("o", 5, 5);
        tree.insert(other);
        assertTrue(tree.getRoot() instanceof InternalNode);
        tree.remove(other);
        assertTrue(tree.getRoot() instanceof LeafNode);
        assertEquals(6, ((LeafNode)tree.getRoot()).getPoints().size());
    }


    /**
     * Tests that an internal node does not merge while it still has too
     * many points, and that removing a missing point changes nothing.
     */
    public void testNoMerge() {
        Point a = new Point("a", 1, 1);
        tree.insert(a);
        tree.insert(new Point("b", 600, 1));
        tree.insert(new Point("c", 1, 600));
        tree.insert(new Point("d", 600, 600));
        tree.insert(new Point("e", 2, 2));
        tree.remove(a);
        assertTrue(tree.getRoot() instanceof InternalNode);

        LeafNode leaf = new LeafNode(new Point("x", 3, 3));
        assertSame(leaf, leaf.remove(a, 0, 0, 1024));
        assertNull(leaf.find(4, 3, 0, 0, 1024));
        assertNull(leaf.find(3, 4, 0, 0, 1024));
    }


    /**
     * Tests that removing every point empties the tree.
     */
    public void testRemoveAll() {
        Point a = new Point("a", 1, 1);
        Point b = new Point("b", 900, 900);
        tree.insert(a);
        tree.insert(b);
        tree.remove(a);
        tree.remove(b);
        assertSame(EmptyNode.getInstance(), tree.getRoot());
    }


    /**
     * Tests that an internal node whose points have all been removed
     * becomes the empty flyweight.
     */
    public void testInternalBecomesEmpty() {
        InternalNode node = new InternalNode();
        Point a = new Point("a", 1, 1);
        node.insert(a, 0, 0, 1024);
        assertSame(EmptyNode.getInstance(), node.remove(a, 0, 0, 1024));
    }


    /**
     * Tests the indentation helper.
     */
    public void testIndent() {
        assertEquals("", QuadTree.indent(0));
        assertEquals("    ", QuadTree.indent(2));
    }
}
