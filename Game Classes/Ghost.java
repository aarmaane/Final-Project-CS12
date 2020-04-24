import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Ghost extends Enemy {
    //Fields
    private static Image[] movingSprites = new Image[5];
    private static Image[] idleSprites;
    private static Image[] attackSprites;
    private static Image[] deathSprites;
    //Constructor
    public Ghost(){
    }
    public static void init(){
        try{
            for(int i = 0; i < 5; i++){
                // MOVE THE PICTURE OUT OF THE UNUSED ASSET FOLDER
                movingSprites[i] = ImageIO.read(new File("Unused assets/Enemies/Ghost/ghost" + (10+i) + ".png"));
            }

        }
        catch (IOException e) {
            System.out.println("Ghost image missing!");
            e.printStackTrace();
        }
    }
    // General methods
    @Override
    public void update(Player player){

    }

    @Override
    public void checkCollision(Rectangle rect) {

    }

    // Getter methods
    @Override
    public Image getSprite() {
        return null;
    }

    @Override
    public Rectangle getHitbox() {
        return null;
    }
}
