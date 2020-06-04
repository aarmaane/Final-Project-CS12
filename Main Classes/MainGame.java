//MainGame.java
//Armaan Randhawa and Shivan Gaur
//This class is the Main game class and is the class that creates the game panel.
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class MainGame extends JFrame {
    // Declaring constants
    public static final String GAMEPANEL = "game";
    public static final String MENUPANEL = "menu";
    public static final String SHOPPANEL = "shop";
    public static final String TRANSITIONPANEL = "transition";
    // Declaring fields
    private GamePanel game;
    private MainMenu menu;
    private ShopPanel shop;
    private TransitionPanel transition;
    private JPanel panelManager;
    private String activePanel;
    private Timer myTimer; // Timer to call the game functions each frame
    private int runTime; // Variable to keep track of the milliseconds that have passed since the start of the game
    public MainGame() throws IOException {
        super("Game"); // Setting the title
        // Initalizing Main Classes
        Button.init();
        // Creating the JPanels for the game
        game = new GamePanel(this);
        menu = new MainMenu(this);
        shop = new ShopPanel(this);
        transition = new TransitionPanel(this);
        panelManager = new JPanel(new CardLayout());
        // Setting up the CardLayout in panelManager
        panelManager.add(game, GAMEPANEL);
        panelManager.add(menu, MENUPANEL);
        panelManager.add(shop, SHOPPANEL);
        panelManager.add(transition, TRANSITIONPANEL);
        switchPanel(MENUPANEL);
        // Creating the JFrame and JPanels
        setSize(960,590);
        setResizable(false);
        setLocationRelativeTo(null);
        add(panelManager);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Image icon = ImageIO.read(new File("Assets/Images/Chests/chestOpen.png"));
        setIconImage(icon);
        setVisible(true);
        // Starting a timer to update the frames
        myTimer = new Timer(10, new TickListener());	 // trigger every 10 ms
        myTimer.start();
        // Running panel setups
        menu.init();
        shop.init();
        Sound.toggleVolume(); // AUTO MUTE
    }
    public void switchPanel(String targetPanel){
        CardLayout cardLayout = (CardLayout) panelManager.getLayout();
        cardLayout.show(panelManager, targetPanel);
        activePanel = targetPanel;
        addNotify(); // Getting the focus of the current panel
    }
    // TickListener Class
    class TickListener implements ActionListener {
        public void actionPerformed(ActionEvent evt){
            switch(activePanel){
                case GAMEPANEL:
                    if(!game.isPaused()){
                        // Main game loop
                        game.checkInputs();
                        game.update();
                        game.checkCollision();
                        game.updateGraphics();
                        game.repaint();
                        // Timer to
                        runTime += 10; // The main game loop is called every 10ms
                        if(runTime == 1000){ // If 1 second has passed
                            runTime = 0;
                            game.iterateTime();
                        }
                    }
                    break;
                case MENUPANEL:
                    menu.update();
                    menu.checkButtons();
                    menu.repaint();
                    break;
                case SHOPPANEL:
                    shop.update();
                    shop.checkButtons();
                    shop.repaint();
                    break;
                case TRANSITIONPANEL:
                    transition.update();
                    transition.repaint();
                    break;
            }
        }
    }
    public GamePanel getGame(){
        return game;
    }
    public static void main(String[] args) throws IOException{
        System.setProperty("sun.java2d.opengl", "True");
        System.out.println(System.getProperty("sun.java2d.opengl"));
        MainGame game = new MainGame();
    }
}
