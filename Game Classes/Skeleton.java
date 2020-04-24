import java.awt.*;

public class Skeleton extends Enemy {
    // Sprites
    private static Image[] idleSprites = new Image[11];
    // Class initialization
    public static void init(){
        idleSprites = Utilities.spriteArrayLoad(idleSprites, "Enemies/Skeleton/idle");
    }
    // Constructor
    public Skeleton(String data){
        String[] dataSplit = data.split(",");
        x = Integer.parseInt(dataSplit[0]);
        y = Integer.parseInt(dataSplit[1]);
        difficulty = Integer.parseInt(dataSplit[2]);
        health = 200 * difficulty;
        maxHealth = health;
        damage = 20;
        isActive = true;
    }
    // General methods
    @Override
    public void update(Player player){
        updateMotion();
    }
    public void updateMotion(){
        // Applying velocity values to position
        x += velocityX;
        y += velocityY;
        // Adding gravity value
        velocityY += GRAVITY;
        // Resetting boolean values so they can be rechecked for the new position
        platformAhead = false;
        platformBehind = false;
    }
    // Getter methods
    @Override
    public Image getSprite() {
        return idleSprites[0];
    }

    @Override
    public Rectangle getHitbox() {
        return new Rectangle((int) x,(int) y,72,96);
    }
}
