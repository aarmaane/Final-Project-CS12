import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Player {
    // Player's fields
    private int[] position = {0,0};
    private double spriteCount = 0;
    // Image Arrays holding Player's Sprites
    private Image[] idleSprites = new Image[4];

    // Constructor
    public Player(){
        // Loading Images
        try{
            for(int i = 0; i < 4; i++){
                idleSprites[i] = ImageIO.read(new File("Assets/Images/Player/idle" + i + ".png"));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tick(){
        spriteCount += 0.05;
        if(spriteCount > 3){
            spriteCount = 0;
        }
    }
    // Method that returns the player's current sprite by looking at various fields
    public Image getSprite(){
        return idleSprites[(int)Math.floor(spriteCount)];
    }
}
