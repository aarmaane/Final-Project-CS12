import java.awt.*;

public class Skeleton extends Enemy {
    // Sprites
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
        deathSprites = Utilities.spriteArrayLoad(movingSprites, "Enemies/Skeleton/death");
        attackSprites = Utilities.spriteArrayLoad(hurtSprites, "Enemies/Skeleton/attack");
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
        int playerX = player.getHitbox().x;
        int slimeX = getHitbox().x;
        if(knockedBack){
            // Not touching Slime's velocity values
        }

        else if(playerX == slimeX || isHurt){
            velocityX = 0; // Stopping movement while maintaining direction
        }

        else if(direction==RIGHT){
            //direction = RIGHT;
            if(platformAhead && !isAttacking){
                velocityX = 0.75;
            }
            else{
                direction=LEFT;
                velocityX = -0.75; // Making the slime stay in place
            }
        }


        else if(direction==LEFT){ // Same as above but for left facing
           // direction = LEFT;
            if(platformBehind && !isAttacking){
                velocityX = -0.75;
            }
            else{
                direction=RIGHT;
                velocityX = 0.75;
            }
        }


        super.updateMotion(player);
    }

    @Override
    public void updateAttack(Player player) {

        // Updating the attacking status
        boolean originalState = isAttacking;
        isAttacking = getHitbox().intersects(player.getHitbox()); // Setting it to true if there is hitbox collision
        if(originalState != isAttacking){ // If there's a change in state, reset the sprite counter
            spriteCount = 0;
        }
        // Checking if the player should be dealt damage
        if(isAttacking && Utilities.roundOff(spriteCount,2) == attackSprites.length/2.0){
            player.enemyHit(this);
            System.out.println(9204);
        }


    }

    @Override
    public void updateSprite() {
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
            spriteCount += 0.08;
            if(spriteCount > attackSprites.length){
                spriteCount = 0;
            }
        }
        else{
            spriteCount += 0.08;
            if(spriteCount > movingSprites.length){
                spriteCount = 0;
            }
        }
    }

    // Getter methods
    @Override
    public Image getSprite() {

        Image sprite = null;
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
            //sprite = idleSprites[spriteIndex];
        }
        // Flipping image since sprites are left facing
        if(direction == LEFT){
            sprite = Utilities.flipSprite(sprite);
        }
        return sprite;
    }

    @Override
    public Rectangle getHitbox() {
        return new Rectangle((int) x,(int) y,72,96);
    }
}
