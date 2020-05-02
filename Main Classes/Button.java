import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Button extends JButton {
    // Button fields
    private Rectangle buttonRect;
    private String text;
    private static Font buttonFont;
    // Button Images
    private Image buttonIdle;
    private Image buttonHover;
    private Image buttonClick;
    public static void init(){
        try{
            buttonFont = Font.createFont(Font.TRUETYPE_FONT, new File("Assets/Fonts/8BitFont.ttf"));
        }
        catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }
    // Constructor
    public Button(Rectangle buttonRect, String text, int fontSize){
        super(text);
        this.buttonRect = buttonRect;
        this.text = text;
        this.setBounds(buttonRect);
        setFont(buttonFont.deriveFont((float)fontSize));
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
    }
    public void drawRect(Graphics g){
        g.drawRect(buttonRect.x, buttonRect.y, buttonRect.width, buttonRect.height);
    }
    public void updateHover(Point point){
        ButtonModel model = getModel();
        if(model.isPressed()){
            setForeground(Color.WHITE);
        }
        else if(buttonRect.contains(point)){
            setForeground(Color.RED);
        }
        else{
            setForeground(Color.BLACK);
        }
    }
}
