import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ShopPanel extends JPanel implements MouseListener {
    // Window related Objects
    private MainGame gameFrame;
    private GamePanel game;
    private Player player;
    private Player dummy = new Player();
    private Font gameFont, gameFontBig;
    private boolean[] checks = new boolean[4];
    private Point mousePoint;
    // Effect related fields
    private int lowPointFrames;
    // Buttons
    private ArrayList<Button> buttons = new ArrayList<>();
    private Button hoveredButton;
    private int[] upgradeAmount = new int[4];
    // Images and Sounds
    private Image checkbox, checkmark;
    private Sound shopMusic = new Sound("Assets/Sounds/Music/shop.wav", 80);
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
        // Loading images
        checkbox= ImageIO.read(new File("Assets/Images/Shop/checkbox.png"));
        checkmark = ImageIO.read(new File("Assets/Images/Shop/checkmark.png"));
        // Declaring Buttons
        Button continueButton = new Button(new Rectangle(getWidth() - 200,getHeight() - 50, 200, 50), "Continue", 35);
        // Left Side Buttons
        Button swordUpgrade = new Button(new Rectangle(0,100, 300, 50), "Upgrade Sword", 35);
        Button castUpgrade = new Button(new Rectangle(0,200, 300, 50), "Upgrade Cast", 35);
        Button healthUpgrade = new Button(new Rectangle(0,300, 300, 50), "Upgrade Health", 35);
        Button staminaUpgrade = new Button(new Rectangle(0,400, 300, 50), "Upgrade Stamina", 35);
        // Right Side Buttons
        Button enableScope = new Button(new Rectangle(getWidth() - 300,400, 300, 50), "Cast Scope", 35);
        Button enableInstantCast = new Button(new Rectangle(getWidth() - 300,300, 300, 50), "Instant Cast", 35);
        Button enableDoubleJump = new Button(new Rectangle(getWidth() - 300,100, 300, 50), "Double Jump", 35);
        Button enableHyperSpeed = new Button(new Rectangle(getWidth() - 300,200, 300, 50), "Hyperspeed", 35);
        // REMOVE LATER
        Button freePoints = new Button(new Rectangle(0,0,200,100),"Give 100", 35);
        buttons.add(freePoints);
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
        Scanner inFile = new Scanner(new BufferedReader(new FileReader("Data/Tooltips.txt")));
        for (Button button : buttons) {
            button.addActionListener(new ButtonListener());
            add(button);
            if(button != continueButton){
                button.addTooltip(inFile.nextLine());
            }
        }
    }
    // Window related methods
    public void paintComponent(Graphics g){
        // Drawing the Background
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, 960, 590);
        // Drawing Points
        g.setColor(Color.BLACK);
        g.setFont(gameFont);
        int xOffset = 0, yOffset = 0;
        if(lowPointFrames > 0){
            lowPointFrames--; xOffset = Utilities.randint(-4,4); yOffset = Utilities.randint(-4,4);
            g.setColor(Color.RED);
        }
        String drawnString = "Player points: " + player.getPoints();
        int textWidth = g.getFontMetrics().stringWidth(drawnString);
        g.drawString(drawnString, getWidth()/2 - textWidth/2 - xOffset, getHeight() - 30 - yOffset);
        // Drawing Titles
        g.setColor(Color.GREEN);
        g.setFont(gameFontBig);
        g.drawString("SHOP", getWidth()/2 - 58, 590 - getHeight());
        g.setColor(new Color(8, 89, 255));
        g.drawString("Abilities",getWidth() - 260,75);
        g.setColor(new Color(222, 255, 10));
        g.drawString("Upgrades",30,75);
        // Drawing Dummy Player
        g.drawImage(Utilities.scaleSprite(dummy.getSprite()), getWidth()/2 - 150,150, this);
        // Drawing upgrade boxes
        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 4; y++){
                if(upgradeAmount[y] > x){
                    g.setColor(Color.RED);
                    g.fillRect(70 + x*20, 150 + y*100, 10, 10);
                }
                g.setColor(Color.BLACK);
                g.drawRect(70 + x*20, 150 + y*100, 10, 10);
            }
        }
        // Drawing Checkboxes
        for(int i = 0; i< checks.length; i++){
            g.drawImage(checkbox,getWidth() - 335,100*(i+1),this);
            if(checks[i]){
                g.drawImage(checkmark,getWidth() - 335,100*(i+1),this);
            }
        }
        // Drawing buttons
        for(Button button: buttons){
           // button.drawRect(g);
            button.draw(g);
        }
        // Drawing tooltips
        if(hoveredButton != null && hoveredButton.hasTooltip()){
            int xPos = mousePoint.x; int yPos = mousePoint.y;
            if(xPos + 300 > getWidth()){
                xPos -= 300;
            }
            if(yPos + 150 > getHeight()){
                yPos -= 150;
            }
            g.setColor(Color.RED);
            g.drawRect(xPos, yPos, 300, 150);
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(xPos, yPos, 300, 150);
            hoveredButton.drawTooltip(g, xPos, yPos);
        }
    }
    public void update(){
        // Updating the dummy player
        dummy.updateSprite(); dummy.restoreHealth();
        // Checking that music is playing
        if(!shopMusic.isPlaying()){
            shopMusic.play();
        }
        // Updating the upgrade boxes
        upgradeAmount[0] = player.getSwordUpgradeNum();
        upgradeAmount[1] = player.getCastUpgradeNum();
        upgradeAmount[2] = player.getHealthUpgradeNum();
        upgradeAmount[3] = player.getStaminaUpgradeNum();
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
    }
    public boolean canBuy(int points){
        if(player.getPoints() < 100){
            lowPointFrames = 50;
            return false;
        }
        return true;
    }
    // Button Listener
    public class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String buttonString = e.getActionCommand();
            switch (buttonString){
                case "Give 100":
                    player.addPoints(100);
                    break;
                case "Continue":
                    String input = JOptionPane.showInputDialog("Enter level number");
                    game.setLevelNum(Integer.parseInt(input));
                    gameFrame.switchPanel(MainGame.TRANSITIONPANEL);
                    shopMusic.stop();
                    break;
                case "Upgrade Sword":
                    if(player.getSwordUpgradeNum() < 8 && canBuy(100)){
                        dummy.attack();
                        player.upgradeSword();
                    }
                    break;
                case "Upgrade Cast":
                    if(player.getCastUpgradeNum() < 8 && canBuy(100)){
                        dummy.castMagic();
                        player.upgradeCast();
                    }
                    break;
                case "Upgrade Health":
                    if(player.getHealthUpgradeNum() < 8 && canBuy(100)){
                        player.upgradeHealth();
                    }
                    break;
                case "Upgrade Stamina":
                    if(player.getStaminaUpgradeNum() < 8 && canBuy(100)){
                        player.upgradeStamina();
                    }
                    break;
                case "Double Jump":
                    if(!player.hasDoubleJump() && canBuy(100)){
                        player.enableDoubleJump();
                        checks[0] = true;
                    }
                    break;
                case "Hyperspeed":
                    if(!player.hasHyperspeed() && canBuy(100)){
                        player.enableHyperspeed();
                        checks[1] = true;
                    }
                    break;
                case "Instant Cast":
                    if(!player.hasInstantCast() && canBuy(100)){
                        player.enableInstantCast();
                        checks[2] = true;
                    }
                    break;
                case "Cast Scope":
                    if(!player.hasCastScope() && canBuy(100)){
                        player.enableCastScope();
                        checks[3] = true;
                    }
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
