import java.awt.*;

public class Wizard extends Enemy {
    // Fields
    // Image Arrays for Sprites
    private static Image[] cast1Sprites = new Image[8];
    private static Image[] cast2Sprites = new Image[8];
    private static Image[] hurtSprites = new Image[4];
    private static Image[] idleSprites = new Image[6];
    private static Image[] deathSprites = new Image[7];
    private boolean isCasting;
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
        damage = 35;
        isActive = true;
        isCasting = true;

    }
    // General methods

   @Override
    public void updateMotion(Player player){
        // Doing nothing since Wizards don't move

        //super.updateMotion(player);
    }





    @Override
    public void updateAttack(Player player) {
        super.updateAttack(player);
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
        /*
        else if(isAttacking){
            spriteCount += 0.05;
            if(spriteCount > idleSprites.length){
                spriteCount = 0;
            }
        }

         */

        else{
            spriteCount += 0.05;
            if(spriteCount > cast1Sprites.length){
                spriteCount = 0;
            }
        }


    }

    @Override
    public void checkCollision(LevelProp prop) {

    }
    @Override
    public boolean isCastFrame(){
        //System.out.println(Utilities.roundOff(spriteCount,2));
        //System.out.println(cast1Sprites.length-1);
        if(isCasting && Utilities.roundOff(spriteCount,2) == cast1Sprites.length-1){
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
        /*
        else if(isAttacking){
            sprite = idleSprites[spriteIndex];
        }

         */
        else{
            sprite = cast1Sprites[spriteIndex];
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
