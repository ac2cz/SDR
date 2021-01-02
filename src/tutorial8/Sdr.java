package tutorial8;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import tutorial8.audio.AudioSink;
import tutorial8.audio.SoundCard;
import tutorial8.audio.Source;
import tutorial8.audio.WavFile;
import tutorial8.signal.Complex;
import tutorial8.signal.ComplexOscillator;
import tutorial8.signal.Delay;
import tutorial8.signal.FirFilter;
import tutorial8.signal.HilbertTransform;

public class Sdr {
	public int sampleRate = 0;
	public int sampleLength = 4096;
	public double[] IQbuffer;
	public double[] IFbuffer;
	public double[] audioBuffer;
	public ComplexOscillator localOsc;
	
	enum Mode {
		AM,
		LSB,
		USB,
		FM
	}
	
	Mode mode = Mode.LSB;
	
	public Sdr(int sampleRate, int sampleLength) {
		this.sampleRate = sampleRate;
		this.sampleLength = sampleLength;
		IQbuffer = new double[sampleLength*2];
		audioBuffer = new double[sampleLength/4]; // just one mono channel, decimated by 4
		IFbuffer = new double[sampleLength*2];
		localOsc = new ComplexOscillator(sampleRate, 0);
	}
	
	public void processing() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		int sampleRate = 192000;
		Source soundCard = new WavFile("ecars_net_7255_HDSDR_20180225_174354Z_7255kHz_RF.wav", sampleRate*2, true);
//		Source soundCard = new SoundCard(sampleRate, sampleRate*2, true);
		AudioSink sink = new AudioSink(sampleRate/4);
		
		FirFilter lowPassI = new FirFilter();
		FirFilter lowPassQ = new FirFilter();
		FirFilter audioLowPass = new FirFilter();

		HilbertTransform ht = new HilbertTransform(48000, 255);
		Delay delay = new Delay((255-1)/2);

		boolean readingData = true;
		int decimateCount=1;
		double gain = 5;

		Thread soundCardThread = new Thread(soundCard);
		soundCardThread.start();

		while (readingData) {
			soundCard.read(IQbuffer);
			if (IQbuffer != null) {
				for (int d=0; d < IQbuffer.length/2; d++) {
					Complex c = localOsc.nextSample();
					c.normalize();
					// Mix 
					IFbuffer[2*d] = IQbuffer[2*d]*c.geti() + IQbuffer[2*d+1]*c.getq();
					IFbuffer[2*d+1] = IQbuffer[2*d+1]*c.geti() - IQbuffer[2*d]*c.getq();

					//Filter pre decimation
					double audioI = lowPassI.filter(IFbuffer[2*d]);
					double audioQ = lowPassQ.filter(IFbuffer[2*d+1]);

					// show the IF (otherwise we see the rotated spectrum)
					IFbuffer[2*d] = audioI;
					IFbuffer[2*d+1] = audioQ;

					//Decimate
					decimateCount++;
					if (decimateCount == 4) {
						decimateCount = 1;

						//Demodulate ssb
						if (mode == Mode.LSB || mode == mode.USB) {
							audioQ = ht.filter(audioQ);
							audioI = delay.filter(audioI);
						}
						double audio = 0;
						if (mode == Mode.USB)
							audio = audioI - audioQ; // USB
						else if (mode == Mode.LSB)
							audio = audioI + audioQ; // LSB
						else if (mode == Mode.AM)
							audio = Math.sqrt(audioI*audioI + audioQ*audioQ);

						audio = audio * gain;
						double fil = audioLowPass.filter(audio);
						audioBuffer[d/4] = fil;
					}
				}
				sink.write(audioBuffer);
			} else 
				readingData = false;
		}
		System.out.println("Finished processing"); // this is helpful in testing to know if we dropped out of the loop
	}
}
