import java.awt.*;

public class Projectile {
    //Constants
    public static final int PLAYER = 0, ENEMY = 1;
    public static final int LEFT = 0, RIGHT = 1;
    //Fields
    private double x, y;
    private double damage, speed;
    private int direction, type;
    private double spriteCount = 0;
    private boolean exploding, doneExploding;
    // Sprite Image Arrays
    private static Image[] playerSprites = new Image[60];
    private static Image[] explosionSprites = new Image[44];
    public static void init(){
        playerSprites = Utilities.spriteArrayLoad(playerSprites, "Projectiles/Iceball/iceball");
        explosionSprites = Utilities.spriteArrayLoad(explosionSprites, "Projectiles/Explosion/explosion");

    }
    public Projectile(int type, double x, double y, double damage, double speed){
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.speed = speed;
        this.type = type;
        if(speed > 0){
            this.direction = RIGHT;
        }
        else{
            this.direction = LEFT;
        }
    }
    public void update(){
        updateSprite();
        updatePos();
    }
    public void updatePos(){
        if(!exploding){
            x+=speed;
        }
    }
    public void updateSprite(){
        spriteCount += 0.5;
        if(exploding && spriteCount >= explosionSprites.length){
            doneExploding = true;
        }
        if(spriteCount >= playerSprites.length){
            spriteCount = 0;
        }
    }
    public Image getSprite(){
        Image sprite;
        int spriteIndex = (int)Math.floor(spriteCount);
        if(exploding){
            sprite = explosionSprites[spriteIndex];
        }
        else{
            sprite = playerSprites[spriteIndex];
            if(direction == RIGHT){
                sprite = Utilities.flipSprite(sprite);
            }
        }
        return sprite;
    }
    public void explode(){
        exploding = true;
        spriteCount = 0;
    }
    // Getter methods
    public Rectangle getHitbox(){
        // Since the area where the projectile applies damage is on the end of the image, offsets must be applied depending on direction
        if(direction == RIGHT) {
            return new Rectangle((int) x+110, (int) y, 58, 18);
        }
        else{
            return new Rectangle((int) x, (int) y, 58, 18);
        }
    }
    public double getX(){
        // When exploding, the picture size changes, so the coordinate must change to accommodate
        if(exploding){
            if(direction == LEFT){
                return x - 30;
            }
            else{
                return x + 120;
            }
        }
        // Returning normal value otherwise
        return x;
    }
    public double getY(){
        // When exploding, the picture size changes, so the coordinate must change to accommodate
        if(exploding){
            return y -20;
        }
        // Returning normal value otherwise
        return y;
    }
    public double getDamage(){return damage;}
    public double getSpeed(){return speed;}
    public boolean isExploding() {return exploding;}
    public boolean isDoneExploding(){return doneExploding;}
}
