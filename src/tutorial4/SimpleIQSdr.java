package tutorial4;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import tutorial3.audio.SoundCard;

public class SimpleIQSdr {

	public static void main(String[] args) throws UnsupportedAudioFileException, 
	IOException, LineUnavailableException {
		int sampleRate = 192000;
		int FFT_LENGTH = 4096;
		//WavFile soundCard = new WavFile("cw_signals.wav");
		SoundCard soundCard = new SoundCard(sampleRate, FFT_LENGTH, true);
		MainWindow window = new MainWindow("SimpleIQ SDR", sampleRate);

		boolean readingData = true;
		while (readingData) {
			double[] buffer = soundCard.read();
			if (buffer != null) {
				window.setData(buffer);
				window.setVisible(true); // causes window to be redrawn
			} else 
				readingData = false;
		}
	}
}
