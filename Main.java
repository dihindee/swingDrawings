//import Screens.Screen;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Main {
    private static String screenNames[] = {
            "VectorScreen","FireScreen"
            ,"PhysicsScreen","BezierScreen"
            ,"StarScreen","GoLScreen"};
    private static ArrayList<Class> screens;
    static Screen activeScreen;
    private static int screenCode = 0;
    static boolean drawInfo = false;
    private static JFrame frame;
    protected static JFrame getFrame() {
        return frame;
    }
    public static void main(String[] args) throws Exception {
        screens = new ArrayList<>();
        for(String a:screenNames)screens.add(Class.forName(a));
        frame = new JFrame("title");
        MainPanel panel = new MainPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setSize(Screen.FRAME_WIDTH, Screen.FRAME_HEIGHT);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F1) {
                    drawInfo = !drawInfo;
                    return;
                }
                if (Character.isDigit(e.getKeyChar())) {
                    int n = e.getKeyChar() - '1';
                    if (screenCode != n && n < screens.size()) {
                        activeScreen.onDispose();
                        screenCode = n;
                        try {
                            activeScreen = (Screen) screens.get(n).getConstructors()[0].newInstance();
                        } catch (Throwable exc) {
                            exc.printStackTrace();
                        }
                    }
                }
            }
        });
        activeScreen = new VectorScreen();
        while (frame.isEnabled()) {
            activeScreen.update();
            panel.repaint();
            Thread.sleep(Screen.SLEEP_TIME);
        }
    }

    static class MainPanel extends JPanel {
        @Override
        public void paint(Graphics g) {
            activeScreen.render(g);
            if (drawInfo) {
                activeScreen.drawInfo(g);
                g.setColor(Color.BLACK);
                g.fillRect(0,Screen.FRAME_HEIGHT-(screenNames.length+2)*16,150,(screenNames.length+2)*16);
                g.setColor(Color.WHITE);
                for(int i=0;i<screenNames.length;i++) {
                    g.drawChars(((i+1)+" - "+screenNames[i]).toCharArray()
                            ,0,screenNames[i].length()+4,10
                            ,Screen.FRAME_HEIGHT+(i-1-screenNames.length)*16);
                }
            }
        }
    }
}
