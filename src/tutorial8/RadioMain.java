package tutorial8;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import tutorial8.gui.MainWindow;

public class RadioMain {

	public static void main(String[] args) {
		int sampleRate = 192000;
		int sampleLength = 4096; 
		
		Sdr sdr = new Sdr(sampleRate, sampleLength);
		MainWindow window = new MainWindow("SDR with Circular Buffer", sdr);

		try {
			sdr.processing();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
	}
}
