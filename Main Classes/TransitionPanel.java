// TransitionPanel.java
// Armaan Randhawa and Shivan Gaur
// Class makes a transition panel that is used in-between levels
import javax.swing.*;
import java.awt.*;

public class TransitionPanel extends JPanel {
    // Panel-related fields
    private MainGame gameFrame;
    private GamePanel game;
    private FadeEffect fade = new FadeEffect();
    // Constructor
    public TransitionPanel(MainGame frame){
        gameFrame = frame;
        game = frame.getGame();
        setSize(960,590);
        fade.start(FadeEffect.FADEIN, 5);
    }
    // Main methods
    // Method that draws the panel
    public void paintComponent(Graphics g){
        // Drawing the text
        g.setColor(Color.BLACK);
        g.fillRect(0,0, 960, 590);
        g.setFont(game.getGameFont());
        g.setColor(Color.WHITE);
        g.drawString("Level " + game.getLevelNum(), 400, 300);
        // Drawing fade
        fade.draw(g);
    }

    // Method that updates the fade on the panel
    public void update(){
        fade.update();
        if(fade.isDoneFadeIn()){
            fade.start(FadeEffect.FADEOUT, 3);
        }
        else if(fade.isDoneFadeOut()){ // When fade out is done, start the level
            game.loadLevel();
            gameFrame.switchPanel(MainGame.GAMEPANEL);
            fade.start(FadeEffect.FADEIN, 3);
        }
    }
}
