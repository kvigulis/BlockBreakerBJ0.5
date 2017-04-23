import java.io.*;
import sun.audio.*;


public class AudioFile {
	
	String path;
	
	public AudioFile(String path) {
		this.path = path;		
	}
	
	public void playAudio(String path) {	

		    try {
		    	InputStream in = new FileInputStream(path);
		    	// create an audiostream from the inputstream
		    	AudioStream audioStream = new AudioStream(in);
		    	AudioPlayer.player.start(audioStream);
		    	// play the audio clip with the audioplayer class		    	
            } catch(Exception e) {
                // code to handle the exception
            }			    		  
	}
	
	public String getPath(){
		return path;
	}
	
	
	
	
	
	
	
	
}