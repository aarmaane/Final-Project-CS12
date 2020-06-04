//Player.java
//Armaan Randhawa and Shivan Gaur
//This class creates the player object of the game
import java.awt.*;
import java.util.ArrayList;

public class Player {
    // Constants
    public static final int RIGHT = 0, LEFT = 1;
    public static final int INITIAL = 0, NORMAL = 1, DEMO = 2;
    private static final double GRAVITY = 0.25;
    ////Fields
    // Player's movement-related fields
    private double x, y;
    private double velocityX, velocityY;
    private double acceleration, maxSpeed;
    private int direction;
    private boolean onGround, onMovingPlat, holdingJump, sprinting, isCrouching;
    private double spriteCount = 0;
    private int jumpCount = 1;
    // Players' gameplay-related fields
    private int health, maxHealth, points;
    private double stamina, maxStamina;
    private int swordDamage, castDamage;
    private boolean isAttacking, isCasting, isHurt, isDying;
    private int healthTimer, energyTimer;
    private int groundAttackNum, airAttackNum;
    // Player's Upgrade-Related Fields
    private boolean hasCastScope, hasInstantCast, hasDoubleJump, hasHyperspeed;
    private int swordUpgradeNum, castUpgradeNum, healthUpgradeNum, staminaUpgradeNum;
    private Integer castTargetX, castTargetY;
    // Image Arrays holding Player's Sprites
    private Image[] idleSprites = new Image[4];
    private Image[] runSprites = new Image[6];
    private Image[] slidingSprites = new Image[2];
    private Image[] crouchSprites = new Image[4];
    private Image[] jumpingSprites = new Image[2];
    private Image[] fallingSprites = new Image[2];
    private Image[] hurtSprites = new Image[3];
    private Image[] dyingSprites = new Image[7];
    private Image[][] groundAttackSprites; // This array will be jagged since attacks have differing lengths
    private Image[][] airAttackSprites; // This array will be jagged too
    private Image[] castSprites = new Image[4];
    // Sound effects for the player
    private Sound jumpSound = new Sound("Assets/Sounds/Effects/jump.wav", 80);

    // Other fields
    private ArrayList<IndicatorText> textQueue = new ArrayList<>();
    // Constructor methods
    public Player(){
        // Setting up movement fields
        direction = RIGHT;
        acceleration = 0.2;
        maxSpeed = 6;
        onGround = true;
        // Setting gameplay fields
        maxStamina = Integer.MAX_VALUE;
        stamina = maxStamina;
        maxHealth=Integer.MAX_VALUE;
        health=maxHealth;
        points=100000;
        swordDamage=10;
        castDamage =15;
        // Loading Images
        fallingSprites = Utilities.spriteArrayLoad(fallingSprites, "Player/fall");
        jumpingSprites = Utilities.spriteArrayLoad(jumpingSprites, "Player/jump");
        slidingSprites = Utilities.spriteArrayLoad(slidingSprites, "Player/slide");
        crouchSprites = Utilities.spriteArrayLoad(crouchSprites, "Player/crouch");
        idleSprites = Utilities.spriteArrayLoad(idleSprites, "Player/idle");
        runSprites = Utilities.spriteArrayLoad(runSprites, "Player/run");
        castSprites = Utilities.spriteArrayLoad(castSprites, "Player/cast");
        hurtSprites = Utilities.spriteArrayLoad(hurtSprites, "Player/hurt");
        dyingSprites = Utilities.spriteArrayLoad(dyingSprites, "Player/die");
        // Loading jagged attack Arrays
        Image[] attack1 = new Image[5];
        Image[] attack2 = new Image[6];
        Image[] attack3 = new Image[6];
        attack1 = Utilities.spriteArrayLoad(attack1, "Player/attack1-");
        attack2 = Utilities.spriteArrayLoad(attack2, "Player/attack2-");
        attack3 = Utilities.spriteArrayLoad(attack3, "Player/attack3-");
        groundAttackSprites = new Image[][]{attack1, attack2, attack3};
        Image[] airAttack1 = new Image[4];
        Image[] airAttack2 = new Image[3];
        airAttack1 = Utilities.spriteArrayLoad(airAttack1, "Player/airattack1-");
        airAttack2 = Utilities.spriteArrayLoad(airAttack2, "Player/airattack2-");
        airAttackSprites = new Image[][]{airAttack1, airAttack2};

    }
    // General methods
    public void move(int type){
        // If the player is doing an action, don't let them move (ignore this call for move())
        if(isCasting || isAttacking || isDying || isCrouching){
            return;
        }
        // Handling sudden movements
        if(type != direction){ // Change in direction
            velocityX = 0;
        }
        if(velocityX == 0 && onGround){ // Start of movement (But not to interrupt jumping sprites)
            spriteCount = 0; // Resetting the sprite counter
        }
        if(isHurt){ // Moving after getting damaged
            isHurt = false;
            spriteCount = 0;
        }
        // Applying the actual velocity
        int midAirOffset = 1; // By default, the offset divides by one and does nothing
        if(!onGround){  // Slowing down acceleration when mid-air
            midAirOffset = 4;
        }
        if(type == RIGHT){
            direction = RIGHT;
            velocityX += acceleration / midAirOffset;
        }
        else{
            direction = LEFT;
            velocityX -= acceleration / midAirOffset;
        }
        // Maintaining speed limit
        if(Math.abs(velocityX) > maxSpeed){
            if(velocityX > maxSpeed){ // Speed limit in positive direction (Right)
                velocityX = maxSpeed;
            }
            else{                     // Speed limit in negative direction (Left)
                velocityX = -maxSpeed;
            }
        }
    }
    public void sprint(){
        //Allows player to move faster if they have enabled hyperspeed
        if(hasHyperspeed && velocityX != 0){
            if(stamina - 1 > 0){
                sprinting = true;
                stamina -= 0.1;
            }
        }
    }
    public void crouch(){
        //Allows the player to crouch
        if(isCasting || isAttacking || !onGround || isDying){
            return;
        }
        isCrouching = true;
    }
    public void unCrouch(){
        isCrouching = false;
    }
    public void jump(int type){
        // If the player is doing an action, don't let them jump
        if(isCasting || isAttacking || isDying){
            return;
        }
        if(type == INITIAL && (onGround || ( hasDoubleJump && jumpCount < 2))){
            spriteCount = 0;
            onGround = false;
            isHurt = false;
            velocityY = -6;
            airAttackNum = 1;
            jumpSound.play();
            jumpCount++;
        }
        else if(type == NORMAL){
            holdingJump = true;

        }
    }
    public void attack(){
        //Method that allows the player to attack
        if(isAttacking || isCasting || isDying) {
            return;
        }
        if((stamina - 5) > 0){
            if(onGround){
                isAttacking = true;
                groundAttackNum++;
                if(groundAttackNum >= groundAttackSprites.length){
                    groundAttackNum = 0;
                }
            }
            else{
                isAttacking = true;
                airAttackNum++;
                if(airAttackNum >= airAttackSprites.length){
                    airAttackNum = 0;
                }
            }
            // If the attacking checks passed, reset sprite and remove stamina
            if(!hasEnergyPower()){
                stamina -= 5;
            }
            isHurt = false;
            spriteCount = 0;
        }
        else{
            textQueue.add(new IndicatorText(getHitbox().x, getHitbox().y, "Stamina Low!", Color.RED));
        }
    }
    public void castMagic(int targetX, int targetY){
        Rectangle hitbox = getHitbox();
        // Getting the correct attackBox
        int originalDir = getDirection();
        if(targetX < hitbox.x){
            direction = LEFT;
        }
        else{
            direction = RIGHT;
        }
        int attackDir = direction;
        Rectangle attackBox = getAttackBox();
        direction = originalDir;
        // Making sure that the target for the projectile is not awkward
        double angle = Math.atan(((double)targetY - attackBox.y)/(targetX - attackBox.x));
        if(hasCastScope) {
            // When the projectile angle is too steep
            if ((targetX > hitbox.x && targetX < hitbox.getMaxX()) || (attackDir == RIGHT && Math.abs(angle) > 1.2) || (attackDir == LEFT && Math.abs(angle) > 1.4)) {
                textQueue.add(new IndicatorText(getHitbox().x - 30, getHitbox().y, "Angle too steep!", Color.RED));
                return;
            }
            // When the projectile target is too close to the player
            else if((attackDir == LEFT && targetX > attackBox.getMaxX() - 20) || (attackDir == RIGHT  && targetX < attackBox.x)){
                textQueue.add(new IndicatorText(getHitbox().x - 30, getHitbox().y, "Target too close!", Color.RED));
                return;
            }
        }
        castTargetX = targetX; castTargetY = targetY;
        castMagic();
    }
    public void castMagic(){
        if(isAttacking || isCasting || !onGround){
            return;
        }
        if((stamina - 10) > 0){
            if(!hasEnergyPower()){
                stamina -= 10;
            }
            isCasting = true;
            isHurt = false;
            spriteCount = 0;
        }
        else{
            textQueue.add(new IndicatorText(getHitbox().x, getHitbox().y, "Stamina Low!", Color.RED));
        }
    }
    // Method to update the Player Object each frame
    public void update(){
        updateMotion();
        updateStamina();
        checkOutOfBounds();
        updateSprite();
        //checkHealth();
    }
    // Method to calculate and apply the physics of the Player
    public void updateMotion(){
        // Updating position from velocities
        x += velocityX;
        y += velocityY;
        // Additional movement given by hyperspeed
        if(sprinting){
            x += velocityX * 0.5;
        }
        // Applying friction force
        if(onGround){ // Friction only applies when the Player is on the ground
            if(velocityX > 0){
                velocityX -= acceleration/2;
                if(velocityX < 0){ // Stopping motion when friction forces movement backwards
                    velocityX = 0;
                }
            }
            else if(velocityX < 0){ // Same as above but for the other direction
                velocityX += acceleration/2;
                if(velocityX > 0){
                    velocityX = 0;
                }
            }
        }
        // Applying gravity
        if(velocityY < 0 && holdingJump){ // If the player is jumping and holding the jump key, use lower gravity to allow for a variable jump height
            velocityY += GRAVITY / 3;
            holdingJump = false; // Resetting the variable so it doesn't get applied next frame without input
        }
        else{ // Otherwise use normal gravity values
            velocityY += GRAVITY;
        }
        // Checking if the Player is falling (This will update onGround when the Player leaves a platform without jumping)
        if(onGround && velocityY > 1){
            onGround = false;
            jumpCount = 1;
            if(isCasting || isAttacking){ // Cancelling any spell cast or attack
                isCasting = false;
                isAttacking = false;
                spriteCount = 0;
            }
        }
        // Resetting movement booleans so they can be set next frame
        onMovingPlat = false; sprinting = false;
    }
    public void updateStamina(){
        if(isCasting || isAttacking || hasEnergyPower()){
            return; // No regeneration during casting/attacks
        }
        else if(velocityX != 0 || !onGround) {
            stamina += 0.01; // Slow regeneration while in motion
        }
        else{
            stamina += 0.07; // Faster regeneration while standing still
        }
        // Making sure stamina doesn't exceed maximum
        if(stamina > maxStamina){
            stamina = maxStamina;
        }
    }
    // Method to keep the Player within the confines of the game
    public void checkOutOfBounds(){
        // Using the hitbox for true X coordinate values since the sprite pictures are larger than the actual player
        Rectangle hitbox = getHitbox();
        if(hitbox.x < 0){ // Player moves offscreen (from the left side)
            int extraMovement = hitbox.x;
            x -= extraMovement; // Shifting the player back into the correct position
        }
    }
    // Method to smoothly update the sprite counter and produce realistic animation of the Player
    public void updateSprite(){
        if(isDying){
            if(spriteCount < dyingSprites.length - 0.05){
                spriteCount += 0.05;
            }
        }
        else if(isCasting){
            if(hasInstantCast){
                spriteCount+=0.30;
            }
            else{
                spriteCount += 0.06;
            }
            if(spriteCount > castSprites.length){
                isCasting = false;
                spriteCount = 0;
            }
        }
        else if(isAttacking){
            spriteCount += 0.1;
            if((onGround && spriteCount > groundAttackSprites[groundAttackNum].length) || (!onGround && spriteCount > airAttackSprites[airAttackNum].length) ){
                isAttacking = false;
                spriteCount = 0;
            }
        }
        else if(isHurt){
            spriteCount += 0.07;
            if(spriteCount > hurtSprites.length){
                spriteCount = 0;
                isHurt = false;
            }
        }
        else if(velocityY < 0){ // Jumping sprites
            if(spriteCount < 1){ // Only playing the first frame only once (no repetition)
                spriteCount += 0.1;
            }
        }
        else if(velocityY > 0 && !onGround){ // Falling sprites
            spriteCount += 0.05 + (Math.pow(velocityY,1.5)/100);
            if(spriteCount > fallingSprites.length){
                spriteCount = 0;
            }
        }
        else if(velocityX != 0){ // Running sprites
            spriteCount += 0.05 + (Math.abs(velocityX)/90); // Scaling sprite speed with player velocity
            if(isCrouching){
                if(spriteCount > slidingSprites.length){
                    spriteCount = 0;
                }
            }
            else{
                if(spriteCount > runSprites.length){
                    spriteCount = 0;
                }
            }

        }
        else{ // Idling sprites
            spriteCount += 0.05;
            if(isCrouching){
                if(spriteCount > crouchSprites.length){
                    spriteCount = 0;
                }
            }
            else{
                if(spriteCount > idleSprites.length){
                    spriteCount = 0;
                }
            }

        }
    }
    public void checkCollision(LevelProp prop){
        //This method checks the collision between the platforms and the player
        Rectangle rect = prop.getRect();
        Rectangle hitbox = getHitbox();
        if(hitbox.intersects(rect)){
            if((int)((hitbox.y + hitbox.height) - velocityY) <= rect.y){
                y = (rect.y - hitbox.height) - (hitbox.y - y); //
                velocityY = 0;
                onGround = true;
                jumpCount = 0;
            }
        }
        if(prop.isMoving() && !onMovingPlat && onGround){//Moving player position for moving platforms
            if(rect.contains(hitbox.x+hitbox.width, hitbox.y+hitbox.height + 1) || rect.contains(hitbox.x, hitbox.y+hitbox.height + 1)){
                x += prop.getXSpeed();
                y += prop.getYSpeed();
                onMovingPlat = true;
            }
        }
    }
    public void gainItem(Item item){
        //This method adds the benefits from an item that the player obtained
        int type = item.getType();
        if(type == Item.COIN){
            points += 100;
        }
        else if(type == Item.DIAMOND){
            points += 1000;
        }
        else if(type == Item.HEALTH){
            if(health+10<=100) {
                health += 10;
            }
            else{
                health = 100;
            }
        }
        else if(type == Item.HEALTHPWR){
            healthTimer=30;
        }
        else if(type == Item.ENERGYPWR){
            energyTimer=30;
            if(stamina < 20){
                stamina = 20;
            }
        }
    }
    public void iterateTime(){
        //This method controls the timers for the energy and health power-ups
        if(energyTimer>0){
            energyTimer-=1;
        }
        if(healthTimer>0){
            healthTimer-=1;
        }
    }
    public void resetPos(int x, int y){
        //This method resets the position of the player
        this.x = x;
        this.y = y;
        velocityX = 0;
        velocityY = 0;
        spriteCount = 0;
    }
    public void setPos(int x, int y){
        //Method that sets the (X,Y) of the player to the given parameters
        this.x = x;
        this.y = y;
    }
    public void restoreHealth(){
        //Method that restores the health and stamina of the player to 100%
        health = maxHealth;
        stamina = maxStamina;
        isDying = false;
    }
    public void enemyHit(Enemy enemy){
        //Method that calculates the physical attacks inflicted by an enemy
        if(isDying){
            return; // Don't register hits while the player is dying
        }
        if(!hasHealthPower()){//damage is neglected if the player has the health power-up
            health -= enemy.getDamage();
            if(velocityX == 0 && !isAttacking && !isCasting){
                isHurt = true;
                spriteCount = 0;
            }
            textQueue.add(new IndicatorText(getHitbox().x, getHitbox().y, "-" + enemy.getDamage(), Color.RED));
        }
        if(health <= 0){//Player death
            isDying = true;
            spriteCount = 0;
        }
    }
    public void castHit(Projectile cast){
        //Method that calculates the damage done by the projectiles
        if(isDying){
            return; // Don't register hits while the player is dying
        }
        if(!hasHealthPower()) {//Damage is neglected if health power-up is activated
            health -= cast.getDamage();
            velocityY = -3;
            if (cast.getSpeed() > 0) {
                velocityX = 3;
            } else {
                velocityX = -3;
            }
            isHurt = true;
            isAttacking = false;
            spriteCount = 0;
            jumpCount = 2;
            if(health <= 0){
                isDying = true;
            }
            textQueue.add(new IndicatorText(getHitbox().x, getHitbox().y, "-" + cast.getDamage(), Color.RED));
        }
    }


    public ArrayList<IndicatorText> flushTextQueue(){
        ArrayList<IndicatorText> temp = textQueue;
        textQueue = new ArrayList<>();
        return temp;
    }
    // Getter methods
    // Method that returns the player's current sprite by looking at various fields
    public Image getSprite(){
        //Returns proper sprite for the player based on the player's situation
        Image sprite;
        int spriteIndex = (int)Math.floor(spriteCount);
        if(isDying){
            sprite = dyingSprites[spriteIndex];
        }
        else if(isCasting){
            sprite = castSprites[spriteIndex];
        }
        else if(isAttacking){
            if(onGround){
                sprite = groundAttackSprites[groundAttackNum][spriteIndex];
            }
            else{
                sprite = airAttackSprites[airAttackNum][spriteIndex];
            }
        }
        else if(isHurt){
            sprite = hurtSprites[spriteIndex];
        }
        else if(velocityY < 0){
            sprite = jumpingSprites[spriteIndex];
        }
        else if(velocityY > 0 && !onGround)  {
            sprite = fallingSprites[spriteIndex];
        }
        else if(velocityX != 0){
            if(isCrouching){
                sprite = slidingSprites[spriteIndex];
            }
            else{
                sprite = runSprites[spriteIndex];
            }
        }
        else{
            if(isCrouching){
                sprite = crouchSprites[spriteIndex];
            }
            else{
                sprite = idleSprites[spriteIndex];
            }
        }
        // Flipping the image since the sprites are all right-facing
        if(direction == LEFT){
            sprite = Utilities.flipSprite(sprite);
        }
        return sprite;
    }
    public Rectangle getHitbox(){
        // Since the sprite images are much larger than the actual Player, offsets must be applied
        if(isCrouching){
            return new Rectangle((int)x + 58, (int)y + 61, 36, 47);
        }
        return new Rectangle((int)x + 58, (int)y + 15, 36, 93);
    }
    public Rectangle getAttackBox(){
        //Returns rectangle object for the attackbox of the player
        int xPos = (int)x + 100;
        if(direction == LEFT){
            xPos = (int)x;
        }
        return new Rectangle(xPos, (int)y + 40, 50, 50);
    }
    public boolean isAttackFrame(){
        double middleFrame;
        if(onGround){
            middleFrame = (double)groundAttackSprites[groundAttackNum].length/2;
        }
        else{
            middleFrame = 1; // Air attacks are more responsive
        }
        if(isAttacking && Utilities.roundOff(spriteCount,1) == middleFrame){
            return true;
        }
        return false;
    }
    public boolean isCastFrame(){
        if(isCasting && Utilities.roundOff(spriteCount,2) == castSprites.length-1){
            return true;
        }
        return false;
    }
    public boolean hasAngledCast(){
        return hasCastScope && castTargetX != null && castTargetY != null;
    }
    public Integer getCastTargetX() {
        Integer temp = castTargetX;
        castTargetX = null;
        return temp;
    }
    public Integer getCastTargetY() {
        Integer temp = castTargetY;
        castTargetY = null;
        return temp;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getStamina() {
        return stamina;
    }
    public double getMaxStamina(){return maxStamina;}
    public int getHealth(){return health;}
    public int getMaxHealth(){return maxHealth;}
    public int getPoints(){return points;}
    public int getCastDamage(){return castDamage;}
    public int getSwordDamage(){return swordDamage;}
    public int getDirection(){return direction;}
    public int getEnergyTime(){return energyTimer;}
    public int getHealthTimer(){return healthTimer;}
    public boolean isAttacking(){return isAttacking;}
    public boolean isCasting(){return isCasting;}
    public boolean hasEnergyPower(){return energyTimer > 0;}
    public boolean hasHealthPower(){return healthTimer > 0;}
    public int getSwordUpgradeNum() {return swordUpgradeNum;}
    public int getCastUpgradeNum() {return castUpgradeNum;}
    public int getHealthUpgradeNum() {return healthUpgradeNum;}
    public int getStaminaUpgradeNum() {return staminaUpgradeNum;}
    public boolean hasCastScope() {return hasCastScope;}
    public boolean hasInstantCast() {return hasInstantCast;}
    public boolean hasDoubleJump() {return hasDoubleJump;}
    public boolean hasHyperspeed() {return hasHyperspeed;}


    public boolean isDead(){
        return isDying && Utilities.roundOff(spriteCount,1) == dyingSprites.length;
    }

    // Setter methods
    public void look(int direction){
        this.direction = direction;
    }
    public void addPoints(int addition){
        points += addition;
    }

    //Upgrade Methods
    public void upgradeSword(){
        swordUpgradeNum++;
        swordDamage *= 1.5;
        points -= 100;
    }
    public void upgradeCast(){
        castUpgradeNum++;
        castDamage *= 1.5;
        points -= 100;
    }
    public void upgradeHealth(){
        healthUpgradeNum++;
        maxHealth *= 1.25;
        points -= 100;
    }
    public void upgradeStamina(){
        staminaUpgradeNum++;
        maxStamina *= 1.25;
        points -= 100;
    }
    public void enableCastScope(){
        hasCastScope = true;
        points -= 100;
    }
    public void enableInstantCast(){
        hasInstantCast = true;
        points -= 100;
    }
    public void enableDoubleJump(){
        hasDoubleJump = true;
        points -= 100;
    }
    public void enableHyperspeed(){
        hasHyperspeed = true;
        points -= 100;
    }
}
