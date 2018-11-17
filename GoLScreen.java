import java.awt.*;
import java.awt.event.*;

public class GoLScreen extends Screen{
    private static final int DOT_SIZE = 5;
    private static final int MAP_WIDTH = FRAME_WIDTH/DOT_SIZE;
    private static final int MAP_HEIGHT = FRAME_HEIGHT/DOT_SIZE;
    private static byte[][] old_map;
    private static byte[][] new_map;
    private static boolean isPlaying = true;
    private int count;
    private static int mouseX,mouseY,mouseState=0,brushSize=0;
    private static MouseAdapter mouseAdapter = new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
            mouseState=(e.getButton() == MouseEvent.BUTTON1 ? 1 : -1);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            mouseState = 0;
        }
    };
    private static MouseWheelListener mouseWheelListener = e -> {
        brushSize-=e.getWheelRotation();
        if(brushSize<0)brushSize=0;
    };
    private static KeyAdapter keyAdapter = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode()==KeyEvent.VK_SPACE)isPlaying=!isPlaying;
        }
    };
    @Override
    void onCreate() {
        title = "Conway's Game of Life";
        old_map = new byte[MAP_WIDTH][MAP_HEIGHT];
        new_map = new byte[MAP_WIDTH][MAP_HEIGHT];
        Main.getFrame().addMouseListener(mouseAdapter);
        Main.getFrame().addKeyListener(keyAdapter);
        Main.getFrame().addMouseWheelListener(mouseWheelListener);
    }
    @Override
    void onDispose() {
        Main.getFrame().removeMouseListener(mouseAdapter);
        Main.getFrame().removeKeyListener(keyAdapter);
        Main.getFrame().removeMouseWheelListener(mouseWheelListener);
    }

    @Override
    public void update() {
        Point p = Main.getFrame().getComponent(0).getMousePosition();
        if(p!=null){
            mouseX = p.x/DOT_SIZE;
            mouseY = p.y/DOT_SIZE;
            if(mouseState==1)old_map[mouseX][mouseY]=new_map[mouseX][mouseY]=1;
            if(mouseState==-1)old_map[mouseX][mouseY]=new_map[mouseX][mouseY]=0;
            for(int i=0;i<2*brushSize;i++){
                for(int j=0;j<2*brushSize;j++){
                    int x = mouseX-brushSize + i,y = mouseY-brushSize+j;
                    if(x>=0&&x<MAP_WIDTH&&y>=0&&y<MAP_HEIGHT){
                        if(mouseState==1)old_map[x][y]=new_map[x][y]=1;
                        if(mouseState==-1)old_map[x][y]=new_map[x][y]=0;
                    }
                }
            }
        }else{
            mouseX=-1;
            mouseState=0;
        }
        if(!isPlaying) return;
        for(int i=0;i<MAP_WIDTH;i++){
            for(int j=0;j<MAP_HEIGHT;j++){
                around(i,j);
                if(count>=2 && count<=3){
                    if(count == 3) new_map[i][j] = 1;
                }else new_map[i][j] = 0;
            }
        }
        for(int i=0;i<MAP_WIDTH;i++){
            System.arraycopy(new_map[i], 0, old_map[i], 0, MAP_HEIGHT);
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);
        if(mouseX!=-1) {
            g.setColor(Color.GRAY);
            g.fillRect(DOT_SIZE*(mouseX - brushSize), DOT_SIZE*(mouseY - brushSize)
                    , 2 * DOT_SIZE * brushSize, 2 * DOT_SIZE * brushSize);
        }
        g.setColor(Color.WHITE);
        for (int i = 0; i < MAP_WIDTH; i++) {
            for (int j = 0; j < MAP_HEIGHT; j++) {
                if(new_map[i][j]==1)
                g.fillRect(i * DOT_SIZE, j * DOT_SIZE, DOT_SIZE, DOT_SIZE);
            }
        }
    }

    @Override
    public void drawInfo(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,230,80);
        g.setColor(Color.WHITE);
        String arr[] ={"Conway's game of life","LMB to create cells",
                "RMB to remove","mouse wheel to change brush size","space to pause"};
        for(int i=0;i<arr.length;i++){
            g.drawChars(arr[i].toCharArray(),0,arr[i].length(),10,15*(i+1));
        }
    }
    private void around(int x, int y) {
        count = 0;
        count += old_map[x][y < MAP_HEIGHT - 1 ? y + 1 : 0];
        count += old_map[x][y > 1 ? y - 1 : MAP_HEIGHT - 1];
        count += old_map[x < MAP_WIDTH - 1 ? x + 1 : 0][y];
        count += old_map[x > 1 ? x - 1 : MAP_WIDTH - 1][y];
        count += old_map[x < MAP_WIDTH - 1 ? x + 1 : 0][y < MAP_HEIGHT - 1 ? y + 1 : 0];
        count += old_map[x < MAP_WIDTH - 1 ? x + 1 : 0][y > 1 ? y - 1 : MAP_HEIGHT - 1];
        count += old_map[x > 1 ? x - 1 : MAP_WIDTH - 1][y < MAP_HEIGHT - 1 ? y + 1 : 0];
        count += old_map[x > 1 ? x - 1 : MAP_WIDTH - 1][y > 1 ? y - 1 : MAP_HEIGHT - 1];
    }
}
