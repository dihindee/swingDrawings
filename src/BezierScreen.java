package src;

import java.awt.*;

public class BezierScreen extends Screen {
    private static final double STEPS = 32;
    private static final int N = 6;
    private static FloatingPoint[] dots;
    private static int Cnk[];
    static float timer = 0;

    @Override
    void onCreate() {
        title = "Bezier line";
        dots = new FloatingPoint[N];
        dots[0] = new FloatingPoint(510, 300, 1, 1, -200);
        dots[5] = new FloatingPoint(510, 300, -0.5f, 0.5f, 250);
        dots[4] = new FloatingPoint(510, 300, 1, 1, 200);
        dots[3] = new FloatingPoint(510, 300, -1.5f, 1.5f, 150);
        dots[2] = new FloatingPoint(510, 300, 2f, 2f, 100);
        dots[1] = new FloatingPoint(510, 300, -2.5f, 2.5f, 50);
        Cnk = new int[N];
        for (int i = 0; i < N; i++) {
            Cnk[i] = f(N - 1) / (f(i) * f(N - 1 - i));
        }
    }

    @Override
    public void update() {
        for (FloatingPoint dot : dots) dot.update();
        timer += SLEEP_TIME / 1000f;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 1280, 720);
        g.setColor(Color.BLUE);
        for (int i = 0; i < N; i++) {
            g.drawOval(dots[i].x - 5, dots[i].y - 5, 10, 10);
            if (i != 0) g.drawLine(dots[i - 1].x, dots[i - 1].y, dots[i].x, dots[i].y);
        }
        g.setColor(Color.WHITE);
        int xold = calcX(0), yold = calcY(0), x, y;
        for (int i = 1; i <= STEPS; i++) {
            x = calcX(i / STEPS);
            y = calcY(i / STEPS);
            g.drawLine(xold, yold, x, y);
            xold = x;
            yold = y;
        }
    }

    @Override
    public void drawInfo(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 200, 20);
        g.setColor(Color.WHITE);
        g.drawChars("Bezier curve on moving points".toCharArray(), 0, 29, 10, 15);
    }

    private static int f(int n) {
        if (n <= 1) return 1;
        int res = 1;
        for (int i = 2; i <= n; i++) res *= i;
        return res;
    }

    private static int calcX(double t) {
        double x = 0;
        for (int i = 0; i < N; i++) {
            x += Cnk[i] * dots[i].x * Math.pow(1 - t, N - 1 - i) * Math.pow(t, i);
        }
        return (int) x;
    }

    private static int calcY(double t) {
        double y = 0;
        for (int i = 0; i < N; i++) {
            y += Cnk[i] * dots[i].y * Math.pow(1 - t, N - 1 - i) * Math.pow(t, i);
        }
        return (int) y;
    }

    public static class FloatingPoint {
        private int centerX, centerY, radius;
        private float wx, wy;
        int x, y;

        FloatingPoint(int centerX, int centerY, float wx, float wy, int radius) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.wx = wx;
            this.wy = wy;
            this.radius = radius;
        }

        void update() {
            x = (int) (centerX + radius * Math.sin(wx * timer));
            y = (int) (centerY + radius * Math.cos(wy * timer));
        }
    }
}
