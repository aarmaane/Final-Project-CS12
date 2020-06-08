// Boss.java
// Armaan Randhawa and Shivan Gaur
// Subclass of the Enemy class, and it creates an object that serves as the final boss of the game.
import org.w3c.dom.css.Rect;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Boss extends Enemy{
    // Fields
    private boolean isSpinning, isPrepCharging, isCharging, dealtChargeDamage;
    private int spinTimer, spinNum;
    // Images
    private static Image bossHealthBar;
    private static Image[] idleSprites = new Image[5];
    private static Image[] movingSprites = new Image[8];
    private static Image[] chargingSprites = new Image[5];
    private static Image[] attackSprites = new Image[9];
    private static Image[] spinSprites = new Image[9];
    private static Image[] deathSprites = new Image[6];
    // Initialization of Class
    public static void init(){
        // Loading sprites
        idleSprites = Utilities.spriteArrayLoad(idleSprites, "Enemies/Boss/idle");
        movingSprites = Utilities.spriteArrayLoad(movingSprites, "Enemies/Boss/moving");
        chargingSprites = Utilities.spriteArrayLoad(chargingSprites, "Enemies/Boss/charging");
        attackSprites = Utilities.spriteArrayLoad(attackSprites, "Enemies/Boss/attack");
        spinSprites = Utilities.spriteArrayLoad(spinSprites, "Enemies/Boss/spin");
        deathSprites = Utilities.spriteArrayLoad(deathSprites, "Enemies/Boss/death");
        // Loading health bar
        try{
            bossHealthBar = ImageIO.read(new File("Assets/Images/Enemies/Boss/healthBar.png"));
        }
        catch (IOException e) {
            System.out.println("Boss health bar missing!");
            e.printStackTrace();
        }
    }

    public Boss(String data) {
        // Constructor
        super(data);
        health = 5000;
        maxHealth = health;
        damage = 150;
    }
    // General methods
    @Override
    public double castHit(Projectile cast){
        if(isSpinning){
            return 0; // Immunity during spin
        }
        // Overridden to remove all knockback and hurt sprites
        double damageDone = (Utilities.randint(70,100)/100.0)*cast.getDamage(); // Boss has higher range of damage
        health -= damageDone;
        // Checking if the boss has died
        if(isDying()){
            isHurt = true;
            spriteCount = 0;
        }
        // Returning damage done
        return damageDone;
    }

    @Override
    public double swordHit(Player player){
        if(isSpinning){
            return 0; // Immunity during spin
        }
        // Overridden to remove all knockback and hurt sprites
        double damageDone = (Utilities.randint(70,100)/100.0)*player.getSwordDamage()*getSpriteAlpha(); // Boss has has higher range of damage
        health -= damageDone;
        // Checking if the boss has died
        if(isDying()){
            isHurt = true;
            spriteCount = 0;
        }
        // Returning damage done
        return damageDone;
    }

    @Override
    public void drawHealth(Graphics g, int levelOffset){
        // Drawing image with health icon
        g.drawImage(bossHealthBar, 318, 500, null);
        // Drawing health bar
        for(int i = 0; i < 100; i++) { // Loop to create a gradient effect on the bar
            g.setColor(new Color(155 + i, 0, 20)); //Changing colour
            g.fillRect(344 + i, 500, (int) (((double) getHealth() / getMaxHealth()) * 300) - i, 25);
        }
    }

    @Override
    public void updateAttack(Player player){
        // Don't attack during death
        if(isDying()){
            return;
        }
        if(isPrepCharging){
            if(isCharging && !dealtChargeDamage && getSpinbox().intersects(player.getHitbox())){
                player.enemyHit(this);
                dealtChargeDamage = true;
            }
        }
        else if(isSpinning){
            // Updating spin
            spinTimer--;
            // Checking for intersection
            if(getSpinbox().intersects(player.getHitbox()) && Utilities.roundOff(spriteCount, 2) == spinSprites.length/2.0){
                player.enemyHit(this);
            }
        }
        // Updating normal attacks
        else{
            boolean originalState = isAttacking;
            isAttacking = getHitbox().intersects(player.getHitbox()); // Setting it to true if there is hitbox collision
            if(originalState != isAttacking){ // If there's a change in state, reset the sprite counter
                spriteCount = 0;
            }
            // Checking for intersection
            if(getHitbox().intersects(player.getHitbox()) && Utilities.roundOff(spriteCount, 2) == attackSprites.length/3.0 - 1){
                player.enemyHit(this);
            }
            // Replenishing spin
            spinTimer++;
        }
        // Activating/deactivating spin
        if(spinTimer >= 600){
            spinNum++;
            isAttacking = false;
            if(spinNum == 3){ // On the 6th spin, activate the charge
                spinTimer = 0;
                spinNum = 0;
                damage = 500;
                isPrepCharging = true;
                dealtChargeDamage = false;
                spriteCount = 0;
            }
            else{
                damage = 300;
                isSpinning = true;
            }
        }
        else if(isSpinning && spinTimer == 0){
            damage = 150;
            isSpinning = false;
        }
    }
    @Override
    public void updateMotion(Player player) {
        // Don't update motion during death
        if(isDying()){
            return;
        }
        if(isPrepCharging){ // Charging movement
            if(!isCharging){ // Staying in place for charging prep
                velocityX = 0;
            }
            else{
                // Charging towards ends of screen
                if(direction == RIGHT){
                    velocityX = 5;
                }
                else{
                    velocityX = -5;
                }
                // Checking if screen bounds are reached
                Rectangle hitbox = getHitbox();
                if(hitbox.x < 0 || hitbox.getMaxX() > 960){
                    isPrepCharging = false;
                    isCharging = false;
                    spriteCount = 0;
                    damage = 250;
                }
            }
        }
        else{ // Normal movement
            Rectangle playerHitbox = player.getHitbox();
            Rectangle hitbox = getHitbox();
            // Moving direction towards the player
            if(playerHitbox.x > hitbox.x + hitbox.width/2){
                direction = RIGHT;
            }
            else{
                direction = LEFT;
            }
            // Moving boss towards the player
            if(playerHitbox.x == hitbox.x + hitbox.width/2 || isAttacking || (isSpinning && getSpinbox().intersects(playerHitbox))){
                velocityX = 0;
            }
            else if(playerHitbox.x > hitbox.x + hitbox.width/2){
                velocityX = 1;
            }
            else{
                velocityX = -1;
            }
            // Moving the boss faster if its spinning
            if(isSpinning){
                velocityX *= 2;
            }
        }
        super.updateMotion(player);
    }

    @Override
    public void updateSprite() {
        if(isDying()){
            spriteCount += 0.05;
            if (spriteCount > deathSprites.length) {
                isActive = false;
            }
        }
        else if(isPrepCharging){
            if(!isCharging){ // Prep charging sprites
                spriteCount += 0.05;
                if (spriteCount > chargingSprites.length) {
                    isCharging = true;
                    spriteCount = 0;
                }
            }
            else{ // Charge sprites (Faster running ones)
                spriteCount += 0.15;
                if(spriteCount > movingSprites.length){
                    spriteCount = 0;
                }
            }
        }
        else if(isSpinning) { // Spin attack
            spriteCount += 0.1;
            if (spriteCount > spinSprites.length - 2) {
                spriteCount = 2;
            }
        }
        else if(isAttacking){ // Regular attack
            spriteCount += 0.1;
            if(spriteCount > attackSprites.length){
                spriteCount = 0;
            }
        }
        else if(velocityX != 0){
            spriteCount += 0.07;
            if(spriteCount > movingSprites.length){
                spriteCount = 0;
            }
        }
        else{
            spriteCount += 0.05;
            if(spriteCount > idleSprites.length){
                spriteCount = 0;
            }
        }
    }
    // Getter methods
    @Override
    public Image getSprite() {
        Image sprite;
        int spriteIndex = (int)Math.floor(spriteCount);
        if(isDying()){
            sprite = deathSprites[spriteIndex];
        }
        else if(isPrepCharging){
            if(!isCharging){
                sprite = chargingSprites[spriteIndex];
            }
            else{
                sprite = movingSprites[spriteIndex];
            }
        }
        else if(isSpinning){
            sprite = spinSprites[spriteIndex];
        }
        else if(isAttacking){
            sprite = attackSprites[spriteIndex];
        }
        else if(velocityX != 0){
            sprite = movingSprites[spriteIndex];
        }
        else{
            sprite = idleSprites[spriteIndex];
        }
        // Flipping image since sprites are right facing
        if(direction == LEFT){
            sprite = Utilities.flipSprite(sprite);
        }
        return sprite;
    }
    @Override
    public Rectangle getHitbox() {
        return new Rectangle((int)x + 110, (int)y + 90, 150, 165);
    }

    public Rectangle getSpinbox(){
        return new Rectangle((int)x + 40, (int)y + 200, 300, 60);
    }
}
