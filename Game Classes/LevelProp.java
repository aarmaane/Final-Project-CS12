// LevelProp.java
// Armaan Randhawa and Shivan Gaur
// Class creates level props that allow for collision and decoration with the ability to move and dissappear

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class LevelProp {
    // Static hashmap to store all platform images with quick access and without duplication
    private static HashMap<String, Image> imageMap = new HashMap<>();
    // Fields
    private double x, y;
    private int width, height;
    private String imageName; // File path of the LevelProp image (Used as a key in the hashmap)
    // Transparency related fields
    private boolean temporary;
    private int disappearX, disappearY;
    private float alpha = (float) 1.0;
    private float propAlpha =  (float) -0.02;
    private boolean disappearing, doneDisappearing;
    // Movement related fields
    private boolean moving;
    private int xLimit, yLimit; // The limits for the platform movement
    private double xMove, yMove, xSpeed, ySpeed; // Field for the amount the prop has shifted and the speed

    //Constructor
    public LevelProp(String data, boolean temporary, boolean moving){
        // Setting up prop fields
        String[] dataSplit = data.split(",");
        x = Integer.parseInt(dataSplit[0]);
        y = Integer.parseInt(dataSplit[1]);
        imageName = dataSplit[2];
        // Setting up fields for special prop types
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
                // Loading the image into the HashMap if it isn't already there
                propImage = ImageIO.read(new File("Assets/Images/Level Props/" + imageName));
                imageMap.put(imageName, propImage);
            }
            else{
                // Us
                propImage = imageMap.get(imageName);
            }
        }
        catch (IOException e) {
            System.out.println(imageName);
            System.out.println("Level prop picture missing!");
            e.printStackTrace();
            System.exit(1);
        }
        // Using the image to set the hitbox of the prop
        width = propImage.getWidth(null);
        height = propImage.getHeight(null);
    }

    // Method to update the position of moving LevelProps
    public void updateMovement(){
        // Adding speed to the position fields
        xMove += xSpeed;
        yMove += ySpeed;
        // Checking limits (and reversing direction of movement)
        if(Math.abs(xMove) >= xLimit || xMove == 0){
            xSpeed *= -1;
        }
        if(Math.abs(yMove) >= yLimit || yMove == 0){
            ySpeed *= -1;
        }
    }

    // Getter methods
    // Method to return the image of the prop
    public Image getPropImage() {
        // Looking up the image by using the file path as a key
        return imageMap.get(imageName);
    }

    //Method that returns the rectangle object that's the hitbox of the level prop
    public Rectangle getRect(){
        if(moving){
            // Accounting for movement of the prop
            return new Rectangle((int)(x + xMove), (int)(y + yMove), width, height);
        }
        return new Rectangle((int)x, (int)y, width, height);
    }

    // Method that returns the alpha value of the disappearing platforms and iterates the value
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

    // Movement related getter methods
    public double getXSpeed() {
        return xSpeed;
    }
    public double getYSpeed() {
        return ySpeed;
    }
}
