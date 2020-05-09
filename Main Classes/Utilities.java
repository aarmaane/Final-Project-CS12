import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Utilities {
    public static Image flipSprite(Image sprite) {
        // Using AffineTransform with Nearest-Neighbour to apply flip while keeping 8-bit style
        AffineTransform flip = AffineTransform.getScaleInstance(-1, 1);
        flip.translate(-sprite.getWidth(null), 0);
        AffineTransformOp flipOp = new AffineTransformOp(flip, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        sprite = flipOp.filter((BufferedImage) sprite, null);
        return sprite;
    }
    public static Image rotateSprite(Image sprite, double rads,int rotX,int rotY) {
       // double rads= Math.toRadians(angle);
        // Using AffineTransform with Nearest-Neighbour to apply flip while keeping 8-bit style
        AffineTransform rot = new AffineTransform();
        rot.rotate(rads,rotX,rotY);
        AffineTransformOp rotateOp = new AffineTransformOp(rot, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        //BufferedImage newImage =new BufferedImage(sprite.getHeight(null), sprite.getWidth(null) );
        return rotateOp.filter((BufferedImage) sprite,null );
    }

    public static Image[] spriteArrayLoad(Image[] targetArray, String fileName) {
        Image[] builtArray = new Image[targetArray.length];
        try {
            for (int i = 0; i < targetArray.length; i++) {
                builtArray[i] = ImageIO.read(new File("Assets/Images/" + fileName + i + ".png"));
            }
        }
        catch (IOException e) {
            System.out.println("Sprite image missing!");
            e.printStackTrace();
        }
        return builtArray;
    }
    // Helper method to load up individual files into ArrayLists with their lines as Strings
    public static ArrayList<String> loadFile(String fileName, int levelNum) throws IOException{
        Scanner inFile = new Scanner(new BufferedReader(new FileReader("Data/Level " + levelNum + "/" + fileName)));
        ArrayList<String> fileContents = new ArrayList<>();
        while(inFile.hasNext()){
            String line = inFile.nextLine();
            if(!line.startsWith("//")){ // Making sure that the line is not a comment
                fileContents.add(line);
            }
        }
        inFile.close();
        return fileContents;
    }

    public static int randint(int low, int high){
        return (int)(Math.random()*(high-low+1)+low);
    }

    public static double roundOff(double number, int decimalPlaces){
        double powerOfTen = Math.pow(10, decimalPlaces);
        return Math.round(number*powerOfTen)/powerOfTen;
    }

}