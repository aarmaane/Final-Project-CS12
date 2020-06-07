// FadeEffect.java
// Armaan Randhawa and Shivan Gaur
// Class creates a fade effect using transparent black rectangles
import java.awt.*;

public class FadeEffect {
    // Constants
    public static final int FADEIN = 0, FADEOUT = 1;
    // Fields
    private int fadeInt = 0, fadeSpeed = 5, activeType;
    private boolean active;

    // Method begins the fade by preparing the fields for the update method
    public void start(int type, int speed){
        active = true;
        activeType = type;
        if(type == FADEIN){
            fadeSpeed = -speed;
            fadeInt = 255;
        }
        else if(type == FADEOUT){
            fadeSpeed = speed;
            fadeInt = 0;
        }
    }

    // Method continues the fade by updating the alpha value
    public void update(){
        if(active){
            fadeInt += fadeSpeed;
            // Checking if the fade is done
            if((activeType == FADEOUT && fadeInt > 255) || (activeType == FADEIN && fadeInt < 0)) {
                active = false;
            }
        }
    }

    // Method draws the transparent rectangle
    public void draw(Graphics g){
        g.setColor(new Color(0, 0, 0, fadeInt));
        g.fillRect(0, 0,960,590);
    }
    // Getter methods
    public boolean isActive() {
        return active;
    }

    public boolean isDoneFadeIn() {
        return !active && activeType == FADEIN;
    }

    public boolean isDoneFadeOut() {
        return !active && activeType == FADEOUT;
    }

    public int getType() {
        return activeType;
    }
}
