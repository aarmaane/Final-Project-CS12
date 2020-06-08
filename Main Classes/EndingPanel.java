import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EndingPanel extends JPanel {
    private MainGame gameFrame;
    private GamePanel game;
    private Player player;
    private FadeEffect fade = new FadeEffect();
    // Background related fields (Same as main menu panel)
    private Background background = new Background("BG0.png,BG1.png,BG2.png", "0,0.3,0.5");
    private Player dummy = new Player();
    private LevelProp platform = new LevelProp("0,0,grassMiddle.png", false, false);
    private int scrollOffset = 0;
    private int screenWidth, platformsX1, platformsX2;
    // Stats related fields
    private Font gameFont, gameFontBig, gameFontGiant, gameFontMark;
    private int frameNum, pointsCounter;
    private double deathCounter;
    private Color markColor;
    private String mark;
    // Button related fields
    private boolean buttonAdded;
    private Button exitButton;
    // Music
    private Sound endingMusic = new Sound("Assets/Sounds/Music/gameEnd.wav", 85);
    // Constructor
    public EndingPanel(MainGame frame){
        gameFrame = frame;
        game = frame.getGame();
        gameFont = game.getGameFont();
        gameFontBig = gameFont.deriveFont(60f);
        gameFontGiant = gameFont.deriveFont(70f);
        gameFontMark = gameFont.deriveFont(150f);
        player = game.getPlayer();
        setSize(960,590);
        setLayout(null);
        // Setting up background animation
        screenWidth = 960;
        dummy.resetPos(0,366);
    }
    // Main methods
    // Method that draws the panel
    public void paintComponent(Graphics g){
        background.draw(g); // Drawing the background
        g.drawImage(dummy.getSprite(),(int) dummy.getX() - 170 - scrollOffset,367 ,this); // Drawing the dummy player
        // Drawing the repeating platforms
        for(int i = 0; i < 10; i++){
            g.drawImage(platform.getPropImage(), platformsX1 + i*144 - scrollOffset,475, this);
            g.drawImage(platform.getPropImage(), platformsX2 + i*144 - scrollOffset,475, this);
        }
        // Drawing exit button
        if(exitButton != null){
            exitButton.draw(g);
            g.drawRect(getWidth()/2 - 100, getHeight() - 100, 200, 50);
        }
        drawStats(g);
        fade.draw(g);
    }

    public void drawStats(Graphics g){
        g.setFont(gameFontBig);
        if(frameNum % 100 < 50){
            g.setColor(Color.CYAN);
        }
        else{
            g.setColor(Color.BLACK);
        }
        g.drawString("Quest Complete!", getWidth()/2 - 207, 40);
        g.setFont(gameFont);
        if(frameNum > 200){
            g.setColor(Color.GREEN);
            String drawnString = "Final Points: " + pointsCounter;
            int stringWidth = g.getFontMetrics().stringWidth(drawnString);
            g.drawString(drawnString, getWidth()/2 - stringWidth/2, 80);
        }
        if(frameNum > 400){
            // Deaths
            g.setColor(Color.RED);
            String drawnString = "Total Deaths: " + (int)Math.floor(deathCounter);
            int stringWidth = g.getFontMetrics().stringWidth(drawnString);
            g.drawString(drawnString, getWidth()/2 - stringWidth/2, 120);
            // Mark header
            g.setFont(gameFontGiant);
            g.setColor(Color.BLACK);
            g.drawString("Mark:", getWidth()/2 - 79, 300);
            // Mark text
            g.setFont(gameFontMark);
            g.setColor(markColor);
            g.drawString(mark, getWidth()/2 - 37, 380);
        }
    }
    // Method to update the general graphics and sound of the panel
    public void update(){
        // Making sure that the music is always looping
        if(Sound.isMuted()){ // Unmuting sound
            Sound.toggleVolume();
        }
        if(!endingMusic.isPlaying()){
            endingMusic.play();
        }
        // Checking if the fade is done and updating it
        fade.update();
        if(fade.isDoneFadeOut()){
            gameFrame.switchPanel(MainGame.MENUPANEL);
            endingMusic.stop();
            game.resetGame();
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
        // Updating button
        if(exitButton != null){
            Point mousePos = getMousePosition();
            if(mousePos != null){
                exitButton.updateHover(mousePos);
            }
        }
    }

    // Method to update the stats display
    public void updateStats() {
        frameNum++;
        if (frameNum > 200) {
            if (pointsCounter < player.getPoints()) {
                for(int i = 0; i < player.getPoints()/1000.0; i++){
                    pointsCounter++;
                }
            }
            else{
                pointsCounter = player.getPoints();
            }
        }
        if (frameNum > 400) {
            if(deathCounter < player.getDeaths()){
                deathCounter += player.getDeaths()/600.0;
            }
            else {
                deathCounter = player.getDeaths();
            }
        }
        if(frameNum > 400){
            double pointsPerDeath = pointsCounter/(deathCounter + 1);
            if(pointsPerDeath >= 10000){
                mark = "S";
                markColor = Color.YELLOW;
            }
            else if(pointsPerDeath >= 8000){
                mark = "A";
                markColor = Color.GREEN;
            }
            else if(pointsPerDeath >= 5000){
                mark = "B";
                markColor = Color.BLUE;
            }
            else if(pointsPerDeath >= 3000){
                mark = "C";
                markColor = Color.ORANGE;
            }
            else if(pointsPerDeath >= 1000){
                mark = "D";
                markColor = new Color(150,75,0);
            }
            else{
                mark = "F";
                markColor = Color.RED;
            }
        }
        if(pointsCounter == player.getPoints() && Math.floor(deathCounter) == player.getDeaths() && !buttonAdded){
            buttonAdded = true;
            exitButton = new Button(new Rectangle(getWidth()/2 - 100, getHeight() - 100, 200, 50), "Return to Menu", 35);
            add(exitButton);
            exitButton.setActionCommand("Exit");
            exitButton.addActionListener(new ButtonListener());
        }
    }
    public class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Exit")){
                fade.start(FadeEffect.FADEOUT, 1);
            }
        }
    }
}
