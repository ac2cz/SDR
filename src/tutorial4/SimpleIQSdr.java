package tutorial4;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import tutorial4.audio.SoundCard;
import tutorial4.audio.WavFile;
import tutorial4.audio.AudioSink;
import tutorial4.signal.FirFilter;

public class SimpleIQSdr {

	public static void main(String[] args) throws UnsupportedAudioFileException, 
	IOException, LineUnavailableException {
		int sampleRate = 192000;
		int FFT_LENGTH = 4096;
		WavFile soundCard = new WavFile("ecars_net_7255_HDSDR_20180225_174354Z_7255kHz_RF.wav", FFT_LENGTH, true);
		//SoundCard soundCard = new SoundCard(sampleRate, FFT_LENGTH, true);
		MainWindow window = new MainWindow("SimpleIQ SDR", sampleRate, FFT_LENGTH);
		AudioSink sink = new AudioSink(192000);
		FirFilter lowPass = new FirFilter();
		double[] audioBuffer = new double[FFT_LENGTH]; // just one mono channel
		
		boolean readingData = true;
		while (readingData) {
			double[] IQbuffer = soundCard.read();
			for (int d=0; d < audioBuffer.length; d++) {
				audioBuffer[d] = lowPass.filter(IQbuffer[2*d]);
				//audioBuffer[d] = IQbuffer[2*d];
			}
			sink.write(audioBuffer);
			if (IQbuffer != null) {
				window.setRfData(IQbuffer);
				window.setAudioData(audioBuffer);
				window.setVisible(true); // causes window to be redrawn
			} else 
				readingData = false;
		}
	}
}
