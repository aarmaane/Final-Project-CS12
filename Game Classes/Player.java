import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {
    // Constants
    public static final int RIGHT = 0, LEFT = 1;
    public static final int INITIAL = 0, NORMAL = 1;
    private static final double GRAVITY = 0.25;
    // Player's movement-related fields
    private double x, y;
    private double velocityX, velocityY;
    private double acceleration, maxSpeed;
    private int direction;
    private int points;
    private boolean onGround, holdingJump;
    private double spriteCount = 0;
    // Players' gameplay-related fields
    private int health, maxHealth;
    private double stamina, maxStamina;
    private int swordDamage, spellDamage;
    private boolean isAttacking, isCasting;
    private int attackNum;
    // Image Arrays holding Player's Sprites
    private Image[] idleSprites = new Image[4];
    private Image[] runSprites = new Image[6];
    private Image[] jumpingSprites = new Image[2];
    private Image[] fallingSprites = new Image[2];
    private Image[][] groundAttackSprites; // This array will be jagged since attacks have differing lengths
    private Image[] airAttackSprites = new Image[5];
    private Image[] castSprites = new Image[4];
    // Other fields
    GamePanel game;
    // Constructor methods
    public Player(GamePanel gamePanel){
        game = gamePanel;
        // Setting up movement fields
        y = 366;
        direction = RIGHT;
        acceleration = 0.2;
        maxSpeed = 6;
        onGround = true;
        // Setting gameplay fields
        maxStamina = 50;
        stamina = maxStamina;
        maxHealth=100;
        health=maxHealth;
        points=0;
        // Loading Images
        spriteLoad(fallingSprites, "fall");
        spriteLoad(jumpingSprites, "jump");
        spriteLoad(idleSprites, "idle");
        spriteLoad(runSprites, "run");
        spriteLoad(castSprites, "cast");
        Image[] attack1 = new Image[5];
        Image[] attack2 = new Image[6];
        Image[] attack3 = new Image[6];
        spriteLoad(attack1, "attack1-");
        spriteLoad(attack2, "attack2-");
        spriteLoad(attack3, "attack3-");
        groundAttackSprites = new Image[][]{attack1, attack2, attack3};

    }
    public void spriteLoad(Image[] targetArray, String fileName){
        try{
            for(int i = 0; i < targetArray.length; i++){
                System.out.println("Assets/Images/Player/" + fileName + i + ".png");
                targetArray[i] = ImageIO.read(new File("Assets/Images/Player/" + fileName + i + ".png"));
            }
        }
        catch (IOException e) {
            System.out.println("Player sprite missing!");
            e.printStackTrace();
        }
    }
    // General methods
    public void move(int type){
        // If the player is doing an action, don't let them move (ignore this call for move())
        if(isCasting || isAttacking){
            return;
        }
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
        // If the player is doing an action, don't let them jump
        if(isCasting || isAttacking){
            return;
        }
        if(type == INITIAL && onGround){
            spriteCount = 0;
            onGround = false;
            velocityY = -6;
        }
        else if(type == NORMAL){
            holdingJump = true;
        }
    }
    public void attack(){
        System.out.println("attack");
        if(!isAttacking && !isCasting && onGround && (stamina - 5) > 0){
            stamina -= 5;
            isAttacking = true;
            spriteCount = 0;
            attackNum++;
            if(attackNum >= groundAttackSprites.length){
                attackNum = 0;
            }
        }
    }
    public void castMagic(){
        System.out.println("magic");
        if(!isAttacking && !isCasting && onGround && (stamina - 10) > 0){
            stamina -= 10;
            isCasting = true;
            spriteCount = 0;
        }
    }
    // Method to update the Player Object each frame
    public void update(){
        updateMotion();
        updateStamina();
        checkOutOfBounds();
        updateSprite();
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
            velocityY += GRAVITY/3;
           holdingJump = false; // Resetting the variable so it doesn't get applied next frame without input
        }
        else{ // Otherwise use normal gravity values
            velocityY += GRAVITY;
        }
        // Checking if the Player is falling (This will update onGround when the Player leaves a platform without jumping)
        if(onGround && velocityY > 1){
            onGround = false;
            if(isCasting || isAttacking){ // Cancelling any spell cast or attack
                isCasting = false;
                isAttacking = false;
                spriteCount = 0;
            }
        }
    }
    public void updateStamina(){
        if(isCasting || isAttacking){
            return; // No regeneration during casting/attacks
        }
        else if(velocityX != 0 || !onGround) {
            stamina += 0.01; // Slow regeneration while in motion
        }
        else{
            stamina += 0.07; // Faster regeneration while standing still
        }
        if(stamina > maxStamina){ // Making sure stamina doesn't exceed maximum
            stamina = maxStamina;
        }
    }
    // Method to keep the Player within the confines of the game
    public void checkOutOfBounds(){
        // Using the hitbox for true X coordinate values since the sprite pictures are larger than the actual player
        Rectangle hitbox = getHitbox();
        if(hitbox.x < 0){ // Player moves offscreen (from the left side)
            int extraMovement = hitbox.x;
            x -= extraMovement; // Shifting the player back into the correct position
        }
    }
    // Method to smoothly update the sprite counter and produce realistic animation of the Player
    public void updateSprite(){
        if(isCasting){
            spriteCount += 0.05;
            if(spriteCount > castSprites.length){
                isCasting = false;
                // Release spell here
                spriteCount = 0;
            }
        }
        else if(isAttacking){
            spriteCount += 0.1;
            if(spriteCount > groundAttackSprites[attackNum].length){
                isAttacking = false;
                spriteCount = 0;
            }
        }
        else if(velocityY < 0){ // Jumping sprites
            if(spriteCount < 1){ // Only playing the first frame only once (no repetition)
                spriteCount += 0.1;
            }
        }
        else if(velocityY > 0 && !onGround){ // Falling sprites
            spriteCount += 0.05 + (Math.pow(velocityY,1.5)/100);
            if(spriteCount > fallingSprites.length){
                spriteCount = 0;
            }
        }
        else if(velocityX != 0){ // Running sprites
            spriteCount += 0.05 + (Math.abs(velocityX)/90); // Scaling sprite speed with player velocity
            if(spriteCount > runSprites.length){
                spriteCount = 0;
            }
        }
        else{ // Idling sprites
            spriteCount += 0.05;
            if(spriteCount > idleSprites.length){
                spriteCount = 0;
            }
        }
    }
    public void checkCollision(Rectangle rect){
        Rectangle hitbox = getHitbox();
        if(hitbox.intersects(rect)){
            if((int)((hitbox.y + hitbox.height) - velocityY) <= rect.y){
                y = (rect.y - hitbox.height) - (hitbox.y - y); //
                velocityY = 0;
                onGround = true;
            }
        }
    }
    public void resetPos(){
        x = 0;
        y = 366;
        velocityX = 0;
        velocityY = 0;
    }
    // Getter methods
    // Method that returns the player's current sprite by looking at various fields
    public Image getSprite(){
        Image sprite = null;
        int spriteIndex = (int)Math.floor(spriteCount);
        if(isCasting){
            sprite = castSprites[spriteIndex];
        }
        else if(isAttacking){
            sprite = groundAttackSprites[attackNum][spriteIndex];
        }
        else if(velocityY < 0){
            sprite = jumpingSprites[spriteIndex];
        }
        else if(velocityY > 0 && !onGround)  {
            sprite = fallingSprites[spriteIndex];
        }
        else if(velocityX != 0){
            sprite = runSprites[spriteIndex];
        }
        else{
            sprite = idleSprites[spriteIndex];
        }
        // Flipping the image since the sprites are all right-facing
        if(direction == LEFT){
            // Using AffineTransform with Nearest-Neighbour to apply flip while keeping 8-bit style
            AffineTransform flip = AffineTransform.getScaleInstance(-1, 1);
            flip.translate(-sprite.getWidth(null), 0);
            AffineTransformOp flipOp = new AffineTransformOp(flip, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            sprite = flipOp.filter((BufferedImage)sprite, null);
        }
        return sprite;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public Rectangle getHitbox(){
        // Since the sprite images are much larger than the actual Player, offsets must be applied
        return new Rectangle((int)x + 58, (int)y + 15, 36, 93);
    }
    public Rectangle getAttackBox(){
        int xPos = (int)x + 120;
        if(direction == LEFT){
            xPos = (int)x;
        }
        return new Rectangle(xPos, (int)y + 40, 30, 50);
    }
    public double getStamina() {
        return stamina;
    }
    public double getMaxStamina(){return maxStamina;}
    public int getHealth(){return health;}
    public int getMaxHealth(){return maxHealth;}
    public int getPoints(){return points;}
}
