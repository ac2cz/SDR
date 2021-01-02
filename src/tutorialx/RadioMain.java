package tutorialx;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.g0kla.rtlsdr4java.DeviceException;

import tutorialx.gui.MainWindow;

public class RadioMain {

	public static void main(String[] args) throws DeviceException {
		int sampleRate = 240000;
		int R = sampleRate / 48000;
		int sampleLength = 1024*R; 
		
		Sdr sdr = new Sdr(sampleRate, sampleLength, 144300000);
		MainWindow window = new MainWindow("RTL SDR", sdr);

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
