import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Platform {
    private static HashMap<String, Image> imageMap = new HashMap<>();
    private double x, y;
    private int width, height;
    private String imageName;
    public Platform(String data){
        String[] dataSplit = data.split(",");
        x = Integer.parseInt(dataSplit[0]);
        y = Integer.parseInt(dataSplit[1]);
        imageName = dataSplit[2];
        Image platformImage = null;
        try{
            if(!imageMap.containsKey(imageName)){
                platformImage = ImageIO.read(new File("Assets/Images/Platforms/" + imageName));
                imageMap.put(imageName, platformImage);
            }
            else{
                platformImage = imageMap.get(imageName);
            }
        }
        catch (IOException e) {
            System.out.println("Platform picture missing!");
            e.printStackTrace();
            System.exit(1);
        }
        width = platformImage.getWidth(null);
        height = platformImage.getHeight(null);
    }
    public void translateX(double offset){
        x += offset;
    }
    // Getter methods
    public Image getPlatformImage(){
        return imageMap.get(imageName);
    }
    public Rectangle getRect(){
        return new Rectangle((int)Math.round(x), (int)Math.round(y), width, height);
    }
}
