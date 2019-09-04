package tutorialp;

import tutorial1.signal.Oscillator;
import tutorialp.signal.Filter;
import tutorialp.signal.FirFilter;

public class FirTest {

public static void main(String[] args) {
	int fs = 192000;
	
	Oscillator osc = new Oscillator(fs, 1000); // Signal we want to recover
	Oscillator osc2 = new Oscillator(fs, 8000); // noise in the channel
	MainWindow window = new MainWindow("Fir Filter Test");
	FirFilter firFilter = new FirFilter(Filter.makeRaiseCosine(fs, 6000, 0.5, 480));
	
	int len = 6000;

	double[] buffer = new double[len];
	double[] buffer2 = new double[len];
	
	for (int n=0; n< len; n++) {
		// Fill buffer with the test signal
		double value = osc.nextSample();
		double value2 = osc2.nextSample();
		buffer[n] = value + value2;
		
		// Filter
		buffer2[n] = firFilter.filter(buffer[n]);
	}
	
	// Show the waveform
	window.setData(buffer);
	window.setData2(buffer2);
	
	window.setVisible(true);
	
	}
}
