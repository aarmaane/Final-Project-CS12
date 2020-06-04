//MainMenu.java
//Armaan Randhawa and Shivan Gaur
//This class is the main menu for the game. Once the player selects the play button this panel closes and the main game starts.
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainMenu extends JPanel {
    // Window related Objects
    private MainGame gameFrame;
    private Sound menuMusic = new Sound("Assets/Sounds/Music/menu.wav", 80);
    private FadeEffect fade = new FadeEffect();
    // Buttons
    private ArrayList<Button> buttons = new ArrayList<>();
    private ArrayList<Button> instructButtons = new ArrayList<>();
    // Background related fields
    private Background background = new Background("BG0.png,BG1.png,BG2.png", "0,0.3,0.5");
    private Player dummy = new Player();
    private LevelProp platform = new LevelProp("0,0,grassMiddle.png", false, false);
    private int scrollOffset = 0;
    private int screenWidth, platformsX1, platformsX2;
    // Other fields
    private boolean inInstructions;
    private Image[] instructPages = new Image[3];
    private int currentPage = 0;

    // Constructor
    public MainMenu(MainGame game){
        // Setting up frame
        gameFrame = game;
        setSize(960,590);
        setLayout(null);
        // Setting up background animation
        screenWidth = 960;
        dummy.resetPos(0,366);
    }
    //Initialization
    public void init(){
        // Declaring buttons
        Button playButton = new Button(new Rectangle((int)getSize().getWidth()/2 - 76,300, 150, 50), "Play", 46);
        Button instructionsButton = new Button(new Rectangle(getWidth()/2 - 149, 350, 300, 50), "Instructions", 46);
        Button quitButton = new Button(new Rectangle(getWidth()/2 - 76 , 400, 150, 50), "Quit", 46);
        buttons.add(playButton);
        buttons.add(instructionsButton);
        buttons.add(quitButton);
        for(Button button: buttons){
            button.addActionListener(new ButtonListener());
            add(button);
        }
        // Declaring instructions buttons
        Button backButton = new Button(new Rectangle(0, 10, 150, 50), "Back", 46);
        Button pageUpButton = new Button(new Rectangle(getWidth()/2 + 325, 450, 100, 35), "Page Up" , 30);
        Button pageDownButton = new Button(new Rectangle(getWidth()/2 - 425, 450, 110, 35), "Page Down" , 30);
        instructButtons.add(backButton);
        instructButtons.add(pageUpButton);
        instructButtons.add(pageDownButton);
        for(Button button: instructButtons){
            button.addActionListener(new ButtonListener());
        }
        // Loading instructions pages
        instructPages = Utilities.spriteArrayLoad(instructPages, "Main Menu/test");
    }
    // Window related methods
    public void paintComponent(Graphics g){
        //Method that draws everything
        background.draw(g);
        g.drawImage(dummy.getSprite(),(int) dummy.getX()- 170 - scrollOffset,367 ,this);
        for(int i = 0; i < 10; i++){
            g.drawImage(platform.getPropImage(), platformsX1 + i*144 - scrollOffset,475, this);
            g.drawImage(platform.getPropImage(), platformsX2 + i*144 - scrollOffset,475, this);
        }
        if(inInstructions){
            // Drawing the instructions page
            drawInstructions(g);
        }
        else{
            // Drawing all of the menu buttons
            for(Button button: buttons){
                button.draw(g);
            }
        }
        g.setColor(Color.WHITE);
        g.drawLine(getWidth()/2 ,0, getWidth()/2, 960);
        fade.draw(g);
    }
    public void drawInstructions(Graphics g){
        //Method that draws the instructions of the game
        for(Button button: instructButtons){
            button.draw(g);
        }
        g.drawImage(instructPages[currentPage], getWidth()/2 - 425, 50, this);
        g.setColor(Color.BLACK);
        // Drawing page number
        String drawnString = "Page " + (currentPage + 1) + "/3";
        int stringLength = g.getFontMetrics().stringWidth(drawnString);
        g.drawString(drawnString, getWidth()/2 - stringLength/2, 25);
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
            if(!inInstructions){
                for(Button button: buttons){
                    button.updateHover(mouse);
                }
            }
            else{
                for(Button button: instructButtons){
                    button.updateHover(mouse);
                }
            }

        }
    }
    // Button Listener
    public class ButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String buttonString = e.getActionCommand();
            switch(buttonString) {
                case "Play":
                    if(!fade.isActive()){
                        fade.start(FadeEffect.FADEOUT, 1);
                    }
                    break;
                case "Quit":
                    System.exit(0);
                    break;
                case "Instructions":
                    inInstructions = true;
                    for(Button button: buttons){
                        remove(button);
                    }
                    for(Button button: instructButtons){
                        add(button);
                    }
                    currentPage = 0;
                    break;
                case "Back":
                    inInstructions = false;
                    for(Button button: buttons){
                        add(button);
                    }
                    for(Button button: instructButtons){
                        remove(button);
                    }
                    break;
                case "Page Up":
                    if(currentPage < 2){
                        currentPage++;
                    }
                    break;
                case "Page Down":
                    if(currentPage > 0){
                        currentPage--;
                    }
                    break;
            }
        }
    }
}
