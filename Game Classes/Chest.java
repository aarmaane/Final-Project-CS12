//Chest.java
//Armaan Randhawa and Shivan Gaur
//This class creates chest objects that shoot out random Item objects to help the player through the game.
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Chest {
    // Declaring fields
    private int x, y;
    private int content, quantity;//Fields for the type  items inside the chest and the amount of items.
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
        //Setting the probabilities of receiving certain items from the chest
        if(randomNumber >= 70){
            quantity = 1;
            if(randomNumber >= 85){
                content = Item.HEALTHPWR;
            }
            else{
                content = Item.ENERGYPWR;
            }
        }
        else{
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
