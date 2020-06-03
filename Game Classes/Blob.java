import java.awt.*;

public class Blob extends Enemy  {
    private static Image[] movingSprites = new Image[8];
    private static Image[] idleSprites= new Image[6];
    private static Image[] attackSprites= new Image[8];
    private static Image[] deathSprites= new Image[7];
    private static Image[] hurtSprites= new Image[1];
    public Blob(String data) {
        super(data);
        health = 500 * difficulty;
        maxHealth = health;
        damage = 15 * difficulty;
    }


    public static void init(){
        movingSprites = Utilities.spriteArrayLoad(movingSprites, "Enemies/Blob/walk");
        //deathSprites = Utilities.spriteArrayLoad(deathSprites, "Enemies/Ghost/death");
        idleSprites = Utilities.spriteArrayLoad(idleSprites, "Enemies/Blob/idle");
        attackSprites = Utilities.spriteArrayLoad(attackSprites, "Enemies/Blob/attack");
        //hurtSprites = Utilities.spriteArrayLoad(hurtSprites, "Enemies/Ghost/hurt");
    }
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
            spriteCount += 0.1;
            if(spriteCount > attackSprites.length){
                spriteCount = 0;
                isAttacking = false;
            }
        }
        else{
            spriteCount += 0.07;
            if(spriteCount > idleSprites.length){
                spriteCount = 0;
            }
        }
    }

    @Override
    public Image getSprite() {
        Image sprite = null;
        int spriteIndex = (int)Math.floor(spriteCount);
        if(isHurt){
            if(health <= 0){
                //sprite = deathSprites[spriteIndex];
            }
            else{
                //sprite = hurtSprites[spriteIndex];
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
