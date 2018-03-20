package tutorial6;

import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import tutorial6.MainWindow;
import tutorial6.audio.SoundCard;
import tutorial6.audio.WavFile;
import tutorial6.audio.AudioSink;
import tutorial6.signal.Complex;
import tutorial6.signal.ComplexOscillator;
import tutorial6.signal.FirFilter;

public class IQSdrTest2 {

	public static void main(String[] args) throws UnsupportedAudioFileException, 
	IOException, LineUnavailableException {
		int sampleRate = 192000;
		int FFT_LENGTH = 4096;
		WavFile soundCard = new WavFile("ecars_net_7255_HDSDR_20180225_174354Z_7255kHz_RF.wav", FFT_LENGTH, true);
		//SoundCard soundCard = new SoundCard(sampleRate, FFT_LENGTH, true);
		MainWindow window = new MainWindow("SimpleIQ SDR", sampleRate, FFT_LENGTH);
		AudioSink sink = new AudioSink(sampleRate);
		FirFilter audioLowPass = new FirFilter();
		
		ComplexOscillator localOsc = new ComplexOscillator(sampleRate, -20000);
		window.setNco(localOsc);

		double[] audioBuffer = new double[FFT_LENGTH]; // just one mono channel
		double[] IQbuffer2 = new double[FFT_LENGTH*2];
		boolean readingData = true;
		
		while (readingData) {
			double[] IQbuffer = soundCard.read();
			if (IQbuffer != null) {
				for (int d=0; d < IQbuffer.length/2; d++) {					
					//NCO Frequency
					Complex c = localOsc.nextSample();
					c.normalize();
					// Mix 
					IQbuffer2[2*d] = IQbuffer[2*d]*c.geti() + IQbuffer[2*d+1]*c.getq();
					IQbuffer2[2*d+1] = IQbuffer[2*d+1]*c.geti() - IQbuffer[2*d]*c.getq();

					double audio = IQbuffer2[2*d] + IQbuffer2[2*d+1]; 

					double fil = audioLowPass.filter(audio);
					audioBuffer[d] = fil;
				}
				sink.write(audioBuffer);

				window.setRfData(IQbuffer);
				window.setRfData2(IQbuffer2);
				window.setAudioData(audioBuffer);
				window.setVisible(true); // causes window to be redrawn
			} else 
				readingData = false;
		} 
	}
}
