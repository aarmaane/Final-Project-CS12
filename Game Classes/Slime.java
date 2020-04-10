import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Slime extends Enemy {
    // Sprites
    private static Image[] movingSprites = new Image[4];
    private static Image[] attackSprites;
    private static Image[] idleSprites;
    private static Image[] deathSprites;
    //Fields
    private int spriteCount = 0;
    //Constructor
    public Slime(String data){
    }
    // Method to initialize the Class by loading sprites
    public static void init(){
        try{
            for(int i = 0; i < 4; i++){
                movingSprites[i] = ImageIO.read(new File("Assets/Images/Enemies/Slime/move" + i + ".png"));
            }
        }
        catch (IOException e) {
            System.out.println("Slime sprites not found!");
            e.printStackTrace();
        }
    }
}
