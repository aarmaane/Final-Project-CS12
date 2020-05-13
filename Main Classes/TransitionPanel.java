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
    public void paintComponent(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0,0, 960, 590);
        g.setFont(game.getGameFont());
        g.setColor(Color.WHITE);
        g.drawString("Level " + game.getLevelNum(), 400, 300);
        // Drawing fade
        fade.draw(g);
    }

    public void update(){
        fade.update();
        if(fade.isDoneFadeIn()){
            fade.start(FadeEffect.FADEOUT, 5);
        }
        else if(fade.isDoneFadeOut()){
            game.loadLevel();
            gameFrame.switchPanel(MainGame.GAMEPANEL);
            fade.start(FadeEffect.FADEIN, 5);
        }
    }
}
