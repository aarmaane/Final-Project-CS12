import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;


class GamePanel extends JPanel implements KeyListener {
    // Window related Objects
    public boolean ready = true;
    private boolean[] keysPressed; // Array that keeps track of keys that are pressed down
    private MainGame gameFrame;

    // Game related Objects
    private Player player = new Player(this);
    private Image[] backgroundLayers = new Image[3];
    private Platform test = new Platform("0,475,platformMiddle.png");

    // Game fields
    private double levelOffset = 0;

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
        g.setColor(new Color(0,0,0));
        g.fillRect(0, 0, 960, 590);
        for(int i = 0; i < 3; i ++){
            g.drawImage(backgroundLayers[i], 0, 0, this);
        }
        g.drawImage(player.getSprite(), (int)player.getX(), (int)player.getY(), this);
        g.drawRect(player.getHitBox().x, player.getHitBox().y, player.getHitBox().width, player.getHitBox().height);
        for(int i = 0; i < 5; i++){
            g.drawImage(test.getPlatformImage(), test.getRect().x +(i*test.getRect().width), test.getRect().y, this);

        }
    }

    // Keyboard related methods
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        // Running code for initially clicked keys
        if(e.getKeyCode() == KeyEvent.VK_SPACE && !keysPressed[KeyEvent.VK_SPACE]){
            System.out.println("jump initial");
            player.jump(Player.INITIAL);
        }
        // Keeping track of whether or not the key is pressed down
        keysPressed[keyCode] = true;
        // DEBUG KEYS
        if(e.getKeyCode() == KeyEvent.VK_BACK_SLASH){
            System.out.println(getMousePosition());
        }

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
        // Side-to-side movement inputs
        if(keysPressed[KeyEvent.VK_D] && keysPressed[KeyEvent.VK_A]){
            // Stop movement
        }
        else if(keysPressed[KeyEvent.VK_D]){
            player.move(Player.RIGHT);
        }
        else if(keysPressed[KeyEvent.VK_A]){
            player.move(Player.LEFT);
        }
        // Jumping input
        if(keysPressed[KeyEvent.VK_SPACE]){
            player.jump(Player.NORMAL);
        }
    }

    public void moveScreen(double offset){
        levelOffset += offset;
        test.translateX(offset);
    }

    // Getter methods
    public double getLevelOffset(){
        return levelOffset;
    }
}