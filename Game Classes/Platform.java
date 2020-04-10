import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Platform {
    private double x, y;
    private int width, height;
    private Image platformImage;
    public Platform(String data){
        String[] dataSplit = data.split(",");
        x = Integer.parseInt(dataSplit[0]);
        y = Integer.parseInt(dataSplit[1]);
        try{
            platformImage = ImageIO.read(new File("Assets/Images/Platforms/" + dataSplit[2]));
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
        return platformImage;
    }
    public Rectangle getRect(){
        return new Rectangle((int)Math.round(x), (int)Math.round(y), width, height);
    }

    public double getX() {
        return x;
    }
}
