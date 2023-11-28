package monkey;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sounds 
{
    // Variabili dello stato interno
    private Clip clip;
    private static Clip soundtrack = null;
        
    public Sounds(String nameSound)
    {    
        try 
        {
            // Ricerco nella cartella /src/sound/ le varie track .wav
            File fx = new File("./src/sounds/" + nameSound);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(fx.toURI().toURL());
            clip = AudioSystem.getClip();       
            clip.open(audioIn);
        } 
        catch (Exception e) 
        {
            System.out.println("Errore, suono non riproducibile, motivo:");
            System.err.println(e.getMessage());
        } 
    }
    
    public void start()
    {
        clip.start();
    }
    
    public void stop()
    {
        // controllo se la clip sia ancora in esecuzione
        if(clip.isRunning())
        {
            clip.stop();
            clip.flush();
        }
    }
    
    public static void playSoundtrack(String nameSound)
    {   
        try 
        {
            // Ricerco nella cartella /src/sound/ le varie track .wav
            File fx = new File("./src/sounds/" + nameSound);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(fx.toURI().toURL()); 
            soundtrack = AudioSystem.getClip();       
            soundtrack.open(audioIn);
            soundtrack.start();
            soundtrack.loop(Clip.LOOP_CONTINUOUSLY);
        } 
        catch (Exception e) 
        {
            System.out.println("Errore, suono non riproducibile, motivo:");
            System.err.println(e.getMessage());
        } 
    }
    
    public static void stopSoundtrack()
    {
        // controllo se la clip sia ancora in esecuzione
        if(soundtrack != null)
        {
            if(soundtrack.isRunning())
            {
                soundtrack.stop();
                soundtrack.flush();
            }
        }
    }
}

