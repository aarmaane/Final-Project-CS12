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
    //Constructor
    public Slime(String data){
        String[] dataSplit = data.split(",");
        x = Integer.parseInt(dataSplit[0]);
        y = Integer.parseInt(dataSplit[1]);
        difficulty = Integer.parseInt(dataSplit[2]);
    }
    // General methods
    @Override
    public void update(){

    }
    // Getter methods
    @Override
    public Image getSprite() {
        return movingSprites[0];
    }

    @Override
    public Rectangle getHitbox() {
        return new Rectangle((int)x, (int)y, 100, 100);
    }
}
