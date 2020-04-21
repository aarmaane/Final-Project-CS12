import java.awt.*;

public class Item {
    // Declaring constants
    public static final int COIN = 0, HEALTH = 1, SHIELDPWR = 2, ENERGYPWR = 3;
    private static final double GRAVITY = 0.25;
    // Declaring fields
    private double x, y, velocityX, velocityY;
    private double bounceX, bounceY;
    private int type;
    // Constructor
    public Item(Chest sourceChest){
        x = sourceChest.getHitbox().x;
        y = sourceChest.getHitbox().y;
        velocityX = Utilities.randint(-5,5);
        velocityY = Utilities.randint(-8,-4);
        bounceX = velocityX/2; bounceY = velocityY/2;
        type = sourceChest.getContent();
    }
    // General methods
    public void checkCollision(Rectangle rect){
        Rectangle hitbox = getHitbox();
        if(hitbox.intersects(rect)){
            if((int)((hitbox.y + hitbox.height) - velocityY) <= rect.y){
                y = (rect.y - hitbox.height) - (hitbox.y - y); // Placing the item on top of the platform
                velocityY = bounceY;
                velocityX = bounceX;
                bounceX /= 2; bounceY /= 2;
            }
        }
    }
    public void update(){
        updateMotion();
    }
    public void updateMotion(){
        x += velocityX;
        y += velocityY;
        // Adding gravity
        velocityY += GRAVITY;
    }
    // Getter methods
    public Rectangle getHitbox(){
        return new Rectangle((int) x, (int) y, 10, 10);
    }
}
