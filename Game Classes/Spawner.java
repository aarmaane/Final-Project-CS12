// Spawner.java
// Armaan Randhawa and Shivan Gaur
// Class creates an object that spawns the desired type of enemy

public class Spawner {
    //Fields
    private int timeLimit; // Time limit for spawned enemies
    private String enemyType;
    private int spawnX,spawnY;
    private int difficulty;
    private int spawnDelay,spawnDelayMax;
    private boolean queuedSpawn; // Boolean if enemy needs to be spawned

    // Constructor
    public Spawner(String data){
        String[] dataSplit = data.split(",");
        // Setting up fields based on String
        spawnX = Integer.parseInt(dataSplit[0]);
        spawnY = Integer.parseInt(dataSplit[1]);
        timeLimit = Integer.parseInt(dataSplit[2]);
        spawnDelayMax = Integer.parseInt(dataSplit[3]);
        spawnDelay = spawnDelayMax;
        enemyType = dataSplit[4];
        difficulty = Integer.parseInt(dataSplit[5]);

    }

    // Method to update the delay counter of the spawner
    public void iterateTime(){
        spawnDelay--;
        // When timer reaches zero, reset timer and set spawn flag to true
        if(spawnDelay < 0){
            queuedSpawn = true;
            spawnDelay = spawnDelayMax;
        }
    }
    public Enemy spawnEnemy(){
        //This method returns the proper enemy that needs to be spawned
        Enemy enemy = null;
        if(enemyType.equals("Ghost")) {
            enemy = new Ghost("" + spawnX + "," + spawnY + "," + difficulty);
            enemy.setTimeLimit(timeLimit);
        }
        else if(enemyType.equals("Slime")) {
            enemy = new Slime("" + spawnX + "," + spawnY + "," + difficulty);
            enemy.setTimeLimit(timeLimit);
        }
        else if(enemyType.equals("Skeleton")) {
            enemy = new Skeleton("" + spawnX + "," + spawnY + "," + difficulty);
            enemy.setTimeLimit(timeLimit);
        }
        else if(enemyType.equals("Wizard")) {
            enemy = new Wizard("" + spawnX + "," + spawnY + "," + difficulty);
            enemy.setTimeLimit(timeLimit);
        }
        else if(enemyType.equals("Crystal")) {
            enemy = new Crystal("" + spawnX + "," + spawnY + "," + difficulty);
            enemy.setTimeLimit(timeLimit);
        }
        return enemy;
    }

    //Getter methods
    public double getSpawnX(){ return spawnX;}
    public double getSpawnY(){ return spawnY;}
    public int getTimeLimit(){ return timeLimit;}
    public String getEnemyType(){ return enemyType;}

    // Method to return true if and enemy needs to be spawned and resets the flag
    public boolean spawnQueued(){
        boolean tmp = queuedSpawn;
        queuedSpawn = false;
        return tmp;
    }
}
