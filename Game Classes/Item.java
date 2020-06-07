//Item.java
//Armaan Randhawa and Shivan Gaur
// Item objects that benefit the player in some way by giving the player power-ups
import java.awt.*;

public class Item {
    // Declaring constants
    public static final int COIN = 0, DIAMOND = 1, HEALTH = 2, HEALTHPWR = 3, ENERGYPWR = 4;
    private static final double GRAVITY = 0.25;
    // Declaring fields
    private double x, y, velocityX, velocityY;
    private double bounceX, bounceY;
    private double spriteCount;
    private int type;
    private boolean used;
    // Declaring sprites
    private static Image[] coinSprites = new Image[5];
    private static Image[] diamondSprites = new Image[6];
    private static Image[] healthSprites = new Image[6];
    private static Image[] healthPwrSprites = new Image[7];
    private static Image[] energySprites = new Image[4];
    // Sounds
    private static Sound coinSound = new Sound("Assets/Sounds/Effects/coin.wav", 80);
    private static Sound healthSound = new Sound("Assets/Sounds/Effects/health.wav", 80);
    private static Sound powerSound = new Sound("Assets/Sounds/Effects/powerUp.wav", 80);

    // Initialize class
    public static void init(){
        // Loading the sprites using the Utilities spriteLoad method
        healthSprites = Utilities.spriteArrayLoad(healthSprites, "Items/Health/health");
        coinSprites = Utilities.spriteArrayLoad(coinSprites, "Items/Coins/coin");
        diamondSprites = Utilities.spriteArrayLoad(diamondSprites, "Items/Coins/diamond");
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
    // Method that checks the items collision with LevelProps
    public void checkCollision(LevelProp prop){
        Rectangle rect = prop.getRect();
        Rectangle hitbox = getHitbox();
        // Checking if the item collided with the top
        if(hitbox.intersects(rect)){
            if((int)((hitbox.y + hitbox.height) - velocityY) <= rect.y){
                y = (rect.y - hitbox.height) - (hitbox.y - y); // Placing the item on top of the platform
                // Making the item bounce
                velocityY = bounceY;
                velocityX = bounceX;
                bounceX /= 2; bounceY /= 2;
            }
        }
    }

    // Method that updates item properties
    public void update(){
        updateMotion();
        updateSprite();
    }

    // Method that updates the item's physics
    public void updateMotion(){
        x += velocityX;
        y += velocityY;
        // Adding gravity
        velocityY += GRAVITY;
    }

    // Method that updates the sprite cycles
    public void updateSprite(){
        spriteCount += 0.1; // Progressing the item
        // Checking the point where the current item resets its cycle
        if(     (type == HEALTH && spriteCount >= healthSprites.length) ||
                (type == COIN && spriteCount >= coinSprites.length) ||
                (type == DIAMOND && spriteCount >= diamondSprites.length) ||
                (type == HEALTHPWR && spriteCount >= healthPwrSprites.length) ||
                (type == ENERGYPWR && spriteCount >= energySprites.length)){
            spriteCount = 0;
        }

    }

    // Method that plays the appropriate sound for the current item type
    public void playSound(){
        if(type == COIN || type == DIAMOND){
            coinSound.stop();
            coinSound.play();
        }
        else if(type == HEALTH){
            healthSound.stop();
            healthSound.play();
        }
        else{
            powerSound.stop();
            powerSound.play();
        }
    }

    // Setter methods
    public void use(){
        used = true;
    }

    // Getter methods
    // Method that returns the items current sprite for drawing
    public Image getSprite(){
        Image sprite = null;
        int spriteIndex = (int)Math.floor(spriteCount);
        if(type == COIN){
            sprite = coinSprites[spriteIndex];
        }
        else if(type == DIAMOND){
            sprite = diamondSprites[spriteIndex];
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
        // Returning true if the bounce values are 0 in X and Y
        return (int) bounceY == 0 && (int) bounceX == 0;
    }
}
