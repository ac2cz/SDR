package tutorial1;
import org.jtransforms.fft.DoubleFFT_1D;

import tutorial1.signal.Oscillator;

public class SdrTutorial1 {

	public static void main(String[] args) throws InterruptedException {

		Oscillator osc = new Oscillator(48000);
		osc.setFrequency(1200);
		int len = 128;
		double[] buffer = new double[len];
		for (int n=0; n< len; n++) {
			double value = osc.nextSample();
			buffer[n] = value;
		}

		DoubleFFT_1D fft;
		fft = new DoubleFFT_1D(128);
		fft.realForward(buffer);
		System.out.println(buffer[0]); // bin 0
		for (int k=1; k<len/2; k++)
			System.out.println(Math.sqrt(buffer[2*k]*buffer[2*k] + buffer[2*k+1]*buffer[2*k+1]));
		System.out.println(Math.sqrt(buffer[1]*buffer[1] + buffer[len/2]*buffer[len/2])); // bin n/2

	}

}
