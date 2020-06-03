//Fire.java
//Armaan Randhawa and Shivan Gaur
//This program is a subclass of the Enemy class and creates fire enemies which do not move but inflict damage when the player collides with them.

import java.awt.*;

public class Fire extends Enemy {
    // Fields
    private int attackDelay = 50;
    // Sprites
    private static Image[] motionSprites = new Image[7];
    public static void init(){
        //This method initializes the class
        motionSprites = Utilities.spriteArrayLoad(motionSprites, "Enemies/Fire/fire");

    }
    public Fire(String data) {
        //Constructor
        super(data);
        health = 100;
        maxHealth=health;
        damage = 15 * difficulty;
    }

    @Override
    public void updateSprite() {
        //This method updates the sprite
        spriteCount+=0.10;
        if(spriteCount > motionSprites.length){
            if(health <=0){//Deactivates the fire object
                isActive = false;
            }
            else{//Resets the sprite cycle
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
        //Updates attack
        isAttacking = getHitbox().intersects(player.getHitbox()); // Setting it to true if there is hitbox collision
        if(isAttacking){
            attackDelay--;
            if(attackDelay == 0){
                player.enemyHit(this);//Inflicting damage
                attackDelay = 50;
            }
        }
    }
    @Override
    public Image getSprite() {
        //Returns the motion sprite at the proper index
        Image sprite;
        int spriteIndex = (int)Math.floor(spriteCount);
        sprite = motionSprites[spriteIndex];
        return sprite;
    }

    @Override
    //Returns rectangle object with the hibox of the fire
    public Rectangle getHitbox() {
        return new Rectangle((int)x + 70, (int)y + 40, 90, 200);
    }


}
