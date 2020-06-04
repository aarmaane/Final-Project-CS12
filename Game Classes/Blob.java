// Blob.java
// Armaan Randhawa and Shivan Gaur
// Subclass of the Enemy class: creates the elite minion object of the final boss.
import java.awt.*;

public class Blob extends Enemy  {
    //Sprite arrays
    private  Image[] movingSprites = new Image[8];
    private  Image[] idleSprites= new Image[6];
    private  Image[] attackSprites= new Image[8];
    private  Image[] deathSprites= new Image[7];
    private  Image[] hurtSprites= new Image[1];
    //Fields
    private double growthFactor;
    private double growthVal = 1.0;
    private double growthChange;
    private boolean grown;
    public Blob(String data) {
        //Constructor
        super(data);
        health = 500 * difficulty;
        maxHealth = health;
        damage = 15 * difficulty;
        blobInit();
    }
    public void blobInit(){
        //Initialing sprite arrays
        movingSprites = Utilities.spriteArrayLoad(movingSprites, "Enemies/Blob/walk");
        //deathSprites = Utilities.spriteArrayLoad(deathSprites, "Enemies/Ghost/death");
        idleSprites = Utilities.spriteArrayLoad(idleSprites, "Enemies/Blob/idle");
        attackSprites = Utilities.spriteArrayLoad(attackSprites, "Enemies/Blob/attack");
        //hurtSprites = Utilities.spriteArrayLoad(hurtSprites, "Enemies/Ghost/hurt");
    }
    @Override
    public void update(Player player){
        //This method updates all the changing properties of the enemy
        updateMotion(player);
        if(!isHurt){
            updateAttack(player);
        }
        updateSize(player);
        updateSprite();

    }
    public void updateSize(Player player){
        if(isHurt && growthChange <0.2 && !grown){
            growthVal+=0.1;
            growthChange+=0.1;
            if(growthChange == 0.2){
                grown = true;
            }
            growthFactor= 1+(.2*(1.0/growthVal));
            growSprites();
        }

    }
    public void growSprites(){
        for(int i =0;i<movingSprites.length;i++){
            movingSprites[i] = Utilities.scaleSprite(movingSprites[i],growthFactor);
            attackSprites[i] = Utilities.scaleSprite(attackSprites[i],growthFactor);
        }
        for(int i =0;i<idleSprites.length;i++){
            idleSprites[i] = Utilities.scaleSprite(idleSprites[i],growthFactor);
        }
        System.out.println(growthVal);
        growthOffsetX=80*(growthVal-.2);
        growthOffsetY=80*(growthVal-.2);
    }
    @Override
    public void updateMotion(Player player){
        // Checking the position of the Player and setting velocity towards them
        int playerX = player.getHitbox().x;
        int blobX = getHitbox().x;
        if(!knockedBack){ // Not touching blob's velocity values if there's knockback
            if(playerX == blobX || isHurt){
                velocityX = 0; // Stopping movement while maintaining direction
            }
            else if(playerX > blobX){
                direction = RIGHT;
                if(platformAhead && !isAttacking){
                    velocityX = 0.5;
                }
                else{
                    velocityX = 0; // Making the blob stay in place
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
        super.updateMotion(player);//calling enemy class method
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
        //This method updates the sprite of the blob based on the action that is happening
        if(isHurt){
            //If the blob is hurt we need to check for whether the attack was fatal enough for death
            if(health <= 0){
                spriteCount += 0.05;
                if(spriteCount > deathSprites.length){//deactivating the blob
                    isActive = false;
                }
            }
            else{//If the blob was not killed then it was simply hurt so the hurt sprite count is reset once it is cycled through
                spriteCount += 0.08;
                if(spriteCount > hurtSprites.length){
                    spriteCount = 0;
                    isHurt = false;
                }
            }
        }
        else if(isAttacking){//If the blob is attacking then restart the animation once it cycles through
            spriteCount += 0.1;
            if(spriteCount > attackSprites.length){
                spriteCount = 0;
                isAttacking = false;
            }
        }
        else{//Idle sprites
            spriteCount += 0.07;
            if(spriteCount > idleSprites.length){
                spriteCount = 0;
            }
            growthChange =0;
            grown = false;
        }
    }

    @Override
    public Image getSprite() {
        //This method returns the appropriate sprite based on the blob's situation
        Image sprite;
        int spriteIndex = (int)Math.floor(spriteCount);
        if(isHurt){
            if(health <= 0){
                sprite = idleSprites[spriteIndex];
            }
            else{
                sprite = idleSprites[spriteIndex];
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
        //This method returns a rectangle object that is the hitbox of the blob
        return new Rectangle((int)(x+23), (int)(y+35), (int)(40*growthVal), (int)(45*growthVal));
    }
}
