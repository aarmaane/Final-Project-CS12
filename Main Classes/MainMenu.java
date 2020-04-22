import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainMenu extends JPanel implements MouseListener {
    // Window related Objects
    private MainGame gameFrame;
    private Player dummy = new Player();
    private Slime dummySlime = new Slime("-150,700,1");
    public MainMenu(MainGame game){
        gameFrame = game;
        setSize(960,590);
        addMouseListener(this);
        Slime.init();
    }
    // Window related methods
    public void paintComponent(Graphics g){
        dummy.move(Player.RIGHT);
        dummySlime.update(dummy);
        dummySlime.checkCollision(new Rectangle(-200,800,100000,1000));
        dummy.checkCollision(new Rectangle(0,800,100000,1000));
        dummy.update();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 960, 590);
        g.drawImage(dummy.getSprite(),(int) dummy.getX()- 170,400 ,this);
        g.drawImage(dummySlime.getSprite(),(int) dummySlime.getX(),445 ,this);
        if(dummy.getX() > 1100){
            g.setColor(Color.RED);
            g.drawString("Intro screen?", 435,200);
        }
        //gameFrame.switchPanel(MainGame.SHOPPANEL); // SKIPPING MENU, REMOVE LATER
    }
    // Mouse related methods
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("test");
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
