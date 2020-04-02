import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


class GamePanel extends JPanel implements KeyListener {
    // Window related Objects
    public boolean ready = true;
    private boolean[] keysPressed; // Array that keeps track of keys that are pressed down
    private MainGame gameFrame;
    // Constructor for GamePanel
    public GamePanel(MainGame game){
        // Setting up the GamePanel
        gameFrame = game;
        setSize(680,750);
        keysPressed = new boolean[KeyEvent.KEY_LAST+1];
        addKeyListener(this);
    }
    // All window related methods
    public void addNotify() {
        super.addNotify();
        requestFocus();
        ready = true;
    }

    public void removeNotify(){
        super.removeNotify();
        ready = false;
    }
    public void paintComponent(Graphics g){
        g.setColor(new Color(0,255,0));
        g.fillRect(0, 0, 680, 750);
        System.out.println("GAME ACTIVE");
    }
    // Keyboard related methods
    @Override
    public void keyPressed(KeyEvent e) {
        // Keeping track of whether or not the key is pressed down
        keysPressed[e.getKeyCode()] = true;
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
    @Override
    public void keyTyped(KeyEvent e) {}
}