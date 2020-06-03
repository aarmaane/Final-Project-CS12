//Crystal.java
//Armaan Randhawa and Shivan Gaur
//This program is a subclass of the Enemy class and creates Crystal enemies that fall due to gravity and injure the player
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Crystal extends Enemy {
    //Fields
    private static Image crystalSprite;
    private boolean hitPlayer, falling;

    public static void init() throws IOException {
        //This method initializes the class by loading the crystal sprite
        crystalSprite = ImageIO.read(new File("Assets/Images/Level Props/Set 3/bottom6.png"));
    }
    public Crystal(String data) {
        //Constructor
        super(data);
        health = 100 * difficulty;
        maxHealth = health;
        damage = 100;
        outOfBoundsPoints = false;//Does not give the player points once the object falls out of bounds
    }
    @Override
    public void updateAttack(Player player){
        //This method determines when to give damage to the player.

        if(getHitbox().x-player.getHitbox().x<Utilities.randint(50,300)){
            //The crystal will start falling when the player is at a randomly determined distance between 50 and 300 pixels away from the crystal
            falling = true;
        }
        if(player.getHitbox().intersects(getHitbox()) && !hitPlayer) {
            //If the crystal hits the player then a boolean is set to true so the player is only hit once.
            player.enemyHit(this);
            hitPlayer = true;
        }
    }
    @Override
    public void checkCollision(LevelProp prop){
        //This method is left empty because the crystals fall off the screen instead of colliding with platforms
    }
    @Override
    public void updateMotion(Player player){
        //This method updates the motion of the crystal object when the falling boolean is set to true
        if(falling) {
            super.updateMotion(player);//Gravity is the only thing pulling the crystal down
        }
    }
    @Override
    public void updateSprite() {
        //This method is left empty since there is only one sprite for the crystal objects
    }

    @Override
    public Image getSprite() {
        return crystalSprite;
    }//Returns the crystal sprite

    @Override
    public Rectangle getHitbox() {
        return new Rectangle((int)x, (int)y, 32, 92);
    }//Returns rectangle object with the hitbox of the crystal
}
