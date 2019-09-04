package tutorialp;

import tutorial1.signal.Oscillator;
import tutorialp.signal.Filter;
import tutorialp.signal.FirFilter;
import tutorialp.signal.PolyPhaseFilter;

public class PPtest {

	public static void main(String[] args) {
		int fs = 192000;
		int R = 10;
		int len = 4000;
		int decimateCount = 0;

		Oscillator osc = new Oscillator(fs, 1000); // Signal we want to recover
		Oscillator osc2 = new Oscillator(fs, 10000); // noise in the channel above the filter freq 
		Oscillator osc3 = new Oscillator(fs, 00); // frequency that will alias close to the signal
		MainWindow window = new MainWindow("PolyPhase Filter Test");

		PolyPhaseFilter polyPhaseFilter = new PolyPhaseFilter(fs, 4000, R, R*48);
		FirFilter firFilter = new FirFilter(Filter.makeRaiseCosine(fs, 4000, 0.5, R*48));

		double[] buffer = new double[len];
		double[] buffer2 = new double[len/R];
		double[] buffer3 = new double[len/R];

		double[] in = new double[R];

		for (int n=0; n< len; n++) {
			// Fill buffer with the test signal
			double value = osc.nextSample();
			double value2 = osc2.nextSample();
			double value3 = osc3.nextSample();
			buffer[n] = value + value2 + value3;
//			buffer[n] = value;

//////			System.out.print("Sample " + n + " ");
/*			
			double filtered = firFilter.filter(buffer[n]);
			in[decimateCount] = buffer[n];
			decimateCount++;
			if (decimateCount == R-1) {
				decimateCount = 0;
				buffer2[n/R] = filtered;
				buffer3[n/R] = polyPhaseFilter.filterDouble(in);
			}
*/
			double filtered = firFilter.filter(buffer[n]);
			in[decimateCount++] = buffer[n];
			if (decimateCount == R) {
				decimateCount = 0;
				buffer2[n/R] = filtered;
				buffer3[n/R] = polyPhaseFilter.filter(in);
			}
		}

		// Show the waveform
		window.setData(buffer);
		window.setData2(buffer2);
		window.setData3(buffer3);

		window.setVisible(true);

	}
}
