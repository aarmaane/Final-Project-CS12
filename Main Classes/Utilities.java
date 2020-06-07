// Utilities.java
// Armaan Randhawa and Shivan Gaur
// Class to store general helper methods for Images, FileIO, and number manipulation
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
    // Method takes a sprite and flips it horizontally
    public static Image flipSprite(Image sprite) {
        // Using AffineTransform with Nearest-Neighbour to apply flip while keeping 8-bit style
        AffineTransform flip = AffineTransform.getScaleInstance(-1, 1);
        flip.translate(-sprite.getWidth(null), 0);
        AffineTransformOp flipOp = new AffineTransformOp(flip, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        sprite = flipOp.filter((BufferedImage) sprite, null);
        return sprite;
    }

    // Method takes a sprite, a radian value, and a coordinate, and rotates the sprite about that coordinate.
    public static Image rotateSprite(Image sprite, double rads,int rotX,int rotY) {
        // Using AffineTransform with Nearest-Neighbour to apply rotate while keeping 8-bit style
        AffineTransform rot = new AffineTransform();
        rot.rotate(rads,rotX,rotY);
        AffineTransformOp rotateOp = new AffineTransformOp(rot, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return rotateOp.filter((BufferedImage) sprite,null );
    }

    // Method that takes a sprite and scales it by the inputted factor
    public static Image scaleSprite(Image sprite, double scaleFactor){
        AffineTransform scale = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
        AffineTransformOp scaleOp = new AffineTransformOp(scale, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        sprite = scaleOp.filter((BufferedImage)sprite, null);
        return sprite;
    }

    // Method that finds the a rectangle with all white pixels in an image
    public static Rectangle rectFinder(Image sprite){
        BufferedImage pic = (BufferedImage) sprite; // Converting the iamge to a buffered one
        // Declaring variables for keeping track of pixels
        int smallestX; int smallestY; int biggestX; int biggestY;
        smallestX = smallestY = Integer.MAX_VALUE;
        biggestX = biggestY = Integer.MIN_VALUE;
        // Going through each pixel and finding the biggest and smallest Xs/Ys of white pixels that it can find
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
        // Returning a rectangle that contains all of the white pixels found (by using the biggest and smallest Xs/Ys as the bounds)
        return new Rectangle(smallestX,smallestY,biggestX - smallestX,biggestY - smallestY);
    }
    //Method takes an unloaded Image array and loads all specified images into it
    public static Image[] spriteArrayLoad(Image[] targetArray, String fileName) {
        Image[] builtArray = new Image[targetArray.length]; // Array that will be returned
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

    // Method that gives an image and alpha channel
    public static Image forceAlpha(Image sprite){
        // Creating a bufferedImage with the alpha channel and drawing the image onto it
        BufferedImage tempImage = new BufferedImage(sprite.getWidth(null), sprite.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = tempImage.createGraphics();
        g2d.drawImage(sprite,0,0,null);
        return tempImage; // Returning the created buffered image
    }

    // Helper method to load up individual files into ArrayLists with their lines as Strings
    public static ArrayList<String> loadFile(String fileName, int levelNum) throws IOException{
        Scanner inFile = new Scanner(new BufferedReader(new FileReader("Data/Level " + levelNum + "/" + fileName)));
        ArrayList<String> fileContents = new ArrayList<>(); // ArrayList that will hold each line
        // Going through each line
        while(inFile.hasNext()){
            String line = inFile.nextLine();
            if(!line.startsWith("//") && !line.isBlank()){ // Making sure that the line is not a comment or blank
                fileContents.add(line);
            }
        }
        inFile.close();
        return fileContents; // Returning the ArrayList of lines built
    }

    // Mr Mckenzie's randint method that returns a random integer
    public static int randint(int low, int high){
        return (int)(Math.random()*(high-low+1)+low);
    }

    //This method takes a double and properly rounds it off to the desired number of decimal places
    public static double roundOff(double number, int decimalPlaces){
        // Rounding by multiplying by powers of ten and slicing off decimals
        double powerOfTen = Math.pow(10, decimalPlaces);
        return Math.round(number*powerOfTen)/powerOfTen;
    }

}