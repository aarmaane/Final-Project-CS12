import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class ShopPanel extends JPanel implements MouseListener {
    // Window related Objects
    private MainGame gameFrame;
    private GamePanel game;
    private Player player;
    private Player dummy = new Player();
    private Sound shopMusic = new Sound("Assets/Sounds/Music/shop.wav", 80);
    private Font gameFont, gameFontBig;
    // Buttons
    private final ArrayList<Button> buttons = new ArrayList<>();
    // Constructor
    public ShopPanel(MainGame frame){
        gameFrame = frame;
        game = frame.getGame();
        player = game.getPlayer();
        gameFont = game.getGameFont();
        gameFontBig = gameFont.deriveFont(60f);
        setSize(960,590);
        addMouseListener(this);
        setLayout(null);
    }
    public void init(){
        // Declaring Buttons
        Button continueButton = new Button(new Rectangle(getWidth() - 200,getHeight() - 50, 200, 50), "Continue", 35);
        continueButton.setActionCommand("Continue");
        Button swordUpgrade = new Button(new Rectangle(0,100, 300, 50), "Upgrade Sword", 35);
        swordUpgrade.setActionCommand("SwordUpgrade");
        Button castUpgrade = new Button(new Rectangle(0,200, 300, 50), "Upgrade Cast", 35);
        castUpgrade.setActionCommand("CastUpgrade");
        // Setting up Button Array
        buttons.add(continueButton);
        buttons.add(swordUpgrade);
        buttons.add(castUpgrade);
        for(Button button: buttons){
            button.addActionListener(new ButtonListener());
            add(button);
        }
    }
    // Window related methods
    public void paintComponent(Graphics g){
        // Drawing the Background
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, 960, 590);
        // Drawing Text
        g.setColor(Color.BLACK);
        g.setFont(gameFont);
        g.drawString("Player points: " + player.getPoints(), getWidth()/2 - 93, getHeight() - 30);
        g.setColor(Color.GREEN);
        g.setFont(gameFontBig);
        g.drawString("SHOP", getWidth()/2 - 58, 590 - getHeight());
        g.drawImage(Utilities.scaleSprite(dummy.getSprite()), getWidth()/2 - 150,150, this);
        g.setColor(Color.BLACK);
        for(Button button: buttons){
            button.drawRect(g);
        }
        g.drawLine(getWidth()/2 ,0, getWidth()/2, 960);
    }
    public void update(){
        // Updating the dummy player
        dummy.updateSprite(); dummy.restoreHealth();
        // Checking that music is playing
        if(!shopMusic.isPlaying()){
            shopMusic.play();
        }
    }
    public void checkButtons(){
        Point mouse = getMousePosition();
        if(mouse != null){
            for(Button button: buttons){
                button.updateHover(mouse);
            }
        }
    }
    // Button Listener
    public class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String buttonString = e.getActionCommand();
            switch (buttonString){
                case "Continue":
                    String input = JOptionPane.showInputDialog("Enter level number");
                    game.setLevelNum(Integer.parseInt(input));
                    gameFrame.switchPanel(MainGame.TRANSITIONPANEL);
                    shopMusic.stop();
                    break;
                case "SwordUpgrade":
                    dummy.attack();
                    break;
                case "CastUpgrade":
                    dummy.castMagic();
            }
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if(!Sound.isMuted()){
            Sound.toggleVolume();
        }
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
