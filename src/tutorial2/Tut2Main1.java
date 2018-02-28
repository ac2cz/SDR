package tutorial2;

import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;
import tutorial2.audio.WavFile;

public class Tut2Main1 {

	public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
		WavFile wavfile = new WavFile("test_sound.wav",128);
		boolean readingData = true;

		while (readingData) {
			double[] buffer = wavfile.read();
			if (buffer != null) {
				for (double d : buffer)
					System.out.println(d);
			} else
				readingData = false;
		}
		wavfile.close();
	}
}
