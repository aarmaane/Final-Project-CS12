// Button.java
// Armaan Randhawa and Shivan Gaur
// Custom button class that extends JButton and automates mostfunctionality
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Button extends JButton {
    private static Font gameFont, gameFontThin;
    // Button fields
    private Rectangle buttonRect; // Rectangle where the button can be clicked
    private String text;
    private String[] tooltips; // Information about the button
    private Font buttonFont;
    private boolean hovered;
    private Color color;

    //Initialization
    public static void init(){
        // Font loaded
        try{
            gameFont = Font.createFont(Font.TRUETYPE_FONT, new File("Assets/Fonts/8BitFont.ttf"));
            gameFontThin = gameFont.deriveFont(30f);
        }
        catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }
    // Constructor
    public Button(Rectangle buttonRect, String text, int fontSize){
        // Setting up fields
        super();
        this.buttonRect = buttonRect;
        this.text = text;
        buttonFont = gameFont.deriveFont((float)fontSize);
        // Setting up JButton properties
        setActionCommand(text);
        setBounds(buttonRect);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
    }

    // Method to draw the JButton text
    public void draw(Graphics g){
        g.setColor(color);
        g.setFont(buttonFont);
        // Drawing the text in the center of the rectangle
        int width = g.getFontMetrics().stringWidth(text);
        int height = g.getFontMetrics().getHeight();
        g.drawString(text, buttonRect.x + (buttonRect.width - width)/2, buttonRect.y + (buttonRect.height - height)/2 + 25);
    }

    // Method to draw the tooltip of the button
    public void drawTooltip(Graphics g, int x, int y){
        g.setFont(gameFontThin);
        g.setColor(Color.GREEN);
        g.drawString("Price: " + tooltips[0], x + 10, y + 20);
        g.setColor(Color.WHITE);
        // Splitting up the text and drawing it on new lines so all of it fits on screen
        String[] wordSplit = tooltips[1].split(" ");
        String line = "";
        int lineCount = 0;
        // Going through each word and fitting as many per line
        for(String word: wordSplit){
            if(g.getFontMetrics().stringWidth(line + word) > 250){
                lineCount++;
                g.drawString(line, x + 10, y + 40 + lineCount*20);
                line = "";
            }
            line += word + " ";
        }
        g.drawString(line, x + 10, y + 40 + (lineCount+1)*20);
    }

    // Method to update text color based on hover status
    public void updateHover(Point point){
        hovered = false;
        ButtonModel model = getModel();
        if(model.isPressed()){
            // White if the button is pressed down
            color = Color.WHITE;
        }
        else if(buttonRect.contains(point)){
            // Red if it's being hovered over
            hovered = true;
            color = Color.RED;
        }
        else{
            // Black otherwise
            color = Color.BLACK;
        }
    }

    // Method to load the button with the tooltip
    public void addTooltip(String text){
        tooltips = text.split(",");
    }

    // Getter methods

    public boolean isHovered() {
        return hovered;
    }

    public boolean hasTooltip(){
        return tooltips != null;
    }
}
