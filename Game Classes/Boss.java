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
    private static Image[] movingSprites = new Image[8];
    private static Image[] chargingSprites = new Image[5];
    private static Image[] attackSprites = new Image[9];
    private static Image[] spinSprites = new Image[9];
    private static Image[] deathSprites = new Image[6];
    // Initialization of Class
    public static void init(){
        idleSprites = Utilities.spriteArrayLoad(idleSprites, "Enemies/Boss/idle");
        movingSprites = Utilities.spriteArrayLoad(movingSprites, "Enemies/Boss/moving");
        chargingSprites = Utilities.spriteArrayLoad(chargingSprites, "Enemies/Boss/charging");
        attackSprites = Utilities.spriteArrayLoad(attackSprites, "Enemies/Boss/attack");
        spinSprites = Utilities.spriteArrayLoad(spinSprites, "Enemies/Boss/spin");
        deathSprites = Utilities.spriteArrayLoad(deathSprites, "Enemies/Boss/death");
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
    public void updateMotion(Player player) {
        velocityX = 0.01;
        super.updateMotion(player);
    }

    @Override
    public void updateSprite() {
        if(velocityX != 0){
            spriteCount += 0.05;
            if(spriteCount > movingSprites.length){
                spriteCount = 0;
            }
        }
        else{
            spriteCount += 0.05;
            if(spriteCount > idleSprites.length){
                spriteCount = 0;
            }
        }
    }
    // Getter methods
    @Override
    public Image getSprite() {
        Image sprite;
        int spriteIndex = (int)Math.floor(spriteCount);
        if(velocityX != 0){
            sprite = movingSprites[spriteIndex];
        }
        else{
            sprite = idleSprites[spriteIndex];
        }
        return sprite;
    }
    @Override
    public Rectangle getHitbox() {
        return new Rectangle((int)x + 110, (int)y + 90, 150, 165);
    }
}
