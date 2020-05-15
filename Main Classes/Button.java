import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Button extends JButton {
    // Button fields
    private Rectangle buttonRect;
    private String text;
    private static Font buttonFont;
    private boolean hovered;
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
        setBounds(buttonRect);
        setFont(buttonFont.deriveFont((float)fontSize));
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
    }
    // Methods
    public void drawRect(Graphics g){
        g.drawRect(buttonRect.x, buttonRect.y, buttonRect.width, buttonRect.height);
    }
    public void updateHover(Point point){
        hovered = false;
        ButtonModel model = getModel();
        if(model.isPressed()){
            setForeground(Color.WHITE);
        }
        else if(buttonRect.contains(point)){
            hovered = true;
            setForeground(Color.RED);
        }
        else{
            setForeground(Color.BLACK);
        }
    }

    public boolean isHovered() {
        return hovered;
    }
}
