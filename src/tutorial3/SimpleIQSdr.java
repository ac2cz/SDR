package tutorial3;

import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import tutorial3.audio.SoundCard;
import tutorial3.audio.WavFile;

public class SimpleIQSdr {

	public static void main(String[] args) throws UnsupportedAudioFileException, 
	IOException, LineUnavailableException {
		int sampleRate = 192000;
		int len = 4096;
		WavFile soundCard = new WavFile("ecars_net_7255_HDSDR_20180225_174354Z_7255kHz_RF.wav", len, true);
		//SoundCard soundCard = new SoundCard(sampleRate, len, true);
		MainWindow window = new MainWindow("SimpleIQ SDR", sampleRate, len);

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
