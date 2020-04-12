import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public abstract class Enemy {
    // Constants
    public static final int RIGHT = 0;
    public static final int LEFT = 1;
    protected static final double GRAVITY = 0.25;
    //Fields
    protected double x, y, velocityX, velocityY;
    protected double spriteCount;
    protected int direction;
    protected int health, damage, difficulty;
    protected boolean isActive;
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
    // Helper methods for subclasses
    protected Image flipImage(Image image){
        // Using AffineTransform with Nearest-Neighbour to apply flip while keeping 8-bit style
        AffineTransform flip = AffineTransform.getScaleInstance(-1, 1);
        flip.translate(-image.getWidth(null), 0);
        AffineTransformOp flipOp = new AffineTransformOp(flip, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = flipOp.filter((BufferedImage)image, null);
        return image;
    }
}
