import jdk.jshell.execution.Util;

import java.awt.*;

public class Item {
    // Declaring constants
    public static final int COIN = 0, HEALTH = 1, SHIELDPWR = 2, ENERGYPWR = 3;
    private static final double GRAVITY = 0.25;
    // Declaring fields
    private double x, y, velocityX, velocityY;
    private double bounceX, bounceY;
    private double spriteCount;
    private int type;
    // Declaring sprites
    private static Image[] coinSprites;
    private static Image[] healthSprites = new Image[6];
    private static Image[] shieldSprites;
    private static Image[] energySprites;
    // Initialize class
    public static void init(){
        healthSprites = Utilities.spriteArrayLoad(healthSprites, "Items/Health/health");
    }
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
        updateSprite();
    }
    public void updateMotion(){
        x += velocityX;
        y += velocityY;
        // Adding gravity
        velocityY += GRAVITY;
    }
    public void updateSprite(){
        spriteCount += 0.1;
        if(spriteCount >= healthSprites.length){
            spriteCount = 0;
        }
    }
    // Getter methods
    public Image getSprite(){
        return healthSprites[(int)Math.floor(spriteCount)];
    }
    public Rectangle getHitbox(){
        return new Rectangle((int) x, (int) y, 32, 32);
    }
}
