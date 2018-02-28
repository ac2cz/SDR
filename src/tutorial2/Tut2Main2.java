package tutorial2;

import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;

import tutorial2.audio.WavFile;

public class Tut2Main2 {

	public static void main(String[] args) throws UnsupportedAudioFileException, IOException {

		MainWindow window = new MainWindow("Test Tool");

		WavFile wavfile = new WavFile("test_sound.wav", 1500);
		boolean readingData = true;

		while (readingData) {
			double[] buffer = wavfile.read();
			if (buffer != null) {
				window.setData(buffer);
				window.setVisible(true);
			} else
				readingData = false;
		}
		wavfile.close();
	}
}
