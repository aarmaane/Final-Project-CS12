import java.awt.*;

public class Wizard extends Enemy {
    //Constants
    public static final int CAST1 = 1, CAST2 = 2;
    // Fields
    private double attackDelay;
    private int castType;
    // Image Arrays for Sprites
    private static Image[] hurtSprites = new Image[4];
    private static Image[] idleSprites = new Image[6];
    private static Image[] deathSprites = new Image[7];
    private Image[] castSprites = new Image[8];
    // Class Initialization
    public static void init(){
        //cast2Sprites = Utilities.spriteArrayLoad(cast1Sprites, "Enemies/Wizard/cast1-");
        hurtSprites = Utilities.spriteArrayLoad(hurtSprites, "Enemies/Wizard/hurt");
        idleSprites = Utilities.spriteArrayLoad(idleSprites, "Enemies/Wizard/idle");
        deathSprites = Utilities.spriteArrayLoad(deathSprites, "Enemies/Wizard/death");
    }
    //Constructor
    public Wizard(String data){
        super(data);
        String[] dataSplit = data.split(",");
        castType = Integer.parseInt(dataSplit[3]);
        health = 300 * difficulty;
        maxHealth = health;
        damage = 35*difficulty;
        castSprites = Utilities.spriteArrayLoad(castSprites, "Enemies/Wizard/cast"+castType+"-");
    }
    // General methods
   @Override
    public void updateMotion(Player player){
       Rectangle playerBox = player.getHitbox();
       Rectangle hitbox = getHitbox();
       if(playerBox.x < hitbox.x){
           direction = LEFT;
       }
       else{
           direction = RIGHT;
       }
    }

    @Override
    public void updateAttack(Player player) {
        if(Math.abs(player.getX()-x)<1000) {
            if (!isAttacking){
                if(castType == CAST1) {
                    attackDelay += 0.005 * difficulty;
                }
                else if(castType == CAST2) {
                    attackDelay += 0.05 * difficulty;
                }
                if (attackDelay > 10) {
                    isAttacking = true;
                    spriteCount = 0;
                }
            }
        }
    }

    @Override
    public void updateSprite() {
        if(isHurt){
            if(health <= 0){
                spriteCount += 0.05;
                if(spriteCount > deathSprites.length){
                    isActive = false;
                }
            }
            else{
                spriteCount += 0.08;
                if(spriteCount > hurtSprites.length){
                    spriteCount = 0;
                    isHurt = false;
                }
            }
        }
        else if(isAttacking){
            spriteCount += 0.1;
            if(spriteCount > castSprites.length){
                spriteCount = 0;
                attackDelay = 0;
                isAttacking = false;
            }
        }
        else{
            spriteCount += 0.07;
            if(spriteCount > idleSprites.length){
                spriteCount = 0;
            }
        }
    }

    @Override
    public void checkCollision(LevelProp prop) {

    }
    @Override
    public boolean isCastFrame(){
        if(isAttacking && Utilities.roundOff(spriteCount,2) == castSprites.length - 1){
            return true;
        }
        return false;
    }

    // Getter methods
    @Override
    public Image getSprite() {
        Image sprite;
        int spriteIndex = (int)Math.floor(spriteCount);
        if(isHurt){
            if(health <= 0){
                sprite = deathSprites[spriteIndex];
            }
            else{
                sprite = hurtSprites[spriteIndex];
            }
        }
        else if(isAttacking){
            sprite = castSprites[spriteIndex];
        }
        else{
            sprite = idleSprites[spriteIndex];
        }
        if(direction == LEFT){
            sprite = Utilities.flipSprite(sprite);
        }
        return sprite;

    }

    @Override
    public Rectangle getHitbox() {
        return new Rectangle((int)x+90, (int)y+50, 65, 91);
    }
    @Override
    public int getCastType(){return castType;}
}
