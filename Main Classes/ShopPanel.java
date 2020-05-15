import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ShopPanel extends JPanel implements MouseListener {
    // Window related Objects
    private MainGame gameFrame;
    private GamePanel game;
    private Player player;
    private Player dummy = new Player();
    private Sound shopMusic = new Sound("Assets/Sounds/Music/shop.wav", 80);
    private Font gameFont, gameFontBig;
    private Image checkbox, checkmark;
    private boolean[] checks = new boolean[4];
    private Point mousePoint;
    // Buttons
    private ArrayList<Button> buttons = new ArrayList<>();
    private Button hoveredButton;
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
    public void init() throws IOException {
        //Loading images
        checkbox= ImageIO.read(new File("Assets/Images/Shop/checkbox.png"));
        checkmark = ImageIO.read(new File("Assets/Images/Shop/checkmark.png"));

        // Declaring Buttons
        Button continueButton = new Button(new Rectangle(getWidth() - 200,getHeight() - 50, 200, 50), "Continue", 35);
        continueButton.setActionCommand("continue");
        Button swordUpgrade = new Button(new Rectangle(0,100, 300, 50), "Upgrade Sword", 35);
        swordUpgrade.setActionCommand("swordUpgrade");
        Button castUpgrade = new Button(new Rectangle(0,200, 300, 50), "Upgrade Cast", 35);
        castUpgrade.setActionCommand("castUpgrade");
        //
        Button healthUpgrade = new Button(new Rectangle(0,300, 300, 50), "Upgrade Health", 35);
        healthUpgrade.setActionCommand("healthUpgrade");
        Button staminaUpgrade = new Button(new Rectangle(0,400, 300, 50), "Upgrade Stamina", 35);
        staminaUpgrade.setActionCommand("staminaUpgrade");
        //
        Button enableScope = new Button(new Rectangle(640,400, 300, 50), "Cast Scope", 35);
        enableScope.setActionCommand("castScope");
        Button enableInstantCast = new Button(new Rectangle(640,300, 300, 50), "Instant Cast", 35);
        enableInstantCast.setActionCommand("instantCast");
        Button enableDoubleJump = new Button(new Rectangle(640,100, 300, 50), "Double jump", 35);
        enableDoubleJump.setActionCommand("doubleJump");
        Button enableHyperSpeed = new Button(new Rectangle(640,200, 300, 50), "Hyperspeed", 35);
        enableHyperSpeed.setActionCommand("hyperspeed");
        // Setting up Button Array
        buttons.add(continueButton);
        buttons.add(swordUpgrade);
        buttons.add(healthUpgrade);
        buttons.add(staminaUpgrade);
        buttons.add(castUpgrade);
        buttons.add(enableScope);
        buttons.add(enableInstantCast);
        buttons.add(enableDoubleJump);
        buttons.add(enableHyperSpeed);
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
        g.setColor(new Color(8, 89, 255));
        g.drawString("Abilities",690,75);
        g.setColor(new Color(222, 255, 10));
        g.drawString("Upgrades",30,75);
        g.drawImage(Utilities.scaleSprite(dummy.getSprite()), getWidth()/2 - 150,150, this);
        g.setColor(Color.BLACK);
        // Drawing Checkboxes
        for(int i = 0; i< checks.length; i++){
            g.drawImage(checkbox,580,100*(i+1),this);
            if(checks[i]){
                g.drawImage(checkmark,575,100*(i+1),this);
            }
        }
        for(Button button: buttons){
            button.drawRect(g);
        }
        // Drawing tooltips
        if(hoveredButton != null){
            int xPos = mousePoint.x; int yPos = mousePoint.y;
            if(xPos + 200 > getWidth()){
                xPos -= 200;
            }
            if(yPos + 150 > getHeight()){
                yPos -= 150;
            }
            g.drawRect(xPos, yPos, 200, 150);
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(xPos, yPos, 200, 150);
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
        mousePoint = getMousePosition();
        hoveredButton = null;
        if(mousePoint != null){
            for(Button button: buttons){
                button.updateHover(mousePoint);
                if(button.isHovered()){
                    hoveredButton = button;
                }
            }
        }
        if(hoveredButton != null){
            System.out.println(hoveredButton.getActionCommand());
        }
    }
    // Button Listener
    public class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String buttonString = e.getActionCommand();
            switch (buttonString){
                case "continue":
                    String input = JOptionPane.showInputDialog("Enter level number");
                    game.setLevelNum(Integer.parseInt(input));
                    gameFrame.switchPanel(MainGame.TRANSITIONPANEL);
                    shopMusic.stop();
                    break;
                case "swordUpgrade":
                    dummy.attack();
                    player.upgradeSword(100,10);
                    break;
                case "castUpgrade":
                    dummy.castMagic();
                    player.upgradeCast(100,10);
                    break;
                case "healthUpgrade":
                    player.upgradeHealth(100,10);
                    break;
                case "staminaUpgrade":
                    player.upgradeStamina(100,10);
                    break;
                case "doubleJump":
                    player.enableDoubleJump(0,10);
                    player.addPoints(100);
                    checks[0] = true;
                    break;
                case "hyperspeed":
                    player.addPoints(100);
                    player.enableHyperspeed(0,10);
                    checks[1] = true;
                    break;
                case "instantCast":
                    player.addPoints(100);
                    player.enableInstantCast(0,10);
                    checks[2] = true;
                    break;
                case "castScope":
                    player.addPoints(100);
                    player.enableCastScope(0,10);
                    checks[3] = true;
                    break;






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
