import java.awt.*;

public class Item {
    // Declaring constants
    public static final int COIN = 0, HEALTH = 1, HEALTHPWR = 2, ENERGYPWR = 3;
    private static final double GRAVITY = 0.25;
    // Declaring fields
    private double x, y, velocityX, velocityY;
    private double bounceX, bounceY;
    private double spriteCount;
    private int type;
    private boolean used;
    // Declaring sprites
    private static Image[] coinSprites = new Image[5];
    private static Image[] healthSprites = new Image[6];
    private static Image[] healthPwrSprites = new Image[7];
    private static Image[] energySprites = new Image[4];
    // Initialize class
    public static void init(){
        healthSprites = Utilities.spriteArrayLoad(healthSprites, "Items/Health/health");
        coinSprites = Utilities.spriteArrayLoad(coinSprites, "Items/Coins/coin");
        healthPwrSprites = Utilities.spriteArrayLoad(healthPwrSprites, "Items/Health/healthpwr");
        energySprites = Utilities.spriteArrayLoad(energySprites, "Items/Energy/energy");
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
        if(     (type == HEALTH && spriteCount >= healthSprites.length) ||
                (type == COIN && spriteCount >= coinSprites.length) ||
                (type == HEALTHPWR && spriteCount >= healthPwrSprites.length) ||
                (type == ENERGYPWR && spriteCount >= energySprites.length)){
            spriteCount = 0;
        }

    }
    public void use(){
        used = true;
    }
    // Getter methods
    public Image getSprite(){
        Image sprite = null;
        int spriteIndex = (int)Math.floor(spriteCount);
        if(type == COIN){
            sprite = coinSprites[spriteIndex];
        }
        else if(type == HEALTH){
            sprite = healthSprites[spriteIndex];
        }
        else if(type == HEALTHPWR){
            sprite = healthPwrSprites[spriteIndex];
        }
        else if(type == ENERGYPWR){
            sprite = energySprites[spriteIndex];
        }
        return sprite;
    }

    public Rectangle getHitbox(){
        return new Rectangle((int) x, (int) y, 32, 32);
    }
    public int getType(){
        return type;
    }
    public boolean isUsed(){
        return used;
    }
    public boolean isSettled(){
        return (int) bounceY == 0 && (int) bounceX == 0;
    }
}
