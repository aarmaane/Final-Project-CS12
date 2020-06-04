//Spawner.java
//Armaan Randhawa and Shivan Gaur
//This class creates an object that spawns the desired type of enemy

public class Spawner {
    //Fields
    private int timeLimit;
    private String enemyType;
    private int spawnX,spawnY;
    private int difficulty;
    public Spawner(String data){
        //Constructor
        String[] dataSplit = data.split(",");
        spawnX = Integer.parseInt(dataSplit[0]);
        spawnY = Integer.parseInt(dataSplit[1]);
        timeLimit = Integer.parseInt(dataSplit[2]);
        enemyType = dataSplit[3];
        difficulty = Integer.parseInt(dataSplit[4]);

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
        return enemy;



    }
    //Getter methods
    public double getSpawnX(){ return spawnX;}
    public double getSpawnY(){ return spawnY;}
    public int getTimeLimit(){ return timeLimit;}
    public String getEnemyType(){ return enemyType;}
}
