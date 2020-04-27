import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Ghost extends Enemy {
    //Fields
    private int spriteAlpha;
    // Images arrays to hold sprites
    private static Image[] movingSprites = new Image[5];
    private static Image[] idleSprites;
    private static Image[] attackSprites;
    private static Image[] deathSprites;

    //Constructor
    public Ghost(){
        super("0,0,0");
        hasAlphaSprites = true;
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
        super.update(player);
    }

    @Override
    public void updateAttack(Player player) {

    }
    // The Ghost hsa fully custom movement, so it needs to be overrided
    @Override
    public void updateMotion(Player player){

    }
    @Override
    public void updateSprite() {

    }
    public void updateSpriteAlpha(){

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
