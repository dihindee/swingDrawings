//package Screens;

import java.awt.*;

import static java.lang.Math.abs;
import static java.lang.Math.hypot;

public class VectorScreen extends Screen {
    private static final int DELTA=30;
    private StaticVector[] vectors;
    private int xs[],ys[];
    @Override
    void onCreate() {
        title = "Vectors";
        int w = FRAME_WIDTH/DELTA - 1,h = FRAME_HEIGHT/DELTA - 1;
        vectors = new StaticVector[w*h];
        xs = new int[3];
        ys = new int[3];
        for(int i=0;i<w;i++) {
            for (int j = 0; j < h; j++) {
                vectors[i * h + j] = new StaticVector((i + 1) * DELTA, (j + 1) * DELTA);
            }
        }
    }
    @Override
    public void update() {
        float x = MouseInfo.getPointerInfo().getLocation().x-Main.getFrame().getX();
        float y = MouseInfo.getPointerInfo().getLocation().y-Main.getFrame().getY();
        for(StaticVector a: vectors){
            float dx = x - a.posX;
            float dy = y - a.posY;//1468
            float mul = (float) (-DELTA * (1 - hypot(dx, dy) / 1500) / hypot(dx, dy));
            a.x =  (int) (mul *dx);
            a.y =  (int) (mul *dy);
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);
        g.setColor(Color.WHITE);
        Color b;
        for(StaticVector a : vectors){
            b = new Color((int) (255*abs(a.x)/hypot(a.x,a.y)),0,(int)(255*abs(a.y)/hypot(a.x,a.y)));
            g.setColor(b);
            xs[0]=a.posX+a.y/3;
            xs[1]=a.posX+a.x;
            xs[2]=a.posX-a.y/3;
            ys[0]=a.posY-a.x/3;
            ys[1]=a.posY+a.y;
            ys[2]=a.posY+a.x/3;
            g.drawPolygon(xs,ys,3);
        }
    }

    @Override
    public void drawInfo(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,150,17);
        g.setColor(Color.WHITE);
        g.drawChars("Just move your mouse".toCharArray(),0,20,10,15);
    }
    public static class StaticVector{
        int posX,posY,x,y;
        StaticVector(int x,int y){
            posX = this.x = x;
            posY = this.y = y;
        }
    }
}
