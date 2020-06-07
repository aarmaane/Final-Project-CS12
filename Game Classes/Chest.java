// Chest.java
// Armaan Randhawa and Shivan Gaur
// Chest objects that shoot out Item objects when the player nears them
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Chest {
    // Declaring fields
    private int x, y;
    private int content, quantity; // Fields for the type/number of items inside the chest
    private boolean opened;
    // Declaring sprites
    private static Image closedSprite;
    private static Image openSprite;

    // Class initialization
    public static void init(){
        // Loading the chest sprites in a static context so all objects can access them
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
        // Taking the position data from the String
        String[] dataSplit = data.split(",");
        x = Integer.parseInt(dataSplit[0]);
        y = Integer.parseInt(dataSplit[1]);
        // Generating a random number to seed the if statement below
        int randomNumber = Utilities.randint(0,100);
        // Using ranges of numbers to set probabilities of receiving certain items from the chest
        if(randomNumber >= 70){
            // Chest holds as powerup
            quantity = 1;
            if(randomNumber >= 85){
                content = Item.HEALTHPWR;
            }
            else{
                content = Item.ENERGYPWR;
            }
        }
        else{
            // Chest holds 1-6 health/points consumables
            quantity = Utilities.randint(1,6);
            if(randomNumber >= 35){
                content = Item.HEALTH;
            }
            else if(randomNumber < 5){
                content = Item.DIAMOND;
            }
            else{
                content = Item.COIN;
            }
        }
    }

    // Setter methods
    public void open(){
        opened = true;
    }

    // Getter methods
    public Image getSprite(){
        // Returning the proper image depending on the status
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
