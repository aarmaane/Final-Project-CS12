// Blob.java
// Armaan Randhawa and Shivan Gaur
// Subclass of the Enemy class: creates the elite minion object of the final boss.
import java.awt.*;

public class Blob extends Enemy  {
    //Sprite arrays
    private Image[] movingSprites = new Image[8];
    private Image[] idleSprites= new Image[6];
    private Image[] attackSprites= new Image[8];
    private Image[] deathSprites= new Image[8];
    //Fields
    private double growth = 1;
    public Blob(String data) {
        // Constructor
        super(data);
        health = 500 * difficulty;
        maxHealth = health;
        damage = 15 * difficulty;
        //Initialing sprite arrays
        movingSprites = Utilities.spriteArrayLoad(movingSprites, "Enemies/Blob/walk");
        deathSprites = Utilities.spriteArrayLoad(deathSprites, "Enemies/Blob/death");
        idleSprites = Utilities.spriteArrayLoad(idleSprites, "Enemies/Blob/idle");
        attackSprites = Utilities.spriteArrayLoad(attackSprites, "Enemies/Blob/attack");

    }
    @Override
    public void update(Player player){
        //This method updates all the changing properties of the enemy
        super.update(player);
        updateSize(player);

    }
    public void updateSize(Player player){
        int originalHeight = getHitbox().height;
        int originalWidth = getHitbox().width;
        // Calculating the growth depending on health lost
        double healthLeft = (double)health / maxHealth;
        if(isDying()){         // Deflating the blob in it's dying
            if(growth > 1){
                growth -= 0.02;
            }
        }
        else{
            growth = 1 + (5 * (1 - healthLeft));
        }
        // Adjusting y position based on new growth
        y -= (getHitbox().height - originalHeight)*2;
        x -= getHitbox().width - originalWidth;

    }
    @Override
    public void updateMotion(Player player){
        // Checking the position of the Player and setting velocity towards them
        int playerX = player.getHitbox().x;
        int blobX = (int)(x + (29 * growth)); // Using the x from the right facing hitbox
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
        super.updateMotion(player); //calling enemy class method
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
                spriteCount += 0.04;
                if(spriteCount > deathSprites.length){//deactivating the blob
                    isActive = false;
                }
            }
            else{ //If the blob is hurt, the spriteCount acts as a timer to deactivate the flag
                spriteCount += 0.08;
                if(spriteCount > 1){ // Hard-coded value to decrease stun time
                    spriteCount = 0;
                    isHurt = false;
                }
            }
        }
        else if(isAttacking){ //If the blob is attacking then restart the animation once it cycles through
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
        }
    }

    @Override
    public Image getSprite() {
        //This method returns the appropriate sprite based on the blob's situation
        Image sprite;
        int spriteIndex = (int)Math.floor(spriteCount);
        if(isHurt){
            if(health <= 0){
                sprite = deathSprites[spriteIndex];
            }
            else{
                sprite = idleSprites[0];
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
        // Applying scale depending on blobs growth amount
        sprite = Utilities.scaleSprite(sprite, growth);
        return sprite;
    }

    @Override
    public Rectangle getHitbox() {
        // Getting the hitbox of the blob with the growth scaling applied to the dimensions
        if(direction == RIGHT){
            // Offset for right-facing sprites is applied in x dimension
            return new Rectangle((int)(x + (29 * growth)), (int)(y + (45 * growth)), (int)(27 * growth), (int)(35 * growth));
        }
        return new Rectangle((int)(x + (25 * growth)), (int)(y + (45 * growth)), (int)(27 * growth), (int)(35 * growth));
    }

    @Override
    public double getX(){
        // Offset for the dying sprite
        if(isDying()){
            return x - 5*growth;
        }
        return x;
    }

    @Override
    public double getY(){
        // Offset for the dying sprites
        if(isDying()){
            return y + 6*growth;
        }
        return y;
    }
}
