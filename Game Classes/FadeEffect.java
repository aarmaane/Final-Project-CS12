import java.awt.*;

public class FadeEffect {
    // Constants
    public static final int FADEIN = 0, FADEOUT = 1;
    // Fields
    private int fadeInt = 0, fadeSpeed = 5, activeType;
    private boolean active;
    // Main methods
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

    public void update(){
        if(active){
            fadeInt += fadeSpeed;
            if((activeType == FADEOUT && fadeInt > 255) || (activeType == FADEIN && fadeInt < 0)) {
                active = false;
            }
        }
    }

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
