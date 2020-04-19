import java.awt.*;

public class Slime extends Enemy {
    // Sprites
    private static Image[] movingSprites = new Image[4];
    private static Image[] attackSprites = new Image[5];
    private static Image[] hurtSprites = new Image[4];
    private static Image[] idleSprites = new Image[4];
    private static Image[] deathSprites = new Image[4];
    //Fields
    private boolean platformAhead;
    private boolean platformBehind;
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
        String[] dataSplit = data.split(",");
        x = Integer.parseInt(dataSplit[0]);
        y = Integer.parseInt(dataSplit[1]);
        difficulty = Integer.parseInt(dataSplit[2]);
        health = 100 * difficulty;
        maxHealth=health;
        damage = 5;
        isActive = true;
    }
    // General methods
    @Override
    public void update(Player player){
        updateMotion(player);
        updateAttack(player);
        updateSprite();
    }
    public void updateMotion(Player player){
        // Checking the position of the Player and setting velocity towards them
        int playerX = player.getHitbox().x;
        int slimeX = getHitbox().x;
        if(knockedBack){
        }
        else if(isHurt){
            velocityX = 0;
        }
        else if(playerX > slimeX && platformAhead){
            direction = RIGHT;
            velocityX = 0.5;

        }
        else if(playerX < slimeX && platformBehind){
            direction = LEFT;
            velocityX = -0.5;
        }
        else{
            velocityX = 0;
        }
        // Applying velocity values to position
        x += velocityX;
        y += velocityY;
        // Adding gravity value
        velocityY += GRAVITY;
        // Resetting boolean values so they can be rechecked for the new position
        platformAhead = false;
        platformBehind = false;
    }
    public void updateAttack(Player player){
        // Updating the attacking status
        boolean originalState = isAttacking;
        isAttacking = getHitbox().intersects(player.getHitbox()); // Setting it to true if there is hitbox collision
        if(originalState != isAttacking){ // If there's a change in state, reset the sprite counter
            spriteCount = 0;
        }
        // Checking if the player should be dealt damage
        if(isAttacking && Math.round(spriteCount*10)/10.0 == attackSprites.length/2.0){
            player.enemyHit(this);
        }
    }
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
    @Override
    public void checkCollision(Rectangle rect){
        Rectangle hitbox = getHitbox();
        if(hitbox.intersects(rect)){
            if((int)((hitbox.y + hitbox.height) - velocityY) <= rect.y){
                y = (rect.y - hitbox.height) - (hitbox.y - y); // Putting the Enemy on top of the platform
                velocityY = 0;
                knockedBack = false;
            }
        }
        // Checking if there are any platforms behind or infront of the Enemy
        if(rect.contains((hitbox.x + hitbox.width + 1),(hitbox.y + hitbox.height + 1))){
            platformAhead = true;
        }
        if(rect.contains((hitbox.x - 1), (hitbox.y + hitbox.height + 1))){
            platformBehind = true;
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
