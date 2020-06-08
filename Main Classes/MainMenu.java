// MainMenu.java
// Armaan Randhawa and Shivan Gaur
// JPanel that holds the main menu. Shows instructions and allows the user to start the game
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainMenu extends JPanel {
    // Window related Objects
    private MainGame gameFrame;
    private GamePanel game;
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
    // Instructions page fields
    private boolean inInstructions;
    private Image[] instructPages = new Image[3]; // Instructions images
    private int currentPage = 0; // Current instructions page open

    // Constructor
    public MainMenu(MainGame game){
        // Setting up frame
        this.gameFrame = game;
        this.game =  gameFrame.getGame();
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
        instructPages = Utilities.spriteArrayLoad(instructPages, "Main Menu/page");
    }
    // Window related methods
    // Method that draws everything
    public void paintComponent(Graphics g){
        background.draw(g); // Drawing the background
        g.drawImage(dummy.getSprite(),(int) dummy.getX()- 170 - scrollOffset,367 ,this); // Drawing the dummy player
        // Drawing the repeating platforms
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
        // Drawing the fade (when the player clicks play)
        fade.draw(g);
    }

    // Method that draws the instructions page
    public void drawInstructions(Graphics g){
        // Drawing instructions buttons
        for(Button button: instructButtons){
            button.draw(g);
        }
        // Drawing the current instructions image
        g.drawImage(instructPages[currentPage], getWidth()/2 - 425, 50, this);
        // Drawing page number (centered on the screen)
        g.setColor(Color.BLACK);
        String drawnString = "Page " + (currentPage + 1) + "/3";
        int stringLength = g.getFontMetrics().stringWidth(drawnString);
        g.drawString(drawnString, getWidth()/2 - stringLength/2, 25);
    }

    // Method that updates all elements of the main menu
    public void update(){
        // Making sure that the music is always looping
        if(!menuMusic.isPlaying()){
            menuMusic.play();
        }
        // Updating the fade effect
        fade.update();
        if(fade.isDoneFadeOut()){ // Once the fade out is done, switch to the game
            menuMusic.stop();
            menuMusic.closeSound();
            game.setLevelNum(1);
            gameFrame.switchPanel(MainGame.TRANSITIONPANEL);
            fade = new FadeEffect(); // Resetting fade for next time
        }
        // Updating the dummy player
        dummy.move(Player.RIGHT);
        dummy.checkCollision(new LevelProp(scrollOffset+",800,invisibleRect.png", false, false));
        dummy.update(false);
        // Updating the background
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

    // Method to update the button colors
    public void checkButtons(){
        if(fade.isActive()){
            return; // Don't update buttons during the fade
        }
        Point mouse = getMousePosition();
        if(mouse != null){
            if(!inInstructions){
                // Updating main menu buttons
                for(Button button: buttons){
                    button.updateHover(mouse);
                }
            }
            else{
                // Updating instructions buttons
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
                    // Starting the fade-out
                    if(!fade.isActive()){
                        fade.start(FadeEffect.FADEOUT, 1);
                    }
                    break;
                case "Quit":
                    // Closing the game
                    System.exit(0);
                    break;
                case "Instructions":
                    // Removing main menu elements and adding instructions
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
                    // Re-adding main menu elements and removing instructions
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
