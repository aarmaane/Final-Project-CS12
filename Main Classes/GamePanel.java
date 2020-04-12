import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


class GamePanel extends JPanel implements KeyListener {
    // Window related Objects
    public boolean ready = true;
    private boolean[] keysPressed; // Array that keeps track of keys that are pressed down
    private MainGame gameFrame;

    // Game related Objects
    private Player player = new Player(this);
    private Image[] backgroundLayers = new Image[3];
    private ArrayList<Platform> platforms = new ArrayList<Platform>();
    private ArrayList<Platform> noCollidePlatforms = new ArrayList<Platform>();
    private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    // Game fields
    private int levelOffset = 0;

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
        // Initalizing the enemy Classes
        Slime.init();
        loadLevel(1);
    }

    // Method to load up all level Objects from the corresponding text files
    public void loadLevel(int levelNum){
        try{
            for(String data: loadFile("Platforms.txt", levelNum)){
                platforms.add(new Platform(data));
            }
            for(String data: loadFile("NoCollidePlatforms.txt", levelNum)){
                noCollidePlatforms.add(new Platform(data));
            }
            for(String data: loadFile("Slimes.txt", levelNum)){
                enemies.add(new Slime(data));
            }
        }
        catch (IOException e) {
            System.out.println("Level " + levelNum + " data incomplete!");
            e.printStackTrace();
        }
    }
    // Helper method to load up individual files into ArrayLists with their lines as Strings
    public ArrayList<String> loadFile(String fileName, int levelNum) throws IOException{
        Scanner inFile = new Scanner(new BufferedReader(new FileReader("Data/Level " + levelNum + "/" + fileName)));
        ArrayList<String> fileContents = new ArrayList<String>();
        while(inFile.hasNextLine()){
            String line = inFile.nextLine();
            if(!line.substring(0,2).equals("//")){ // Making sure that the line is not a comment
                fileContents.add(line);
            }
        }
        inFile.close();
        return fileContents;
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
        // Drawing the background
        g.setColor(new Color(0,0,0));
        g.fillRect(0, 0, 960, 590);
        for(int i = 0; i < 3; i ++){
            g.drawImage(backgroundLayers[i], 0, 0, this);
        }
        // Drawing the level
        for(Platform platform: platforms){
            Rectangle platformRect = platform.getRect();
            g.drawImage(platform.getPlatformImage(), platformRect.x - levelOffset, platformRect.y, this);
        }
        for(Platform platform: noCollidePlatforms){
            Rectangle platformRect = platform.getRect();
            g.drawImage(platform.getPlatformImage(), platformRect.x - levelOffset, platformRect.y, this);
        }
        // Drawing enemies
        for(Enemy enemy: enemies){
            g.drawImage(enemy.getSprite(), (int)enemy.getX() - levelOffset, (int)enemy.getY(), this);
            drawHealth(g, enemy);
            g.drawRect(enemy.getHitbox().x - levelOffset, enemy.getHitbox().y, enemy.getHitbox().width, enemy.getHitbox().height);
        }
        // Drawing the Player
        g.drawImage(player.getSprite(), (int)player.getX() - levelOffset, (int)player.getY(), this);
        g.drawRect(player.getHitbox().x - levelOffset, player.getHitbox().y, player.getHitbox().width, player.getHitbox().height);
    }
    public void drawHealth(Graphics g, Enemy enemy){
        int health = enemy.getHealth();
    }
    // Keyboard related methods
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        // Running code for initially clicked keys
        if(e.getKeyCode() == KeyEvent.VK_SPACE && !keysPressed[KeyEvent.VK_SPACE]){
            player.jump(Player.INITIAL);
        }
        // Keeping track of whether or not the key is pressed down
        keysPressed[keyCode] = true;
        // DEBUG KEYS
        if(e.getKeyCode() == KeyEvent.VK_BACK_SLASH){
            if(getMousePosition() != null){
                System.out.println(getMousePosition() + " True x = " + (getMousePosition().x - levelOffset));
            }
        }
        else if(e.getKeyCode() == KeyEvent.VK_CLOSE_BRACKET){
            player.resetPos();
        }

    }
    @Override
    public void keyReleased(KeyEvent e) {
        keysPressed[e.getKeyCode()] = false;
    }
    @Override
    public void keyTyped(KeyEvent e) {}

    // Game related methods
    public void update(){
        player.update();
        for(Enemy enemy: enemies){
            enemy.update(player);
        }
        calculateOffset();
    }
    public void calculateOffset(){
        Rectangle hitbox = player.getHitbox();
        if(hitbox.x + hitbox.width > 480){
            levelOffset = (hitbox.x + hitbox.width) - 480;
        }
        else{
            levelOffset = 0;
        }
    }
    public void checkCollision(){
        for(Platform platform: platforms){
            player.checkCollision(platform.getRect());
            for(Enemy enemy: enemies){
                enemy.checkCollision(platform.getRect());
            }
        }
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

    // Getter methods
    public double getLevelOffset(){
        return levelOffset;
    }
}