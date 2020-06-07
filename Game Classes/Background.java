// Background.java
// Armaan Randhawa and Shivan Gaur
// Class to take layers of a background and draw them with a parallax effect.

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Background {
    // Fields
    private double[] layersX, movementScalars; // Array of x positions and speed of each background layer
    private Image[] layerImages; // Array of images for each layer
    private int previousOffset = 0; // Keeps track of the background offset during each call of update()
    private boolean ignoreNegative;

    // Constructor
    public Background(String imageData, String scalars){
        // Splitting up the data inputted
        String[] nameSplit = imageData.split(",");
        String[] scalarSplit = scalars.split(",");
        // Declaring sizes of the three corresponding arrays
        layerImages = new Image[nameSplit.length];
        layersX = new double[nameSplit.length];
        movementScalars = new double[nameSplit.length];
        // Loading images
        try{
            for(int i = 0; i < nameSplit.length; i++){
                layerImages[i] = ImageIO.read(new File("Assets/Images/Backgrounds/" + nameSplit[i]));
                movementScalars[i] = Double.parseDouble(scalarSplit[i]);
            }
        }
        catch (IOException e) {
            System.out.println("Background image missing!");
            e.printStackTrace();
        }
    }

    // Method to update the background layer positions
    public void update(int offset){
        // Calculating the screen movement from the last call of update()
        int delta = previousOffset - offset;
        previousOffset = offset;
        // Respecting the ignoreNegative boolean
        if(ignoreNegative && delta > 0){
            return;
        }
        // Moving each layer and shifting the effect forwards or backwards if needed
        for(int i = 0; i < layerImages.length; i++){
            layersX[i] += delta * movementScalars[i]; // Moving according to that layers scalar
            if(layersX[i] < -960){
                layersX[i] += 960;
            }
            else if(layersX[i] > 0){
                layersX[i] -= 960;
            }
        }
    }

    // Method to draw the background layers
    public void draw(Graphics g){
        for(int i = 0; i < layerImages.length; i++){
            g.drawImage(layerImages[i], (int)layersX[i], 0, null);
            g.drawImage(layerImages[i], (int)layersX[i] + 960, 0, null);

        }
    }

    // Method to restrict the parallax motion to only forward
    public void ignoreNegative(){
        ignoreNegative = true;
    }
}
