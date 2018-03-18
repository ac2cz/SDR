package tutorial4;

import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import tutorial4.audio.AudioSink;
import tutorial4.audio.SoundCard;
import tutorial4.audio.WavFile;
import tutorial4.signal.FirFilter;

public class SimpleIQSdr {

	public static void main(String[] args) throws UnsupportedAudioFileException, 
	IOException, LineUnavailableException {
		int sampleRate = 192000;
		int len = 4096;
		//WavFile soundCard = new WavFile("ecars_net_7255_HDSDR_20180225_174354Z_7255kHz_RF.wav", len, true);
		SoundCard soundCard = new SoundCard(sampleRate, len, true);
		MainWindow window = new MainWindow("SimpleIQ SDR", sampleRate, len);

		AudioSink sink = new AudioSink(sampleRate);
		FirFilter lowPass = new FirFilter();

		double[] audio = new double[len];		
		boolean readingData = true;

		while (readingData) {
			double[] buffer = soundCard.read();
			if (buffer != null) {
				for (int d=0; d < audio.length; d++) {
					audio[d] = lowPass.filter(buffer[2*d]);
				}
				sink.write(audio);	
				window.setData(buffer);
				window.setVisible(true); // causes window to be redrawn
			} else 
				readingData = false;
		}
	}
}
