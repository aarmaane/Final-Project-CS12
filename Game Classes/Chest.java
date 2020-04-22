import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Chest {
    // Declaring constants
    public static final int COINS = 0, HEALTH = 1, HEALTHPWR = 2, ENERGYPWR = 3;
    // Declaring fields
    private int x, y;
    private int content, quantity;
    private boolean opened;
    // Declaring sprites
    private static Image closedSprite;
    private static Image openSprite;
    // Class initialization
    public static void init(){
        try{
            closedSprite = ImageIO.read(new File("Assets/Images/Chests/chestClosed.png"));
            openSprite = ImageIO.read(new File("Assets/Images/Chests/chestOpen.png"));
        }
        catch (IOException e) {
            System.out.println("Chest sprites not found!");
            e.printStackTrace();
        }
    }
    // Constructor
    public Chest(String data){
        String[] dataSplit = data.split(",");
        x = Integer.parseInt(dataSplit[0]);
        y = Integer.parseInt(dataSplit[1]);
        int randomNumber = Utilities.randint(0,100);
        if(randomNumber >= 90){
            quantity = 1;
            if(randomNumber >= 95){
                content = HEALTHPWR;
            }
            else{
                content = ENERGYPWR;
            }
        }
        else{
            quantity = Utilities.randint(1,6);
            if(randomNumber >= 45){
                content = HEALTH;
            }
            else{
                content = COINS;
            }
        }
    }
    public void open(){
        opened = true;
    }
    // Getter methods
    public Image getSprite(){
        if(opened){
            return openSprite;
        }
        return closedSprite;
    }
    public Rectangle getHitbox(){
        return new Rectangle(x, y, 48, 48);
    }
    public int getContent(){
        return content;
    }
    public int getQuantity(){
        return quantity;
    }
    public boolean isClosed(){
        return !opened;
    }
}
