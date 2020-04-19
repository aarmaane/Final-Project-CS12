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
    // General methods
    public void castHit(Projectile cast){
        health -= (Utilities.randint(80,100)/100.0)*cast.getDamage();
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
    }
    public void swordHit(Player player){
        health -= (Utilities.randint(80,100)/100.0)*player.getSwordDamage();
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
    }
    // Declaring methods that subclasses need to implement
    public abstract void update(Player player);
    public abstract void checkCollision(Rectangle rect);
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
