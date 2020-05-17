import java.awt.*;

public class Fire extends Enemy {
    private static Image[] motionSprites = new Image[7];
    public static void init(){
        motionSprites = Utilities.spriteArrayLoad(motionSprites, "Enemies/Fire/fire");

    }
    public Fire(String data) {
        super(data);
        health = 100;
        maxHealth=health;
        damage = 15*difficulty;
    }

    @Override
    public void updateSprite() {
        spriteCount+=0.10;
        if(spriteCount > motionSprites.length){
            spriteCount = 0;
        }
    }
    public void updateMotion(Player player){
        // Applying velocity values to position
        x += velocityX;
        y += velocityY;
        // Adding gravity value
        //velocityY += GRAVITY;
        // Resetting boolean values so they can be rechecked for the new position
        platformAhead = false;
        platformBehind = false;
        onMovingPlat = false;
    }
    @Override
    public Image getSprite() {
        Image sprite;
        int spriteIndex = (int)Math.floor(spriteCount);
        sprite = motionSprites[spriteIndex];
        return sprite;
    }

    @Override
    public Rectangle getHitbox() {
        return new Rectangle((int)x + 10, (int)y + 25, 80, 45);
    }


}
