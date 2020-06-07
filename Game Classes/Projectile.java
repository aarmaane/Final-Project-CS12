// Projectile.java
// Armaan Randhawa and Shivan Gaur
// Class creates projectile objects that are used when the player or certain enemies use their casting abilities
import java.awt.*;

public class Projectile {
    //Constants
    public static final int PLAYER = 0, ENEMY = 1;
    public static final int LEFT = 0, RIGHT = 1;
    //Fields
    private double x, y;
    private double damage, speed;
    private int direction, type;
    private double spriteCount = 0, flightTime;
    private boolean exploding, doneExploding;
    private Image[] projectileSprites;
    // Angled projectile fields
    private double angle;
    private Rectangle angledRect;
    private boolean isAngled;
    // Sprite Image Arrays
    private static Image[] iceSprites = new Image[60];
    private static Image[] darkSprites = new Image[60];
    private static Image[] explosionSprites = new Image[44];

    //Class Initialization
    public static void init(){
        iceSprites = Utilities.spriteArrayLoad(iceSprites, "Projectiles/Iceball/iceball");
        darkSprites = Utilities.spriteArrayLoad(darkSprites, "Projectiles/DarkCast/darkCast");
        explosionSprites = Utilities.spriteArrayLoad(explosionSprites, "Projectiles/Explosion/explosion");
    }

    //First Constructor ---> 1D projectile
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
        assignArray();

    }

    //Second Constructor---> 2D projectile
    public Projectile(int type, double startX, double startY,double targX,double targY ,double damage, double speed){
        this.type= type;
        this.damage = damage;
        isAngled = true;
        // Setting speed and direction depending on target
        if(targX > startX){
            this.speed = speed;
            this.direction = RIGHT;
        }
        else{
            this.speed = -speed;
            this.direction = LEFT;
        }
        this.angle = Math.atan((targY - startY)/(targX - startX));
        assignArray();
        angledRect = Utilities.rectFinder(getSprite()); // Using the rectFinder method to calculate the hitbox
        // Assigning x and y with proper offset
        x = startX - angledRect.x;
        y = startY - angledRect.y;
    }

    // Method assigns the proper array of sprites to the projectile object based on the type
    public void assignArray(){
        if(type == PLAYER){
            projectileSprites = iceSprites;
        }
        else{
            projectileSprites = darkSprites;
        }
    }

    // Method updates the projectile properties
    public void update(){
        updateSprite();
        updatePos();
    }

    //Updates the motion and (x,y) position of the projectile
    public void updatePos(){
        if(!exploding){
            // Updating X and Y coordinate
            if(isAngled){
                // Angled update
                x += Math.cos(angle) * speed;
                y += Math.sin(angle) * speed;
            }
            else{
                // 1D update (only moving in X)
                x += speed;
            }
            // Updating the time counter and forcing the projectile to explode after a while
            flightTime++;
            if(flightTime > 400){
                explode();
            }
        }
    }

    // Method to update the sprite counter of the projectile
    public void updateSprite(){
        spriteCount += 0.5; // Adding to the counter
        // Checking if the explosion is finished
        if(exploding && spriteCount >= explosionSprites.length){
            doneExploding = true;
        }
        // Looping through the projectile sprites once limit is hit
        if(spriteCount >= projectileSprites.length){
            spriteCount = 0;
        }
    }

    // Makes the projectile explode and prepares it to be swept up by the garbage collector
    public void explode(){
        exploding = true;
        spriteCount = 0;
    }

    // Getter methods
    // Gets projectile's current sprite
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
            if(isAngled){ //Rotating the projectile sprites at the proper coordinate
                // Getting width and height of projectile before rotation
                int noRotHeight = projectileSprites[0].getHeight(null);
                int noRotWidth = projectileSprites[0].getWidth(null);
                // Calculating the proper rotation point of the image
                if(Math.sin(angle) * speed < 0){
                    if(Math.cos(angle) * speed < 0){
                        sprite = Utilities.rotateSprite(sprite,angle,0, noRotHeight);
                    }
                    else{
                        sprite = Utilities.rotateSprite(sprite,angle, noRotWidth,noRotHeight/2);
                    }
                }
                else{
                    sprite = Utilities.rotateSprite(sprite,angle,noRotWidth/2,noRotHeight/2);

                }
            }
        }
        return sprite;
    }

    // Method to get the hitbox where the projectile hits entites
    public Rectangle getHitbox(){
        if(isAngled){
            // Using the calculated hitbox by assignRect
            return new Rectangle(angledRect.x + (int)x, angledRect.y + (int)y, angledRect.width, angledRect.height);
        }
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
            if(isAngled){
                return angledRect.x + x - 20;
            }
            else if(direction == LEFT){
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
            if(isAngled){
                return angledRect.y + y - 10;
            }
            return y - 20;
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
