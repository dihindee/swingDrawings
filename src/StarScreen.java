package src;

import java.awt.*;

import static java.lang.Math.*;

public class StarScreen extends Screen {
    private static int COUNT = 800;
    private double len[], angle[];

    @Override
    void onCreate() {
        title = "Starfield";
        len = new double[COUNT];
        angle = new double[COUNT];
        for (int i = 0; i < COUNT; i++) {
            len[i] = random() * 150 + 10;
            angle[i] = random() * 360;
        }
    }

    @Override
    public void update() {
        for (int i = 0; i < COUNT; i++) {
            len[i] += pow(len[i], 1.3) / 500;
            if (4 * pow(len[i], 2) > FRAME_WIDTH * FRAME_WIDTH + FRAME_HEIGHT * FRAME_HEIGHT) {
                len[i] = random() * 150 + 10;
                angle[i] = random() * 360;
            }
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        g.setColor(Color.WHITE);//max 5
        int x, y, z;
        for (int i = 0; i < COUNT; i++) {
            x = (int) (FRAME_WIDTH / 2 + len[i] * cos(angle[i]));
            y = (int) (FRAME_HEIGHT / 2 + len[i] * sin(angle[i]));
            z = 1 + (int) (15 * len[i] / hypot(FRAME_WIDTH, FRAME_HEIGHT));
            g.fillRect(x, y, z, z);
        }
    }

    @Override
    public void drawInfo(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 260, 20);
        g.setColor(Color.WHITE);
        g.drawChars("Standart screensaver from Windows XP".toCharArray(), 0, 36, 10, 15);
    }
}
