import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {
    // Constants
    public static final int RIGHT = 0;
    public static final int LEFT = 1;
    // Player's movement-related fields
    private double x, y;
    private double velocityX, velocityY;
    private int direction;
    private double acceleration, gravity;
    private double spriteCount = 0;
    // Image Arrays holding Player's Sprites
    private Image[] idleSprites = new Image[4];

    // Constructor
    public Player(){
        // Setting up fields
        direction = RIGHT;
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

    public void move(int type){
        if(type == RIGHT){
            direction = RIGHT;
            x += 1;
        }
        else{
            direction = LEFT;
            x -= 1;
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
        Image sprite;
        sprite = idleSprites[(int)Math.floor(spriteCount)];
        // Flipping the image since the sprites are right-facing
        if(direction == LEFT){
            // Using AffineTransform with Nearest-Neighbour to apply flip while keeping 8-bit style
            AffineTransform flip = AffineTransform.getScaleInstance(-1, 1);
            flip.translate(-sprite.getWidth(null), 0);
            AffineTransformOp flipOp = new AffineTransformOp(flip, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            sprite = flipOp.filter((BufferedImage) sprite, null);
        }
        return sprite;
    }
    // Getter methods

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
