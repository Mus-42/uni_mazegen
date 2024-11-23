package knu.mus.maze;

import java.util.Arrays;
import static com.raylib.Raylib.*;
import static com.raylib.Jaylib.RED;
import static com.raylib.Jaylib.GREEN;

/** 2-dimensional rectangular maze */
public class Maze2D extends Maze {
    int width;
    int height;

    /** Construct new maze
     * @param width
     * @param height 
     * */
    Maze2D(int width, int height) {
        super(width * height);

        this.width = width;
        this.height = height;
    }

    /** Rebuilds <code>MazeDisplay2d</code> cache from maze tree
     * @param display 
     * display
     * */
    public void rebuildDisplay(MazeDisplay2D display) {
        display.reset();
        display.setFinishFlagPosition(this.rootIndex);
        for (int i = 0; i < this.parents.length; i++) {
            if (this.parents[i] == -1) {
                continue;
            }
            display.addWay(i, this.parents[i]);
        }
    }

    /** get nodes neighbours ar given index
     * @param i
     * node index
     * */
    @Override
    public int[] neighbourAt(int i) {
        int w = this.width;
        int[] neighbours = new int[4];
        int last = 0;
        
        int x = i % w;
        int y = i / w;

        if (x > 0) {
            neighbours[last++] = i - 1;
        }
        if (x+1 < w) {
            neighbours[last++] = i + 1;
        }
        if (y > 0) {
            neighbours[last++] = i - w;
        }
        if (y+1 < this.height) {
            neighbours[last++] = i + w;
        }

        return Arrays.copyOfRange(neighbours, 0, last);
    }
}
