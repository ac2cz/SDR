package tutorial7;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import tutorial7.audio.SoundCard;
import tutorial7.audio.WavFile;
import tutorial7.audio.AudioSink;
import tutorial7.signal.Complex;
import tutorial7.signal.ComplexOscillator;
import tutorial7.signal.Delay;
import tutorial7.signal.FirFilter;
import tutorial7.signal.HilbertTransform;

public class SimpleIQSdr2 {

	public static void main(String[] args) throws UnsupportedAudioFileException, 
	IOException, LineUnavailableException {
		int sampleRate = 192000;
		int FFT_LENGTH = 4096;
//		WavFile soundCard = new WavFile("ecars_net_7255_HDSDR_20180225_174354Z_7255kHz_RF.wav", FFT_LENGTH, true);
//		WavFile soundCard = new WavFile("Fox-1A_Pass_HDSDR_20160104_002038Z_145960kHz_RF.wav", FFT_LENGTH, true);
		SoundCard soundCard = new SoundCard(sampleRate, FFT_LENGTH, true);
		MainWindow window = new MainWindow("SimpleIQ SDR", sampleRate, FFT_LENGTH);
		AudioSink sink = new AudioSink(sampleRate/4);
		ComplexOscillator localOsc = new ComplexOscillator(sampleRate, 0);//20103
		ComplexOscillator bfo = new ComplexOscillator(48000, 600);
		window.setNco(localOsc);
		FirFilter lowPassI = new FirFilter();
		FirFilter lowPassQ = new FirFilter();
		FirFilter audioLowPass = new FirFilter();
		
		HilbertTransform ht = new HilbertTransform(48000, 255);
		Delay delay = new Delay((255-1)/2);
		
		double[] audioBuffer = new double[FFT_LENGTH/4]; // just one mono channel, decimated by 4
		double[] IQbuffer2 = new double[FFT_LENGTH*2];
		
		boolean readingData = true;
		int decimateCount=1;
		double gain = 10;
		
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
										
					//Filter pre decimation
					double audioI = lowPassI.filter(IQbuffer2[2*d]);
					double audioQ = lowPassQ.filter(IQbuffer2[2*d+1]);

					// show the IF (otherwise we see the rotated spectrum)
					IQbuffer2[2*d] = audioI;
					IQbuffer2[2*d+1] = audioQ;
					
					//Decimate
					decimateCount++;
					if (decimateCount == 4) {
						decimateCount = 1;
					
						double audio;
						//Demodulate ssb
						audioQ = ht.filter(audioQ);
						audioI = delay.filter(audioI);
						//audio = audioI - audioQ; // USB
						audio = audioI + audioQ; // LSB
						
						// am detector
//						audio = Math.sqrt(audioI*audioI + audioQ*audioQ);

						audio = audio * gain;
						double fil = audioLowPass.filter(audio);
						audioBuffer[d/4] = fil;
					}
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
