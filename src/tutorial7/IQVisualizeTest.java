package tutorial7;

import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import tutorial7.audio.AudioSink;
import tutorial7.signal.Complex;
import tutorial7.signal.ComplexOscillator;
import tutorial7.signal.Delay;
import tutorial7.signal.FirFilter;
import tutorial7.signal.HilbertTransform;

public class IQVisualizeTest {

	public static void main(String[] args) throws UnsupportedAudioFileException, 
	IOException, LineUnavailableException {
		int sampleRate = 192000;
		int FFT_LENGTH = 4096;
		TestWindow window = new TestWindow("IQ Visulaization", sampleRate, FFT_LENGTH);
		AudioSink sink = new AudioSink(sampleRate);
		ComplexOscillator localOsc = new ComplexOscillator(sampleRate, 0);//20103
		window.setNco(localOsc);
		ComplexOscillator signal1 = new ComplexOscillator(192000, -19000);
		double ampSig1 = 0.000005;
		
		FirFilter lowPassI = new FirFilter();
		FirFilter lowPassQ = new FirFilter();
		HilbertTransform ht = new HilbertTransform(48000, 255);
		Delay delay = new Delay((255-1)/2);
		double[] audioBuffer = new double[FFT_LENGTH/4]; // one mono channel, decimated by 4
		double[] audioBuffer1 = new double[FFT_LENGTH/4]; // one mono channel, decimated by 4
		double[] audioBuffer2 = new double[FFT_LENGTH/4]; // one mono channel, decimated by 4
		double[] IQbuffer2 = new double[FFT_LENGTH*2];
		
		boolean readingData = true;
		int decimateCount=1;
		double gain = 100;
		
		while (readingData) {
			double[] IQbuffer = new double[FFT_LENGTH*2];
			if (IQbuffer != null) {
				for (int d=0; d < IQbuffer.length/2; d++) {
					// Insert the test data
					Complex c1 = signal1.nextSample();
					IQbuffer[2*d] = c1.geti() * ampSig1 + dither(); ; 
					IQbuffer[2*d+1] = c1.getq() * ampSig1 + dither();
					
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
						
						//Demodulate
		//				audioQ = ht.filter(audioQ);
		//				audioI = delay.filter(audioI);
						//double audio = audioI - audioQ; // USB
						double audio = audioI + audioQ; // LSB
						
						audio = audio * gain;
						audioBuffer1[d/4] = (audioI) * gain*50;
						audioBuffer2[d/4] = (audioQ) * gain*50;
						audioBuffer[d/4] = audio;
					}
				}
				sink.write(audioBuffer);

				window.setRfData(IQbuffer);
				window.setRfData2(IQbuffer2);
				window.setAudioData(audioBuffer1);
				window.setAudioData2(audioBuffer2);
				window.setVisible(true); // causes window to be redrawn
//				try {
//					Thread.sleep(500);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
			} else 
				readingData = false;
		}
	}
	
	private static double dither() {
		return Math.random()/100000;
	}
}
