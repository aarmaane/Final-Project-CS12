import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ShopPanel extends JPanel implements MouseListener {
    // Window related Objects
    private MainGame gameFrame;
    private Player player;
    // Constructor
    public ShopPanel(MainGame game){
        gameFrame = game;
        player = gameFrame.getPlayer();
        setSize(960,590);
        addMouseListener(this);
    }
    // Window related methods
    public void paintComponent(Graphics g){
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, 960, 590);
        gameFrame.switchPanel(MainGame.GAMEPANEL); // SKIPPING SHOP, REMOVE LATER
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        gameFrame.switchPanel(MainGame.GAMEPANEL);
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
