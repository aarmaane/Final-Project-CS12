import java.awt.*;

public class Boss extends Enemy{
    // Fields

    // Constructor
    public Boss(String data) {
        super(data);
        health = 5000;
        damage = 250;
    }
    @Override
    public void updateSprite() {

    }
    // Getter methods
    @Override
    public Image getSprite() {
        return null;
    }

    @Override
    public Rectangle getHitbox() {
        return null;
    }
}
