package knu.mus.maze;

import java.util.Arrays;

import static com.raylib.Raylib.*;

/** Main entry point class */
public class Main {
    static final int WINDOW_W = 1280;
    static final int WINDOW_H = 720;

    private Main() {}

    /** Main function
     * @param args
     * app args
     * */
    public static void main(String args[]) {
        InitWindow(WINDOW_W, WINDOW_H, "Mazegen42");
        SetTargetFPS(60);

        SetWindowState(FLAG_WINDOW_RESIZABLE | FLAG_WINDOW_TOPMOST);
        ClearWindowState(FLAG_WINDOW_HIDDEN);

        AppState state = new AppState();

        while (!WindowShouldClose()) {
            state.tick();
        }

        state.deinit();
        CloseWindow();
    }
}
