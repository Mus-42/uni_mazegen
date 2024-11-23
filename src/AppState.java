package knu.mus.maze;

import static com.raylib.Raylib.*;
import static com.raylib.Jaylib.BLANK;
import static com.raylib.Jaylib.BLACK;
import static com.raylib.Jaylib.GREEN;
import static com.raylib.Jaylib.WHITE;

/** Holder for all application state and ui */
public class AppState {
    // 2d maze size
    static final int w = 20;
    static final int h = 20;
    // 3d maze size
    static final int szX = 10;
    static final int szY = 10;
    static final int szZ = 10;

    final float offsetX = 1.5f;
    final float offsetY = 1.5f;

    final int PREVIEW_SIZE = 480;

    Random random;
    MazeGenerator mazeGen;

    TileDisplay tileDisplay;
    MazeDisplay2D display2d;
    MazeDisplay3D display3d;

    Maze2D maze2d;
    Maze3D maze3d;
    // texture to render 3d maze preview into
    RenderTexture renderTexture;

    boolean isIn2DMode;
    boolean pathDirty;
    boolean display2dDirty;
    // selected slice layer for 3D maze
    int selectedLayer;
    int[] pathNodes;

    /** creates new */
    public AppState() {
        this.random = new Random();
        this.mazeGen = new MazeGenerator(this.random);

        this.tileDisplay = new TileDisplay();
        this.display3d = new MazeDisplay3D(szX, szY, szZ);

        this.maze2d = new Maze2D(w, h);
        this.maze3d = new Maze3D(szX, szY, szZ);

        this.renderTexture = LoadRenderTexture(PREVIEW_SIZE, PREVIEW_SIZE);

        this.selectedLayer = 0;
        this.pathNodes = new int[] { -1 };

        this.newMaze2d();
    }

    /** updates for past frame and display new state */
    public void tick() {
        // CONTROLS:
        // mode switch
        if (IsKeyPressed(KEY_T)) {
            if (this.isIn2DMode) {
                this.newMaze3d();
            } else {
                this.newMaze2d();
            }
        }
    
        // regenerate maze
        if (IsKeyPressed(KEY_R)) {
            if (this.isIn2DMode) {
                this.newMaze2d();
            } else {
                this.newMaze3d();
            }
        }

        // layer selection
        if (!this.isIn2DMode) {
            if (IsKeyPressed(KEY_W) && this.selectedLayer + 1 < this.szY) {
                this.selectedLayer += 1;
                this.display2dDirty = true;
            }

            if (IsKeyPressed(KEY_S) && this.selectedLayer - 1 >= 0) {
                this.selectedLayer -= 1;
                this.display2dDirty = true;
            }
        }

        float offset2dX = offsetX;
        float offset2dY = offsetY;
        float screenHeight = GetScreenHeight();
        
        // wee need extra space for 3d display
        if (!this.isIn2DMode) {
            offset2dX += PREVIEW_SIZE / (float)TileDisplay.TILE_SIZE + 2;
        }

        int mouseX = (int)Math.floor(GetMouseX() / (float)TileDisplay.TILE_SIZE - offset2dX);
        int mouseY = (int)Math.floor(GetMouseY() / (float)TileDisplay.TILE_SIZE - offset2dY);

        int mouseTile = 0;
        if (this.isIn2DMode) {
            mouseTile = Math.clamp(mouseX, 0, w-1) + w * Math.clamp(mouseY, 0, h-1);
        } else {
            mouseTile = this.maze3d.toIndex(
                Math.clamp(mouseX, 0, szX-1),
                this.selectedLayer,
                Math.clamp(mouseY, 0, szZ-1)
            );
            display3d.setCursorPosition(mouseTile);
        }

        if (!this.isIn2DMode && this.display2dDirty) {
            maze3d.rebuildDisplay2d(this.selectedLayer, this.display2d);
            this.display2dDirty = false;
        }

        if (this.pathDirty || this.pathNodes[0] != mouseTile && IsKeyDown(KEY_P)) {
            if (this.isIn2DMode) {
                this.pathNodes = maze2d.getPathToFinish(mouseTile);
            } else {
                this.pathNodes = maze3d.getPathToFinish(mouseTile);
            }
            this.pathDirty = false;
        }

        BeginDrawing();
        ClearBackground(BLANK);

        if (!this.isIn2DMode) {
            // draw 3d stuff into texture
            BeginTextureMode(this.renderTexture);
            UpdateCamera(this.display3d.camera, CAMERA_ORBITAL);
            ClearBackground(BLACK);
            BeginMode3D(this.display3d.camera);
            display3d.display(this.selectedLayer);
            display3d.displayPath(this.pathNodes);
            EndMode3D();
            EndTextureMode();

            DrawTextureRec(
                this.renderTexture.texture(),
                new Rectangle()
                    .x(0)
                    .y(0)
                    .width(PREVIEW_SIZE)
                    .height(-PREVIEW_SIZE),
                new Vector2()
                    .x(24)
                    .y((screenHeight - PREVIEW_SIZE) / 2),
                WHITE
            );
        }

        if (this.isIn2DMode) {
            display2d.displayPath(offset2dX, offset2dY, this.pathNodes);
        } else {
            display2d.displayPath3d(offset2dX, offset2dY, this.selectedLayer, szX, szY, szZ, this.pathNodes);
        }

        display2d.display(offset2dX, offset2dY);

        // cursor
        DrawRectangleLinesEx(
            new Rectangle()
            .x((mouseX + offset2dX) * TileDisplay.TILE_SIZE)
            .y((mouseY + offset2dY) * TileDisplay.TILE_SIZE)
            .width(TileDisplay.TILE_SIZE)
            .height(TileDisplay.TILE_SIZE),
            2f,
            GREEN
        );

        DrawFPS(20, 20);
        EndDrawing();
    }

    /** manually frees allocated resources */
    public void deinit() {
        UnloadRenderTexture(this.renderTexture);
    }

    private void newMaze2d() {
        this.isIn2DMode = true;
        this.display2d = new MazeDisplay2D(w, h, this.tileDisplay);
        this.mazeGen.generate(this.maze2d); 
        this.maze2d.rebuildDisplay(this.display2d);
        this.pathDirty = true;
    }

    private void newMaze3d() {
        this.isIn2DMode = false;
        this.display2d = new MazeDisplay2D(szX, szZ, this.tileDisplay);
        this.mazeGen.generate(this.maze3d);

        this.maze3d.rebuildDisplay(this.display3d);
        this.maze3d.rebuildDisplay2d(this.selectedLayer, this.display2d);
        this.pathDirty = true;
        this.display2dDirty = true;
    }
}
