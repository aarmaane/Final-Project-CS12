//Skeleton.java
//Armaan Randhawa and Shivan Gaur
//This program is a subclass of the Enemy class and it creates skeleton enemies that walk back and forth on a platform
import java.awt.*;

public class Skeleton extends Enemy {
    // Sprite arrays
    private static Image[] movingSprites = new Image[13];
    private static Image[] attackSprites = new Image[18];
    private static Image[] hurtSprites = new Image[3];
    private static Image[] idleSprites = new Image[11];
    private static Image[] deathSprites = new Image[14];
    // Class initialization
    public static void init(){
        idleSprites = Utilities.spriteArrayLoad(idleSprites, "Enemies/Skeleton/idle");
        movingSprites = Utilities.spriteArrayLoad(movingSprites, "Enemies/Skeleton/walk");
        hurtSprites = Utilities.spriteArrayLoad(hurtSprites, "Enemies/Skeleton/hurt");
        deathSprites = Utilities.spriteArrayLoad(deathSprites, "Enemies/Skeleton/death");
        attackSprites = Utilities.spriteArrayLoad(attackSprites, "Enemies/Skeleton/attack");
    }
    // Constructor
    public Skeleton(String data){
        super(data);
        health = 200 * difficulty;
        maxHealth = health;
        damage = 20*difficulty;
    }
    // General methods
    @Override
    public void updateMotion(Player player){
        //Method that updates motion of the skeleton
        // Skeleton custom movement
        int playerX = player.getHitbox().x;
        int skeletonX = getHitbox().x;
        if(!knockedBack && !(velocityY > 1)){ // Only moving if there isn't knockback and if they aren't falling
            if(isHurt || isAttacking){
                velocityX = 0; // Stopping movement while maintaining direction
                if(isAttacking){
                    if(playerX > skeletonX){
                        direction = RIGHT;
                    }
                    else{
                        direction = LEFT;
                    }
                }
            }
            else if(direction==RIGHT){
                if(platformAhead){
                    velocityX = 0.75;
                }
                else{
                    direction=LEFT;
                    velocityX = -0.75; // Making the skeleton stay in place
                }
            }
            else if(direction==LEFT){ // Same as above but for left facing
                if(platformBehind){
                    velocityX = -0.75;
                }
                else{
                    direction=RIGHT;
                    velocityX = 0.75;
                }
            }
        }
        super.updateMotion(player);
    }

    @Override
    public void updateAttack(Player player) {
        //Method that determines when to inflict damage on the player
        super.updateAttack(player);
        // Checking if the player should be dealt damage
        if(isAttacking && Utilities.roundOff(spriteCount,2) == attackSprites.length/2.5){
            player.enemyHit(this);
        }
    }
    @Override
    public void updateSprite() {
        //Method restarting sprite cycles
        if(isHurt){
            if(health <= 0){
                spriteCount += 0.1;
                if(spriteCount > deathSprites.length){
                    isActive = false;
                }
            }
            else{
                spriteCount += 0.08;
                if(spriteCount > hurtSprites.length){
                    spriteCount = 0;
                    isHurt = false;
                }
            }
        }
        else if(isAttacking){
            spriteCount += 0.1;
            if(spriteCount > attackSprites.length){
                spriteCount = 0;
            }
        }
        else{
            spriteCount += 0.1;
            if(spriteCount > movingSprites.length){
                spriteCount = 0;
            }
        }
    }

    // Getter methods
    @Override
    public Image getSprite() {
        //Method that returns the proper sprite for the current situation of the skeleton
        Image sprite;
        int spriteIndex = (int)Math.floor(spriteCount);
        if(isHurt){
            if(health <= 0){
                sprite = deathSprites[spriteIndex];
            }
            else{
                sprite = hurtSprites[spriteIndex];
            }
        }
        else if(isAttacking){
            sprite = attackSprites[spriteIndex];
        }
        else if(velocityX != 0){
            sprite = movingSprites[spriteIndex];
        }
        else{
            sprite = idleSprites[spriteIndex];
        }
        // Flipping image since sprites are left facing
        if(direction == LEFT){
            sprite = Utilities.flipSprite(sprite);
        }
        return sprite;
    }

    @Override
    public Rectangle getHitbox() {
        //This method returns a rectangle object for the hitbox of the skeleton
        if(direction == LEFT){
            //There is a 20 pixel offset to the x position if the skeleton is facing left due to how the sprites were formatted
            return new Rectangle((int) x + 20,(int) y + 20,50,76);
        }
        return new Rectangle((int) x,(int) y + 20,50,76);//without offset
    }

    @Override
    public double getX(){
        // The attacking sprite is different from the rest, so an offset is applied
        if(isAttacking && direction==LEFT){
            return x - 50;
        }
        return x;
    }
    @Override
    public double getY(){
        // The attacking sprite is different from the rest, so an offset is applied
        if(isAttacking){
            return y-15;
        }
        return y;
    }
}
