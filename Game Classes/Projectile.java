import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
    private Image[] projectilePics = new Image[60];
    private double spriteCount = 0;

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
        spriteLoad(projectilePics,"Iceball/iceball");

    }
    public void spriteLoad(Image[] targetArray, String fileName){
        try{
            for(int i = 0; i < targetArray.length; i++){
                System.out.println("Assets/Images/Projectiles/" + fileName + i + ".png");
                targetArray[i] = ImageIO.read(new File("Assets/Images/Projectiles/" + fileName + i + ".png"));
            }
        }
        catch (IOException e) {
            System.out.println("Projectile sprite missing!");
            e.printStackTrace();
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
        if(spriteCount >= projectilePics.length){
            spriteCount = 0;
        }
    }
    public Image getSprite(){
        Image sprite;
        int spriteIndex=(int)Math.floor(spriteCount);
        sprite = projectilePics[spriteIndex];
        if(direction == RIGHT){
            sprite = Utilities.flipSprite(sprite);
        }
        return sprite;
    }
    public void explode(){
        exploding = true;
    }
    public Rectangle getRect(){
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
