import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Crystal extends Enemy {
    private static Image crystalSprite;
    private boolean hitPlayer, falling;
    public static void init() throws IOException {
        crystalSprite = ImageIO.read(new File("Assets/Images/Level Props/Set 3/bottom6.png"));
    }
    public Crystal(String data) {
        super(data);
        health = 100 * difficulty;
        maxHealth = health;
        damage = 100;
        outOfBoundsPoints = false;
    }
    @Override
    public void updateAttack(Player player){
        if(getHitbox().x-player.getHitbox().x<Utilities.randint(50,300)){
            falling = true;
        }
        if(player.getHitbox().intersects(getHitbox()) && !hitPlayer) {
            player.enemyHit(this);
            hitPlayer = true;
        }
    }
    @Override
    public void checkCollision(LevelProp prop){

    }
    @Override
    public void updateMotion(Player player){
        if(falling) {
            super.updateMotion(player);
        }
    }
    @Override
    public void updateSprite() {

    }

    @Override
    public Image getSprite() {
        return crystalSprite;
    }

    @Override
    public Rectangle getHitbox() {
        return new Rectangle((int)x, (int)y, 32, 92);
    }
}
