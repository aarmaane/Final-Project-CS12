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
    private FadeEffect fade = new FadeEffect();
    // Buttons
    private final ArrayList<Button> buttons = new ArrayList<>();
    // Background related fields
    private Background background = new Background("BG0.png,BG1.png,BG2.png", "0,0.3,0.5");
    private Player dummy = new Player();
    private LevelProp platform = new LevelProp("0,0,grassMiddle.png", false, false);
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
        // Declaring buttons
        Button playButton = new Button(new Rectangle(396,300, 150, 50), "Play", 46);
        playButton.setActionCommand("Play");

        Button instructButton = new Button(new Rectangle(323, 350, 300, 50), "Instructions", 46);
        instructButton.setActionCommand("Instruct");

        Button quitButton = new Button(new Rectangle(396 , 400, 150, 50), "Quit", 46);
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
        background.draw(g);
        g.drawImage(dummy.getSprite(),(int) dummy.getX()- 170 - scrollOffset,367 ,this);
        for(int i = 0; i < 10; i++){
            g.drawImage(platform.getPropImage(), platformsX1 + i*144 - scrollOffset,475, this);
            g.drawImage(platform.getPropImage(), platformsX2 + i*144 - scrollOffset,475, this);
        }
        for(Button button: buttons){
            button.drawRect(g);
        }
        g.setColor(Color.RED);
        g.drawString("Main menu", 435,200);
        g.setColor(Color.WHITE);
        g.drawLine(getWidth()/2 ,0, getWidth()/2, 960);
        fade.draw(g);
    }
    public void update(){
        // Making sure that the music is always looping
        if(!menuMusic.isPlaying()){
            menuMusic.play();
        }
        // Updating the fade effect
        fade.update();
        if(fade.isDoneFadeOut()){
            menuMusic.stop();
            menuMusic.closeSound();
            gameFrame.switchPanel(MainGame.SHOPPANEL);
        }
        // Updating the dummy player
        dummy.move(Player.RIGHT);
        dummy.checkCollision(new LevelProp(scrollOffset+",800,invisibleRect.png", false, false));
        dummy.update();
        background.update(scrollOffset);
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
        if(fade.isActive()){
            return; // Don't update buttons during the fade
        }
        Point mouse = getMousePosition();
        if(mouse != null){
            for(Button button: buttons){
                button.updateHover(mouse);
            }
        }
    }
    // Button Listener
    public class ButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String buttonString = e.getActionCommand();
            if(buttonString.equals("Play")){
                if(!fade.isActive()){
                    fade.start(FadeEffect.FADEOUT, 1);
                }
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
