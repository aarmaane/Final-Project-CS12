import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.security.Key;


class GamePanel extends JPanel implements KeyListener {
    // Window related Objects
    public boolean ready = true;
    private boolean[] keysPressed; // Array that keeps track of keys that are pressed down
    private MainGame gameFrame;

    // Game related Objects
    private Player player = new Player();
    private Image[] backgroundLayers = new Image[3];

    // Constructor for GamePanel
    public GamePanel(MainGame game){
        // Setting up the GamePanel
        gameFrame = game;
        setSize(960,590);
        keysPressed = new boolean[KeyEvent.KEY_LAST+1];
        addKeyListener(this);
        try{
            for(int i = 0; i < 3; i++){
                backgroundLayers[i] = ImageIO.read(new File("Assets/Images/Background/BG" + (i+1) + ".png"));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // All window related methods
    public void addNotify() {
        super.addNotify();
        requestFocus();
        ready = true;
        System.out.println("add notfiy");
    }
    public void removeNotify(){
        super.removeNotify();
        ready = false;
        System.out.println("remove notify");
    }
    public void paintComponent(Graphics g){
        g.setColor(new Color(0,255,0));
        g.fillRect(0, 0, 960, 590);
        for(int i = 0; i < 3; i ++){
            g.drawImage(backgroundLayers[i], 0, 0, this);
        }
        g.drawImage(player.getSprite(), (int)player.getX(), (int)player.getY(), this);
    }

    // Keyboard related methods
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        // Running code for initially clicked keys
        if(e.getKeyCode() == KeyEvent.VK_SPACE && !keysPressed[KeyEvent.VK_SPACE]){
            System.out.println("jump");
        }
        // Keeping track of whether or not the key is pressed down
        keysPressed[keyCode] = true;

    }
    @Override
    public void keyReleased(KeyEvent e) {
        keysPressed[e.getKeyCode()] = false;
    }
    @Override
    public void keyTyped(KeyEvent e) {}

    // Game related methods
    public void tick(){
        player.tick();
    }

    public void checkInputs(){
        if(keysPressed[KeyEvent.VK_D]){
            System.out.println("right");
            player.move(Player.RIGHT);
        }
        else if(keysPressed[KeyEvent.VK_A]){
            System.out.println("left");
            player.move(Player.LEFT);
        }
    }
}