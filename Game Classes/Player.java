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
    private double acceleration, maxSpeed, gravity;
    private int direction;
    private boolean onGround;
    private double spriteCount = 0;
    // Image Arrays holding Player's Sprites
    private Image[] idleSprites = new Image[4];
    private Image[] runSprites = new Image[6];
    // Constructor
    public Player(){
        // Setting up fields
        y = 366;
        direction = RIGHT;
        acceleration = 0.2;
        gravity = 0.8;
        maxSpeed = 6;
        // Loading Images
        try{
            for(int i = 0; i < 4; i++){
                idleSprites[i] = ImageIO.read(new File("Assets/Images/Player/idle" + i + ".png"));
            }
            for(int i = 0; i < 6; i++){
                runSprites[i] = ImageIO.read(new File("Assets/Images/Player/run" + i + ".png"));
            }
        }
        catch (IOException e) {
            System.out.println("Player image missing!");
            e.printStackTrace();
        }
    }

    public void move(int type){
        // Handling sudden movements
        if(type != direction){ // Change in direction
            velocityX = 0;
        }
        if(velocityX == 0){ // Start of movement
            spriteCount = 0; // Resetting the sprite counter
        }
        // Applying the actual velocity
        if(type == RIGHT){
            direction = RIGHT;
            velocityX += acceleration;
        }
        else{
            direction = LEFT;
            velocityX -= acceleration;
        }
        // Maintaining speed limit
        if(Math.abs(velocityX) > maxSpeed){
            if(velocityX > maxSpeed){ // Speed limit in positive direction (Right)
                velocityX = maxSpeed;
            }
            else{                     // Speed limit in negative direction (Left)
                velocityX = -maxSpeed;
            }
        }
    }
    public void tick(){
        // Updating position from velocities
        x += velocityX;
        y += velocityY;
        // Applying friction force
        if(velocityX > 0){
            velocityX -= acceleration/2;
            if(velocityX < 0){ // Stopping motion when friction forces movement backwards
                velocityX = 0;
            }
        }
        else if(velocityX < 0){ // Same as above but for the other direction
            velocityX += acceleration/2;
            if(velocityX > 0){
                velocityX = 0;
            }
        }
        // Maintaining Player-Screen bounds (Using the hitbox for true X coordinate values)
        Rectangle hitBox = getHitBox();
        if(hitBox.x < 0){ // Player moves offscreen
            int extraMovement = hitBox.x;
            x -= extraMovement; // Shifting the player back into the correct position
        }
        else if(hitBox.x + hitBox.width > 480){ // Player moves to the middle of the screen
            int extraMovement = (hitBox.x + hitBox.width) - 480;
            x -= extraMovement; // Shifting the player back into the correct position
        }
        // Updating the sprite
        if(velocityY != 0){ // Jumping/falling sprites
            // Nothing yet
        }
        else if(velocityX != 0){
            spriteCount += 0.05 + (Math.abs(velocityX)/100);
            if(spriteCount > 5){
                spriteCount = 0;
            }
        }
        else{
            spriteCount += 0.05;
            if(spriteCount > 3){
                spriteCount = 0;
            }
        }
    }
    // Method that returns the player's current sprite by looking at various fields
    public Image getSprite(){
        Image sprite = null;
        if(velocityY != 0){
            // Jumping/falling sprites
        }
        else if(velocityX != 0){
            sprite = runSprites[(int)Math.floor(spriteCount)];
        }
        else{
            sprite = idleSprites[(int)Math.floor(spriteCount)];
        }
        // Flipping the image since the sprites are all right-facing
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
    public Rectangle getHitBox(){
        // Since the sprite images are much larger than the actual Player, offsets must be applied
        return new Rectangle((int)x + 50, (int)y + 15, 50, 95);
    }
}
