import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ShopPanel extends JPanel implements MouseListener {
    // Window related Objects
    private MainGame gameFrame;
    private GamePanel game;
    private Player player;
    private Font gameFont;
    // Constructor
    public ShopPanel(MainGame frame){
        gameFrame = frame;
        game = gameFrame.getGame();
        player = game.getPlayer();
        setSize(960,590);
        addMouseListener(this);
    }
    // Window related methods
    public void paintComponent(Graphics g){
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, 960, 590);
        g.setColor(Color.BLACK);
        g.drawString("Player points:" + player.getPoints(), 400, 300);
        g.drawString("Click to go to game", 400, 400);
        //gameFrame.switchPanel(MainGame.GAMEPANEL); // SKIPPING SHOP, REMOVE LATER
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        String input = JOptionPane.showInputDialog("Enter level number");
        game.loadLevel(Integer.parseInt(input));
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
