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
    // Declaring fields
    private GamePanel game;
    private MainMenu menu;
    private ShopPanel shop;
    private JPanel panelManager;
    private String activePanel;
    private Timer myTimer; // Timer to call the game functions each frame
    private int runTime; // Variable to keep track of the milliseconds that have passed since the start of the game
    private int timePassed;
    public MainGame() throws IOException {
        super("Game"); // Setting the title
        // Creating the JPanels for the game
        game = new GamePanel(this);
        menu = new MainMenu(this);
        shop = new ShopPanel(this);
        panelManager = new JPanel(new CardLayout());
        // Setting up the CardLayout in panelManager
        panelManager.add(game, GAMEPANEL);
        panelManager.add(menu, MENUPANEL);
        panelManager.add(shop, SHOPPANEL);
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
            if(activePanel.equals(GAMEPANEL) && !game.isPaused()){
                // Main game loop
                game.checkInputs();
                game.update();
                game.checkCollision();
                game.repaint();
                // Timer to
                timePassed+=10; // The main game loop is called every 10ms
                if(timePassed==1000){ // If 1 second has passed
                    timePassed=0;
                    game.iterateTime();
                }
            }
            else if(activePanel.equals(MENUPANEL)){
                menu.update();
                menu.repaint();
            }
            else if(activePanel.equals(SHOPPANEL)){
                shop.repaint();
            }
        }
    }
    public Player getPlayer(){
        return game.getPlayer();
    }
    public static void main(String[] args) throws IOException{
        System.setProperty("sum.java2d.opengl", "True");
        System.out.println(System.getProperty("sun.java2d.opengl"));
        MainGame game = new MainGame();
    }
}
