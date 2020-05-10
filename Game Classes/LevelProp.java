import org.w3c.dom.css.Rect;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class LevelProp {
    // Hashmap to store all platform images with quick access and without duplication
    private static HashMap<String, Image> imageMap = new HashMap<>();
    // Object fields
    private double x, y;
    private int width, height;
    private String imageName;
    // Transparency related fields
    private boolean temporary;
    private int disappearX, disappearY;
    private float alpha = (float) 1.0;
    private float propAlpha =  (float) -0.02;
    private boolean disappearing, doneDisappearing;
    // Movement related fields
    private boolean moving;
    private int xLimit, yLimit;
    private double xMove, yMove, xSpeed, ySpeed;
    public LevelProp(String data, boolean temporary, boolean moving){
        // Setting up prop fields
        String[] dataSplit = data.split(",");
        x = Integer.parseInt(dataSplit[0]);
        y = Integer.parseInt(dataSplit[1]);
        imageName = dataSplit[2];
        if(temporary){
            this.temporary = true;
            disappearX = Integer.parseInt(dataSplit[3]);
            disappearY = Integer.parseInt(dataSplit[4]);
        }
        if(moving){
            this.moving = true;
            xLimit = Integer.parseInt(dataSplit[3]);
            yLimit = Integer.parseInt(dataSplit[4]);
            xSpeed = Double.parseDouble(dataSplit[5]);
            ySpeed = Double.parseDouble(dataSplit[6]);
        }
        // Loading the prop image
        Image propImage = null;
        try{
            if(!imageMap.containsKey(imageName)){
                propImage = ImageIO.read(new File("Assets/Images/Level Props/" + imageName));
                imageMap.put(imageName, propImage);
            }
            else{
                propImage = imageMap.get(imageName);
            }
        }
        catch (IOException e) {
            System.out.println(imageName);
            System.out.println("Level prop picture missing!");
            e.printStackTrace();
            System.exit(1);
        }
        width = propImage.getWidth(null);
        height = propImage.getHeight(null);
    }
    public void updateMovement(){
        // Updating movement
        xMove += xSpeed;
        yMove += ySpeed;
        // Checking limits
        if(Math.abs(xMove) >= xLimit || xMove == 0){
            xSpeed *= -1;
        }
        if(Math.abs(yMove) >= yLimit || yMove == 0){
            ySpeed *= -1;
        }
    }

    // Getter methods
    public Image getPropImage(){
        return imageMap.get(imageName);
    }
    public Rectangle getRect(){
        if(moving){
            return new Rectangle((int)(x + xMove), (int)(y + yMove), width, height);
        }
        return new Rectangle((int)x, (int)y, width, height);
    }
    public float getSpriteAlpha(){
        if(disappearing) {
            alpha += propAlpha;
            if (Utilities.roundOff(alpha, 2) == 0) {
                doneDisappearing = true;
            }

        }
        return alpha ;
    }
    // Transparency related getter methods
    public boolean isTemporary(){return temporary;}
    public boolean isMoving(){return moving;}
    public void disappear(){ disappearing = true;}
    public boolean isDoneDisappearing(){ return doneDisappearing;}
    public int getDisappearX() {
        return disappearX;
    }
    public int getDisappearY() {
        return disappearY;
    }
    public double getXSpeed() {
        return xSpeed;
    }

    public double getYSpeed() {
        return ySpeed;
    }
}
