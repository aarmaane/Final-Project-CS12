import org.w3c.dom.css.Rect;

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
    public static final int INITIAL = 0;
    public static final int NORMAL = 1;
    // Player's movement-related fields
    private double x, y;
    private double velocityX, velocityY;
    private double acceleration, maxSpeed, gravity;
    private int direction;
    private boolean onGround, holdingJump;
    private double spriteCount = 0;
    // Players' gameplay-related fields
    private int health, maxHealth;
    private int stamina, maxStamina;
    private int swordDamage, spellDamage;
    // Image Arrays holding Player's Sprites
    private Image[] idleSprites = new Image[4];
    private Image[] runSprites = new Image[6];
    private Image[] jumpingSprites = new Image[2];
    private Image[] fallingSprites = new Image[2];
    // Other fields
    GamePanel game;
    // Constructor
    public Player(GamePanel gamePanel){
        // Setting up fields
        game = gamePanel;
        y = 366;
        direction = RIGHT;
        acceleration = 0.2;
        gravity = 0.25;
        maxSpeed = 6;
        onGround = true;
        // Loading Images
        try{
            for(int i = 0; i < 2; i++){
                fallingSprites[i] = ImageIO.read(new File("Assets/Images/Player/fall" + i + ".png"));
                jumpingSprites[i] = ImageIO.read(new File("Assets/Images/Player/jump" + i + ".png"));

            }
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
        if(velocityX == 0 && onGround){ // Start of movement (But not to interrupt jumping sprites)
            spriteCount = 0; // Resetting the sprite counter
        }
        // Applying the actual velocity
        int midAirOffset = 1; // By default, the offset divides by one and does nothing
        if(!onGround){  // Slowing down acceleration when mid-air
            midAirOffset = 4;
        }
        if(type == RIGHT){
            direction = RIGHT;
            velocityX += acceleration / midAirOffset;
        }
        else{
            direction = LEFT;
            velocityX -= acceleration / midAirOffset;
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
    public void jump(int type){
        if(type == INITIAL && onGround){
            spriteCount = 0;
            onGround = false;
            velocityY = -6;
        }
        else if(type == NORMAL){
            holdingJump = true;
        }
    }
    // Method to update the Player Object each frame
    public void update(){
        updateMotion();
        checkOutOfBounds();
        updateSprites();
    }
    // Method to calculate and apply the physics of the Player
    public void updateMotion(){
        // Updating position from velocities
        x += velocityX;
        y += velocityY;
        // Applying friction force
        if(onGround){ // Friction only applies when the Player is on the ground
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
        }
        // Applying gravity
        if(velocityY < 0 && holdingJump){ // If the player is jumping and holding the jump key, use lower gravity to allow for a variable jump height
            velocityY += gravity/3;
           holdingJump = false; // Resetting the variable so it doesn't get applied next frame without input
        }
        else{ // Otherwise use normal gravity values
            velocityY += gravity;
        }
        // Checking if the Player is falling (This will update onGround when the Player leaves a platform without jumping)
        if(onGround && velocityY > 1){
            onGround = false;
        }
    }
    // Method to keep the Player within the confines of the game
    public void checkOutOfBounds(){
        // Using the hitbox for true X coordinate values since the sprite pictures are larger than the actual player
        Rectangle hitBox = getHitBox();
        if(hitBox.x < 0){ // Player moves offscreen (from the left side)
            int extraMovement = hitBox.x;
            x -= extraMovement; // Shifting the player back into the correct position
        }
    }
    // Method to smoothly update the sprite counter and produce realistic animation of the Player
    public void updateSprites(){
        if(velocityY < 0){ // Jumping sprites
            if(spriteCount < 1){ // Only playing the animation once through (no repetition)
                spriteCount += 0.1;
            }
        }
        else if(velocityY > 0 && !onGround){ // Falling sprites
            spriteCount += 0.05 + (Math.pow(velocityY,1.5)/100);
            if(spriteCount > 2){
                spriteCount = 0;
            }
        }
        else if(velocityX != 0){ // Running sprites
            spriteCount += 0.05 + (Math.abs(velocityX)/90); // Scaling sprite speed with player velocity
            if(spriteCount > 6){
                spriteCount = 0;
            }
        }
        else{ // Idling sprites
            spriteCount += 0.05;
            if(spriteCount > 4){
                spriteCount = 0;
            }
        }
    }
    public void checkCollision(Rectangle rect){
        Rectangle hitBox = getHitBox();
        if(hitBox.intersects(rect)){
            if((int)((hitBox.y + hitBox.height) - velocityY) <= rect.y){
                y = (rect.y - hitBox.height) - (hitBox.y - y); //
                velocityY = 0;
                onGround = true;
            }
        }
    }
    // Getter methods
    // Method that returns the player's current sprite by looking at various fields
    public Image getSprite(){
        Image sprite = null;
        if(velocityY < 0){
            sprite = jumpingSprites[(int)Math.floor(spriteCount)];
        }
        else if(velocityY > 0 && !onGround)  {
            sprite = fallingSprites[(int)Math.floor(spriteCount)];
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
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public Rectangle getHitBox(){
        // Since the sprite images are much larger than the actual Player, offsets must be applied
        return new Rectangle((int)x + 50, (int)y + 15, 50, 93);
    }
}
