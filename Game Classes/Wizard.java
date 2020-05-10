import java.awt.*;

public class Wizard extends Enemy {
    // Fields
    private double attackDelay;
    // Image Arrays for Sprites
    private static Image[] cast1Sprites = new Image[8];
    private static Image[] cast2Sprites = new Image[8];
    private static Image[] hurtSprites = new Image[4];
    private static Image[] idleSprites = new Image[6];
    private static Image[] deathSprites = new Image[7];
    // Class Initialization
    public static void init(){
        cast1Sprites = Utilities.spriteArrayLoad(cast1Sprites, "Enemies/Wizard/cast1-");
        cast2Sprites = Utilities.spriteArrayLoad(cast2Sprites, "Enemies/Wizard/cast2-");
        hurtSprites = Utilities.spriteArrayLoad(hurtSprites, "Enemies/Wizard/hurt");
        idleSprites = Utilities.spriteArrayLoad(idleSprites, "Enemies/Wizard/idle");
        deathSprites = Utilities.spriteArrayLoad(deathSprites, "Enemies/Wizard/death");
    }
    //Constructor
    public Wizard(String data){
        super(data);
        health = 300 * difficulty;
        maxHealth = health;
        damage = 0;
    }
    // General methods
   @Override
    public void updateMotion(Player player){
       Rectangle playerBox = player.getHitbox();
       if(playerBox.x > getHitbox().x){
           direction = LEFT;
       }
       else{
           direction = RIGHT;
       }
    }

    @Override
    public void updateAttack(Player player) {
        if(!isAttacking){
            attackDelay += 0.05;
            if(attackDelay > 10){
                isAttacking = true;
                spriteCount = 0;
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
            if(spriteCount > cast1Sprites.length){
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
        if(isAttacking && Utilities.roundOff(spriteCount,2) == cast1Sprites.length - 1){
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
            sprite = cast1Sprites[spriteIndex];
        }
        else{
            sprite = idleSprites[spriteIndex];
        }
        if(direction == RIGHT){
            sprite = Utilities.flipSprite(sprite);
        }
        return sprite;

    }

    @Override
    public Rectangle getHitbox() {
        return new Rectangle((int)x+90, (int)y+50, 65, 91);
    }
}
