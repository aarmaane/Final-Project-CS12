import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Button extends JButton {
    private static Font gameFont, gameFontThin;
    // Button fields
    private Rectangle buttonRect;
    private String text;
    private String[] tooltips;
    private Font buttonFont;
    private boolean hovered;
    private Color color;
    public static void init(){
        try{
            gameFont = Font.createFont(Font.TRUETYPE_FONT, new File("Assets/Fonts/8BitFont.ttf"));
            gameFontThin = gameFont.deriveFont(30f);
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
        buttonFont = gameFont.deriveFont((float)fontSize);
        setActionCommand(text);
        setBounds(buttonRect);
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
    public void drawTooltip(Graphics g, int x, int y){
        g.setFont(gameFontThin);
        g.setColor(Color.GREEN);
        g.drawString("Price: " + tooltips[0], x + 10, y + 20);
        g.setColor(Color.WHITE);
        String[] wordSplit = tooltips[1].split(" ");
        String line = "";
        int lineCount = 0;
        for(String word: wordSplit){
            if(g.getFontMetrics().stringWidth(line + word) > 250){
                lineCount++;
                g.drawString(line, x + 10, y + 40 + lineCount*20);
                line = "";
            }
            line += word + " ";
        }
        g.drawString(line, x + 10, y + 40 + (lineCount+1)*20);
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
    public void addTooltip(String text){
        tooltips = text.split(",");
    }

    public boolean isHovered() {
        return hovered;
    }

    public boolean hasTooltip(){
        return tooltips != null;
    }
}
