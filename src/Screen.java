package src;//package Screens;//package Screens;

import java.awt.*;

public abstract class Screen {
    public static final int FRAME_WIDTH = 1280, FRAME_HEIGHT = 720, SLEEP_TIME = 16;
    String title;

    Screen() {
        onCreate();
        Main.getFrame().setTitle(title);
    }

    void onDispose() {
    }

    abstract void onCreate();

    public abstract void update();

    public abstract void render(Graphics g);

    public abstract void drawInfo(Graphics g);
}
