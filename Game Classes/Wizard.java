// Wizard.java
// Armaan Randhawa and Shivan Gaur
// Subclass of the Enemy class that creates wizards that shoot projectiles towards the player
import java.awt.*;

public class Wizard extends Enemy {
    //Constants
    public static final int CAST1 = 1, CAST2 = 2;
    // Fields
    private double attackDelay;
    private String spriteType;
    private int castType; // Type of casting the wizard performs
    // Image Arrays for Sprites
    private Image[] hurtSprites = new Image[4];
    private Image[] idleSprites = new Image[6];
    private Image[] deathSprites = new Image[7];
    private Image[] castSprites = new Image[8];


    // Constructor
    public Wizard(String data){
        super(data);
        String[] dataSplit = data.split(",");
        castType = Integer.parseInt(dataSplit[3]);
        spriteType = dataSplit[4];
        health = 300 * difficulty;
        maxHealth = health;
        damage = 35*difficulty;
        hurtSprites = Utilities.spriteArrayLoad(hurtSprites, "Enemies/Wizard/"+spriteType+"hurt");
        idleSprites = Utilities.spriteArrayLoad(idleSprites, "Enemies/Wizard/"+spriteType+"idle");
        deathSprites = Utilities.spriteArrayLoad(deathSprites, "Enemies/Wizard/"+spriteType+"death");
        castSprites = Utilities.spriteArrayLoad(castSprites, "Enemies/Wizard/"+spriteType+"cast"+castType+"-"); // Not static since it will differ
    }
    // General methods
    @Override
    public void updateMotion(Player player){
        //This method updates the motion of the wizard. The wizard does not move, but he does change direction
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
        //This method updates the attack of the enemy
        if(Math.abs(player.getX()-x)<1000) {
            if (!isAttacking){
                if(castType == CAST1) {// Cast1 is much more difficult so the delay is longer
                    attackDelay += 0.005 * difficulty;
                }
                else if(castType == CAST2) {//Delay is 10x shorter for cast2
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
        //Restarting sprite cycles
        if(isHurt){
            if(health <= 0){ // Dying sprites
                spriteCount += 0.05;
                if(spriteCount > deathSprites.length){
                    isActive = false;
                }
            }
            else{ // Hurt sprites
                spriteCount += 0.08;
                if(spriteCount > hurtSprites.length){
                    spriteCount = 0;
                    isHurt = false;
                }
            }
        }
        else if(isAttacking){ // Attacking sprites
            spriteCount += 0.1;
            if(spriteCount > castSprites.length){
                spriteCount = 0;
                attackDelay = 0;
                isAttacking = false;
            }
        }
        else{ // Idle sprites
            spriteCount += 0.07;
            if(spriteCount > idleSprites.length){
                spriteCount = 0;
            }
        }
    }

    @Override
    public boolean isCastFrame(){
        //This method checks whether the wizard is in the cast frame or not and returns a boolean
        if(isAttacking && Utilities.roundOff(spriteCount,2) == castSprites.length - 1){
            return true;
        }
        return false;
    }

    // Getter methods
    @Override
    public Image getSprite(){
        //Returns the correct Image sprite based on the wizard's situation
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
    //Returns rectangle object with the hitbox of the wizard
    public Rectangle getHitbox() {
        return new Rectangle((int)x+90, (int)y+50, 65, 91);
    }

    @Override
    //Returns the cast type of the wizard
    public int getCastType(){return castType;}
    @Override
    public String getSpriteType(){return spriteType;}
}
