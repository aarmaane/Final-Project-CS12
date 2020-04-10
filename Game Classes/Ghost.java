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
    public Ghost(){
        //Constructor

    }
    public static void init(){
        try{
            for(int i = 0; i < 5; i++){
                movingSprites[i] = ImageIO.read(new File("Unused assets/Enemies/Ghost/ghost" + (10+i) + ".png"));
            }

        }
        catch (IOException e) {
            System.out.println("Player image missing!");
            e.printStackTrace();
        }
    }
}
