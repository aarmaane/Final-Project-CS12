import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Slime extends Enemy {
    // Sprites
    private static Image[] movingSprites = new Image[4];
    private static Image[] attackSprites;
    private static Image[] idleSprites = new Image[4];
    private static Image[] deathSprites;
    //Fields

    // Method to initialize the Class by loading sprites
    public static void init(){
        try{
            for(int i = 0; i < 4; i++){
                movingSprites[i] = ImageIO.read(new File("Assets/Images/Enemies/Slime/move" + i + ".png"));
                idleSprites[i] = ImageIO.read(new File("Assets/Images/Enemies/Slime/idle" + i + ".png"));
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
        health = 100 * difficulty;
        maxHealth=health;
    }
    // General methods
    @Override
    public void update(Player player){
        updateMotion(player);
        updateSprite();
    }
    public void updateMotion(Player player){
        // Checking the position of the Player and setting velocity towards them
        int playerX = player.getHitbox().x;
        int slimeX = getHitbox().x;
        if(playerX > slimeX){
            direction = RIGHT;
            velocityX = 0.5;
        }
        else if(playerX < slimeX){
            direction = LEFT;
            velocityX = -0.5;
        }
        else{
            velocityX = 0;
        }
        // Applying velocity values to position
        x += velocityX;
        y += velocityY;
        // Adding gravity value
        velocityY += GRAVITY;
    }
    public void updateSprite(){
        spriteCount += 0.05;
        if(spriteCount > 4){
            spriteCount = 0;
        }
    }
    @Override
    public void checkCollision(Rectangle rect){
        Rectangle hitbox = getHitbox();
        if(hitbox.intersects(rect)){
            if((int)((hitbox.y + hitbox.height) - velocityY) <= rect.y){
                y = (rect.y - hitbox.height) - (hitbox.y - y); //
                velocityY = 0;
            }
        }
    }
    // Getter methods
    @Override
    public Image getSprite() {
        Image sprite = null;
        if(velocityX != 0){
            sprite = movingSprites[(int)Math.floor(spriteCount)];
        }
        else{
            sprite = idleSprites[(int)Math.floor(spriteCount)];
        }
        if(direction == RIGHT){
            sprite = flipImage(sprite);
        }
        return sprite;
    }

    @Override
    public Rectangle getHitbox() {
        return new Rectangle((int)x + 10, (int)y + 25, 80, 45);
    }
}
