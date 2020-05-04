import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class MainMenu extends JPanel implements MouseListener {
    // Window related Objects
    private MainGame gameFrame;
    private Sound menuMusic = new Sound("Assets/Sounds/Music/menu.wav", 80);
    // Buttons
    private final ArrayList<Button> buttons = new ArrayList<>();
    // Background related fields
    private Image[] backgroundLayers = new Image[3];
    private Player dummy = new Player();
    private LevelProp platform = new LevelProp("0,0,grassMiddle.png");
    private int scrollOffset = 0;
    private int screenWidth, platformsX1, platformsX2;
    public MainMenu(MainGame game){
        // Setting up frame
        gameFrame = game;
        setSize(960,590);
        setLayout(null);
        // Setting up background animation
        screenWidth = 960;
        dummy.resetPos(0,366);
        backgroundLayers = Utilities.spriteArrayLoad(backgroundLayers, "Background/BG");
        // Declaring buttons
        Button.init();
        Button playButton = new Button(new Rectangle(400,300, 150, 50), "Play", 46);
        playButton.setActionCommand("Play");

        Button instructButton = new Button(new Rectangle(328, 350, 300, 50), "Instructions", 46);
        instructButton.setActionCommand("Instruct");

        Button quitButton = new Button(new Rectangle(400 , 400, 150, 50), "Quit", 46);
        quitButton.setActionCommand("Quit");
        buttons.add(playButton);
        buttons.add(instructButton);
        buttons.add(quitButton);
        for(Button button: buttons){
            button.addActionListener(new ButtonListener());
            add(button);
        }
        addMouseListener(this);
    }
    // Window related methods
    public void paintComponent(Graphics g){
        for(Image layer: backgroundLayers){
            g.drawImage(layer,0,0,this);
        }
        g.drawImage(dummy.getSprite(),(int) dummy.getX()- 170 - scrollOffset,367 ,this);
        for(int i = 0; i < 10; i++){
            g.drawImage(platform.getPropImage(), platformsX1 + i*144 - scrollOffset,475, this);
            g.drawImage(platform.getPropImage(), platformsX2 + i*144 - scrollOffset,475, this);
        }
        g.setColor(Color.RED);
        g.drawString("Main menu", 435,200);
    }
    public void update(){
        // Making sure that the music is always looping
        if(!menuMusic.isPlaying()){
            menuMusic.play();
        }
        // Updating the dummy player
        dummy.move(Player.RIGHT);
        dummy.checkCollision(new Rectangle(scrollOffset,800,1000,1000));
        dummy.update();
        // Updating the platforms to make them look continuous
        if(dummy.getHitbox().x > 300){
            scrollOffset =  dummy.getHitbox().x - 300;
        }
        if(platformsX1 - scrollOffset + 1440 == screenWidth){
            platformsX2 = platformsX1 + 1440;
        }
        if(platformsX2 - scrollOffset + 1440 == screenWidth){
            platformsX1 = platformsX2 + 1440;
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
    // Button Listner
    public class ButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String buttonString = e.getActionCommand();
            if(buttonString.equals("Play")){
                menuMusic.stop();
                menuMusic.closeSound();
                gameFrame.switchPanel(MainGame.SHOPPANEL);
            }
            else if(buttonString.equals("Quit")){
                System.exit(0);
            }
            else{
                System.out.println("Button not implemented");
            }
        }
    }
    // Mouse related methods
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println(getMousePosition());
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
