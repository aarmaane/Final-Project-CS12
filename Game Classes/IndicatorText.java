// IndicatorText.java
// Armaan Randhawa and Shivan Gaur
// Creates the indicator text that appears for things like damage infliction and important messages during game play.
import java.awt.*;

public class IndicatorText {
    //Fields
    private int x, y;
    private String text;
    private Color color;

    // Constructor
    public IndicatorText(int x, int y, String text, Color color){
        this.x = x;
        this.y = y;
        this.text = text;
        this.color = color;
    }

    // General methods
    // Method to update the position and transparency of the text
    public void update(){
        y -= 1; // Making the text float up
        // Making the text more transparent
        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() - 2);
    }

    // Getter methods
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public String getString(){
        return text;
    }
    public Color getColor(){
        return color;
    }
    public boolean isDone() {
        return color.getAlpha() < 2;
    }
}
