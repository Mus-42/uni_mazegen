package knu.mus.maze;

import static com.raylib.Raylib.*;
import static com.raylib.Jaylib.WHITE;
import static com.raylib.Jaylib.RED;

import java.util.Arrays;

/** class for displays tiles */
public class TileDisplay {
    private static final int TILE_SIZE_NATIVE_PIXELS = 16;
    private static final int TEXTURE_SCALE_FACTOR = 2;

    /** Size of single tile in screen-space coords */
    public static final int TILE_SIZE = TILE_SIZE_NATIVE_PIXELS * TEXTURE_SCALE_FACTOR;

    private static final Vector2 WALL_VERTICAL_ORIGIN = new Vector2()
        .x(2 * TEXTURE_SCALE_FACTOR)
        .y(8 * TEXTURE_SCALE_FACTOR);

    private static final Rectangle WALL_VERTICAL_RECT = new Rectangle()
        .x(0 * TEXTURE_SCALE_FACTOR)
        .y(0 * TEXTURE_SCALE_FACTOR)
        .width(4 * TEXTURE_SCALE_FACTOR)
        .height(24 * TEXTURE_SCALE_FACTOR);

    private static final Vector2 WALL_HORIZONTAL_ORIGIN = new Vector2()
        .x(8 * TEXTURE_SCALE_FACTOR)
        .y(2 * TEXTURE_SCALE_FACTOR);

    private static final Rectangle WALL_HORIZONTAL_RECT = new Rectangle()
        .x(4 * TEXTURE_SCALE_FACTOR)
        .y(0 * TEXTURE_SCALE_FACTOR)
        .width(16 * TEXTURE_SCALE_FACTOR)
        .height(13 * TEXTURE_SCALE_FACTOR);

    private static final Vector2 FINISH_FLAG_ORIGIN = new Vector2()
        .x(1 * TEXTURE_SCALE_FACTOR)
        .y(10 * TEXTURE_SCALE_FACTOR);

    private static final Rectangle FINISH_FLAG_RECT = new Rectangle()
        .x(4 * TEXTURE_SCALE_FACTOR)
        .y(13 * TEXTURE_SCALE_FACTOR)
        .width(4 * TEXTURE_SCALE_FACTOR)
        .height(10 * TEXTURE_SCALE_FACTOR);

    private static final Vector2 LADDER_UP_ORIGIN = new Vector2()
        .x(4 * TEXTURE_SCALE_FACTOR)
        .y(14 * TEXTURE_SCALE_FACTOR);

    private static final Rectangle LADDER_UP_RECT = new Rectangle()
        .x(20 * TEXTURE_SCALE_FACTOR)
        .y(0 * TEXTURE_SCALE_FACTOR)
        .width(8 * TEXTURE_SCALE_FACTOR)
        .height(14 * TEXTURE_SCALE_FACTOR);

    private static final Vector2 LADDER_DOWN_ORIGIN = new Vector2()
        .x(4 * TEXTURE_SCALE_FACTOR)
        .y(0 * TEXTURE_SCALE_FACTOR);

    private static final Rectangle LADDER_DOWN_RECT = new Rectangle()
        .x(28 * TEXTURE_SCALE_FACTOR)
        .y(0 * TEXTURE_SCALE_FACTOR)
        .width(8 * TEXTURE_SCALE_FACTOR)
        .height(14 * TEXTURE_SCALE_FACTOR);

    private Texture tilemap;
    private Vector2 position;

    /** constructs new stile display with default texture */
    public TileDisplay() {
        try {
            byte[] imageData = getClass().getResourceAsStream("/res/tiles32.png").readAllBytes();
            Image tilemapImage = LoadImageFromMemory(".png", imageData, imageData.length);
            this.tilemap = LoadTextureFromImage(tilemapImage);
            UnloadImage(tilemapImage);
        } catch (Exception e) {
            System.out.println("texture loading failed!"); 
        }

        this.position = new Vector2();
    }

    /** draws vertical wall 
     * @param x
     * origin x
     * @param y
     * origin y
     * */
    public void drawWallVertical(float x, float y) {
        this.position.x(x * TILE_SIZE - WALL_VERTICAL_ORIGIN.x());
        this.position.y(y * TILE_SIZE - WALL_VERTICAL_ORIGIN.y());

        DrawTextureRec(
            this.tilemap, 
            WALL_VERTICAL_RECT,
            this.position, 
            WHITE
        );
    }

    /** draws horizontal wall 
     * @param x
     * origin x
     * @param y
     * origin y
     * */
    public void drawWallHorizontal(float x, float y) {
        this.position.x(x * TILE_SIZE - WALL_HORIZONTAL_ORIGIN.x());
        this.position.y(y * TILE_SIZE - WALL_HORIZONTAL_ORIGIN.y());

        DrawTextureRec(
            this.tilemap, 
            WALL_HORIZONTAL_RECT,
            this.position, 
            WHITE
        );
    }

    /** draws finish flag 
     * @param x
     * origin x
     * @param y
     * origin y
     * */
    public void drawFinishFlag(float x, float y) {
        this.position.x(x * TILE_SIZE - FINISH_FLAG_ORIGIN.x());
        this.position.y(y * TILE_SIZE - FINISH_FLAG_ORIGIN.y());

        DrawTextureRec(
            this.tilemap, 
            FINISH_FLAG_RECT,
            this.position, 
            WHITE
        );
    }

    /** draws ladder up
     * @param x
     * origin x
     * @param y
     * origin y
     * */
    public void drawLadderUp(float x, float y) {
        this.position.x(x * TILE_SIZE - LADDER_UP_ORIGIN.x());
        this.position.y(y * TILE_SIZE - LADDER_UP_ORIGIN.y());

        DrawTextureRec(
            this.tilemap, 
            LADDER_UP_RECT,
            this.position, 
            WHITE
        );
    }

    /** draws ladder down 
     * @param x
     * origin x
     * @param y
     * origin y
     * */
    public void drawLadderDown(float x, float y) {
        this.position.x(x * TILE_SIZE - LADDER_DOWN_ORIGIN.x());
        this.position.y(y * TILE_SIZE - LADDER_DOWN_ORIGIN.y());

        DrawTextureRec(
            this.tilemap, 
            LADDER_DOWN_RECT,
            this.position, 
            WHITE
        );
    }
    
    /** draws ladder down 
     * @param fromX
     * from x
     * @param fromY
     * from y
     * @param toX
     * to x
     * @param toY
     * to y
     * */
    public void drawPathSegment(float fromX, float fromY, float toX, float toY) {
        DrawLine(
            Math.round(fromX * TILE_SIZE),
            Math.round(fromY * TILE_SIZE),
            Math.round(toX   * TILE_SIZE),
            Math.round(toY   * TILE_SIZE),
            RED
        );
    }
}
