package knu.mus.maze;

import static com.raylib.Raylib.*;
import static com.raylib.Jaylib.WHITE;

import java.util.Arrays;
import java.util.ArrayList;

/** Utility for displaying 2D maze-like thing (2d maze or 3d maze slice) */
public class MazeDisplay2D {
    TileDisplay tileDisplay;

    int w;
    int h;

    boolean hasVerticalWall[];
    boolean hasHorizontalWall[];

    ArrayList<Integer> wayUp;
    ArrayList<Integer> wayDown;

    int finishFlagPosition;
    
    /** Creates new display 
     * @param w
     *  width
     * @param h
     *  height 
     * @param tileDisplay
     *  tile display
     * */
    public MazeDisplay2D(int w, int h, TileDisplay tileDisplay) {
        this.tileDisplay = tileDisplay;

        this.w = w;
        this.h = h;

        this.hasVerticalWall = new boolean[(w+1) * h];
        this.hasHorizontalWall = new boolean[w * (h+1)];

        this.wayUp = new ArrayList<>();
        this.wayDown = new ArrayList<>();
    }

    /** resets display to ready-to construct state. */
    public void reset() {
        Arrays.fill(this.hasVerticalWall, true);
        Arrays.fill(this.hasHorizontalWall, true);
        this.wayUp.clear();
        this.wayDown.clear();
        this.finishFlagPosition = -1;
    }

    /** sets finish flag position.
     * @param pos
     * flag position node index
     * means -1 no flag */
    public void setFinishFlagPosition(int pos) {
        this.finishFlagPosition = pos;
    }

    /** add path from 2 node indecies 
     * @param a
     * first node 
     * @param b
     * second node 
     * */
    public void addWay(int a, int b) {
        int w = this.w;
        int h = this.h;
        int ax = a % w;
        int ay = a / w;
        int bx = b % w;
        int by = b / w;
        if (ax == bx) {
            int l = Math.min(ay, by);
            int r = Math.max(ay, by);
            if (l + 1 != r) {
                throw new RuntimeException("unable to add way from a to b");
            }
            this.hasHorizontalWall[r * w + ax] = false;
            
        } else if (ay == by) {
            int l = Math.min(ax, bx);
            int r = Math.max(ax, bx);
            if (l + 1 != r) {
                throw new RuntimeException("unable to add way from a to b");
            }
            this.hasVerticalWall[ay * (w+1) + r] = false;
        } else {
            throw new RuntimeException("unable to add way from a to b");
        }
    }

    /** add path from node index somewhere up (used for 3d maze)
     * @param at 
     * node
     * */
    public void addWayUp(int at) {
        this.wayUp.add(at);
    }

    /** add path from node index somewhere down (used for 3d maze)
     * @param at
     * node
     * */
    public void addWayDown(int at) {
        this.wayDown.add(at);
    }

    /** display maze to screen. with offset 
     * @param offsetX
     * offset x in world-space
     * @param offsetY
     * offset y in world-space */
    public void display(float offsetX, float offsetY) {
        int w = this.w;
        int h = this.h;

        for (int i = 0; i < this.wayDown.size(); i++) {
            int x = this.wayDown.get(i) % w;
            int y = this.wayDown.get(i) / w;
            this.tileDisplay.drawLadderDown(0.5f + x + offsetX, 0.7f + y + offsetY);
        }

        for (int j = 0; j < w; j++) {
            if (this.hasHorizontalWall[j]) {
                this.tileDisplay.drawWallHorizontal(0.5f + j + offsetX, offsetY);
            }
        }

        for (int i = 0; i < h; i++) {
            for (int j = 0; j <= w; j++) {
                if (this.hasVerticalWall[i*(w+1) + j]) {
                    this.tileDisplay.drawWallVertical(j + offsetX, 0.5f + i + offsetY);
                }
            }

            for (int j = 0; j < w; j++) {
                if (this.hasHorizontalWall[(i+1)*w + j]) {
                    this.tileDisplay.drawWallHorizontal(0.5f + j + offsetX, 1 + i + offsetY);
                }
            }
        }
    
        for (int i = 0; i < this.wayUp.size(); i++) {
            int x = this.wayUp.get(i) % w;
            int y = this.wayUp.get(i) / w;
            this.tileDisplay.drawLadderUp(0.5f + x + offsetX, 0.7f + y + offsetY);
        }

        if (this.finishFlagPosition != -1) {
            int x = this.finishFlagPosition % w;
            int y = this.finishFlagPosition / w;
            this.tileDisplay.drawFinishFlag(0.5f + x + offsetX, 0.7f + y + offsetY);
        }
    }

    /** display path nodes. with offset 
     * @param offsetX
     * offset x in world-space
     * @param offsetY
     * offset y in world-space 
     * @param nodes
     * path nodes
     * */
    public void displayPath(float offsetX, float offsetY, int[] nodes) {
        for (int i = 1; i < nodes.length; i++) {
            int ax = nodes[i - 1] % w;
            int ay = nodes[i - 1] / w;
            int bx = nodes[i] % w;
            int by = nodes[i] / w;

            this.tileDisplay.drawPathSegment(
                ax + offsetX + 0.5f,
                ay + offsetY + 0.7f,
                bx + offsetX + 0.5f,
                by + offsetY + 0.7f
            );
        }
    }

    /** display projected 3d path nodes
     * @param offsetX
     * offset x in world-space
     * @param offsetY
     * offset y in world-space 
     * @param layer
     * layer
     * @param szX
     * 3D maze szX
     * @param szY
     * 3D maze szY
     * @param szZ
     * 3D maze szZ
     * @param nodes
     * path nodes 
     * */
    public void displayPath3d(float offsetX, float offsetY, int layer, int szX, int szY, int szZ, int[] nodes) {
        for (int i = 1; i < nodes.length; i++) {
            int p1x = nodes[i - 1] % szX;
            int p1yz = nodes[i - 1] / szX;
            int p1y = p1yz % szY;
            int p1z = p1yz / szY;

            int p2x = nodes[i] % szX;
            int p2yz = nodes[i] / szX;
            int p2y = p2yz % szY;
            int p2z = p2yz / szY;

            if (p1y != layer && p2y != layer) {
                continue;
            }

            if (p1y == layer && p2y == layer) {
                this.tileDisplay.drawPathSegment(
                    p1x + offsetX + 0.5f,
                    p1z + offsetY + 0.7f,
                    p2x + offsetX + 0.5f,
                    p2z + offsetY + 0.7f
                );
                continue;
            }

            float d1 = 0f;
            float d2 = 0f;

            if (p1y > layer) {
                d1 += 0.2f;
            } else if (p1y < layer) {
                d1 -= 0.2f;
            }

            if (p2y > layer) {
                d2 += 0.2f;
            } else if (p2y < layer) {
                d2 -= 0.2f;
            }

            this.tileDisplay.drawPathSegment(
                p1x + offsetX + 0.5f,
                p1z + offsetY + 0.7f - d1,
                p2x + offsetX + 0.5f,
                p2z + offsetY + 0.7f - d2
            );

        }
    }
}
