//Utilities.java
//Armaan Randhhawa and Shivan Gaur
/*The purpose of this class is to store useful general methods that can be used anywhere in the overall project, so that multiple redundant
lines of code are saved.
 */
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
        //This method takes a sprite and flips it horizontally
        // Using AffineTransform with Nearest-Neighbour to apply flip while keeping 8-bit style
        AffineTransform flip = AffineTransform.getScaleInstance(-1, 1);
        flip.translate(-sprite.getWidth(null), 0);
        AffineTransformOp flipOp = new AffineTransformOp(flip, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        sprite = flipOp.filter((BufferedImage) sprite, null);
        return sprite;
    }
    public static Image rotateSprite(Image sprite, double rads,int rotX,int rotY) {
        //This method takes a sprite, a radian value, and a coordinate, and rotates the sprite about that coordinate.
        // Using AffineTransform with Nearest-Neighbour to apply rotate while keeping 8-bit style
        AffineTransform rot = new AffineTransform();
        rot.rotate(rads,rotX,rotY);
        AffineTransformOp rotateOp = new AffineTransformOp(rot, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return rotateOp.filter((BufferedImage) sprite,null );
    }
    public static Image scaleSprite(Image sprite){
        AffineTransform scale = AffineTransform.getScaleInstance(2, 2);
        AffineTransformOp scaleOp = new AffineTransformOp(scale, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        sprite = scaleOp.filter((BufferedImage)sprite, null);
        return sprite;
    }
    public static Rectangle rectFinder(Image sprite){
        BufferedImage pic = (BufferedImage) sprite;
        int smallestX; int smallestY; int biggestX; int biggestY;
        smallestX = smallestY = Integer.MAX_VALUE;
        biggestX = biggestY = Integer.MIN_VALUE;
        for(int x = 0; x < pic.getWidth(); x++){
            for(int y = 0; y < pic.getHeight(); y++){
                Color pixel = new Color(pic.getRGB(x,y));
                if(pixel.equals(Color.WHITE)){
                    if(x > biggestX){
                        biggestX = x;
                    }
                    if(x < smallestX){
                        smallestX = x;
                    }
                    if(y > biggestY){
                        biggestY = y;
                    }
                    if(y < smallestY){
                        smallestY = y;
                    }
                }
            }
        }
        return new Rectangle(smallestX,smallestY,biggestX - smallestX,biggestY - smallestY);
    }
    public static Image[] spriteArrayLoad(Image[] targetArray, String fileName) {
        //This method takes an unloaded Image array and a file path, and loads all the images into the array.
        Image[] builtArray = new Image[targetArray.length];
        try {
            for (int i = 0; i < targetArray.length; i++) {
                builtArray[i] = ImageIO.read(new File("Assets/Images/" + fileName + i + ".png"));//loading image
                // Adding the alpha channel if the image isn't supporting it by default
                if(((BufferedImage)builtArray[i]).getType() != BufferedImage.TYPE_INT_ARGB){
                    builtArray[i] = forceAlpha(builtArray[i]);
                }
            }
        }
        catch (IOException e) {//exception
            System.out.println("Sprite image missing!");
            e.printStackTrace();
        }
        return builtArray;
    }
    public static Image forceAlpha(Image sprite){
        BufferedImage tempImage = new BufferedImage(sprite.getWidth(null), sprite.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = tempImage.createGraphics();
        g2d.drawImage(sprite,0,0,null);
        return tempImage;
    }
    // Helper method to load up individual files into ArrayLists with their lines as Strings
    public static ArrayList<String> loadFile(String fileName, int levelNum) throws IOException{
        /*This method returns an arraylist of strings from the loaded text data files of each level by taking in the file path along with the
         current level and loading each line in the files.
         */
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
        //Mr Mckenzie's randint method that returns a random integer within the given parameters(inclusive).
        return (int)(Math.random()*(high-low+1)+low);
    }

    public static double roundOff(double number, int decimalPlaces){
        //This method takes a double and properly rounds it off to the desired number of decimal places by using manipulation of powers of 10.
        double powerOfTen = Math.pow(10, decimalPlaces);
        return Math.round(number*powerOfTen)/powerOfTen;
    }

}