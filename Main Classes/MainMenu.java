import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainMenu extends JPanel implements MouseListener {
    // Window related Objects
    private MainGame gameFrame;
    private Sound menuMusic = new Sound("Assets/Sounds/Music/menu.wav", 80);
    private Player dummy = new Player();
    private LevelProp platform = new LevelProp("0,0,grassMiddle.png");
    private int scrollOffset = 0;
    private int screenWidth, platformsX1, platformsX2;
    public MainMenu(MainGame game){
        gameFrame = game;
        setSize(960,590);
        screenWidth = 960;
        addMouseListener(this);
    }
    // Window related methods
    public void paintComponent(Graphics g){
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 960, 590);
        g.drawImage(dummy.getSprite(),(int) dummy.getX()- 170 - scrollOffset,367 ,this);
        for(int i = 0; i < 10; i++){
            g.drawImage(platform.getPropImage(), platformsX1 + i*144 - scrollOffset,475, this);
            g.drawImage(platform.getPropImage(), platformsX2 + i*144 - scrollOffset,475, this);
        }
        g.setColor(Color.RED);
        g.drawString("Main menu", 435,200);
    }
    public void update(){
        dummy.move(Player.RIGHT);
        dummy.checkCollision(new Rectangle(scrollOffset,800,1000,1000));
        dummy.update();

        if(!menuMusic.isPlaying()){
            menuMusic.play();
        }
        if(dummy.getHitbox().x > 300){
            scrollOffset =  dummy.getHitbox().x - 300;
        }
        if(platformsX1 - scrollOffset + 1440 == screenWidth){
            platformsX2 = platformsX1 + 1440;
        }
        if(platformsX2 - scrollOffset + 1440 == screenWidth){
            platformsX1 = platformsX2 + 1440;
        }
        // SKIPPING MENU
        //menuMusic.stop();
        //menuMusic.closeSound();
        //gameFrame.switchPanel(MainGame.SHOPPANEL);
    }
    // Mouse related methods
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("test");
        menuMusic.stop();
        menuMusic.closeSound();
        gameFrame.switchPanel(MainGame.SHOPPANEL);
    }
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) { }
}
