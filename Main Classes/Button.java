import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Button extends JButton {
    private static Font gameFont;
    // Button fields
    private Rectangle buttonRect;
    private String text;
    private Font buttonFont;
    private boolean hovered;
    private Color color;
    public static void init(){
        try{
            gameFont = Font.createFont(Font.TRUETYPE_FONT, new File("Assets/Fonts/8BitFont.ttf"));
        }
        catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }
    // Constructor
    public Button(Rectangle buttonRect, String text, int fontSize){
        super();
        this.buttonRect = buttonRect;
        this.text = text;
        setBounds(buttonRect);
        buttonFont = gameFont.deriveFont((float)fontSize);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
    }
    // Methods
    public void draw(Graphics g){
        g.setColor(color);
        g.setFont(buttonFont);
        int width = g.getFontMetrics().stringWidth(text);
        int height = g.getFontMetrics().getHeight();
        g.drawString(text, buttonRect.x + (buttonRect.width - width)/2, buttonRect.y + (buttonRect.height - height)/2 + 25);
    }
    public void drawRect(Graphics g){
        g.setColor(Color.BLACK);
        g.drawRect(buttonRect.x, buttonRect.y, buttonRect.width, buttonRect.height);
    }
    public void updateHover(Point point){
        hovered = false;
        ButtonModel model = getModel();
        if(model.isPressed()){
            color = Color.WHITE;
        }
        else if(buttonRect.contains(point)){
            hovered = true;
            color = Color.RED;
        }
        else{
            color = Color.BLACK;
        }
    }

    public boolean isHovered() {
        return hovered;
    }
}
