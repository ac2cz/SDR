package tutorialx;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.g0kla.rtlsdr4java.DeviceException;

import tutorialx.signal.FmDemodulator;
import tutorialp.signal.Filter;
import tutorialp.signal.PolyPhaseFilter;
import tutorialx.audio.AudioSink;
import tutorialx.audio.RtlSource;
import tutorialx.audio.SoundCard;
import tutorialx.audio.Source;
import tutorialx.audio.WavFile;
import tutorialx.device.UsbDevice;
import tutorialx.signal.Complex;
import tutorialx.signal.ComplexOscillator;
import tutorialx.signal.Delay;
import tutorialx.signal.FirFilter;
import tutorialx.signal.HilbertTransform;

public class Sdr {
	public static final int AF_SAMPLE_RATE = 48000;
	public int sampleRate = 0;
	public double[] IQbuffer;
	public double[] IFbuffer;
	public double[] audioBuffer;
	public ComplexOscillator localOsc;
	public int R = 0; // decimation rate
	public int sampleLength = 0;
	public long centerFrequency = 0;
	UsbDevice usb;
	
	enum Mode {
		AM,
		LSB,
		USB,
		FM
	}
	Mode mode = Mode.FM;
	FmDemodulator fm = new FmDemodulator();
	double gain = 0.05; // .05 for FM 10 for SSB
	boolean ppFilter = true;
	HilbertTransform ht;
	Delay delay;
	FirFilter audioLowPass;
	
	public Sdr(int sampleRate, int sampleLength, long centerFrequency) {
		this.sampleRate = sampleRate;
		this.sampleLength = sampleLength;
		this.centerFrequency = centerFrequency;
		R = sampleRate / AF_SAMPLE_RATE;
		System.out.println("Decimation by: " + R);
		IQbuffer = new double[sampleLength*2];
		audioBuffer = new double[sampleLength/R]; // just one mono channel, decimated by R
		IFbuffer = new double[sampleLength*2];
		localOsc = new ComplexOscillator(sampleRate, 0);
		ht = new HilbertTransform(AF_SAMPLE_RATE, 255);
		delay = new Delay((255-1)/2);
		audioLowPass = new FirFilter();
	}

	public void processing() throws UnsupportedAudioFileException, IOException, LineUnavailableException, DeviceException {
	//			Source soundCard = new WavFile("ecars_net_7255_HDSDR_20180225_174354Z_7255kHz_RF.wav", sampleRate*2, true);
		usb = new UsbDevice((short)0x0bda,(short)0x2838, sampleRate);
		usb.setTunedFrequency(centerFrequency);
		Source soundCard = new RtlSource(sampleRate*5); // give the buffer plenty of space
		usb.addListener((RtlSource)soundCard); // we need to cast soundCard to RtlSource because that is a Listner.  Source is not.
		
	//	Source soundCard = new SoundCard(sampleRate, sampleRate*2, true);
		AudioSink sink = new AudioSink(AF_SAMPLE_RATE);

//		FirFilter lowPassI = new FirFilter();
//		FirFilter lowPassQ = new FirFilter();
		FirFilter lowPassI = new FirFilter(Filter.makeRaiseCosine(sampleRate, 12000, 0.5, 256));
		FirFilter lowPassQ = new FirFilter(Filter.makeRaiseCosine(sampleRate, 12000, 0.5, 256));
		PolyPhaseFilter polyPhaseFilterI = new PolyPhaseFilter(sampleRate, 12000, R, R*10);
		PolyPhaseFilter polyPhaseFilterQ = new PolyPhaseFilter(sampleRate, 12000, R, R*10);

		boolean readingData = true;
		int decimateCount=1;

		double[] inI = new double[R];
		double[] inQ = new double[R];
		double audioI = 0;
		double audioQ = 0;
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

					if (!ppFilter) {
						//Filter pre decimation
						audioI = lowPassI.filter(IFbuffer[2*d]);
						audioQ = lowPassQ.filter(IFbuffer[2*d+1]);

						// show the IF (otherwise we see the rotated spectrum)
						IFbuffer[2*d] = audioI;
						IFbuffer[2*d+1] = audioQ;
						
						//Decimate
						decimateCount++;
						if (decimateCount == R) {
							decimateCount = 1;
							double fil = demodulate(audioI, audioQ);
							audioBuffer[d/R] = fil;
						}
					} else {
						inI[decimateCount] = IFbuffer[2*d];
						inQ[decimateCount++] = IFbuffer[2*d+1];
						if (decimateCount == R) {
							decimateCount = 0;
							audioI = polyPhaseFilterI.filter(inI);
							audioQ = polyPhaseFilterQ.filter(inQ);

							double fil = demodulate(audioI, audioQ);
							audioBuffer[d/R] = fil;
						}
					}
				}
				sink.write(audioBuffer);

			} else 
				readingData = false;
			}
			System.out.println("Finished processing"); // this is helpful in testing to know if we dropped out of the loop
		}
	
	public void exit() {
		if (usb != null)
			usb.exit();
	}
	
	double demodulate(double audioI, double audioQ) {
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
		else if (mode == Mode.FM)
			audio = fm.demodulate(audioI, audioQ);
		audio = audio * gain;
		double fil = audioLowPass.filter(audio);
		return fil;
	}
}
