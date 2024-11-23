package knu.mus.maze;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Deterministic, dimension/shape agnostic maze generator
 */
public class MazeGenerator {
    List<Head> heads;
    int[] childCount;
    Random random;
    int size;

    /** creates new maze
     * @param random
     * Random number generator
     */
    public MazeGenerator(Random random) {
        this.random = random;
        this.heads = new ArrayList<>();
    }

    /** re generages maze 
     * @param maze
     * maze
     * */
    public void generate(Maze maze) {
        this.size = maze.parents.length;
        this.childCount = new int[this.size];

        Arrays.fill(this.childCount, 0);
        Arrays.fill(maze.parents, -1);

        int root = this.random.nextIntRange(0, this.size);
        maze.rootIndex = root;

        for (int n: maze.neighbourAt(root)) {
            this.heads.add(new Head(root, n));
        }

        while (!this.heads.isEmpty()) {
            int i = this.randomHeadIndex();
            Head head = this.heads.get(i);
            this.heads.remove(i);

            maze.parents[head.targetCell] = head.parentCell;
            this.childCount[head.parentCell] += 1;
                
            for (int n: maze.neighbourAt(head.targetCell)) {
                if (n != root && maze.parents[n] == -1) {
                    this.heads.add(new Head(head.targetCell, n));
                }
            }
        
            this.heads.removeIf(h -> maze.parents[h.targetCell] != -1);
        }
    }

    private int randomHeadIndex() {
        int totalWeight = 0;
        for (Head head : this.heads) {
            totalWeight += this.headWeight(head);
        }
        int randomWeight = this.random.nextIntRange(0, totalWeight);
        int i = 0;
        int currentWeight = 0;
        for (Head head : this.heads) {
            currentWeight += this.headWeight(head);
            if (currentWeight >= randomWeight) {
                return i;
            }
            i += 1;
        }
        assert false; // should be unreachable
        return -1;
    }

    private int headWeight(Head head) {
        int childs = this.childCount[head.parentCell];
        if (childs == 0) return 10000;
        if (childs == 1) return 10;
        if (childs == 2) return 5;
        return 1;
    }
}

class Head {
    int parentCell;
    int targetCell;

    Head(int parent, int target) {
        this.parentCell = parent;
        this.targetCell = target;
    }
}
