//Boss.java
//Armaan randhawa and Shivan Gaur
//This program is a subclass of the Enemy class, and it creates an object that serves as the final boss of the game.
import java.awt.*;
import java.awt.image.BufferedImage;

public class Boss extends Enemy{
    // Fields
    // Images
    private static Image bossHealthBar;
    private static Image[] idleSprites = new Image[5];
    // Initialization of Class
    public static void init(){
        idleSprites = Utilities.spriteArrayLoad(idleSprites, "Enemies/Boss/idle");
    }

    public Boss(String data) {
        // Constructor
        super(data);
        health = 5000;
        damage = 250;
    }
    // General methods
    @Override
    public void drawHealth(Graphics g, int levelOffset){
        g.drawString("BOSS HEALTH: " + health, 500, 400);
    }
    @Override
    public void updateSprite() {
        spriteCount += 0.05;
        if(spriteCount > idleSprites.length){
            spriteCount = 0;
        }
    }
    // Getter methods
    @Override
    public Image getSprite() {
        return idleSprites[(int) Math.floor(spriteCount)];
    }
    @Override
    public Rectangle getHitbox() {
        return new Rectangle((int)x + 110, (int)y + 90, 150, 165);
    }
}
