import javax.swing.*;
import java.awt.*;

public class TransitionPanel extends JPanel {
    // Panel-related fields
    private MainGame gameFrame;
    // Constructor
    public TransitionPanel(MainGame frame){
        gameFrame = frame;
        setSize(960,590);
    }
    // Main methods
    public void paintComponent(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0,0, 960, 590);

    }
}
