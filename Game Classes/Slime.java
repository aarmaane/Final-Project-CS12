import java.awt.*;

public class Slime extends Enemy {
    // Sprites
    private static Image[] movingSprites = new Image[4];
    private static Image[] attackSprites = new Image[5];
    private static Image[] hurtSprites = new Image[4];
    private static Image[] idleSprites = new Image[4];
    private static Image[] deathSprites = new Image[4];
    // Method to initialize the Class by loading sprites
    public static void init(){
        movingSprites = Utilities.spriteArrayLoad(movingSprites, "Enemies/Slime/move");
        attackSprites = Utilities.spriteArrayLoad(movingSprites, "Enemies/Slime/attack");
        hurtSprites = Utilities.spriteArrayLoad(hurtSprites, "Enemies/Slime/hurt");
        idleSprites = Utilities.spriteArrayLoad(idleSprites, "Enemies/Slime/idle");
        deathSprites = Utilities.spriteArrayLoad(idleSprites, "Enemies/Slime/death");
    }
    //Constructor
    public Slime(String data){
        super(data);
        health = 100 * difficulty;
        maxHealth=health;
        damage = 5;
    }
    // General methods
    @Override
    public void updateMotion(Player player){
        // Checking the position of the Player and setting velocity towards them
        int playerX = player.getHitbox().x;
        int slimeX = getHitbox().x;
        if(!knockedBack){ // Not touching Slime's velocity values if there's knockback
            if(playerX == slimeX || isHurt){
                velocityX = 0; // Stopping movement while maintaining direction
            }
            else if(playerX > slimeX){
                direction = RIGHT;
                if(platformAhead && !isAttacking){
                    velocityX = 0.5;
                }
                else{
                    velocityX = 0; // Making the slime stay in place
                }
            }
            else{ // Same as above but for left facing
                direction = LEFT;
                if(platformBehind && !isAttacking){
                    velocityX = -0.5;
                }
                else{
                    velocityX = 0;
                }
            }
        }
        super.updateMotion(player);
    }
    @Override
    public void updateAttack(Player player){
        super.updateAttack(player);
        // Checking if the player should be dealt damage
        if(isAttacking && Utilities.roundOff(spriteCount,2) == attackSprites.length/2.0){
            player.enemyHit(this);
        }
    }
    @Override
    public void updateSprite(){
        if(isHurt){
            if(health <= 0){
                spriteCount += 0.05;
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
            spriteCount += 0.05;
            if(spriteCount > attackSprites.length){
                spriteCount = 0;
            }
        }
        else{
            spriteCount += 0.05;
            if(spriteCount > movingSprites.length){
                spriteCount = 0;
            }
        }
    }
    // Getter methods
    @Override
    public Image getSprite() {
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
        if(direction == RIGHT){
            sprite = Utilities.flipSprite(sprite);
        }
        return sprite;
    }

    @Override
    public Rectangle getHitbox() {
        return new Rectangle((int)x + 10, (int)y + 25, 80, 45);
    }
}
