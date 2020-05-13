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
    private Font gameFont;
    // Buttons
    private final ArrayList<Button> buttons = new ArrayList<>();
    // Constructor
    public ShopPanel(MainGame frame){
        gameFrame = frame;
        game = frame.getGame();
        player = game.getPlayer();
        gameFont = game.getGameFont();
        setSize(960,590);
        addMouseListener(this);
        setLayout(null);
        // Declaring buttons
        Button continueButton = new Button(new Rectangle(0,0, 200, 50), "Continue", 35);
        continueButton.setActionCommand("Continue");
        buttons.add(continueButton);
        for(Button button: buttons){
            button.addActionListener(new ButtonListener());
            add(button);
        }
    }
    // Window related methods
    public void paintComponent(Graphics g){
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, 960, 590);
        g.setColor(Color.BLACK);
        g.setFont(gameFont);
        g.drawString("Player points:" + player.getPoints(), 400, 300);
        g.drawString("Click to go to game", 400, 400);
        for(Button button: buttons){
            button.drawRect(g);
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
            if(buttonString.equals("Continue")){
                String input = JOptionPane.showInputDialog("Enter level number");
                game.setLevelNum(Integer.parseInt(input));
                gameFrame.switchPanel(MainGame.TRANSITIONPANEL);
            }
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {

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
