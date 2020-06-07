// Sound.java
// Armaan Randhawa and Shivan Gaur
// Class to create sound objects with volume control

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Sound {
    // Static Fields
    private static ArrayList<Sound> madeSounds = new ArrayList<>(); // ArrayList with all sounds created
    private static boolean isMuted;
    // Object fields
    private AudioInputStream inputStream;
    private Clip clip;
    private String filePath;
    FloatControl volume;
    // Booleans for static control of sounds
    private boolean wasPaused;
    private float originalGain;

    // Constructor
    public Sound(String filePath, int volumeLevel){
        this.filePath = filePath;
        // Getting a clip from the system
        try {
            clip = AudioSystem.getClip();
        }
        catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        loadClip();
        // Setting the volume
        volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        setVolume(volumeLevel);
        // Keeping track of this sound object
        madeSounds.add(this);
    }

    // Method that loads the sound file into the clip
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
    //Basic Sound Methods
    //Method that plays the sound from the start
    public void play(){
        clip.setMicrosecondPosition(0);
        clip.start();
    }

    // Method that resumes the sound from where it stopped
    public void resume(){
        clip.start();
    }

    // Method that stops the sound
    public void stop(){
        clip.stop();
    }

    // Method that closes the thread that the sound is playing on
    public void closeSound(){
        clip.close();
        madeSounds.remove(this); // Remove from tracked sounds
    }

    // Method that returns if the sound is currently being played
    public boolean isPlaying(){
        return clip.isActive();
    }

    // Setters
    public void setVolume(int volumeLevel){
        float range = volume.getMaximum() - volume.getMinimum(); // Getting the range of volume provided by the system
        float gain = (float)(range * (volumeLevel/100.0)) + volume.getMinimum(); // Calculating the gain
        volume.setValue(gain);
    }
    public void setGain(float gain){
        volume.setValue(gain);
    }

    public void forceMute(){
        originalGain = getGain();
        setVolume(0);
    }

    // Getters
    public float getGain(){
        return volume.getValue();
    }
    // Static methods
    // Method that pauses all sounds
    public static void pauseAll(){
        for(Sound sound: madeSounds){
            if(sound.isPlaying()){
                sound.stop();
                sound.wasPaused = true;
            }
        }
    }

    // Method that resumes all sounds after they were paused using pauseAll
    public static void resumeAll(){
        for(Sound sound: madeSounds){
            if(sound.wasPaused){
                sound.resume();
                sound.wasPaused = false;
            }
        }
    }

    // Method that mutes/unmutes all sounds
    public static void toggleVolume(){
        if(!isMuted){ // Muting
            for(Sound sound: madeSounds){
                sound.originalGain = sound.getGain();
                sound.setVolume(0);
            }
            isMuted = true;
        }
        else{ // Unmuting
            for(Sound sound: madeSounds){
                sound.setGain(sound.originalGain);
            }
            isMuted = false;
        }
    }
    public static boolean isMuted(){
        return isMuted;
    }
}
