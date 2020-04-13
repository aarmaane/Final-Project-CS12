import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound {
    private AudioInputStream inputStream;
    private Clip clip;
    private String filePath;
    // Constructor
    public Sound(String filePath){
        this.filePath = filePath;
        try {
            clip = AudioSystem.getClip();
        }
        catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        loadClip();
    }
    private void loadClip(){
        try{
            inputStream = AudioSystem.getAudioInputStream(new File(filePath));
            clip.open(inputStream);
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Sound error");
            e.printStackTrace();
        }
    }
    public void play(){
        clip.setMicrosecondPosition(0);
        clip.start();
    }
    public void resume(){
        clip.start();
    }
    public void pause(){
        clip.stop();
    }
    public void closeSound(){
        clip.close();
    }
    public boolean hasStarted(){
        return clip.isOpen();
    }
    public boolean isPlaying(){
        return clip.isActive();
    }
}
