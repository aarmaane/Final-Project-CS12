import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Background {
    private double[] layersX, movementScalars;
    private Image[] layerImages;
    private int previousOffset = 0;
    private boolean ignoreNegative;
    public Background(String imageData, String scalars){
        String[] nameSplit = imageData.split(",");
        String[] scalarSplit = scalars.split(",");
        layerImages = new Image[nameSplit.length];
        layersX = new double[nameSplit.length];
        movementScalars = new double[nameSplit.length];
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
    public void update(int offset){
        int delta = previousOffset - offset;
        previousOffset = offset;
        if(ignoreNegative && delta > 0){
            return;
        }
        for(int i = 0; i < layerImages.length; i++){
            layersX[i] += delta * movementScalars[i];
            if(layersX[i] < -960){
                layersX[i] += 960;
            }
            else if(layersX[i] > 0){
                layersX[i] -= 960;
            }
        }

    }
    public void draw(Graphics g){
        for(int i = 0; i < layerImages.length; i++){
            g.drawImage(layerImages[i], (int)layersX[i], 0, null);
            g.drawImage(layerImages[i], (int)layersX[i] + 960, 0, null);

        }
    }
    public void ignoreNegative(){
        ignoreNegative = true;
    }
}
