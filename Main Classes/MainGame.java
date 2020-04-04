import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGame extends JFrame {
    // Declaring constants
    private final String GAMEPANEL = "game";
    private final String MENUPANEL = "menu";
    // Declaring fields
    private GamePanel game;
    private MainMenu menu;
    private JPanel panelManager;
    private String activePanel = MENUPANEL;
    private Timer myTimer; // Timer to call the game functions each frame
    private int runTime; // Variable to keep track of the miliseconds that have passed since the start of the game
    public MainGame(){
        super("Frogger"); // Setting the title
        // Creating the JPanels for the game
        game = new GamePanel(this);
        menu = new MainMenu(this);
        panelManager = new JPanel(new CardLayout());
        // Setting up the CardLayout in panelManager
        panelManager.add(game, GAMEPANEL);
        panelManager.add(menu, MENUPANEL);
        CardLayout cardLayout = (CardLayout) panelManager.getLayout();
        cardLayout.show(panelManager, MENUPANEL);
        // Creating the JFrame and JPanels
        setSize(960,590);
        setResizable(false);
        setLocationRelativeTo(null);
        add(panelManager);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        // Starting a timer to update the frames
        myTimer = new Timer(10, new TickListener());	 // trigger every 10 ms
        myTimer.start();
    }
    public void switchPanel(){
        CardLayout cardLayout = (CardLayout) panelManager.getLayout();
        cardLayout.show(panelManager, GAMEPANEL);
        activePanel = GAMEPANEL;
    }
    // TickListener Class
    class TickListener implements ActionListener {
        public void actionPerformed(ActionEvent evt){
            if(game != null && activePanel.equals(GAMEPANEL) && game.ready){
                // Main game loop
                game.tick();
                game.repaint();
            }
            else if(menu != null && activePanel.equals(MENUPANEL)){
                menu.repaint();
            }
        }
    }
    public static void main(String[] args){
        MainGame game = new MainGame();
    }
}
