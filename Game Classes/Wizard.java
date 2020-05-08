import java.awt.*;

public class Wizard extends Enemy {
    // Fields
    // Image Arrays for Sprites
    private static Image[] castSprites = new Image[8];
    private static Image[] dyingSprites;
    // Class Initialization
    public static void init(){
        castSprites = Utilities.spriteArrayLoad(castSprites, "Enemies/Wizard/cast1-");
    }
    //Constructor
    public Wizard(String data){
        super(data);
        health = 300 * difficulty;
        maxHealth = health;
        damage = 35;
        isActive = true;

    }
    // General methods
   // @Override
    /*
    public void updateMotion(Player player){
        // Doing nothing since Wizards don't move
    }

     */
    @Override
    public void updateAttack(Player player) {

    }

    @Override
    public void updateSprite() {
        spriteCount += 0.05;
        if(spriteCount > castSprites.length){
            spriteCount = 0;
        }
    }

    @Override
    public void checkCollision(LevelProp prop) {

    }

    // Getter methods
    @Override
    public Image getSprite() {
        Image sprite;
        int spriteIndex = (int)Math.floor(spriteCount);
        return castSprites[spriteIndex];

    }

    @Override
    public Rectangle getHitbox() {
        return new Rectangle((int)x, (int)y, 1, 1);
    }
}
