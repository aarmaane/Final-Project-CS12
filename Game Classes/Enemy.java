import java.awt.*;

public abstract class Enemy {
    // Constants
    public static final int RIGHT = 0;
    public static final int LEFT = 1;
    protected static final double GRAVITY = 0.25;
    //Fields
    protected double x, y, velocityX, velocityY;
    protected int direction, spriteCount;
    protected int health, damage;
    protected int difficulty;
    protected boolean isActive;
    // Declaring methods
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
}
