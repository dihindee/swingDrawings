//package Screens;

//import Screens.Screen;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class FireScreen extends Screen {
    private static final int PCOUNT = 2000;
    private ArrayList<Pixel> list;
    @Override
    void onCreate() {
        title = "Fire";
        list = new ArrayList<>(PCOUNT);
        for(int i=0;i<PCOUNT;i++)list.add(new Pixel());

    }

    @Override
    public void update() {
        for(int i=0;i<PCOUNT;i++){
            list.get(i).update();
            if(list.get(i).remove)list.set(i,new Pixel());
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        Color color;
        for (Pixel a : list) {
            color = new Color(a.r, a.g, a.b);
            g.setColor(color);
            g.fillRect((int) (FRAME_WIDTH / 2 + a.x),
                    (int) (FRAME_HEIGHT * 0.8 + a.y), Pixel.PIX_SIZE, Pixel.PIX_SIZE);
            a.angle += 0.02f;
            a.x += sin(2 * a.angle) * 2 * (1 + sin(pow(a.x,2) +  pow(a.y,2)));
            a.y -= abs(cos(.5f * a.angle) * 2 * (1 + cos(pow(a.y,2) + pow(a.x,2))));
        }
    }

    @Override
    public void drawInfo(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawChars("Just fire".toCharArray(),0,9,10,15);
    }
    static class Pixel{
        static final int REDUCE=1,PIX_SIZE=4;
        float x,y,angle;
        int r,g,b;
        boolean remove = false;
        Pixel(){
            x=y=.0f;
            angle = (float) (Math.random()*10);
            r = g = b = 255;
        }
        void update(){
            if(b>0){b-=(int)(REDUCE/Math.random());if(b<0)b=0;}
            else {
                if(g>0) {g-=(int)(REDUCE/Math.random());if(g<0)g=0;}

                else {
                    if(r>0){r-=(int)(REDUCE/Math.random());if(r<0)r=0;}
                    else {remove = true;}
                }
            }
        }
    }
}
