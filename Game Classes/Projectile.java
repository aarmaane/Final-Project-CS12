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
    // Angled projectile fields
    private double angle;
    private boolean isAngled;
    // Sprite Image Arrays
    private static Image[] projectileSprites = new Image[60];
    private static Image[] explosionSprites = new Image[44];
    public static void init(){
        explosionSprites = Utilities.spriteArrayLoad(explosionSprites, "Projectiles/Explosion/explosion");

    }
    public Projectile(int type, double x, double y, double damage, double speed,String imagePath){
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
        projectileSprites = Utilities.spriteArrayLoad(projectileSprites, "Projectiles/"+imagePath);
    }
    public Projectile(int type, double startX, double startY,double targX,double targY ,double damage, double speed,String imagePath){
        this.type= type;
        this.x = startX;
        this.y = startY;
        this.damage = damage;
        this.speed = speed;
        isAngled = true;
        if(speed > 0){
            this.direction = RIGHT;
        }
        else{
            this.direction = LEFT;
        }
        this.angle = Math.atan((targY - getHitbox().y)/(targX - getHitbox().x));
        projectileSprites = Utilities.spriteArrayLoad(projectileSprites, "Projectiles/"+imagePath);
    }
    public void update(){
        updateSprite();
        updatePos();
    }
    public void updatePos(){
        if(!exploding){
            if(isAngled){
                x += Math.cos(angle) * speed;
                y += Math.sin(angle) * speed;
            }
            else{
                x += speed;
            }
        }


    }
    public void updateSprite(){
        spriteCount += 0.5;
        if(exploding && spriteCount >= explosionSprites.length){
            doneExploding = true;
        }
        if(spriteCount >= projectileSprites.length){
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
            sprite = projectileSprites[spriteIndex];
            if(direction == RIGHT){
                sprite = Utilities.flipSprite(sprite);
            }
            if(isAngled){
                sprite = Utilities.rotateSprite(sprite,angle,68,9);
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
    public int getType(){ return type;}
    public double getAngle(){ return angle;}
}
