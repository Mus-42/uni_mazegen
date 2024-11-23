package knu.mus.maze;

import java.util.Arrays;
import static com.raylib.Raylib.*;
import static com.raylib.Jaylib.RED;
import static com.raylib.Jaylib.GREEN;
import static com.raylib.Jaylib.BLUE;
import static com.raylib.Jaylib.YELLOW;
import static com.raylib.Jaylib.ORANGE;
import static com.raylib.Jaylib.LIME;

/** 3D display for maze */
public class MazeDisplay3D {
    static final Color TRANSPARENT_GREEN = new Color()
        .g((byte)255)
        .a((byte)96);

    int w;
    int h;
    int l;

    int[] parents;
    int finishFlagPosition;
    int cursorPosition;

    Camera3D camera;

    Vector3 from;
    Vector3 to;

    /** crete new diplay
     * @param w
     * szX 
     * @param h
     * szY
     * @param l
     * szZ
     * */
    public MazeDisplay3D(int w, int h, int l) {
        this.w = w;
        this.h = h;
        this.l = l;

        this.from = new Vector3();
        this.to = new Vector3();

        this.camera = new Camera3D()
            ._position(new Vector3().x(w * 1.3f).y(h * 1.1f).z(0))
            .target(new Vector3().x((w - 1) / 2f).y((h - 1) / 2.f).z((l - 1) / 2f))
            .up(new Vector3().x(0).y(1).z(0))
            .fovy(90)
            .projection(CAMERA_PERSPECTIVE);
    }

    /** convert 3d coords to node index
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
        return (z * this.h + y) * this.w + x;
    }

    /** sets finish flag position 
     * @param pos
     * new posigion
     * */
    public void setFinishFlagPosition(int pos) {
        this.finishFlagPosition = pos;
    }
    
    /** cursor position 
     * @param pos
     * new posigion
     * */
    public void setCursorPosition(int pos) {
        this.cursorPosition = pos;
    }

    /** set parents array 
     * @param parents 
     * new parents 
     * */
    public void setParents(int[] parents) {
        assert parents.length == this.w * this.h * this.l;
        this.parents = parents;
    }

    /** display maze
     * @param selected 
     * selected layer to highlight it 
     * */
    public void display(int selected) {
        for (int x = 0; x < this.w; x++) {
            for (int y = 0; y < this.h; y++) {
                for (int z = 0; z < this.l; z++) {
                    int parent = this.parents[this.toIndex(x, y, z)];

                    if (parent == -1) continue;

                    var color = TRANSPARENT_GREEN;
                    float radius = 0.04f;
                    int py = parent / this.w % this.h;
                    if (y == selected || py == selected) {
                        if (py == selected && y == selected) {
                            color = LIME;
                            radius = 0.07f;
                        } else {
                            color = YELLOW;
                            radius = 0.05f;
                        }
                    }

                    indexToVec(this.to, parent);

                    DrawCylinderEx(
                        this.from.x(x).y(y).z(z),
                        this.to,
                        radius,
                        radius,
                        10,
                        color
                    );
                }
            }
        }

        indexToVec(this.from, this.finishFlagPosition);
        DrawSphere(this.from, 0.3f, BLUE);
        indexToVec(this.from, this.cursorPosition);
        DrawSphere(this.from, 0.3f, ORANGE);
    }

    private void indexToVec(Vector3 vec, int index) {
        int px = index % this.w;
        int pyz = index / this.w;
        int py = pyz % this.h;
        int pz = pyz / this.h;

        vec.x(px).y(py).z(pz);
    }

    /** displays path in maze
     * @param nodes
     * nodes
     * */
    public void displayPath(int[] nodes) {
        for (int i = 1; i < nodes.length; i++) {
            indexToVec(this.from, nodes[i-1]);
            indexToVec(this.to, nodes[i]);

            DrawCylinderEx(
                this.from,
                this.to,
                0.07f,
                0.07f,
                10,
                RED 
            );
        }
    }
}
