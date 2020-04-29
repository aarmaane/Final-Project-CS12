import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class LevelProp {
    private static HashMap<String, Image> imageMap = new HashMap<>();
    private double x, y;
    private int width, height;
    private String imageName;
    public LevelProp(String data){
        String[] dataSplit = data.split(",");
        x = Integer.parseInt(dataSplit[0]);
        y = Integer.parseInt(dataSplit[1]);
        imageName = dataSplit[2];
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
            System.out.println("Level prop picture missing!");
            e.printStackTrace();
            System.exit(1);
        }
        width = propImage.getWidth(null);
        height = propImage.getHeight(null);
    }
    // Getter methods
    public Image getPropImage(){
        return imageMap.get(imageName);
    }
    public Rectangle getRect(){
        return new Rectangle((int)Math.round(x), (int)Math.round(y), width, height);
    }
}
