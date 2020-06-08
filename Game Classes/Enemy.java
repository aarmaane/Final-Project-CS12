// Enemy.java
// Armaan Randhawa and Shivan Gaur
// Abstract class that lays out basic enemy characteristics such as physics, collisions, attacks, and graphics

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public abstract class Enemy {
    // Constants
    public static final int RIGHT = 0, LEFT = 1;
    protected static final double GRAVITY = 0.25;
    //Fields
    // All fields are protected, which allows subclasses to inherit the fields.
    protected double x, y, velocityX, velocityY;
    protected double spriteCount;
    protected int timeAlive; //Variable for enemies who have a timer on them
    protected int direction;
    protected int health, maxHealth, damage, difficulty;
    protected boolean isActive, isHurt, isAttacking, knockedBack, onMovingPlat;
    protected boolean platformBehind, platformAhead;
    protected boolean hasAlphaSprites; //Boolean for whether the enemy becomes transparent
    protected boolean hasTimeLimit; //Boolean for whether the enemy has a timer or not
    protected boolean outOfBoundsPoints; //Boolean for whether the enemy gives the player points once it is out of bounds
    // Images
    protected static Image healthBar;

    // Class initialization
    public static void init() throws IOException {
        // Loading genric health bar for all enemies
        try {
            healthBar = ImageIO.read(new File("Assets/Images/Enemies/healthBar.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Constructor
    public Enemy(String data){
        String[] dataSplit = data.split(",");
        x = Integer.parseInt(dataSplit[0]);
        y = Integer.parseInt(dataSplit[1]);
        difficulty = Integer.parseInt(dataSplit[2]);
        outOfBoundsPoints = true;
    }

    // General methods
    public void activate(){
        isActive = true;
    }

    // Method that draws the enemy's health above them
    public void drawHealth(Graphics g, int levelOffset){
        if(health != maxHealth){ // Only drawing if they have lost health
            Rectangle hitBox = getHitbox();
            int healthBarOffset = ((hitBox.width-100)/2);
            g.setColor(Color.RED);
            g.fillRect(hitBox.x-levelOffset+10+healthBarOffset,hitBox.y-10,(int)(((double)health/maxHealth)*88),13); // Filled bar
            g.drawImage(healthBar,hitBox.x-levelOffset+healthBarOffset,hitBox.y-15,null); // Health bar image
        }
    }

    // Method delivers and returns the damage done to the enemy by a player projectile
    public double castHit(Projectile cast){
        //The damage done is also based on the transparency of the enemy which is why damage can vary with ghosts as their transparency changes
        double damageDone = (Utilities.randint(80,100)/100.0)*cast.getDamage()*getSpriteAlpha();
        // Reducing health and adding knockback
        health -= damageDone;
        velocityY = -3;
        if(cast.getSpeed() > 0){
            velocityX = 3;
        }
        else{
            velocityX = -3;
        }
        // Setting flags
        isHurt = true;
        knockedBack = true;
        isAttacking = false;
        spriteCount = 0;
        return damageDone;
    }

    // Method delivers and returns the damage done to the enemy by a sword hit
    public double swordHit(Player player){
        //Same as the casthit
        double damageDone = (Utilities.randint(80,100)/100.0)*player.getSwordDamage()*getSpriteAlpha();
        health -= damageDone;
        velocityY = -4;
        if(player.getDirection() == Player.RIGHT){
            velocityX = 4;
        }
        else{
            velocityX = -4;
        }
        isHurt = true;
        knockedBack = true;
        isAttacking = false;
        spriteCount = 0;
        return damageDone;
    }
    /* Below methods are commonly overridden by subclasses with different properties */

    //This method checks the collision between the LevelProps and the enemy
    public void checkCollision(LevelProp prop){
        // Declaring rects
        Rectangle rect = prop.getRect();
        Rectangle hitbox = getHitbox();
        // Checking if the enemy should be placed on top of the LevelProp
        if(hitbox.intersects(rect)){
            if((int)((hitbox.y + hitbox.height) - velocityY) <= rect.y){
                y = (rect.y - hitbox.height) - (hitbox.y - y); // Putting the Enemy on top of the platform
                velocityY = 0;
                knockedBack = false;
            }
        }
        // Checking if they are on a moving platform
        if(prop.isMoving() && !onMovingPlat && !knockedBack){
            if(rect.contains(hitbox.x+hitbox.width, hitbox.y+hitbox.height + 1) || rect.contains(hitbox.x, hitbox.y+hitbox.height + 1)){
                // Moving the enemy along with the platform
                x += prop.getXSpeed();
                y += prop.getYSpeed();
                onMovingPlat = true;
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

    // Method updates all the changing properties of the enemy
    public void update(Player player){
        updateMotion(player);
        if(!isHurt){ // Enemy can't attack while being hurt
            updateAttack(player);
        }
        updateSprite();

    }

    // Method updates the physics of the enemy
    public void updateMotion(Player player){
        // Applying velocity values to position
        x += velocityX;
        y += velocityY;
        // Adding gravity value
        velocityY += GRAVITY;
        // Resetting boolean values so they can be rechecked for the new position
        platformAhead = false;
        platformBehind = false;
        onMovingPlat = false;
    }

    // Method updates the attacking status of the enemy
    public void updateAttack(Player player){
        boolean originalState = isAttacking;
        isAttacking = getHitbox().intersects(player.getHitbox()); // Setting it to true if there is hitbox collision
        if(originalState != isAttacking){ // If there's a change in state, reset the sprite counter
            spriteCount = 0;
        }
    }

    // Method is used to count down the number of seconds remaining for enemies with timers on them
    public void iterateTime(){
        if(hasTimeLimit){
            if(timeAlive>0) {
                timeAlive -= 1;
            }
            else{//If the enemy's time is up then their health is set to 0 and the garbage collector sweeps it up
                health = 0;
                isHurt = true;
                spriteCount = 0;
            }
        }
    }

    // Method sets the time limit for how long timed enemies are alive for when they are created by spawners
    public void setTimeLimit(int timeLimit){
        hasTimeLimit = true;
        timeAlive = timeLimit;
    }
    // Declaring methods that subclasses need to implement
    public abstract void updateSprite(); // Method that updates spriteCounters based on enemy's status
    public abstract Image getSprite(); // Method that returns the enemy's current sprite for drawing
    public abstract Rectangle getHitbox(); // Method that returns the hitbox of the enemy as a Rectangle

    // Getter methods
    public float getSpriteAlpha(){
        return (float)1.0;
    }
    public boolean isCastFrame(){
        return false;
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public int getHealth(){
        return health;
    }
    public int getMaxHealth(){
        return maxHealth;
    }
    public int getHealthPercent(){
        return (health/maxHealth)*100;
    }
    public int getDamage(){
        return damage;
    }
    public boolean isActive() {
        return isActive;
    }
    public boolean isDying(){
        return health <= 0;
    }
    public boolean isDead(){
        return !isActive && isDying();
    }
    public boolean hasAlphaSprites(){
        return hasAlphaSprites;
    }
    public int getDirection(){return direction;}
    public boolean hasOutOfBoundsPoints(){ return outOfBoundsPoints;}
    public boolean hasLimitedTime(){ return hasTimeLimit;}
    public int getCastType(){ return 2;}
    public int getDifficulty(){ return difficulty;}
    public String getSpriteType(){ return "";}
}
