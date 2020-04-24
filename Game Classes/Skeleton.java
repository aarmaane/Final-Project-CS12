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
        super(data);
        health = 200 * difficulty;
        maxHealth = health;
        damage = 20;
        isActive = true;
    }
    // General methods
    @Override
    public void updateMotion(Player player){
        // Skeleton custom movement
        super.updateMotion(player);
    }

    @Override
    public void updateAttack(Player player) {

    }

    @Override
    public void updateSprite() {

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
