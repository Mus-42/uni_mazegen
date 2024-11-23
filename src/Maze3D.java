package knu.mus.maze;

import java.util.Arrays;

/** 3 Dimensional maze */
public class Maze3D extends Maze {
    int sizeX;
    int sizeY;
    int sizeZ;

    /** constucts new maze
     * @param sizeX
     * size x
     * @param sizeY
     * size y
     * @param sizeZ
     * size z
     * */
    Maze3D(int sizeX, int sizeY, int sizeZ) {
        super(sizeX * sizeY * sizeZ);

        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }

    /** rebuild display from maze 
     * @param display 
     * display to rebuild 
     * */
    public void rebuildDisplay(MazeDisplay3D display) {
        //assert this.sizeX * this.sizeY * this.sizeZ == display
        display.setFinishFlagPosition(this.rootIndex);
        display.setParents(this.parents);
    }

    /** rebuilds 2d display for layer slice 
     * @param layer 
     * selected layer 
     * @param display 
     * display to rebuild 
     * */
    public void rebuildDisplay2d(int layer, MazeDisplay2D display) {
        display.reset();
        for (int x = 0; x < this.sizeX; x++) {
            for (int y = 0; y < this.sizeY; y++) {
                for (int z = 0; z < this.sizeZ; z++) {
                    int parent = this.parents[this.toIndex(x, y, z)];
                    if (parent == -1) continue;

                    int px = parent % this.sizeX;
                    int pyz = parent / this.sizeX;
                    int py = pyz % this.sizeY;
                    int pz = pyz / this.sizeY;

                    if (y == layer || py == layer) {
                        int p1 = pz * this.sizeX + px;
                        int p2 = z  * this.sizeX + x;
                        if (py == layer && y == layer) {
                            display.addWay(p1, p2);
                        } else {
                            int p = py == layer ? p1 : p2;
                            if (py >= layer && y >= layer) {
                                display.addWayUp(p);
                            } else {
                                display.addWayDown(p);
                            }
                        }
                    }
                }
            }
        }

        int fx = this.rootIndex % this.sizeX;
        int fyz = this.rootIndex / this.sizeX;
        int fy = fyz % this.sizeY;
        int fz = fyz / this.sizeY;

        if (fy == layer) {
            display.setFinishFlagPosition(fz * this.sizeX + fx);
        }
    }

    /** get nodes neighbours ar given index
     * @param i
     * node index
     * */
    @Override
    public int[] neighbourAt(int i) {
        int x = i % this.sizeX;
        int yz = i / this.sizeX;
        int y = yz % this.sizeY;
        int z = yz / this.sizeY;
        return neighbourAtXYZ(x, y, z);
    }

    /** convers position to index
     * @param x
     * x
     * @param y
     * y
     * @param z
     * z
     * @return 
     * index
     * */
    public int toIndex(int x, int y, int z) {
        return (z * this.sizeY + y) * this.sizeX + x;
    }

    /** get all neighbour nodes at given position
     * @param x
     * x
     * @param y
     * y
     * @param z
     * z
     * @return
     * neighbour nodes
     * */
    public int[] neighbourAtXYZ(int x, int y, int z) {
        int[] neighbours = new int[6];
        int last = 0;
        
        if (x > 0) {
            neighbours[last++] = this.toIndex(x-1, y, z);
        }
        if (x+1 < this.sizeX) {
            neighbours[last++] = this.toIndex(x+1, y, z);
        }
        if (y > 0) {
            neighbours[last++] = this.toIndex(x, y-1, z);
        }
        if (y+1 < this.sizeY) {
            neighbours[last++] = this.toIndex(x, y+1, z);
        }
        if (z > 0) {
            neighbours[last++] = this.toIndex(x, y, z-1);
        }
        if (z+1 < this.sizeZ) {
            neighbours[last++] = this.toIndex(x, y, z+1);
        }

        return Arrays.copyOfRange(neighbours, 0, last);
    }
}
