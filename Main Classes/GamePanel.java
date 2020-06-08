// GamePanel.java
// Armaan Randhawa and Shivan Gaur
// Class that runs all of logic for the game and paints it on screen

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

class GamePanel extends JPanel implements KeyListener, MouseListener {
    // Window related Objects
    private boolean[] keysPressed; // Array that keeps track of keys that are pressed down
    private MainGame gameFrame;
    // Game related fields
    private Player player = new Player();
    private int levelNum, timeLeft;
    private int levelEndX, levelEndResetX; // Positions of end game loop
    private int levelOffset = 0; // Offset for level scrolling
    private boolean paused = false;
    // Game state related fields
    private int barFade = 0, barFadeAddition = 5; // Stat bar flashing fields
    private boolean specialEnding, levelEnding, pointsGiven, bossSpawned; // Level ending flags
    private int endScreenFrames, bonusPoints;
    // Game Images
    private Image staminaBar;
    private Image healthBar;
    private Background background;
    // Game Sounds
    private Sound levelMusic = new Sound("Assets/Sounds/Music/level1.wav", 80);
    private Sound castSound = new Sound("Assets/Sounds/Effects/cast.wav", 80);
    private Sound castHitSound = new Sound("Assets/Sounds/Effects/castHit.wav", 80);
    private Sound[] swordSounds = {new Sound("Assets/Sounds/Effects/sword1.wav", 80),
                                   new Sound("Assets/Sounds/Effects/sword2.wav", 80),
                                   new Sound("Assets/Sounds/Effects/sword3.wav", 80)};
    private Sound[] hitSounds = {new Sound("Assets/Sounds/Effects/hit1.wav", 80),
                                 new Sound("Assets/Sounds/Effects/hit2.wav", 80),
                                 new Sound("Assets/Sounds/Effects/hit3.wav", 80)};
    private Sound pauseSound = new Sound("Assets/Sounds/Effects/pause.wav", 80);
    // ArrayLists that hold game objects
    private ArrayList<LevelProp> platforms = new ArrayList<>();
    private ArrayList<LevelProp> noCollideProps = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private ArrayList<Spawner> spawners = new ArrayList<>();
    private ArrayList<Chest> chests = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<IndicatorText> indicatorText = new ArrayList<>();
    // Fonts
    private Font gameFont, gameFontBig, gameFontSmall;
    // Graphics related fields
    private Composite comp;
    private FadeEffect fade = new FadeEffect();

    // Constructor for GamePanel
    public GamePanel(MainGame game) throws IOException {
        // Setting up the GamePanel
        gameFrame = game;
        setSize(960,590);
        keysPressed = new boolean[KeyEvent.KEY_LAST+1];
        addKeyListener(this);
        addMouseListener(this);
        try{
            // Loading Images
            staminaBar = ImageIO.read(new File("Assets/Images/Player/staminaBar.png"));
            healthBar = ImageIO.read(new File("Assets/Images/Player/healthBar.png"));
            // Loading fonts
            gameFont = Font.createFont(Font.TRUETYPE_FONT, new File("Assets/Fonts/8BitFont.ttf"));
            gameFont = gameFont.deriveFont(30f);
            gameFontBig = gameFont.deriveFont(50f);
            gameFontSmall = gameFont.deriveFont(25f);
        }
        catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        // Initializing the game Classes
        Enemy.init();
        Slime.init();
        Skeleton.init();
        Ghost.init();
        Fire.init();
        Crystal.init();
        Boss.init();
        Projectile.init();
        Chest.init();
        Item.init();
    }

    // Method to load up all level Objects from the corresponding text files
    public void loadLevel(){
        // Emptying previous values
        platforms.clear();
        noCollideProps.clear();
        enemies.clear();
        chests.clear();
        projectiles.clear();
        items.clear();
        spawners.clear();
        levelEnding = false; pointsGiven = false; bossSpawned = false; endScreenFrames = 0; paused = false; bonusPoints = 0;
        try{
            // Setting up level fields
            ArrayList<String> levelData = Utilities.loadFile("LevelData.txt", levelNum);
            timeLeft = Integer.parseInt(levelData.get(0));
            levelEndX =  Integer.parseInt(levelData.get(1));
            levelEndResetX =  Integer.parseInt(levelData.get(2));
            levelMusic.closeSound();
            levelMusic = new Sound("Assets/Sounds/Music/" + levelData.get(3), 80);
            background = new Background(levelData.get(4), levelData.get(5));
            specialEnding = Boolean.parseBoolean(levelData.get(6));
            /* Loading Game-Object Arrays */

            // Loading platforms
            for(String data: Utilities.loadFile("DisappearingPlatforms.txt", levelNum)){
                platforms.add(new LevelProp(data, true, false));
            }
            for(String data: Utilities.loadFile("Platforms.txt", levelNum)){
                platforms.add(new LevelProp(data, false, false));
            }
            for(String data: Utilities.loadFile("MovingPlatforms.txt", levelNum)){
                platforms.add(new LevelProp(data, false, true));
            }
            // Loading level props without collision
            for(String data: Utilities.loadFile("NoCollideProps.txt", levelNum)){
                noCollideProps.add(new LevelProp(data, false, false));
            }
            for(String data: Utilities.loadFile("MovingNoCollide.txt", levelNum)){
                noCollideProps.add(new LevelProp(data, false, true));
            }
            // Loading enemies
            for(String data: Utilities.loadFile("Slimes.txt", levelNum)){
                enemies.add(new Slime(data));
            }
            for(String data: Utilities.loadFile("Skeletons.txt", levelNum)){
                enemies.add(new Skeleton(data));
            }
            for(String data: Utilities.loadFile("Ghosts.txt", levelNum)){
                enemies.add(new Ghost(data));
            }
            for(String data: Utilities.loadFile("Wizards.txt", levelNum)){
                enemies.add(new Wizard(data));
            }
            for(String data: Utilities.loadFile("Fires.txt", levelNum)){
                enemies.add(new Fire(data));
            }
            for(String data: Utilities.loadFile("Crystals.txt", levelNum)){
                enemies.add(new Crystal(data));
            }
            for(String data: Utilities.loadFile("Blobs.txt", levelNum)){
                enemies.add(new Blob(data));
            }
            for(String data: Utilities.loadFile("Spawners.txt", levelNum)){
                spawners.add(new Spawner(data));
            }
            // Loading chests
            for(String data: Utilities.loadFile("Chests.txt", levelNum)){
                chests.add(new Chest(data));
            }
        }
        catch (IOException e) {
            System.out.println("Level " + levelNum + " data incomplete!");
            e.printStackTrace();
        }
        // Resetting the Player
        player.restoreStamina();
        if(player.isDead()){
            player.restoreHealth();
        }
        player.resetPos(0 ,-112);
        // Resetting music
        levelMusic.play();
        if(Sound.isMuted()){ // Forcing mute if the game is already muted
            levelMusic.forceMute();
        }
        fade.start(FadeEffect.FADEIN, 3); // Starting fade in
    }

    // All window related methods
    public void addNotify() {
        super.addNotify();
        requestFocus();
        System.out.println("add notfiy");
    }
    public void removeNotify(){
        super.removeNotify();
        paused = true;
        System.out.println("remove notify");
    }
    public void paintComponent(Graphics g){
        // Setting up Graphics2D
        Graphics2D g2d = (Graphics2D) g;
        if(comp == null){
            comp = g2d.getComposite();
        }
        // Drawing the background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 960, 590);
        background.draw(g);
        // Drawing the level
        for(LevelProp platform: platforms){
            // Only drawing items that are visible on screen
            if(platform.getRect().x + platform.getRect().width - levelOffset > 0 && platform.getRect().x - levelOffset < 960){
                Rectangle platformRect = platform.getRect();
                if(platform.isTemporary()){ // Drawing transparency of temporary platforms
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, platform.getSpriteAlpha()));
                    g2d.drawImage(platform.getPropImage(), platformRect.x - levelOffset, platformRect.y, this);
                    g2d.setComposite(comp);
                }
                else{ // Drawing normal platform image
                    g.drawImage(platform.getPropImage(), platformRect.x - levelOffset, platformRect.y, this);

                }
            }
        }
        for(LevelProp prop: noCollideProps){ // Drawing the props with no collision
            Rectangle propRect = prop.getRect();
            g.drawImage(prop.getPropImage(), propRect.x - levelOffset, propRect.y, this);
        }
        // Drawing chests
        for(Chest chest: chests){
            g.drawImage(chest.getSprite(), chest.getHitbox().x-levelOffset,chest.getHitbox().y, this);
        }
        // Drawing enemies
        for(Enemy enemy: enemies){
            // Only drawing items that are visible on screen
            if(enemy.getHitbox().x + enemy.getHitbox().width - levelOffset > 0 && enemy.getHitbox().x - levelOffset < 960){
                if(enemy.hasAlphaSprites() && !enemy.isDying()){ // Drawing enemies with transparency
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, enemy.getSpriteAlpha()));
                    g2d.drawImage(enemy.getSprite(), (int)enemy.getX() - levelOffset, (int)enemy.getY(), this);
                    g2d.setComposite(comp);
                }
                else{ // Drawing normal enemies
                    g.drawImage(enemy.getSprite(), (int)enemy.getX() - levelOffset, (int)enemy.getY(), this);
                }
                enemy.drawHealth(g, levelOffset); // Drawing health bar of enemy
            }
        }
        // Drawing Projectiles
        for(Projectile projectile: projectiles){
            // Only drawing items that are visible on screen
            if(projectile.getHitbox().x + projectile.getHitbox().width - levelOffset > 0 && projectile.getHitbox().x - levelOffset < 960){
                g.drawImage(projectile.getSprite(),(int)projectile.getX()-levelOffset, (int)projectile.getY(),this);
            }
        }
        // Drawing items
        g.setColor(Color.RED);
        for(Item item: items){
            g.drawImage(item.getSprite(), item.getHitbox().x - levelOffset, item.getHitbox().y, this);
        }
        // Drawing indicator text
        g.setFont(gameFont);
        for(IndicatorText text: indicatorText){
            g.setColor(text.getColor());
            g.drawString(text.getString(), text.getX() - levelOffset, text.getY());

        }
        // Drawing the Player
        g.drawImage(player.getSprite(), (int)player.getX() - levelOffset, (int)player.getY(), this);
        // Drawing game stats
        /*Fills in both of the stat bars from darker shades to lighter shades by increasing the respective rgb value by 1 while shifting the
        rectangle over each time. */
        for(int i = 0; i < 100; i++) {
            //Health
            g.setColor(new Color(155 + i, 0, 0));//Changing colour
            g.fillRect(59+i, 30, (int) (((double) player.getHealth() / player.getMaxHealth()) * 198)-i, 14);
            if(player.getHealthTimer()>0){
                g.setColor(new Color(255,20,100+i, barFade));
                g.fillRect(59+i, 30, (int) (((double) player.getHealth() / player.getMaxHealth()) * 198)-i, 14);
            }
            //Stamina
            g.setColor(new Color(0, 155 + i, 0));
            g.fillRect(59 + i, 83, (int) ((player.getStamina() / player.getMaxStamina()) * 198) - i, 14);
            if(player.getEnergyTime()>0) {
                g.setColor(new Color(155 + i, 155 + i, 0, barFade));
                g.fillRect(59 + i, 83, (int) ((player.getStamina() / player.getMaxStamina()) * 198) - i, 14);
            }
        }
        // Drawing time left for powerups
        if(player.getHealthTimer() > 0){
            g.setColor(new Color(255,20,200));
            g.drawString(""+player.getHealthTimer(),267,41);
        }
        if(player.getEnergyTime() > 0){
            g.setColor(new Color(255 , 255 , 0));
            g.drawString("" + player.getEnergyTime(), 267, 96);
        }
        // Drawing the stats
        g.setColor(Color.BLACK);
        g.drawImage(healthBar, 10,10,this);
        g.drawImage(staminaBar, 10,65,this);
        g.drawString("Time: "+timeLeft,800,20);
        g.setFont(gameFontSmall);
        g.drawString("Points: "+ player.getPoints(),70,68);
        if(Sound.isMuted()){
            g.drawString("Sound muted", 600, 20);
        }
        // Drawing various special screens
        if(levelEnding){
            drawEnding(g);
        }
        if(paused){
            drawPause(g);
        }
        if(fade.isActive()){
            fade.draw(g);
        }
    }

    // Drawing the transparent pause menu on top of the game
    public void drawPause(Graphics g){
        g.setColor(new Color(0,0,0, 100));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.WHITE);
        g.setFont(gameFont);
        g.drawString("Press ESC to unpause", 335, 330);
        g.setFont(gameFontBig);
        g.drawString("Paused", 400, 300);
    }

    // Drawing the end of level text
    public void drawEnding(Graphics g){
        // Level complete text
        g.setFont(gameFontBig);
        g.setColor(Color.BLACK);
        g.drawString("Level Complete!", getWidth()/2 - 173, 180);
        if(endScreenFrames > 200){ // Once enough time passes, draw the bonus points text
            g.setFont(gameFont);
            String displayString = "Time bonus: " + bonusPoints;
            int stringSize = g.getFontMetrics().stringWidth(displayString);
            g.drawString(displayString, getWidth()/2 - stringSize/2, 200);

        }
    }
    // Keyboard related methods
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode > keysPressed.length){
            return; // If the key entered is unrecognized, ignore this call of keyPressed
        }
        // Running code for initially clicked keys
        if(!keysPressed[keyCode]){
            if(keyCode == KeyEvent.VK_M){ // Mute/Unmute
                Sound.toggleVolume();
            }
            else if(keyCode == KeyEvent.VK_ESCAPE){ // Pause
                paused = !paused;
                if(paused){
                    Sound.pauseAll(); // Stopping sound
                    pauseSound.stop(); pauseSound.play();
                }
                else{
                    Sound.resumeAll(); // Resuming previously stopped sounds
                }
                repaint();
            }
            else if(!paused && (!levelEnding || specialEnding)){ // Making sure player can send inputs
                if(keyCode == KeyEvent.VK_SPACE || keyCode == KeyEvent.VK_W){ // Initial jumping
                    player.jump(Player.INITIAL);
                }
                else if(keyCode == KeyEvent.VK_O){ // Attacking
                    player.attack();
                }
                else if(keyCode == KeyEvent.VK_P){ // Magic casting
                    player.castMagic();
                }
            }
        }
        // Keeping track of whether or not the key is pressed down
        keysPressed[keyCode] = true;
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() > keysPressed.length){
            return; // If the key entered is unrecognized, ignore this call of keyReleased
        }
        // Keeping track of whether or not the key is pressed down
        keysPressed[e.getKeyCode()] = false;
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {
        if(!paused && (!levelEnding || specialEnding)){ // Making sure player can send inputs
            // Declaring important variables
            Point mousePos = getMousePosition();
            Rectangle playerHitbox = player.getHitbox();
            boolean wasPerformingAction = player.isAttacking() || player.isCasting();
            // Handling input
            if(e.getButton() == MouseEvent.BUTTON1){
                player.attack();
            }
            else if(e.getButton() == MouseEvent.BUTTON3){
                player.castMagic(mousePos.x + levelOffset, mousePos.y);
            }
            // Checking if the player has performed an action and correcting his direction
            if(wasPerformingAction != (player.isAttacking() || player.isCasting())){
                if(playerHitbox.x - levelOffset > mousePos.x){
                    player.look(Player.LEFT);
                }
                else{
                    player.look(Player.RIGHT);
                }
            }
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) { }

    // Game related methods
    // Method that updates all game objects
    public void update(){
        player.update(specialEnding); // Updating player
        indicatorText.addAll(player.flushTextQueue()); // Taking indicator texts out from the player
        // Updating enemies
        for(Enemy enemy: enemies){
            if(enemy.isActive()){ // Only update active enemies
                enemy.update(player);
                checkEnemyCast(enemy);
            }
            else{
                checkActivation(enemy);
            }
        }
        for(Projectile projectile: projectiles){
            projectile.update();
        }
        for(Item item: items){
            item.update();
        }
        for(IndicatorText text: indicatorText){
            text.update();
        }
        for(LevelProp platform: platforms){
            updateProp(platform);
        }
        for(LevelProp prop: noCollideProps){
            updateProp(prop);
        }
        for(Spawner spawner: spawners){
            if(spawner.spawnQueued()) {
                spawnEnemy(spawner);
            }
        }
        // Updating general game status
        checkPlayerAction();
        collectGarbage();
    }

    // Method that spawns an enemy from a spawner object
    public void spawnEnemy(Spawner spawner){
        if(Math.abs(player.getX()-spawner.getSpawnX()) <= 700){
            enemies.add(spawner.spawnEnemy());
        }
    }

    // Method that updates special level props
    public void updateProp(LevelProp prop){
        // Checking if it's temporary and needs to disappear
        if(prop.isTemporary() && player.getX() > prop.getDisappearX() && player.getY() > prop.getDisappearY()){
            prop.disappear();
        }
        // Checking if it's moving and updating the platform
        if(prop.isMoving()){
            prop.updateMovement();
        }
    }

    // Method that activates enemies that are close to the player
    public void checkActivation(Enemy enemy){
        if(!enemy.isDying() && Math.abs(enemy.getHitbox().x - player.getHitbox().x) < 1000){
            enemy.activate();
        }
    }

    // Method that checks for any actions that the player will perform
    public void checkPlayerAction(){
        // Checking if the Player has used their sword attack
        if(player.isAttackFrame()){ // Checking if this is the frame where attacks land
            int randomSwordIndex = Utilities.randint(0,2);
            swordSounds[randomSwordIndex].stop();
            swordSounds[randomSwordIndex].play(); // Playing the sword sound effect
            // Going through each enemy and checking for collisions
            for(Enemy enemy:enemies){
                if(player.getAttackBox().intersects(enemy.getHitbox()) && !enemy.isDying()){
                    // Dealing damage and making a indicator text to display damage dealt
                    double damageDone = enemy.swordHit(player);
                    damageDone = Utilities.roundOff(damageDone, 1);
                    player.addPoints(player.getSwordDamage());
                    indicatorText.add(new IndicatorText(enemy.getHitbox().x, enemy.getHitbox().y, "-" + damageDone, Color.ORANGE));
                    int randomHitIndex = Utilities.randint(0,2);
                    hitSounds[randomHitIndex].stop();
                    hitSounds[randomHitIndex].play(); // Playing the hit effect
                }
            }
        }
        // Checking if the Player has cast
        if(player.isCastFrame()){
            // Setting up the projectile variables
            Rectangle hitBox = player.getHitbox();
            Rectangle attackBox = player.getAttackBox();
            int speed = 5;
            int xPos = attackBox.x;
            // Creating the projectile
            if(player.hasAngledCast()){ // Angled projectile
                if(player.getDirection() == Player.LEFT){
                    xPos = (int)attackBox.getMaxX() - 20;
                }
                projectiles.add(new Projectile(Projectile.PLAYER, xPos ,hitBox.y + hitBox.height / 2.0 - 5 ,player.getCastTargetX(),player.getCastTargetY(),player.getCastDamage(),speed));
            }
            else { // Straight projectile
                if(player.getDirection() == Player.RIGHT){
                    xPos -= 120;
                }
                else{
                    speed = -speed;
                }
                projectiles.add(new Projectile(Projectile.PLAYER, xPos, hitBox.y + hitBox.height / 2.0 - 5, player.getCastDamage(), speed));
            }
            castSound.play(); // Playing cast sound
        }
        // Check if the player is dead
        if(player.isDead() && !fade.isActive()){ // Fade out death
            fade.start(FadeEffect.FADEOUT, 1);
        }
        if(player.getHitbox().y > getHeight() && player.getHealth() > 0){ // Player falls off screen
            player.kill();
        }
    }

    // Method to check if an enemy has produced a cast
    public void checkEnemyCast(Enemy enemy){
        // Only certain enemies can cast
        if(enemy.isCastFrame()){
            // Declaring hitboxes
            Rectangle wizardHitBox = enemy.getHitbox();
            Rectangle playerHitbox = player.getHitbox();
            // Setting up projectile fields
            int startX = wizardHitBox.x - 10;
            int speed = 2;
            String spriteType  = enemy.getSpriteType();
            int projectileType;
            if(spriteType.equals("")){
                projectileType = Projectile.ENEMY;
            }
            else{
                projectileType =  Projectile.ELITEENEMY;
            }
            if(enemy.getDirection() == Enemy.RIGHT){
                startX = wizardHitBox.x + wizardHitBox.width + 10;
            }
            // Creating the appropriate cast
            if(enemy.getCastType() == Wizard.CAST2){
                projectiles.add(new Projectile(projectileType, startX, wizardHitBox.y, playerHitbox.x, playerHitbox.y, enemy.getDamage(), speed));
                castSound.play();
            }
            else if(enemy.getCastType() == Wizard.CAST1){
                // Making ten random casts towards the player
                for(int i = 0; i < 10; i++){
                    projectiles.add(new Projectile(projectileType, playerHitbox.x+Utilities.randint(-450,450), 0, playerHitbox.x+Utilities.randint(-150,150), 590, enemy.getDamage(), speed));
                    castSound.play();
                }
            }
        }
    }

    // Method that removes objects that are no longer in use
    public void collectGarbage(){
        // Using removeIf for Arrays that only need removal of items
        projectiles.removeIf(Projectile::isDoneExploding);
        items.removeIf(item -> (item.isUsed() || item.getHitbox().y > this.getHeight()));
        platforms.removeIf(LevelProp::isDoneDisappearing);
        indicatorText.removeIf(IndicatorText::isDone);
        // Using for loops for Arrays that need to keep track of removals
        for(int i = enemies.size() - 1; i >= 0; i--){
            Enemy enemy = enemies.get(i);
            if(enemy.isDead()){ // If player kills the enemy
                enemies.remove(i); // Deleting enemy
                if(!enemy.hasLimitedTime()) {
                    // Giving points
                    player.addPoints(150 * enemy.getDifficulty());
                    indicatorText.add(new IndicatorText(enemy.getHitbox().x, enemy.getHitbox().y, "+100", Color.YELLOW));
                }
            }
            else if(enemy.getY() > this.getHeight()){ // If the enemy falls off screen
                enemies.remove(i); // Deleting enemy
                if(enemy.hasOutOfBoundsPoints()){
                    // Giving points
                    player.addPoints(100);
                    indicatorText.add(new IndicatorText(enemy.getHitbox().x, enemy.getHitbox().y, "+100", Color.YELLOW));
                }
            }
        }
    }

    // Method to check collisions between game objects
    public void checkCollision(){
        // Checking collision with Level platforms
        for(LevelProp platform: platforms){
            player.checkCollision(platform); // Player collision
            for(Enemy enemy: enemies){ // Enemy collision
                enemy.checkCollision(platform);
            }
            for(Item item: items){ // Item collision
                item.checkCollision(platform);
            }
        }
        // Checking projectile collision
        for(Projectile projectile:projectiles){
            for(Enemy enemy:enemies) {
                if(projectile.getType() == Projectile.PLAYER) { // Checking if player casts are hitting enemies
                    if (!enemy.isDying() && !projectile.isExploding() && enemy.getHitbox().intersects(projectile.getHitbox())) {
                        // Dealing damage, creating indicator text, playing sound
                        double damageDone = enemy.castHit(projectile);
                        damageDone = Utilities.roundOff(damageDone, 1);
                        player.addPoints((int) projectile.getDamage());
                        projectile.explode();
                        castHitSound.stop();
                        castHitSound.play();
                        indicatorText.add(new IndicatorText(enemy.getHitbox().x, enemy.getHitbox().y, "-" + damageDone, Color.ORANGE));

                    }
                }
                else{ // Checking if enemy casts are hitting the player
                    if (!projectile.isExploding() && player.getHitbox().intersects(projectile.getHitbox())) {
                        // Dealing damage, creating indicator text, playing sound
                        player.castHit(projectile);
                        projectile.explode();
                        castHitSound.stop();
                        castHitSound.play();
                    }
                }
            }
        }
        //Checking item collision
        Rectangle playerHitbox = player.getHitbox();
        for(Item item: items){
            // Checking if the player is picking up an item
            if(item.getHitbox().intersects(playerHitbox) && item.isSettled()) {
                // Running item routines
                player.gainItem(item);
                item.use();
                item.playSound();
                // Creating appropriate indicator text
                if(item.getType() == Item.HEALTH){
                    indicatorText.add(new IndicatorText(playerHitbox.x, playerHitbox.y, "+10", Color.GREEN));
                }
                else if(item.getType() == Item.COIN){
                    indicatorText.add(new IndicatorText(playerHitbox.x, playerHitbox.y, "+100", Color.YELLOW));
                }
                else if(item.getType() == Item.DIAMOND){
                    indicatorText.add(new IndicatorText(playerHitbox.x, playerHitbox.y, "+1000", Color.YELLOW));
                }
            }
        }
        // Checking chest collision
        for(Chest chest: chests){
            Rectangle chestHitbox = chest.getHitbox();
            // Checking if player' s feet are on the chest and the chest is still closed
            if(chest.isClosed() && playerHitbox.intersects(chestHitbox) && (playerHitbox.y + playerHitbox.height) == (chestHitbox.y + chestHitbox.height)){
                chest.open();
                // Generating items
                for(int i = 0; i < chest.getQuantity(); i++){
                    items.add(new Item(chest));
                }
            }
        }
        // Checking if the player has reached the end of the level
        if(specialEnding){ // Checking if a special ending level is finished
             if(!bossSpawned && enemies.size() == 1){ // When only the special wizard is left
                 // Spawning the boss
                 enemies.clear();
                 enemies.add(new Boss("500,-300,20"));
                 bossSpawned = true;
             }
             else if(bossSpawned && enemies.size() == 0){ // When the boss is killed
                 // Finishing the level
                 levelEnding = true;
             }
        }
        else if(!levelEnding && playerHitbox.x > levelEndX){ // Checking if the player has reached the level ending start point
            // Starting level end routines
            levelEnding = true;
            background.ignoreNegative();
            enemies.clear();
        }
        else if(playerHitbox.x > levelEndResetX){ // Resetting the player in the level ending loop so it plays smoothly
            int overshoot = levelEndResetX - playerHitbox.x;
            player.setPos(levelEndX + overshoot, (int)player.getY());
            calculateOffset(); // Recalculating offset due to sudden movement
        }
    }

    // Method that updates the graphics related objects
    public void updateGraphics(){
        changeFade();
        calculateOffset();
        background.update(levelOffset);
        if(levelEnding){
            updateLevelEnd();
        }
    }

    // Updating and checking the level fade
    public void changeFade(){
        if(fade.isActive()){
            fade.update(); // Updating the fade object
            if(fade.isDoneFadeOut()){
                levelMusic.stop();
                // Sending the player to another panel
                if(levelNum < 5 || player.isDead()){ // Sending the user to the shop for next level prep
                    gameFrame.switchPanel(MainGame.SHOPPANEL);
                }
                else{ // Sending the player to the endpanel since it's the last level
                    gameFrame.switchPanel(MainGame.ENDPANEL);
                }
            }
        }
        // Allowing the powerup fade to continue
        barFade += barFadeAddition;
        if(barFade == 0 || barFade == 255){ // Reversing fade direction
            barFadeAddition *= -1;
        }
    }

    // Method that calculates the scrolling offset
    public void calculateOffset(){
        Rectangle hitbox = player.getHitbox();
        if(hitbox.x + hitbox.width > 480 && !specialEnding){ // No scrolling during first part of level and special level
            levelOffset = (hitbox.x + hitbox.width) - 480;
        }
        else{
            levelOffset = 0;
        }
    }

    // Method that updates the level end routines
    public void updateLevelEnd(){
        endScreenFrames++; // Updating counter
        if(endScreenFrames > 200){
            if(timeLeft > 0){ // Updating the bonus points field
                timeLeft--;
                bonusPoints += 10;
            }
            else if(!pointsGiven){ // Giving the bonus points to the player
                pointsGiven = true;
                player.addPoints(bonusPoints);
                indicatorText.add(new IndicatorText((int)player.getX(),(int)player.getY(), "+"+bonusPoints, Color.YELLOW));
            }
        }
        if(endScreenFrames == 600){ // Starting the fade out so the level ends
            fade.start(FadeEffect.FADEOUT, 1);
        }
    }

    // Method that checks for user keyboard input
    public void checkInputs(){
        if(levelEnding && !specialEnding){ // Forcing inputs during level end (not for special level)
            player.unCrouch(); // Undoing any crouch applied before the level end
            player.move(Player.RIGHT);
            return;
        }
        // Side-to-side movement inputs
        if(!(keysPressed[KeyEvent.VK_D] && keysPressed[KeyEvent.VK_A])){ // Not allowing both inputs at once
            if(keysPressed[KeyEvent.VK_D]){
                player.move(Player.RIGHT);
            }
            else if(keysPressed[KeyEvent.VK_A]){
                player.move(Player.LEFT);
            }
        }
        // Sliding input
        if(keysPressed[KeyEvent.VK_S]){
            player.crouch();
        }
        else{ // Undoing crouching flags
            player.unCrouch();
        }
        // Jumping input
        if(keysPressed[KeyEvent.VK_SPACE] || keysPressed[KeyEvent.VK_W]){
            player.jump(Player.NORMAL);
        }
        // Hyperspeed input
        if(keysPressed[KeyEvent.VK_SHIFT]){
            player.sprint();
        }
    }

    // Method that keeps track of time for game objects (should be called every second)
    public void iterateTime(){
        if(!levelEnding){ // Updating game timer
            timeLeft-=1;
        }
        // Updating game entity timers
        player.iterateTime();
        for(Enemy enemy: enemies){
            enemy.iterateTime();
        }
        for(Spawner spawner: spawners){
            spawner.iterateTime();
        }
    }
    // Setter methods
    public void setLevelNum(int level){
        levelNum = level;
    }
    public void resetGame(){
        player = new Player();
        levelNum = 0;
    }
    // Getter methods
    public Player getPlayer(){
        return player;
    }
    public Font getGameFont(){
        return gameFont;
    }
    public int getLevelNum(){
        return levelNum;
    }
    public boolean isPaused(){
        return paused;
    }
}