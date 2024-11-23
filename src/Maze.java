package knu.mus.maze;

import java.util.Arrays;

/** Transparent wrapper around maze-tree */
public abstract class Maze {
    /** maze tree root index */
    public int rootIndex;
    /** parent nodes for each index */
    public int[] parents;

    /** create new maze tree
     * @param size
     * nodes count
     * */
    Maze(int size) {
        this.rootIndex = -1;
        this.parents = new int[size];
        Arrays.fill(this.parents, -1);

    }

    /** build path to <code>rootIndex</code>
     * @param from
     * start node
     * @return
     * path nodes
     * */
    public int[] getPathToFinish(int from) {
        assert 0 <= from && from < this.parents.length;
        
        int pathLen = 1;
        int current = from;
        while (current != this.rootIndex) {
            current = this.parents[current];
            pathLen += 1;
        }

        int[] path = new int[pathLen];
        path[0] = from;
        current = from;

        int pathPos = 1;
        while (current != this.rootIndex) {
            current = this.parents[current];
            path[pathPos] = current;
            pathPos += 1;
        }

        return path;
    }

    /** getter for neighbouts at given node index 
     * @param i
     * node
     * @return
     * neighbour nodes 
     * */
    public abstract int[] neighbourAt(int i);
}
