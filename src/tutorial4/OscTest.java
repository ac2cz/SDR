package tutorial4;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import tutorial4.signal.Oscillator;
import tutorial4.audio.SoundCard;
import tutorial4.audio.AudioSink;
import tutorial4.signal.FirFilter;

public class OscTest {

	public static void main(String[] args) throws UnsupportedAudioFileException, 
	IOException, LineUnavailableException {
		int sampleRate = 192000;
		int FFT_LENGTH = 4096;
		//WavFile soundCard = new WavFile("cw_signals.wav");
		SoundCard soundCard = new SoundCard(sampleRate, FFT_LENGTH, true);
		MainWindow window = new MainWindow("SimpleIQ SDR", sampleRate, FFT_LENGTH);
		AudioSink sink = new AudioSink(192000);
		FirFilter lowPass = new FirFilter();
		double[] audioBuffer = new double[FFT_LENGTH]; // just one mono channel, decimated by 4 to 48000
		
		Oscillator osc = new Oscillator(192000);
		osc.setFrequency(500);
		double oscAmp = 0.01;
		Oscillator osc2 = new Oscillator(192000);
		osc2.setFrequency(5000);
		double oscAmp2 = 0.01;
		Oscillator osc3 = new Oscillator(192000);
		osc3.setFrequency(60000);
		double oscAmp3 = 0.01;
		double[] IQbuffer = new double[FFT_LENGTH*2];
		

		boolean readingData = true;
		while (readingData) {
			for (int n=0; n< FFT_LENGTH; n++) {
				double value = osc.nextSample();
//				IQbuffer[2*n] = value*oscAmp;
//				IQbuffer[2*n+1] = value*oscAmp;
			}
			for (int n=0; n< FFT_LENGTH; n++) {
				double value = osc2.nextSample();
				IQbuffer[2*n] = value*oscAmp2;
				IQbuffer[2*n+1] = value*oscAmp2;
			//	IQbuffer[2*n] += value*oscAmp2;
			//	IQbuffer[2*n+1] *= value*oscAmp2;
			}
			for (int n=0; n< FFT_LENGTH; n++) {
				double value = osc3.nextSample();
				IQbuffer[2*n] += value*oscAmp3;
				IQbuffer[2*n+1] *= value*oscAmp3;
			}
			//double[] IQbuffer = soundCard.read();
			for (int d=0; d < audioBuffer.length; d++) {
				audioBuffer[d] = lowPass.filter(IQbuffer[2*d]);
				//audioBuffer[d] = IQbuffer[8*d];
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
