import javax.swing.*;

public class EndingPanel extends JPanel {
    private MainGame gameFrame;
    private GamePanel game;
    // Constructor
    public EndingPanel(MainGame frame){
        gameFrame = frame;
        game = frame.getGame();
        setSize(960,590);
    }
}
