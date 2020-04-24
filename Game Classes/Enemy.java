import java.awt.*;

public abstract class Enemy {
    // Constants
    public static final int RIGHT = 0, LEFT = 1;
    protected static final double GRAVITY = 0.25;
    //Fields
    protected double x, y, velocityX, velocityY;
    protected double spriteCount;
    protected int direction;
    protected int health, maxHealth, damage, difficulty;
    protected boolean isActive, isHurt, isAttacking, knockedBack;
    protected boolean platformBehind, platformAhead;
    // General methods
    public void checkCollision(Rectangle rect){
        if(this.getClass() == Skeleton.class){
            System.out.println();
        }
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
    public double castHit(Projectile cast){
        double damageDone = (Utilities.randint(80,100)/100.0)*cast.getDamage();
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
        spriteCount = 0;
        return damageDone;
    }
    public double swordHit(Player player){
        double damageDone = (Utilities.randint(80,100)/100.0)*player.getSwordDamage();
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
        spriteCount = 0;
        return damageDone;
    }
    // Declaring methods that subclasses need to implement
    public abstract void update(Player player);
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
    public boolean isDead(){
        return (!isActive && health <= 0);
    }
}
