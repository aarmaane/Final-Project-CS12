import java.awt.*;

public class Projectile {
    //Constants
    public static final int PLAYER = 0, ENEMY = 1;
    public static final int LEFT = 0, RIGHT = 1;
    //Fields
    private double x, y;
    private double damage;
    private double speed;
    private int direction;
    private int type;
    private boolean exploding;
    private static Image[] playerSprites = new Image[60];
    private double spriteCount = 0;
    public static void init(){
        playerSprites = Utilities.spriteArrayLoad(playerSprites, "Projectiles/Iceball/iceball");
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
        x+=speed;
    }
    public void updateSprite(){
        spriteCount += 0.5;
        if(spriteCount >= playerSprites.length){
            spriteCount = 0;
        }
    }
    public Image getSprite(){
        Image sprite;
        int spriteIndex = (int)Math.floor(spriteCount);
        sprite = playerSprites[spriteIndex];
        if(direction == RIGHT){
            sprite = Utilities.flipSprite(sprite);
        }
        return sprite;
    }
    public void explode(){
        exploding = true;
    }
    // Getter methods
    public Rectangle getHitbox(){
        // Since the sprite images are much larger than the actual Player, offsets must be applied
        if(direction == RIGHT) {
            return new Rectangle((int) x+110, (int) y, 58, 18);
        }
        else{
            return new Rectangle((int) x, (int) y, 58, 18);
        }
    }
    public double getDamage(){return damage;}
    public double getX(){return x;}
    public double getY(){return y;}
    public double getSpeed(){return speed;}
    public boolean isExploding(){return exploding;}
}
