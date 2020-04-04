import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainMenu extends JPanel implements MouseListener {
    // Window related Objects
    private MainGame gameFrame;
    public MainMenu(MainGame game){
        gameFrame = game;
        setSize(960,590);
        addMouseListener(this);
    }
    // Window related methods
    public void paintComponent(Graphics g){
        g.setColor(new Color(255,0,0));
        g.fillRect(0, 0, 960, 590);
    }
    // Mouse related methods
    @Override
    public void mouseClicked(MouseEvent e) {
        gameFrame.switchPanel();
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
