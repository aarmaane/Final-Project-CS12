import java.awt.*;

public abstract class Enemy {
    //Fields
    protected double x, y;
    protected int health, damage;
    protected int difficulty;
    protected boolean active;
    // Declaring methods
    public abstract void update();
    public abstract Image getSprite();
    public abstract Rectangle getHitbox();
    // Getter methods
    public double getX(){return x;}
    public double getY(){return y;}
}
