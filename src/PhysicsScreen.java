package src;//package Screens;

//import Screens.src.Screen;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static java.lang.Math.sqrt;

public class PhysicsScreen extends Screen {
    private static final int PCOUNT = 1000, PX = 640, PY = 360, GCONST = 10000;
    static final float C = 1000;
    private static final float t_delta = .016f;
    private static ArrayList<Particle> particles;
    static ArrayList<Attractor> attractors;
    private static Attractor mouseAttractor;
    private static int mass;
    private static MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            mouseAttractor.size = 1;
            if (e.getButton() == MouseEvent.BUTTON1)
                mouseAttractor.sign = 1;
            else if (e.getButton() == MouseEvent.BUTTON3) mouseAttractor.sign = -1;
            mouseAttractor.mass = mass;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            mouseAttractor.mass = 0;
            mouseAttractor.size = 0;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            mouseAttractor.mass = 0;
            mouseAttractor.size = 0;
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mouseAttractor.x = e.getXOnScreen();
            mouseAttractor.y = e.getYOnScreen();
        }

    };
    private static MouseWheelListener mouseWheelListener = new MouseWheelListener() {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            mass -= 4 * e.getWheelRotation();
            if (mass < 0) mass = 0;
            if (mouseAttractor.mass > 0) mouseAttractor.mass = mass;
        }
    };

    @Override
    void onCreate() {
        title = "Physics";
        Main.getFrame().addMouseListener(mouseListener);
        Main.getFrame().addMouseWheelListener(mouseWheelListener);
        mouseAttractor = new Attractor(0, 0, 16, 1);
        mouseAttractor.mass = 0;
        particles = new ArrayList<>(PCOUNT);
        attractors = new ArrayList<>();
        attractors.add(mouseAttractor);
    }

    @Override
    void onDispose() {
        Main.getFrame().removeMouseListener(mouseListener);
        Main.getFrame().removeMouseWheelListener(mouseWheelListener);
    }

    @Override
    public void update() {
        Point p;
        p = Main.getFrame().getComponent(0).getMousePosition();
        if (p != null) {
            mouseAttractor.pos.x = p.x;
            mouseAttractor.pos.y = p.y;
        }
        for (int i = 0; i < particles.size(); i++) {
            particles.get(i).update(t_delta);
            if (particles.get(i).remove)
                particles.set(i, new Particle(PX, PY, (float) (Math.PI * (2 * Math.random()))));
        }
        if (particles.size() < PCOUNT) particles.add(new Particle(PX, PY, (float) (Math.PI * (2 * Math.random()))));
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        g.setColor(Color.CYAN);
        for (Particle a : particles) {
            g.drawLine((int) (a.x - a.vx * t_delta), (int) (a.y - a.vy * t_delta), (int) (a.x), (int) (a.y));
        }
        for (Attractor a : attractors) {
            if (a == mouseAttractor) {
                if (a.sign > 0) g.setColor(Color.RED);
                else g.setColor(Color.BLUE);
                g.drawOval(a.pos.x - (mass / 2), a.pos.y - mass / 2, mass, mass);
            }
        }
    }

    @Override
    public void drawInfo(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 270, 70);
        g.setColor(Color.WHITE);
        String arr[] = {"Physics simulation", "LMB to positive attraction"
                , "RMB to negative", "Scroll wheel to control amount of force"};
        for (int i = 0; i < arr.length; i++) {
            g.drawChars(arr[i].toCharArray(), 0, arr[i].length(), 10, 15 * (i + 1));
        }
    }

    public static class Particle {

        float x, y;
        float vx, vy;
        boolean remove = false;

        Particle(int x, int y, float angle) {
            this.x = x;
            this.y = y;
            this.vx = .21f * (float) (Math.sin(angle) * C);
            this.vy = .21f * (float) (Math.cos(angle) * C);
        }

        void update(float delta) {
            float dx, dy, sqvx, sqvy;
            for (Attractor a : attractors) {
                dx = a.pos.x - x;
                dy = a.pos.y - y;
                dx *= dx;
                dy *= dy;
                vx += (float) (a.pos.x > x ? 1 : -1) * ((GCONST * a.mass * a.sign) * (sqrt(dx / (dx + dy)) / (dx + dy)));
                vy += (float) (a.pos.y > y ? 1 : -1) * ((GCONST * a.mass * a.sign) * (sqrt(dy / (dx + dy)) / (dx + dy)));
                sqvx = vx * vx;
                sqvy = vy * vy;
                if (sqvx + sqvy > C * C) {
                    vx = (float) ((vx > 0 ? 1 : -1) * C * sqrt(sqvx / (sqvx + sqvy)));
                    vy = (float) ((vy > 0 ? 1 : -1) * C * sqrt(sqvy / (sqvx + sqvy)));
                }
                if (dx + dy <= a.size * a.size + 100) remove = true;
            }
            x += vx * delta;
            y += vy * delta;
            if (x < 0 || x > FRAME_WIDTH || y < 0 || y > FRAME_HEIGHT) remove = true;
        }
    }

    public static class Attractor {
        int sign;
        float x, y, size;
        float mass;
        Point pos;

        Attractor(int x, int y, int size, int sign) {
            pos = new Point(x, y);
            this.size = size;
            this.mass = size * size / 4;
            this.sign = sign;
        }
    }
}
