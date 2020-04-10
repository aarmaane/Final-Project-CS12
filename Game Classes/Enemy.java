import java.awt.*;

public abstract class Enemy {
    //Fields
    protected double x, y;
    protected int health, damage;
    protected int difficulty;
    protected boolean active;
    public abstract Image getSprite();
    // Getter methods
    public double getX(){return x;}
    public double getY(){return y;}
}
