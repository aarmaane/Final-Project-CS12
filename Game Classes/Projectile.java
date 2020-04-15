import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Projectile {
    //Constants
    public static final int PLAYER = 0, ENEMY = 1;
    //Fields
    private double x, y;
    private double damage;
    private double speed;
    private int type;
    private Image[] projectilePics = new Image[5];

    public Projectile(int type, double x, double y, double damage, double speed){
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.speed = speed;
        this.type = type;
        spriteLoad(projectilePics,"projectile");

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

    public double getDamage(){return damage;}
    public double getX(){return x;}
    public double getY(){return y;}
    public double getSpeed(){return speed;}
}
