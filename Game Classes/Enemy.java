import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public abstract class Enemy {
    // Constants
    public static final int RIGHT = 0, LEFT = 1;
    protected static final double GRAVITY = 0.25;
    //Fields
    protected double x, y, velocityX, velocityY;
    protected double spriteCount;
    protected int timeAlive;
    protected int direction;
    protected int health, maxHealth, damage, difficulty;
    protected boolean isActive, isHurt, isAttacking, knockedBack, onMovingPlat;
    protected boolean platformBehind, platformAhead;
    protected boolean hasAlphaSprites;
    protected boolean hasTimeLimit;
    protected boolean outOfBoundsPoints;
    // Images
    protected static Image healthBar;
    // Class initialization
    public static void init() throws IOException {
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
    public void drawHealth(Graphics g, int levelOffset){
        if(health != maxHealth){ // Only drawing if they have lost health
            Rectangle hitBox = getHitbox();
            int healthBarOffset = ((100-hitBox.width)/8);
            g.setColor(Color.RED);
            g.fillRect(hitBox.x-levelOffset-healthBarOffset,hitBox.y-10,(int)(((double)health/maxHealth)*88),13);
            g.drawImage(healthBar,hitBox.x-levelOffset-13-healthBarOffset,hitBox.y-15,null);
        }
    }
    public void checkCollision(LevelProp prop){
        Rectangle rect = prop.getRect();
        Rectangle hitbox = getHitbox();
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
    public double castHit(Projectile cast){
        double damageDone = (Utilities.randint(80,100)/100.0)*cast.getDamage()*getSpriteAlpha();
        health -= damageDone;
        velocityY = -3;
        if(cast.getSpeed() > 0){
            velocityX = 3;
        }
        else{
            velocityX = -3;
        }
        isHurt = true;
        knockedBack = true;
        isAttacking = false;
        spriteCount = 0;
        return damageDone;
    }
    public double swordHit(Player player){
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
    public void update(Player player){
        updateMotion(player);
        if(!isHurt){
            updateAttack(player);
        }
        updateSprite();

    }
    public void iterateTime(){
        if(hasTimeLimit){
            if(timeAlive>0) {
                timeAlive -= 1;
            }
            else{
                health = 0;
                isHurt = true;
                spriteCount = 0;
            }
        }
    }
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
    public void updateAttack(Player player){
        // Updating the attacking status
        boolean originalState = isAttacking;
        isAttacking = getHitbox().intersects(player.getHitbox()); // Setting it to true if there is hitbox collision
        if(originalState != isAttacking){ // If there's a change in state, reset the sprite counter
            spriteCount = 0;
        }
    }
    public void setTimeLimit(int timeLimit){
        hasTimeLimit = true;
        timeAlive = timeLimit;
    }

    public float getSpriteAlpha(){
        return (float)1.0;
    }
    public boolean isCastFrame(){
        return false;
    }
    // Declaring methods that subclasses need to implement
    public abstract void updateSprite();
    public abstract Image getSprite();
    public abstract Rectangle getHitbox();

    // Getter methods
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
    public int getDirection(){ return direction;}
    public boolean hasOutOfBoundsPoints(){ return outOfBoundsPoints;}

    public int getCastType(){ return 2;}
}
