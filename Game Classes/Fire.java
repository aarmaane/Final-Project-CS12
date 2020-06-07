// Fire.java
// Armaan Randhawa and Shivan Gaur
// Subclass of the Enemy class and creates fire enemies which do not move but inflict damage when the player collides with them.

import java.awt.*;

public class Fire extends Enemy {
    // Fields
    private int attackDelay = 50; // Field to keep track of delay before fire deals damage
    // Sprites
    private static Image[] motionSprites = new Image[7];

    // Class initalization
    public static void init(){
        // Loading sprites using Utilities spriteLoad method
        motionSprites = Utilities.spriteArrayLoad(motionSprites, "Enemies/Fire/fire");
    }

    // Constructor
    public Fire(String data) {
        super(data);
        health = 100;
        maxHealth=health;
        damage = 15 * difficulty;
    }

    @Override
    public void updateSprite() {
        spriteCount+=0.10;
        if(spriteCount > motionSprites.length){
            if(health <=0){ // Deactivates the fire object
                isActive = false;
            }
            else{ //Resets the sprite cycle
                spriteCount = 0;
            }
        }
    }

    @Override
    public void checkCollision(LevelProp prop){
        // Fire has no motion, do nothing
    }

    @Override
    public void updateMotion(Player player){
        // Fire has no motion, do nothing
    }

    @Override
    public void updateAttack(Player player){
        isAttacking = getHitbox().intersects(player.getHitbox()); // Setting attacking flag to true if there is hitbox collision
        if(isAttacking){
            attackDelay--; // Iterating counter
            if(attackDelay == 0){
                player.enemyHit(this); //Inflicting damage
                attackDelay = 50; // Resetting counter
            }
        }
    }

    @Override
    public Image getSprite() {
        int spriteIndex = (int)Math.floor(spriteCount);
        return motionSprites[spriteIndex];
    }

    @Override
    public Rectangle getHitbox() {
        return new Rectangle((int)x + 70, (int)y + 40, 90, 200);
    }
}
