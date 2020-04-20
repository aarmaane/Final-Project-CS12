import java.awt.*;

public class Chest {
    // Declaring constants
    public static final int COINS = 0, HEALTH = 1, SHIELDPWR = 2, ENERGYPWR = 3;
    // Declaring fields
    private int x, y;
    private int content, quantity;
    private boolean opened;
    // Declaring sprites
    private Image closedSprite;
    private Image openSprite;
    // Constructor
    public Chest(String data){
        String[] dataSplit = data.split(",");
        x = Integer.parseInt(dataSplit[0]);
        y = Integer.parseInt(dataSplit[1]);
        int randomNumber = Utilities.randint(0,100);
        if(randomNumber >= 90){
            quantity = 1;
            if(randomNumber >= 95){
                content = SHIELDPWR;
            }
            else{
                content = ENERGYPWR;
            }
        }
        else{
            content = Utilities.randint(1,6);
            if(randomNumber >= 45){
                content = HEALTH;
            }
            else{
                content = COINS;
            }
        }
    }
}
