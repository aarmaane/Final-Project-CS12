import java.awt.*;

public class Spawner {
    private int timeLimit;
    private String enemyType;
    private int spawnX,spawnY;
    private int difficulty;
    public Spawner(String data){
        String[] dataSplit = data.split(",");
        spawnX = Integer.parseInt(dataSplit[0]);
        spawnY = Integer.parseInt(dataSplit[1]);
        timeLimit = Integer.parseInt(dataSplit[2]);
        enemyType = dataSplit[3];
        difficulty = Integer.parseInt(dataSplit[4]);
        //double spawnX, double spawnY,int timeLimit, Enemy enemy,int difficulty

    }
    public Enemy spawnEnemy(){
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
    public double getSpawnX(){ return spawnX;}
    public double getSpawnY(){ return spawnY;}
    public int getTimeLimit(){ return timeLimit;}
    public String getEnemyType(){ return enemyType;}
}
